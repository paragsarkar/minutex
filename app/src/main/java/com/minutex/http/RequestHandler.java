package com.minutex.http;

/**
 * Created by Abinfosoft on 7/4/2016.
 */
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This class puts requests in Volley queue.
 * <p/>
 * Created by parag on 17/9/15.
 */
public class RequestHandler<RequestBody, ResponseBody, Domain> {

    /**
     * The source provider class.
     * ?
     */
    private HttpHandler<RequestBody, ResponseBody, Domain> httpHandler;

    /**
     * Default Error Listener.
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", error.getLocalizedMessage());
            // TODO: commented this line becaues we are not using this object and I changed the onErrorResponse method
            // httpHandler.onErrorResponse(error);
        }
    };

    /**
     * Constructor for the source definitions.
     *
     * @param httpHandler
     */
    public RequestHandler(HttpHandler<RequestBody, ResponseBody, Domain> httpHandler) {
        this.httpHandler = httpHandler;
    }

    /**
     * This method prepares the request object and puts it in Volley Queue
     *
     * @param tag
     */
    public void executeOnNetwork(String tag) {
        final String url;
        if (!httpHandler.getRequestParams().isEmpty() && httpHandler.responseBodyType != HttpHandler.RESPONSE_BODY_STRING) {
            // prepare URL
            StringBuilder sb = new StringBuilder(httpHandler.getURL());
            sb.append("?");
            for (Map.Entry<String, String> entry : httpHandler.getRequestParams().entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = sb.toString();
        } else {
            url = httpHandler.getURL();
        }

        GenericRequest request = new GenericRequest<>(
                httpHandler.getHttpRequestMethod(),
                url,
                httpHandler.getRequestBody(),
                httpHandler.beanTypeClass,
                httpHandler.domainClass,
                new GenericRequest.ResponseListener<ResponseBody>() {
                    @Override
                    public void onResponse(ResponseBody response, Map<String, String> header) {
                        httpHandler.onResponse(response, header);
                    }

                    @Override
                    public void onNetworkError(VolleyError volleyError) {
                        httpHandler.parseNetworkError(volleyError);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("String",error.toString());
                    }
                });
        RequestManager.getInstance().addToRequestQueue(request, tag);
    }
}
