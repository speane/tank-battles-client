package com.speane.game.entities.network.authentication;

import com.google.gson.Gson;
import com.speane.game.entities.network.http.request.AuthorizationRequest;
import com.speane.game.entities.network.http.request.RequestSender;
import com.speane.game.entities.network.http.response.HttpResponse;
import com.speane.game.entities.network.http.response.ResponseReceiver;
import com.speane.game.entities.network.userinfo.RegistrationInfo;
import com.speane.game.entities.network.userinfo.UserInfo;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class AuthenticationManager {
    private final String SUCCESS_STATUS_CODE = "200";

    private String host;
    private int port;

    private Gson gsonSerializer;

    public AuthenticationManager(String host, int port) {
        this.host = host;
        this.port = port;
        gsonSerializer = new Gson();
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

    public UserInfo register(String login, String password, String email) throws IOException {
        RegistrationInfo registrationInfo = new RegistrationInfo();
        registrationInfo.login = login;
        registrationInfo.password = password;
        registrationInfo.email = email;
        Socket server = new Socket(host, port);
        new RequestSender(server).sendRegistrationRequest(registrationInfo);
        HttpResponse response = new ResponseReceiver(server).getNextResponse();
        if (response.getStatusLine().getStatusCode().equals(SUCCESS_STATUS_CODE)) {
            return gsonSerializer.fromJson(new String(response.getMessageBody()), UserInfo.class);
        }
        else {
            return null;
        }
    }

    public UserInfo updateInfo(UserInfo userInfo) throws IOException {
        Socket server = new Socket(host, port);
        new RequestSender(server).sendUpdateRequest(userInfo);
        HttpResponse response = new ResponseReceiver(server).getNextResponse();
        if (response.getStatusLine().getStatusCode().equals(SUCCESS_STATUS_CODE)) {
            return gsonSerializer.fromJson(new String(response.getMessageBody()), UserInfo.class);
        }
        else {
            return null;
        }
    }
}
