package com.speane.game.entities.network.http;

import java.util.HashMap;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class HttpMessage {
    protected HashMap<String, String> headers;
    protected byte[] messageBody;

    public HttpMessage() {
        headers = new HashMap<>();
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
        headers.put("Content-Length", Integer.toString(messageBody.length));
    }
}
