package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.TankGame;
import com.speane.game.help.Config;
import com.speane.game.help.TextureManager;

/**
 * Created by Evgeny Shilov on 22.05.2016.
 */
public class AcceptRegistrationScreen extends ScreenAdapter {
    private TankGame game;
    private Stage stage;
    private Skin skin;

    public AcceptRegistrationScreen(TankGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Config.DESKTOP_SCREEN_WIDTH, Config.DESKTOP_SCREEN_HEIGHT));
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        addBackground();
        addButtons();
        addLabel();
    }

    private void addLabel() {
        Label label = new Label("Thanks for registration. Details were sent to your email", skin);
        label.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2 + 60, Align.center);
        stage.addActor(label);
    }

    private void addButtons() {
        TextButton authorizeButton = new TextButton("Authorize", skin);
        authorizeButton.setSize(150, 30);
        authorizeButton.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        authorizeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                game.setScreen(new AuthorizationScreen(game));
            }
        });
        stage.addActor(authorizeButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        stage.act();
        stage.draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void addBackground() {
        Image backgroundImage = new Image(TextureManager.START_MENU_BACKGROUND_TEXTURE);
        backgroundImage.setSize(Config.DESKTOP_SCREEN_WIDTH, Config.DESKTOP_SCREEN_HEIGHT);
        stage.addActor(backgroundImage);
    }
}
