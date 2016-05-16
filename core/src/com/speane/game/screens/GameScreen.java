package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.State;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.help.*;
import com.speane.game.transfers.MoveTank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Camera camera;
    private Viewport viewport;
    private Tank player;
    private SpriteBatch batch;
    private Renderer renderer;
    private Map<Integer, Tank> enemies;
    private Networker networker;
    private InputHandler inputHandler;
    private CollisionDetector collisionDetector;
    private String playerName;

    public GameScreen(String playerName) {
        if (!playerName.equals("")) {
            this.playerName = playerName;
        }
        else {
            this.playerName = "Unnamed";
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {

        System.out.println(this.playerName);

        initEntities();
        initNetwork();
        initCamera();
        viewport = new FitViewport(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT, camera);
        loadResources();
        Resourses.backgroundMusic.setLooping(true);
        Resourses.backgroundMusic.play();
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    private void initEntities() {
        initTanks();
    }

    private void initNetwork() {
        networker = new Networker(enemies);
    }

    private void initTanks() {
        player = new Tank(MathUtils.random(Settings.DESKTOP_SCREEN_WIDTH),
                MathUtils.random(Settings.DESKTOP_SCREEN_HEIGHT),
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
        return ((x > Settings.DESKTOP_SCREEN_WIDTH)
                || (x < 0)
                || (y > Settings.DESKTOP_SCREEN_HEIGHT)
                || (y < 0));
    }

    private void draw() {
        clearScreen();
        batch.begin();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        drawEnemies();
        if (player.getState() == State.ALIVE) {
            drawTank(player, Resourses.tankTexture);
        }
        else {
            drawTank(player, Resourses.deadTankTexture);
            renderer.showMessage("GAME OVER " + player.getScore());
        }
        renderer.drawText("Lives: " + player.getLives(), 0, Settings.DESKTOP_SCREEN_HEIGHT);
        renderer.drawText("Score: " + player.getScore(), 0, Settings.DESKTOP_SCREEN_HEIGHT - 50);
        batch.end();
    }

    private void drawTank(Tank tank, Texture texture) {
        renderer.draw(tank, texture);
        for (Bullet bullet : tank.getBullets()) {
            renderer.draw(bullet, Resourses.bulletTextTexture);
        }
    }

    private void drawEnemies() {
        for (Tank enemy : enemies.values()) {
            if (enemy.getState() == State.ALIVE) {
                drawTank(enemy, Resourses.enemyTankTexture);
            }
            else {
                drawTank(enemy, Resourses.deadTankTexture);
            }
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.75f, 0.9f, 0.8f, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
