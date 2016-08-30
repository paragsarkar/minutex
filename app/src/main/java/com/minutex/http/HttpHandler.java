package com.minutex.http;

import android.Manifest;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public abstract class HttpHandler<RequestBodyBean, ResponseBeanType, Domain> {

    public static final int HTTP_GET = Request.Method.GET;
    public static final int HTTP_POST = Request.Method.POST;
    public static final int HTTP_PUT = Request.Method.PUT;
    public static final int HTTP_DELETE = Request.Method.DELETE;
    public static final int HTTP_TRACE = Request.Method.TRACE;
    public static final int HTTP_OPTIONS = Request.Method.OPTIONS;
    public static final int HTTP_PATCH = Request.Method.PATCH;
    public static final int HTTP_HEAD = Request.Method.HEAD;
    public static final int REQUEST_BODY_NONE = -1;
    public static final int REQUEST_BODY_JSON_OBJECT = 0;
    public static final int REQUEST_BODY_JSON_ARRAY = 1;
    public static final int REQUEST_BODY_STRING = 2;
    public static final int PARSE_REQ_BODY_AS_JSN_OBJECT = 3;
    public static final int PARSE_REQ_BODY_AS_JSN_ARRAY = 4;
    public static final int NO_PARSE = -2;
    public static final int PARSE_REQ_BODY_AS_STRING = 5;
    public static final int RESPONSE_BODY_JSON_OBJECT = 0;
    public static final int RESPONSE_BODY_JSON_ARRAY = 1;
    public static final int RESPONSE_BODY_STRING = 2;
    private static final String TAG = "HttpHandler";
    /*
     * Used for identifying response type.
     */
    protected int responseBodyType;
    /**
     * Used for identifying request requestBodyType.
     */
    protected int requestBodyType;
    protected Class<ResponseBeanType> beanTypeClass;
    protected Class<Domain> domainClass;

    @RequiresPermission(Manifest.permission.INTERNET)
    public HttpHandler(@HttpHandler.RequestBodyType int requestBodyType,
                       @HttpHandler.ResponseBodyType int responseBodyType,
                       Class<ResponseBeanType> beanTypeClass, Class<Domain> domainClass) {
        this.responseBodyType = responseBodyType;
        this.requestBodyType = requestBodyType;
        this.beanTypeClass = beanTypeClass;
        this.domainClass = domainClass;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public HttpHandler(@HttpHandler.RequestBodyType int requestBodyType,
                       @HttpHandler.ResponseBodyType int responseBodyType,
                       Class<ResponseBeanType> beanTypeClass) {
        this.responseBodyType = responseBodyType;
        this.requestBodyType = requestBodyType;
        this.beanTypeClass = beanTypeClass;
    }

    /**
     * This method is used to set up the Http request object. For simple GET requests,
     * it can be null,
     *
     * @return the data received from server.
     */
    @HttpMethod
    public abstract int getHttpRequestMethod();

    /**
     * Populate request parameters in this method. The key value pair will be sent as
     * ?key1=value1&key2=value2 appended to the URL. Return null in case not required.
     *
     * @return Request Parameter Map
     */
    @CallSuper
    public Map<String, String> getRequestParams() {
        return new HashMap<String, String>();
    }

    /**
     * Populate request requestBodyType in this method. Return null in case if not applicable.
     *
     * @return Request requestBodyType in {@link JSONObject} or {@link JSONArray} format.
     */

    public RequestBodyBean getRequestBody() {
        return null;
    }

    /**
     * The endpoint URL for the REST API Call.
     *
     * @return the endpoint URL.
     */
    public abstract String getURL();

    /**
     * Handle error response, you can show message to the user and implement retry.
     *
     * @param error which is the {@link VolleyError} object.
     */
    public void onErrorResponse(Response error) {
        return;
    }

    /**
     * This method handles network error for the request.
     *
     * @param volleyError which is the {@link VolleyError} object.
     */
    public void parseNetworkError(VolleyError volleyError) {
        if (volleyError != null && volleyError.getLocalizedMessage() != null) {
            Log.e("FVolleyError", volleyError.getLocalizedMessage());
        } else {
            Log.e("FVolleyError", "Unknown Network Error");
        }

        return;
    }

    /**
     * This method is used to process the response received from server.
     *
     * @param result  Server response may be a string, json array or json object.
     * @param headers Response headers as Map
     */
    public abstract void onResponse(ResponseBeanType result, Map<String, String> headers);

    /**
     * This method sets the request header.
     *
     * @return Map of key value pair
     */
    public Map<String, String> getRequestHeaders() {
        return Collections.emptyMap();
    }

    /**
     * This method is used to make the call to the endpoint.
     *
     * @param tag which is the identifier for volley request, use class name of the callee.
     */
    public void executeWithTag(String tag) {
        new RequestHandler<>(this).executeOnNetwork(tag);
    }

    @IntDef({HTTP_GET, HTTP_DELETE, HTTP_HEAD, HTTP_OPTIONS, HTTP_PATCH, HTTP_PUT, HTTP_POST, HTTP_TRACE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HttpMethod {
    }

    @IntDef({REQUEST_BODY_NONE, REQUEST_BODY_JSON_ARRAY, REQUEST_BODY_JSON_OBJECT,
            REQUEST_BODY_STRING, PARSE_REQ_BODY_AS_JSN_OBJECT, PARSE_REQ_BODY_AS_JSN_ARRAY, NO_PARSE,
            PARSE_REQ_BODY_AS_STRING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestBodyType {
    }

    @IntDef({RESPONSE_BODY_JSON_ARRAY, RESPONSE_BODY_JSON_OBJECT, RESPONSE_BODY_STRING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponseBodyType {
    }
}

