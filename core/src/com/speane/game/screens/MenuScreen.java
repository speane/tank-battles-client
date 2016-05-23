package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.TankGame;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.START_MENU_BACKGROUND_TEXTURE;

/**
 * Created by Evgeny Shilov on 23.05.2016.
 */
public class MenuScreen extends ScreenAdapter {
    protected final int FIELD_WIDTH = 150;
    protected final int FIELD_HEIGHT = 30;
    protected final int INDENT = 50;
    protected final String CONNECTION_ERROR_TEXT_MESSAGE = "Can't connect to server";
    protected final String EMPTY_STRING = "";
    protected Skin skin;
    protected Stage stage;
    protected TankGame game;

    public MenuScreen(TankGame game) {
        this.game = game;
        String SKIN_FILE_PATH = "data/uiskin.json";
        this.skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        this.stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
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

    protected void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
