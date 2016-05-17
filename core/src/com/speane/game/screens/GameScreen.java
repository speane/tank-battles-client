package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.speane.game.TankGame;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.State;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.help.*;
import com.speane.game.transfers.MoveTank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.speane.game.help.TextureManager.*;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;
    private Tank player;
    private SpriteBatch batch;
    private Renderer renderer;
    private Map<Integer, Tank> enemies;
    private Networker networker;
    private InputHandler inputHandler;
    private CollisionDetector collisionDetector;
    private String playerName;
    private TankGame game;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    public GameScreen(TankGame game) {
        this.game = game;
        playerName = game.getPlayerName();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        tiledMap = game.getAssetManager().get("tiledmaps/tank_battles.tmx");
        initEntities();
        initNetwork();
        initCamera();
        loadResources();
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);
        Resourses.backgroundMusic.setLooping(true);
        Resourses.backgroundMusic.play();
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        //camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        //camera.update();
        viewport = new FitViewport(Config.WORLD_WIDTH, Config.WORLD_HEIGHT, camera);
        viewport.apply(true);
    }

    private void initEntities() {
        initTanks();
    }

    private void initNetwork() {
        networker = new Networker(enemies);
    }

    private void initTanks() {
        player = new Tank(MathUtils.random(Config.DESKTOP_SCREEN_WIDTH),
                MathUtils.random(Config.DESKTOP_SCREEN_HEIGHT),
                MathUtils.random(360));
        enemies = new HashMap<Integer, Tank>();
    }

    private void loadResources() {
        batch = new SpriteBatch();
        renderer = new Renderer(batch);
        MoveTank moveTank = new MoveTank();
        moveTank.rotation = player.getRotation();
        moveTank.x = player.getPosition().x;
        moveTank.y = player.getPosition().y;
        networker.move(moveTank);
        inputHandler = new InputHandler(player, networker);
        collisionDetector = new CollisionDetector(enemies, player);
    }

    @Override
    public void render(float delta) {
        if (player.getState() == State.ALIVE) {
            inputHandler.queryInput();
        }
        updateAllBullets();
        collisionDetector.checkCollisions();
        draw();
    }

    private void updateAllBullets() {
        updateTankBullets(player);
        for (Tank enemy : enemies.values()) {
            updateTankBullets(enemy);
        }
    }

    private void updateTankBullets(Tank tank) {
        Iterator<Bullet> iterator = tank.getBullets().iterator();
        Bullet bullet;
        while (iterator.hasNext()) {
            bullet = iterator.next();
            bullet.move(Direction.FORWARD);
            if (isOutOfScreen(bullet.getPosition().x, bullet.getPosition().y)) {
                iterator.remove();
            }
        }
    }

    private boolean isOutOfScreen(float x, float y) {
        return ((x > Config.DESKTOP_SCREEN_WIDTH)
                || (x < 0)
                || (y > Config.DESKTOP_SCREEN_HEIGHT)
                || (y < 0));
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();

        //clearScreen();
        batch.begin();



        drawEnemies();
        if (player.getState() == State.ALIVE) {
            drawTank(player, TANK_TEXTURE);
        }
        else {
            drawTank(player, DEAD_TANK_TEXTURE);
            renderer.showMessage("GAME OVER " + player.getScore());
        }
        renderer.drawText("Lives: " + player.getLives(), 0, Config.DESKTOP_SCREEN_HEIGHT);
        renderer.drawText("Score: " + player.getScore(), 0, Config.DESKTOP_SCREEN_HEIGHT - 50);
        batch.end();
    }

    private void drawTank(Tank tank, TextureRegion texture) {
        renderer.draw(tank, texture);
        for (Bullet bullet : tank.getBullets()) {
            renderer.draw(bullet, BULLET_TEXTURE);
        }
    }

    private void drawEnemies() {
        for (Tank enemy : enemies.values()) {
            if (enemy.getState() == State.ALIVE) {
                drawTank(enemy, ENEMY_TANK_TEXTURE);
            }
            else {
                drawTank(enemy, DEAD_TANK_TEXTURE);
            }
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.75f, 0.9f, 0.8f, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
