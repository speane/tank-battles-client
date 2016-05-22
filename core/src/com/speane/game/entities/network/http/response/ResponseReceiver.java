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
        DataInputStream dataInputStream = new DataInputStream(server.getInputStream());
        HttpResponse httpResponse = new HttpResponse();
        String tempLine = dataInputStream.readUTF();
        httpResponse.setStatusLine(new StatusLine(tempLine));
        while (!(tempLine = dataInputStream.readUTF()).equals("")) {
            String[] headerParts = tempLine.split(": ");
            httpResponse.getHeaders().put(headerParts[0], headerParts[1]);
        }
        if (httpResponse.getHeaders().containsKey("Content-Length")) {
            byte[] bytes = new byte[Integer.parseInt(httpResponse.getHeaders().get("Content-Length"))];
            dataInputStream.readFully(bytes);
            httpResponse.setMessageBody(bytes);
        }
        return httpResponse;
    }
}
