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
    private final String WRONG_LOGIN_CODE = "402";
    private final String WRONG_PASSWORD_CODE = "403";
    private final String USER_EXISTS_CODE = "405";
    private final String ALREADY_AUTHORIZED_CODE = "407";
    private String host;
    private int port;
    private Gson gsonSerializer;

    public AuthenticationManager(String host, int port) {
        this.host = host;
        this.port = port;
        gsonSerializer = new Gson();
    }

    public UserInfo authorize(String login, String password) throws IOException, NoSuchUserException,
            WrongPasswordException, UserAuthorizedException {
        Socket serverSocket = new Socket(host, port);
        new RequestSender(serverSocket).sendAuthorizationRequest(new AuthorizationRequest(login, password));
        HttpResponse response = new ResponseReceiver(serverSocket).getNextResponse();
        if (response.getStatusLine().getStatusCode().equals(SUCCESS_STATUS_CODE)) {
            return gsonSerializer.fromJson(new String(response.getMessageBody()), UserInfo.class);
        }
        else {
            switch (response.getStatusLine().getStatusCode()) {
                case WRONG_LOGIN_CODE:
                    throw new NoSuchUserException();
                case WRONG_PASSWORD_CODE:
                    throw new WrongPasswordException();
                case ALREADY_AUTHORIZED_CODE:
                    throw new UserAuthorizedException();
                default:
                    return null;
            }
        }
    }

    public UserInfo register(String login, String password, String email) throws IOException,
            UserAlreadyExistsException {
        RegistrationInfo registrationInfo = new RegistrationInfo(login, password, email);

        Socket server = new Socket(host, port);
        new RequestSender(server).sendRegistrationRequest(registrationInfo);

        HttpResponse response = new ResponseReceiver(server).getNextResponse();
        if (response.getStatusLine().getStatusCode().equals(SUCCESS_STATUS_CODE)) {
            return gsonSerializer.fromJson(new String(response.getMessageBody()), UserInfo.class);
        }
        else {
            switch (response.getStatusLine().getStatusCode()) {
                case USER_EXISTS_CODE:
                    throw new UserAlreadyExistsException();
                default:
                    return null;
            }
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
