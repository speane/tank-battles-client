package com.speane.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.speane.game.TankGame;
import com.speane.game.entities.network.authentication.AuthenticationManager;
import com.speane.game.entities.network.authentication.NoSuchUserException;
import com.speane.game.entities.network.authentication.UserAuthorizedException;
import com.speane.game.entities.network.authentication.WrongPasswordException;
import com.speane.game.entities.network.userinfo.UserInfo;
import com.speane.game.help.Config;

import java.io.IOException;


/**
 * Created by Evgeny Shilov on 19.05.2016.
 */
public class AuthorizationScreen extends MenuScreen {
    private TextField loginTextField;
    private TextField passwordTextField;
    private Label statusLabel;

    private final String WRONG_PASSWORD_ERROR_MESSAGE = "Wrong password";
    private final String WRONG_USERNAME_ERROR_MESSAGE = "Wrong username";
    private final String WRONG_DATA_ERROR_MESSAGE = "Wrong login or password";
    private final String ALREADY_AUTHORIZED_ERROR_MESSAGE = "User already authorized";

    private AuthenticationManager authenticationManager;

    public AuthorizationScreen(TankGame game) {
        super(game);
        authenticationManager = new AuthenticationManager(Config.SERVER_HOST, Config.SERVER_PORT);
    }

    private void addStatusLabel() {
        statusLabel = new Label(EMPTY_STRING, skin);
        statusLabel.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2 - INDENT * 2,
                Config.DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 5, Align.center);
        stage.addActor(statusLabel);
    }

    private void addInputFields() {
        addLoginInputField();
        addPasswordInputField();
    }

    private void addLoginInputField() {
        loginTextField = new TextField(EMPTY_STRING, skin);
        loginTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        loginTextField.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2 + INDENT, Align.center);
        String LOGIN_FIELD_TEXT = "Login";
        loginTextField.setMessageText(LOGIN_FIELD_TEXT);
        stage.addActor(loginTextField);
    }

    private void addPasswordInputField() {
        passwordTextField = new TextField(EMPTY_STRING, skin);
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        passwordTextField.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        String PASSWORD_FIELD_TEXT = "Password";
        passwordTextField.setMessageText(PASSWORD_FIELD_TEXT);
        stage.addActor(passwordTextField);
    }

    private void addAuthorizeButton() {
        String AUTHORIZE_BUTTON_TEXT = "Authorize";

        TextButton authorizeButton = new TextButton(AUTHORIZE_BUTTON_TEXT, skin);
        authorizeButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        authorizeButton.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2,
                Config.DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 2, Align.center);
        authorizeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                try {
                    UserInfo userInfo = authenticationManager.authorize(loginTextField.getText(),
                            passwordTextField.getText());
                    if (userInfo != null) {
                        game.setScreen(new StartScreen(game, userInfo));
                        dispose();
                    }
                    else {
                        statusLabel.setText(WRONG_DATA_ERROR_MESSAGE);
                    }
                } catch (IOException e) {
                    statusLabel.setText(CONNECTION_ERROR_TEXT_MESSAGE);
                } catch (WrongPasswordException e) {
                    statusLabel.setText(WRONG_PASSWORD_ERROR_MESSAGE);
                } catch (NoSuchUserException e) {
                    statusLabel.setText(WRONG_USERNAME_ERROR_MESSAGE);
                } catch (UserAuthorizedException e) {
                    statusLabel.setText(ALREADY_AUTHORIZED_ERROR_MESSAGE);
                }
            }
        });
        stage.addActor(authorizeButton);
    }

    private void addRegisterButton() {
        String REGISTER_BUTTON_TEXT = "Register";
        TextButton registerButton = new TextButton(REGISTER_BUTTON_TEXT, skin);
        registerButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        registerButton.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2,
                Config.DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 3, Align.center);
        registerButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                game.setScreen(new RegistrationScreen(game));
            }
        });

        stage.addActor(registerButton);
    }

    private void addButtons() {
        addAuthorizeButton();
        addRegisterButton();
    }

    @Override
    public void show() {
        super.show();
        addInputFields();
        addButtons();
        addStatusLabel();
    }
}
