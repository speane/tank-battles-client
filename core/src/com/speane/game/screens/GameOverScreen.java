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
import com.speane.game.entities.network.authentication.AuthenticationManager;
import com.speane.game.entities.network.userinfo.UserInfo;
import com.speane.game.help.Config;
import com.speane.game.help.TextureManager;

import java.io.IOException;

/**
 * Created by Evgeny Shilov on 19.05.2016.
 */
public class GameOverScreen extends ScreenAdapter {
    private TankGame game;
    private Stage stage;
    private Skin skin;
    private UserInfo userInfo;
    private int score;
    private Label statusLabel;

    public GameOverScreen(TankGame game, UserInfo userInfo, int score) {
        this.game = game;
        this.userInfo = userInfo;
        this.score = score;
        stage = new Stage(new FitViewport(Config.DESKTOP_SCREEN_WIDTH, Config.DESKTOP_SCREEN_HEIGHT));
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateInto();
        addBackground();
        addButtons();
        addLabels();
    }

    private void updateInto() {
        userInfo.battlesPlayed++;
        if (userInfo.bestScore < score) {
            userInfo.bestScore = score;
        }
        try {
            userInfo = new AuthenticationManager(Config.SERVER_HOST, Config.SERVER_PORT).updateInfo(userInfo);
        } catch (IOException e) {
            statusLabel.setText("Can't connect to server");
        }
    }

    private void addLabels() {
        Label gameOverLabel = new Label("Game Over", skin);
        gameOverLabel.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2 + 50, Align.center);
        gameOverLabel.setFontScale(1.4f);
        stage.addActor(gameOverLabel);

        Label scoreLabel = new Label("Your score: " + score, skin);
        scoreLabel.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2 + 20, Align.center);
        scoreLabel.setFontScale(1.3f);
        stage.addActor(scoreLabel);

        statusLabel = new Label("", skin);
        statusLabel.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        stage.addActor(statusLabel);
    }

    private void addButtons() {
        TextButton backButton = new TextButton("Back to menu", skin);
        backButton.setSize(200, 50);
        backButton.setPosition(Config.DESKTOP_SCREEN_WIDTH / 2, Config.DESKTOP_SCREEN_HEIGHT / 2 - 100, Align.center);
        backButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                game.setScreen(new StartScreen(game, userInfo));
            }
        });
        stage.addActor(backButton);
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