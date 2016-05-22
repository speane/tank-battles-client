package com.speane.game.entities.network.http.request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class RequestSender {
    public void sendRequest(Socket server, HttpRequest request) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());

        dataOutputStream.writeUTF(request.getRequestLine().toString());

        for (String header : request.getHeaders().keySet()) {
            dataOutputStream.writeUTF(header + ": " + request.getHeaders().get(header));
        }

        dataOutputStream.writeUTF("");

        if (request.getMessageBody() != null) {
            dataOutputStream.write(request.getMessageBody());
        }
    }
}
