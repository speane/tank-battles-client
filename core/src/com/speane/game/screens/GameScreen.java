package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.speane.game.TankGame;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.help.*;
import com.speane.game.transfers.MoveTank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.BULLET_TEXTURE;
import static com.speane.game.help.TextureManager.ENEMY_TANK_TEXTURE;
import static com.speane.game.help.TextureManager.TANK_TEXTURE;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private boolean gameOver = false;
    private int score;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Tank player;
    private SpriteBatch batch;
    private Renderer renderer;
    private Map<Integer, Tank> enemies;
    private NetworkManager networkManager;
    private InputHandler inputHandler;
    private CollisionDetector collisionDetector;
    private String playerName;
    private TankGame game;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private float levelWidth;
    private float levelHeight;

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
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)tiledMap.getLayers().get("background");
        levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        levelHeight = tiledMapTileLayer.getHeight() * tiledMapTileLayer.getTileHeight();
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
        networkManager = new NetworkManager(enemies);
    }

    private void initTanks() {
        player = new Tank(TANK_TEXTURE, (MathUtils.random(Config.DESKTOP_SCREEN_WIDTH)),
                MathUtils.random(Config.DESKTOP_SCREEN_HEIGHT),
                MathUtils.random(360));
        enemies = new HashMap<Integer, Tank>();
    }

    private void loadResources() {
        batch = new SpriteBatch();
        renderer = new Renderer(batch);
        MoveTank moveTank = new MoveTank();
        moveTank.rotation = player.getRotation();
        moveTank.x = player.getX();
        moveTank.y = player.getY();
        networkManager.move(moveTank);
        inputHandler = new InputHandler(player, networkManager, tiledMap);
        collisionDetector = new CollisionDetector(enemies, player);
    }

    @Override
    public void render(float delta) {
        if (!gameOver) {
            inputHandler.queryInput();
        }
        updateAllBullets();
        collisionDetector.checkCollisions();

        if (CollisionDetector.collidesWithLayer(
                (TiledMapTileLayer)tiledMap.getLayers().get("indestructible"),
                player.getCollisionModel())) {
            System.out.println("COLLISION!!!");
        }

        updateCamera();
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
            if (isOutOfScreen(bullet.getX(), bullet.getY())) {
                iterator.remove();
            }
        }
    }

    private void updateCamera() {
        float newCameraX;
        float newCameraY;
        if ((player.getX() < DESKTOP_SCREEN_WIDTH / 2) ||
                ((player.getX() + DESKTOP_SCREEN_WIDTH / 2) > levelWidth)) {
            newCameraX = camera.position.x;
        }
        else {
            newCameraX = player.getX();
        }

        if ((player.getY() < DESKTOP_SCREEN_HEIGHT / 2) ||
                ((player.getY() + DESKTOP_SCREEN_HEIGHT / 2) > levelHeight)) {
            newCameraY = camera.position.y;
        }
        else {
            newCameraY = player.getY();
        }
        camera.position.set(newCameraX, newCameraY, camera.position.z);
        camera.update();
        orthogonalTiledMapRenderer.setView(camera);
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
        drawTank(player, TANK_TEXTURE);
        renderer.showMessage("GAME OVER " + score);
        renderer.drawText("Health: " + player.getHealthPoints(), 0, Config.DESKTOP_SCREEN_HEIGHT);
        renderer.drawText("Score: " + score, 0, Config.DESKTOP_SCREEN_HEIGHT - 50);
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
            drawTank(enemy, ENEMY_TANK_TEXTURE);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.75f, 0.9f, 0.8f, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
