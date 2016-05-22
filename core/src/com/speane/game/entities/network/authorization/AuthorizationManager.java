package com.speane.game.entities.network.authorization;

import com.google.gson.Gson;
import com.speane.game.entities.network.http.request.AuthorizationRequest;
import com.speane.game.entities.network.http.request.RequestSender;
import com.speane.game.entities.network.http.response.HttpResponse;
import com.speane.game.entities.network.http.response.ResponseReceiver;
import com.speane.game.entities.network.userinfo.UserInfo;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class AuthorizationManager {
    private final String SUCCESS_STATUS_CODE = "200";

    private String host;
    private int port;

    public AuthorizationManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public UserInfo authorize(String login, String password) throws IOException {
        Socket serverSocket = new Socket(host, port);
        new RequestSender(serverSocket).sendAuthorizationRequest(new AuthorizationRequest(login, password));
        HttpResponse response = new ResponseReceiver(serverSocket).getNextResponse();
        if (response.getStatusLine().getStatusCode().equals(SUCCESS_STATUS_CODE)) {
            return new Gson().fromJson(new String(response.getMessageBody()), UserInfo.class);
        }
        else {
            return null;
        }
    }
}
