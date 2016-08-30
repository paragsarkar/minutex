package com.minutex.http;

import android.provider.SyncStateContract;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.minutex.common.constants.Constants;
import com.minutex.common.util.AndroidUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class GenericRequest <RequestBody, ResponseBody, Domain> extends Request<ResponseBody> {
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Gson gson = new Gson();
    private ResponseListener<ResponseBody> mResponseListener;
    private Class<ResponseBody> mResponseBodyClass;
    private RequestBody mRequestBody;
    private Class<Domain> mDomainClass;
    private Map<String, String> responseHeader;
    private boolean responseRetrieved;
    private Map<String, String> requestHeader;

    public GenericRequest(int method, String url, RequestBody requestBody,
                          Class<ResponseBody> responseBodyClass,
                          Class<Domain> domainClass,
                          ResponseListener<ResponseBody> responseListener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mResponseListener = responseListener;
        this.mResponseBodyClass = responseBodyClass;
        this.mRequestBody = requestBody;
        this.mDomainClass = domainClass;
        this.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    @Override
    protected Response<ResponseBody> parseNetworkResponse(NetworkResponse response) {
        try {
            responseHeader = response.headers;
            responseRetrieved = true;
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            if (List.class.isAssignableFrom(this.mResponseBodyClass)) {
                String array = "[]";
                if (!json.isEmpty()) {
                    if (json.startsWith("{")) {
                        // list is inside the json object
                        int startIndex = json.indexOf('[');
                        int endIndex = json.lastIndexOf(']');
                        if (startIndex != -1 && endIndex != -1) {
                            array = json.substring(startIndex, endIndex + 1);
                        }
                    } else {
                        array = json;
                    }
                }
                return Response.success(
                        deserialize(array),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                // Some other pojo class
                return Response.success(
                        deserialize(json, this.mResponseBodyClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        mResponseListener.onNetworkError(volleyError);
        return volleyError;
    }

    @Override
    protected void deliverResponse(ResponseBody response) {
        mResponseListener.onResponse(response, getResponseHeader());
    }

    private ResponseBody deserialize(String obj, Class<ResponseBody> tClass) {
        return AndroidUtil.deserialize(obj, tClass);
    }

    private ResponseBody deserialize(String array) {
        return gson.fromJson(array, new ListOfObject<Domain>(this.mDomainClass));
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mRequestBody != null) {
            String body = gson.toJson(this.mRequestBody);
            if (Constants.DEBUG)
                Log.d("RequestBody", body);
            try {
                return body.getBytes(getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                Log.e("Request", e.getLocalizedMessage());
            }
        }
        return super.getBody();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.requestHeader != null ? this.requestHeader : super.getHeaders();
    }

    public Map<String, String> getResponseHeader() {
        if (responseRetrieved) {
            return responseHeader;
        } else {
            return Collections.emptyMap();
        }
    }

    private class ListOfObject<X> implements ParameterizedType {

        private Class<?> wrapped;

        public ListOfObject(Class<X> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }

    interface ResponseListener<ResponseBody> {
        void onResponse(ResponseBody response, Map<String, String> header);

        void onNetworkError(VolleyError volleyError);
    }
}