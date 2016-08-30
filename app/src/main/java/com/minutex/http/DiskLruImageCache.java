package com.minutex.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class DiskLruImageCache implements ImageLoader.ImageCache {

    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 0;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "HttpCallable #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>();
    private static int IO_BUFFER_SIZE = 8 * 1024;
    private static ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    final int cacheSize = 1024 * 1024 * 10; // 10 MB
    private DiskLruCache mDiskCache;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private int mCompressQuality = 70;
    private LruCache<String, Bitmap> mMemCache;

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
                             Bitmap.CompressFormat compressFormat, int quality) {
        try {
            final File diskCacheDir = getDiskCacheDir(context, uniqueName);
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
            mCompressFormat = compressFormat;
            mCompressQuality = quality;
            mMemCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return value.getRowBytes() * value.getHeight();
                }
            };
        } catch (IOException e) {
            Log.wtf("DLRUImageCache", e.getLocalizedMessage());
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {

        final String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    @Override
    public void putBitmap(final String key, final Bitmap data) {
        mMemCache.put(createKey(key), data);
        // set value in memory and return after invoking to store
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DiskLruCache.Editor editor = null;
                try {
                    editor = mDiskCache.edit(createKey(key));
                    if (editor == null) {
                        return;
                    }

                    if (writeBitmapToFile(data, editor)) {
                        mDiskCache.flush();
                        editor.commit();
                        Log.d("cache_test_DISK_", "image put on disk cache " + key);
                    } else {
                        editor.abort();
                        Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
                    }
                } catch (IOException e) {
                    Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
                    try {
                        if (editor != null) {
                            editor.abort();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(runnable);
    }

    @Override
    public Bitmap getBitmap(String key) {

        Bitmap bitmap = mMemCache.get(createKey(key));
        if (bitmap == null) {
            // not present in memory, retrieve from Disk
            DiskLruCache.Snapshot snapshot = null;
            try {

                snapshot = mDiskCache.get(createKey(key));
                if (snapshot == null) {
                    return null;
                }
                final InputStream in = snapshot.getInputStream(0);
                if (in != null) {
                    final BufferedInputStream buffIn =
                            new BufferedInputStream(in, IO_BUFFER_SIZE);
                    bitmap = BitmapFactory.decodeStream(buffIn);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (snapshot != null) {
                    snapshot.close();
                }
            }

            // set back to memory
            mMemCache.put(createKey(key), bitmap);
        }
        return bitmap;

    }

    public boolean containsKey(String key) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(createKey(key));
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;

    }

    public void clearCache() {
        try {
            mDiskCache.delete();
            mMemCache.evictAll();
        } catch (IOException e) {
            Log.e("DLRUImageCache", e.getLocalizedMessage());
        }
    }

    public File getCacheFolder() {
        return mDiskCache.getDirectory();
    }

    /**
     * Creates a unique cache key based on a url value
     *
     * @param url url to be used in key creation
     * @return cache key value
     */
    private String createKey(String url) {
        return String.valueOf(url.hashCode());
    }
}
