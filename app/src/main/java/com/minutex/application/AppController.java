package com.minutex.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.minutex.http.ImageCacheManager;
import com.minutex.http.RequestManager;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class AppController extends Application {
    private static int IMAGE_CACHE_SIZE = 1024 * 1024 * 10; // 10 MB
    private static Bitmap.CompressFormat DISK_IMAGE_CACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static int DISK_IMAGE_CACHE_QUALITY = 100; // PNG is loss-less
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.init(getApplicationContext());
        ImageCacheManager.init(getApplicationContext(),
                this.getPackageCodePath(),
                IMAGE_CACHE_SIZE,
                DISK_IMAGE_CACHE_COMPRESS_FORMAT,
                DISK_IMAGE_CACHE_QUALITY,
                ImageCacheManager.CacheMode.DISK);
    }


}
