package com.speane.game.entities.network.http.request;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class RegistrationRequest {
    private String login;
    private String password;
    private String email;

    public RegistrationRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
