package com.speane.game.entities.network.userinfo;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class LoginInfo {
    public String userName;
    public String password;

    public LoginInfo() {
        userName = "";
        password = "";
    }

    public LoginInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
