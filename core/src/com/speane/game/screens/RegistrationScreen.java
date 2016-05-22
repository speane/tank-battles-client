package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.TankGame;
import com.speane.game.entities.network.authentication.AuthenticationManager;
import com.speane.game.help.Config;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.START_MENU_BACKGROUND_TEXTURE;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class RegistrationScreen extends ScreenAdapter {
    private final int FIELD_WIDTH = 150;
    private final int FIELD_HEIGHT = 30;
    private final int INDENT = 50;
    private final String CONNECTION_ERROR_TEXT_MESSAGE = "Can't connect to server";
    private final String EMPTY_STRING = "";
    private Skin skin;
    private Stage stage;
    private TankGame game;

    private TextField loginTextField;
    private TextField firstPasswordField;
    private TextField secondPasswordField;
    private TextField emailTextField;

    private Label statusLabel;

    private AuthenticationManager authorizationManager;

    public RegistrationScreen(TankGame game) {
        this.game = game;
        String SKIN_FILE_PATH = "data/uiskin.json";
        this.skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        this.stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
        authorizationManager = new AuthenticationManager(Config.SERVER_HOST, Config.SERVER_PORT);
    }

    @Override
    public void show() {
        addBackground();
        addInputFields();
        addButtons();
        addStatusLabel();
    }

    private void addStatusLabel() {
        statusLabel = new Label(EMPTY_STRING, skin);
        statusLabel.setPosition(DESKTOP_SCREEN_WIDTH / 2 - INDENT * 2, DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 5, Align.center);
        stage.addActor(statusLabel);
    }

    private void addBackground() {
        Gdx.input.setInputProcessor(stage);
        Image backgroundImage = new Image(START_MENU_BACKGROUND_TEXTURE);
        backgroundImage.setSize(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT);
        stage.addActor(backgroundImage);
    }

    private void addInputFields() {
        loginTextField = new TextField(EMPTY_STRING, skin);
        loginTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        loginTextField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 + INDENT, Align.center);
        String LOGIN_FIELD_TEXT = "Login";
        loginTextField.setMessageText(LOGIN_FIELD_TEXT);
        stage.addActor(loginTextField);

        emailTextField = new TextField(EMPTY_STRING, skin);
        emailTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        emailTextField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        String EMAIL_FIELD_TEXT = "e-mail";
        emailTextField.setMessageText(EMAIL_FIELD_TEXT);
        stage.addActor(emailTextField);

        firstPasswordField = new TextField(EMPTY_STRING, skin);
        firstPasswordField.setPasswordMode(true);
        firstPasswordField.setPasswordCharacter('*');
        firstPasswordField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        firstPasswordField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 - INDENT, Align.center);
        String PASSWORD_FIELD_TEXT = "Enter password";
        firstPasswordField.setMessageText(PASSWORD_FIELD_TEXT);
        stage.addActor(firstPasswordField);

        secondPasswordField = new TextField(EMPTY_STRING, skin);
        secondPasswordField.setPasswordMode(true);
        secondPasswordField.setPasswordCharacter('*');
        secondPasswordField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        secondPasswordField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 2, Align.center);
        String REPEAT_PASSWORD_FIELD_TEXT = "Confirm password";
        secondPasswordField.setMessageText(REPEAT_PASSWORD_FIELD_TEXT);
        stage.addActor(secondPasswordField);
    }

    private void addButtons() {
        String AUTHORIZE_BUTTON_TEXT = "Authorize";

        TextButton authorizeButton = new TextButton(AUTHORIZE_BUTTON_TEXT, skin);
        authorizeButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        authorizeButton.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 5, Align.center);
        authorizeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                game.setScreen(new AuthorizationScreen(game));
                dispose();
            }
        });
        String REGISTER_BUTTON_TEXT = "Register";
        TextButton registerButton = new TextButton(REGISTER_BUTTON_TEXT, skin);
        registerButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        registerButton.setPosition(DESKTOP_SCREEN_WIDTH / 2,
                DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 4, Align.center);
        registerButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

            }
        });
        stage.addActor(registerButton);
        stage.addActor(authorizeButton);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        stage.act();
        stage.draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
