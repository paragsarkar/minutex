package com.minutex.domain;

import com.android.volley.VolleyError;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class ServerResponse extends VolleyError {
    private String message;
    private String messageCode;
    private String requestStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
