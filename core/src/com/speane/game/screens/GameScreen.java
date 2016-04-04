package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.transfers.CreatePlayer;
import com.speane.game.transfers.MoveTank;
import com.speane.game.transfers.ShootTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.speane.game.help.Settings.*;
import static com.speane.game.help.Messages.*;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Tank player;

    private SpriteBatch batch;
    private Texture tankTexture;
    private Texture enemyTexture;
    private Texture bulletTexture;
    private Touchpad touchpad;

    private Client client;

    private Map<Integer, Tank> enemies;

    @Override
    public void show() {
        loadResources();
        initEntities();
        initNetwork();
        touchpad = new Touchpad(0, new Touchpad.TouchpadStyle());
    }

    @Override
    public void render(float delta) {
        queryInput();
        updateAllBullets();
        clearScreen();
        draw();
    }

    private void updateAllBullets() {
        updateTankBullets(player);
        for (Tank enemy : enemies.values()) {
            updateTankBullets(enemy);
        }
    }

    private void updateTankBullets(Tank tank) {
        for (Bullet bullet : tank.getBullets()) {
            bullet.moveY(3);
        }
    }

    private void initNetwork() {
        client = new Client();
        registerClasses();
        new Thread(client).start();
        try {
            client.connect(CLIENT_WAIT_TIMEOUT, SERVER_IP, PORT);
        } catch (IOException e) {
            System.out.println(CONNECTION_FAILED_MESSAGE);
        }
        initNetworkListener();
    }

    private void registerClasses() {
        Kryo kryo = client.getKryo();
        kryo.register(MoveTank.class);
        kryo.register(CreatePlayer.class);
        kryo.register(ShootTank.class);
    }

    private void initNetworkListener() {
        Listener listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof MoveTank) {
                    MoveTank moveTank = (MoveTank) o;
                    Tank enemy = enemies.get(moveTank.id);
                    enemy.setX(moveTank.x);
                    enemy.setY(moveTank.y);
                    enemy.setRotation(moveTank.rotation);
                }
                if (o instanceof CreatePlayer) {
                    CreatePlayer newPlayer = (CreatePlayer) o;
                    enemies.put(newPlayer.id, new Tank(newPlayer.x, newPlayer.y, newPlayer.rotation));
                }
                if (o instanceof ShootTank) {
                    ShootTank shootTank = (ShootTank) o;
                    Tank tank = enemies.get(shootTank.id);
                    tank.shoot();
                    System.out.println("Shoot " + ((ShootTank) o).id);
                }
            }
        };

        client.addListener(new Listener.QueuedListener(listener) {
            @Override
            protected void queue(Runnable runnable) {
                Gdx.app.postRunnable(runnable);
            }
        });
    }

    private void initEntities() {
        initTanks();
    }

    private void initTanks() {
        player = new Tank(0, 0, 0);
        enemies = new HashMap<Integer, Tank>();
    }

    private void loadResources() {
        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");
        enemyTexture = new Texture("enemy.png");
        bulletTexture = new Texture("bullet.png");
    }

    private void draw() {
        batch.begin();

        drawEnemies();
        drawTank(player);
        touchpad.draw(batch, 12);

        batch.end();
    }

    private void drawEnemies() {
        for (Tank enemy : enemies.values()) {
            drawEnemyTank(enemy);
        }
    }

    private void drawTank(Tank tank) {
        drawBullets(tank.getBullets());
        batch.draw(
                tankTexture,
                tank.getX(),
                tank.getY(),
                tankTexture.getWidth() / 2,
                tankTexture.getHeight() / 2,
                tankTexture.getWidth(),
                tankTexture.getHeight(),
                1,
                1,
                tank.getRotation(),
                0,
                0,
                tankTexture.getWidth(),
                tankTexture.getHeight(),
                false,
                false
        );
    }

    private void drawBullets(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            batch.draw(bulletTexture, bullet.getX(), bullet.getY());
        }
    }

    private void drawEnemyTank(Tank tank) {
        drawBullets(tank.getBullets());
        batch.draw(
                enemyTexture,
                tank.getX(),
                tank.getY(),
                enemyTexture.getWidth() / 2,
                enemyTexture.getHeight() / 2,
                enemyTexture.getWidth(),
                enemyTexture.getHeight(),
                1,
                1,
                tank.getRotation(),
                0,
                0,
                enemyTexture.getWidth(),
                enemyTexture.getHeight(),
                false,
                false
        );
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.75f, 0.9f, 0.8f, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void queryInput() {
        boolean moved = false;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (lPressed) {
            player.rotate(1);
            moved = true;
            System.out.println(player.getRotation());
        }
        if (rPressed) {
            player.rotate(-1);
            moved = true;
            System.out.println(player.getRotation());
        }
        if (uPressed) {
            player.moveForward(2);
            moved = true;
        }
        if (dPressed) {
            player.moveBackward();
            moved = true;
        }

        if (spacePressed) {
            player.shoot();
            ShootTank shootTank = new ShootTank();
            client.sendTCP(shootTank);
        }

        if (moved) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = player.getX();
            moveTank.y = player.getY();
            moveTank.rotation = player.getRotation();
            client.sendTCP(moveTank);
        }
    }
}
