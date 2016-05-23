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

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        String CONTENT_LENGTH_HEADER_NAME = "Content_Length";
        this.messageBody = messageBody;
        headers.put(CONTENT_LENGTH_HEADER_NAME, Integer.toString(messageBody.length));
    }
}
