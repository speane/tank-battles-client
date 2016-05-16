package com.speane.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.speane.game.help.Resourses;

import static com.speane.game.help.Settings.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Settings.DESKTOP_SCREEN_WIDTH;

/**
 * Created by Evgeny Shilov on 16.05.2016.
 */
public class StartScreen extends ScreenAdapter {
    private Stage stage;
    private Game game;

    public StartScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        stage = new Stage(new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        Image backgroundImage = new Image(Resourses.startScreenBackgroundTexture);
        stage.addActor(backgroundImage);

        final TextField textField = new TextField("", skin);
        textField.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2 - 100, Align.center);
        textField.setMessageText("Enter your name");
        stage.addActor(textField);

        ImageButton imageButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(Resourses.playButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(Resourses.playButtonPressedTexture)));
        imageButton.setPosition(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2, Align.center);
        imageButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(textField.getText()));
                dispose();
            }
        });
        stage.addActor(imageButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
