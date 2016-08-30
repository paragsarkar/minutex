package com.minutex.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class ImageCacheManager
{
        private static ImageCacheManager mInstance;

        /**
         * Volley image loader
         */
        private ImageLoader mImageLoader;

        /**
         * Image cache implementation
         */
        private ImageLoader.ImageCache mImageCache;

        public ImageCacheManager(Context context, String uniqueName, int cacheSize,
                                 Bitmap.CompressFormat compressFormat, int quality, CacheMode mode) {
            switch (mode) {
                case MEMORY:
                    this.mImageCache = new BitmapLruImageCache(cacheSize);
                    break;
                case DISK:
                    mImageCache = new DiskLruImageCache(context, uniqueName, cacheSize, compressFormat, quality);
                    break;
            }
            this.mImageLoader = new ImageLoader(RequestManager.getInstance().getRequestQueue(), mImageCache);

        }

        /**
         * Initializer for the manager. Must be called prior to use.
         *
         * @param cacheSize max size for the cache
         */
        public static void init(Context context, String uniqueName, int cacheSize,
                                Bitmap.CompressFormat compressFormat, int quality, CacheMode mode) {
            if (mInstance == null) {
                mInstance = new ImageCacheManager(context, uniqueName, cacheSize, compressFormat, quality, mode);
            }
        }

        /**
         * @return instance of the cache manager
         */
        public static ImageCacheManager getInstance() {
            if (mInstance == null) {
                throw new IllegalStateException("Not initialized");
            }
            return mInstance;
        }

        /**
         * @return instance of the image loader
         */
        public ImageLoader getImageLoader() {
            return mImageLoader;
        }

    public enum CacheMode {
        MEMORY, DISK
    }
}

