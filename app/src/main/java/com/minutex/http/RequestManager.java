package com.minutex.http;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class RequestManager {
    public static final String TAG = "RequestManager";
    private static RequestManager mRequestManager;
    private RequestQueue mRequestQueue;

    private RequestManager(Context context) {
        // No instance should be created outside of this class
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static void init(Context context) {
        mRequestManager = new RequestManager(context);
    }

    @Nullable
    public static RequestManager getInstance() {
        VolleyLog.DEBUG = false;

        if (mRequestManager == null) {
            Log.wtf(TAG, "RequestManager.init() method should be called before getting the instance. " +
                    "The best place to make this call is in Application class.");
            throw new IllegalStateException("Not Initialized");
        }
        return mRequestManager;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.v("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     * @deprecated Use #addToRequestQueue(Request, String)
     */
    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
