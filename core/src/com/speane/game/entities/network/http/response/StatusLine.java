package com.speane.game.entities.network.http.response;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class StatusLine {
    private String httpVersion;
    private String statusCode;
    private String reasonMessage;

    public StatusLine() {
        httpVersion = "";
        statusCode = "";
        reasonMessage = "";
    }

    public StatusLine(String statusLine) {
        String[] parts = statusLine.split(" ");
        httpVersion = parts[0];
        statusCode = parts[1];
        reasonMessage = parts[2];
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    public String toString() {
        return httpVersion + " " + statusCode + " " + reasonMessage;
    }
}
