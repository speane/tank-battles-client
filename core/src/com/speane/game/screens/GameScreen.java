package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.speane.game.entities.Tank;
import com.speane.game.transfers.CreatePlayer;
import com.speane.game.transfers.MoveTank;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Tank player;

    private SpriteBatch batch;
    private Texture tankTexture;
    private Texture enemyTexture;

    private Client client;

    private Map<Integer, Tank> enemies;

    @Override
    public void show() {
        loadResources();
        initEntities();
        initNetwork();
    }

    @Override
    public void render(float delta) {
        queryInput();
        clearScreen();
        draw();
    }

    private void initNetwork() {
        client = new Client();
        registerClasses();
        new Thread(client).start();
        try {
            client.connect(20000, "localhost", 7777);
        } catch (IOException e) {
            System.out.println("Unable to connect");
        }
        initNetworkListener();
    }

    private void registerClasses() {
        Kryo kryo = client.getKryo();
        kryo.register(MoveTank.class);
        kryo.register(CreatePlayer.class);
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
                }
                if (o instanceof CreatePlayer) {
                    CreatePlayer newPlayer = (CreatePlayer) o;
                    enemies.put(newPlayer.id, new Tank(newPlayer.x, newPlayer.y));
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
        player = new Tank(0, 0);
        enemies = new HashMap<>();
    }

    private void loadResources() {
        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");
        enemyTexture = new Texture("enemy.png");
    }

    private void draw() {
        batch.begin();

        drawEnemies();
        drawTank(player);

        batch.end();
    }

    private void drawEnemies() {
        for (Tank enemy : enemies.values()) {
            drawEnemyTank(enemy);
        }
    }

    private void drawTank(Tank tank) {
        batch.draw(tankTexture, tank.getX(), tank.getY());
    }

    private void drawEnemyTank(Tank tank) {
        batch.draw(enemyTexture, tank.getX(), tank.getY());
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void queryInput() {
        boolean moved = false;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (lPressed) {
            player.moveX(-2);
            moved = true;
        }
        if (rPressed) {
            player.moveX(2);
            moved = true;
        }
        if (uPressed) {
            player.moveY(2);
            moved = true;
        }
        if (dPressed) {
            player.moveY(-2);
            moved = true;
        }

        if (moved) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = player.getX();
            moveTank.y = player.getY();
            client.sendTCP(moveTank);
        }
    }
}
