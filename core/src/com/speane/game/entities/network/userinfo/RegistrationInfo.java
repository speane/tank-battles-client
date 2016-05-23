package com.speane.game.entities.network.userinfo;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class RegistrationInfo {
    public String login;
    public String password;
    public String email;

    public RegistrationInfo() {
        login = "";
        password = "";
        email = "";
    }

    public RegistrationInfo(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
