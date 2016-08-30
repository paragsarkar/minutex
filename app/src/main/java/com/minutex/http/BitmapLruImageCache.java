package com.minutex.http;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

private final String TAG = "BitmapLruImageCache";

        public BitmapLruImageCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        public Bitmap getBitmap(String url) {
            Log.v(TAG, "Retrieved item from Mem Cache");
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap){
            Log.v(TAG,"Added item to Mem Cache");
        put(url,bitmap);
        }
        }
