package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.TankGame;
import com.speane.game.entities.network.authentication.AuthenticationManager;
import com.speane.game.help.Config;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.START_MENU_BACKGROUND_TEXTURE;

/**
 * Created by Evgeny Shilov on 23.05.2016.
 */
public class MenuScreen extends ScreenAdapter {
    private final int FIELD_WIDTH = 150;
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

    public MenuScreen(TankGame game) {
        this.game = game;
        String SKIN_FILE_PATH = "data/uiskin.json";
        this.skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        this.stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
        authenticationManager = new AuthenticationManager(Config.SERVER_HOST, Config.SERVER_PORT);
    }

    @Override
    public void show() {
        addBackground();
    }

    private void addBackground() {
        Gdx.input.setInputProcessor(stage);
        Image backgroundImage = new Image(START_MENU_BACKGROUND_TEXTURE);
        backgroundImage.setSize(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT);
        stage.addActor(backgroundImage);
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
