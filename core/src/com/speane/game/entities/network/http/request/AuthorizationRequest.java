package com.speane.game.entities.network.http.request;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class AuthorizationRequest {
    private String password;
    private String login;

    public AuthorizationRequest(String login, String password) {
        this.password = password;
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
