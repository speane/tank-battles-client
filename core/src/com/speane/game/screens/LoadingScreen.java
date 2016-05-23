package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.speane.game.TankGame;
import com.speane.game.help.TextureManager;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;

/**
 * Created by Evgeny Shilov on 17.05.2016.
 */
public class LoadingScreen extends ScreenAdapter {
    private static final float PROGRESS_BAR_WIDTH = 200;
    private static final float PROGRESS_BAR_HEIGHT = 50;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float progress;
    private final TankGame game;

    public LoadingScreen(TankGame game) {
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(DESKTOP_SCREEN_WIDTH / 2, DESKTOP_SCREEN_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(DESKTOP_SCREEN_WIDTH, DESKTOP_SCREEN_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        game.getAssetManager().load("textures/archive/tank_battles_assets.atlas", TextureAtlas.class);
        game.getAssetManager().load("tiledmaps/tank_battles.tmx", TiledMap.class);
        game.getAssetManager().load("sound/background.mp3", Music.class);
        game.getAssetManager().load("sound/blast.wav", Sound.class);
        game.getAssetManager().load("sound/hit.wav", Sound.class);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    public void update() {
        if (game.getAssetManager().update()) {
            TextureAtlas textureAtlas = game.getAssetManager().get("textures/archive/tank_battles_assets.atlas");
            TextureManager.init(textureAtlas);
            game.setScreen(new AuthorizationScreen(game));
        } else {
            progress = game.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (DESKTOP_SCREEN_WIDTH - PROGRESS_BAR_WIDTH) / 2,
                (DESKTOP_SCREEN_HEIGHT - PROGRESS_BAR_HEIGHT) / 2,
                PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
}
