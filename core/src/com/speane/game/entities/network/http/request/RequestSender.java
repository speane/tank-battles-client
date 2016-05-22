package com.speane.game.entities.network.http.request;

import com.google.gson.Gson;
import com.speane.game.entities.network.userinfo.LoginInfo;
import com.speane.game.entities.network.userinfo.RegistrationInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class RequestSender {
    private Socket socket;
    private Gson gsonSerializer;

    public RequestSender(Socket socket) {
        this.socket = socket;
        gsonSerializer = new Gson();
    }

    public void sendRequest(HttpRequest request) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.writeUTF(request.getRequestLine().toString());

        for (String header : request.getHeaders().keySet()) {
            dataOutputStream.writeUTF(header + ": " + request.getHeaders().get(header));
        }

        dataOutputStream.writeUTF("");

        if (request.getMessageBody() != null) {
            dataOutputStream.write(request.getMessageBody());
        }
    }

    public void sendAuthorizationRequest(AuthorizationRequest request) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setRequestLine(new RequestLine("POST /authorization HTTP/1.1"));
        httpRequest.getHeaders().put("Host", socket.getInetAddress().getHostName());
        httpRequest.getHeaders().put("Connection", "Close");
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.password = request.getPassword();
        loginInfo.userName = request.getLogin();
        httpRequest.setMessageBody(new Gson().toJson(loginInfo).getBytes(Charset.forName("utf-8")));
        sendRequest(httpRequest);
    }

    public void sendRegistrationRequest(RegistrationInfo registrationInfo) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setRequestLine(new RequestLine("POST /registration HTTP/1.1"));
        httpRequest.getHeaders().put("Host", socket.getInetAddress().getHostName());
        httpRequest.getHeaders().put("Connection", "Close");
        httpRequest.setMessageBody(gsonSerializer.toJson(registrationInfo).getBytes(Charset.forName("utf-8")));
        sendRequest(httpRequest);
    }
}
