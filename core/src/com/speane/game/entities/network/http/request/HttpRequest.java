package com.speane.game.entities.network.http.request;

import com.speane.game.entities.network.http.HttpMessage;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class HttpRequest extends HttpMessage {
    private RequestLine requestLine;

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }
}
