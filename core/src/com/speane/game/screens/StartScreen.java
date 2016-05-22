package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.TankGame;
import com.speane.game.entities.network.userinfo.UserInfo;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.*;

/**
 * Created by Evgeny Shilov on 16.05.2016.
 */
public class StartScreen extends ScreenAdapter {
    private Stage stage;
    private TankGame game;
    private UserInfo userInfo;

    public StartScreen(TankGame game, UserInfo userInfo) {
        this.game = game;
        this.userInfo = userInfo;
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Image backgroundImage = new Image(START_MENU_BACKGROUND_TEXTURE);
        backgroundImage.setSize(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT);
        stage.addActor(backgroundImage);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(PLAY_BUTTON_TEXTURE);
        style.imageOver = new TextureRegionDrawable(PLAY_BUTTON_PRESSED_TEXTURE);

        ImageButton imageButton = new ImageButton(style);
        imageButton.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        imageButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(game, userInfo));
                dispose();
            }
        });
        stage.addActor(imageButton);
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
