package com.speane.game.entities.network.http.request;

import com.google.gson.Gson;
import com.speane.game.entities.network.userinfo.LoginInfo;
import com.speane.game.entities.network.userinfo.RegistrationInfo;
import com.speane.game.entities.network.userinfo.UserInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Evgeny Shilov on 14.05.2016.
 */
public class RequestSender {
    private final String DEFAULT_CHARSET = "utf-8";


    private Socket socket;
    private Gson gsonSerializer;

    public RequestSender(Socket socket) {
        this.socket = socket;
        gsonSerializer = new Gson();
    }

    public void sendRequest(HttpRequest request) throws IOException {
        String HEADER_DELIMITER = ": ";

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(request.getRequestLine().toString());

        for (String header : request.getHeaders().keySet()) {
            dataOutputStream.writeUTF(header + HEADER_DELIMITER + request.getHeaders().get(header));
        }

        String EMPTY_STRING = "";
        dataOutputStream.writeUTF(EMPTY_STRING);

        if (request.getMessageBody() != null) {
            dataOutputStream.write(request.getMessageBody());
        }
    }

    private HttpRequest createRequest(String URI) {
        String REQUEST_LINE_DELIMITER = " ";
        String HTTP_VERSION = "HTTP/1.1";
        String AUTHENTICATION_METHOD = "POST";
        String HOST_HEADER_NAME = "Host";
        String HOST_HEADER_VALUE = socket.getInetAddress().getHostName();
        String CONNECTION_HEADER_NAME = "Connection";
        String CONNECTION_HEADER_VALUE = "Close";

        HttpRequest request = new HttpRequest();
        request.setRequestLine(new RequestLine(AUTHENTICATION_METHOD + REQUEST_LINE_DELIMITER + URI +
                REQUEST_LINE_DELIMITER + HTTP_VERSION));
        request.getHeaders().put(HOST_HEADER_NAME, HOST_HEADER_VALUE);
        request.getHeaders().put(CONNECTION_HEADER_NAME, CONNECTION_HEADER_VALUE);

        return request;
    }

    public void sendAuthorizationRequest(AuthorizationRequest request) throws IOException {
        String AUTHORIZATION_URI = "/authorization";

        HttpRequest httpRequest = createRequest(AUTHORIZATION_URI);
        LoginInfo loginInfo = new LoginInfo(request.getLogin(), request.getPassword());
        httpRequest.setMessageBody(gsonSerializer.toJson(loginInfo).getBytes(Charset.forName(DEFAULT_CHARSET)));

        sendRequest(httpRequest);
    }

    public void sendRegistrationRequest(RegistrationInfo registrationInfo) throws IOException {
        String REGISTRATION_URI = "/registration";

        HttpRequest httpRequest = createRequest(REGISTRATION_URI);
        httpRequest.setMessageBody(gsonSerializer.toJson(registrationInfo).getBytes(Charset.forName(DEFAULT_CHARSET)));

        sendRequest(httpRequest);
    }

    public void sendUpdateRequest(UserInfo userInfo) throws IOException {
        String UPDATE_INFO_URI = "/updateuserinfo";

        HttpRequest httpRequest = createRequest(UPDATE_INFO_URI);
        httpRequest.setMessageBody(gsonSerializer.toJson(userInfo).getBytes(Charset.forName(DEFAULT_CHARSET)));

        sendRequest(httpRequest);
    }
}
