package com.speane.game.screens;

import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by Evgeny Shilov on 19.05.2016.
 */
public class AuthorizationScreen extends ScreenAdapter {
    /*private final int FIELD_WIDTH = 150;
    private final int FIELD_HEIGHT = 30;
    private final int INDENT = 50;
    private final String CONNECTION_ERROR_TEXT_MESSAGE = "Can't connect to server";
    private final String EMPTY_STRING = "";
    private Skin skin;
    private Stage stage;
    private TankGame game;

    private TextField loginTextField;
    private TextField passwordTextField;
    private Label statusLabel;

    private AuthenticationManager authenticationManager;

    public AuthorizationScreen(TankGame game) {
        this.game = game;
        String SKIN_FILE_PATH = "data/uiskin.json";
        this.skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        this.stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
        authenticationManager = new AuthenticationManager(Config.SERVER_HOST, Config.SERVER_PORT);
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

        passwordTextField = new TextField(EMPTY_STRING, skin);
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        passwordTextField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        String PASSWORD_FIELD_TEXT = "Password";
        passwordTextField.setMessageText(PASSWORD_FIELD_TEXT);
        stage.addActor(passwordTextField);
    }

    private void addButtons() {
        String AUTHORIZE_BUTTON_TEXT = "Authorize";

        TextButton authorizeButton = new TextButton(AUTHORIZE_BUTTON_TEXT, skin);
        authorizeButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        authorizeButton.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 2, Align.center);
        authorizeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                try {
                    UserInfo userInfo = authenticationManager.authorize(loginTextField.getText(),
                            passwordTextField.getText());
                    if (userInfo != null) {
                        statusLabel.setText("CONNECTED: " + userInfo.name);
                        game.setScreen(new StartScreen(game, userInfo));
                        dispose();
                    }
                    else {
                        statusLabel.setText("Wrong login or password");
                    }
                } catch (IOException e) {
                    statusLabel.setText(CONNECTION_ERROR_TEXT_MESSAGE);
                } catch (WrongPasswordException e) {
                    statusLabel.setText("Wrong password");
                } catch (NoSuchUserException e) {
                    statusLabel.setText("Wrong username");
                }
            }
        });
        String REGISTER_BUTTON_TEXT = "Register";
        TextButton registerButton = new TextButton(REGISTER_BUTTON_TEXT, skin);
        registerButton.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        registerButton.setPosition(DESKTOP_SCREEN_WIDTH / 2,
                DESKTOP_SCREEN_HEIGHT / 2 - INDENT * 3, Align.center);
        registerButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                game.setScreen(new RegistrationScreen(game));
            }
        });
        stage.addActor(authorizeButton);
        stage.addActor(registerButton);
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
    }*/
}
