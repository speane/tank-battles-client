package com.speane.game.entities.network.http.response;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Evgeny Shilov on 15.05.2016.
 */
public class ResponseReceiver {
    private Socket server;

    public ResponseReceiver(Socket socket) {
        this.server = socket;
    }

    public HttpResponse getNextResponse() throws IOException {
        String CONTENT_LENGTH_HEADER_NAME = "Content-Length";
        String EMPTY_STRING = "";
        String HEADER_DELIMITER = ": ";
        int HEADER_NAME_INDEX = 0;
        int HEADER_VALUE_INDEX = 1;

        DataInputStream dataInputStream = new DataInputStream(server.getInputStream());
        HttpResponse httpResponse = new HttpResponse();
        String tempLine = dataInputStream.readUTF();
        httpResponse.setStatusLine(new StatusLine(tempLine));
        while (!(tempLine = dataInputStream.readUTF()).equals(EMPTY_STRING)) {
            String[] headerParts = tempLine.split(HEADER_DELIMITER);
            httpResponse.getHeaders().put(headerParts[HEADER_NAME_INDEX], headerParts[HEADER_VALUE_INDEX]);
        }
        if (httpResponse.getHeaders().containsKey(CONTENT_LENGTH_HEADER_NAME)) {
            byte[] bytes = new byte[Integer.parseInt(httpResponse.getHeaders().get(CONTENT_LENGTH_HEADER_NAME))];
            dataInputStream.readFully(bytes);
            httpResponse.setMessageBody(bytes);
        }
        return httpResponse;
    }
}
