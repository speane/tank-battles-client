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
import com.speane.game.transfers.SomeRequest;
import com.speane.game.transfers.TankMovement;

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
        loadResourses();
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
        client.start();
        try {
            client.connect(5000, "localhost", 7777);
        } catch (IOException e) {
            System.out.println("Unable to connect");
        }
        initNetworkListener();
    }

    private void registerClasses() {
        Kryo kryo = client.getKryo();
        //kryo.register(String.class);
        kryo.register(SomeRequest.class);
        kryo.register(TankMovement.class);
    }

    private void initNetworkListener() {
        Listener listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof SomeRequest) {
                    SomeRequest response = (SomeRequest) o;
                    System.out.println(response.text);
                    System.out.println(response.x);
                    System.out.println(response.y);
                }

                if (o instanceof TankMovement) {
                    player.moveX(((TankMovement) o).x);
                    player.moveY(((TankMovement) o).y);
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
        enemies.put(1, new Tank(90,29));
        enemies.put(2, new Tank(345, 256));
    }

    private void loadResourses() {
        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");
        enemyTexture = new Texture("enemy.png");
    }

    private void initClient() {
        client = new Client();
        Kryo kryo = client.getKryo();
        //kryo.register(SomeRequest.class);


        client.start();
        try {
            client.connect(5000, "localhost", 7777);
        } catch (IOException e) {
            System.out.println("Unable to connect");
        }
        Listener listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
            }
        };

        client.addListener(new Listener.QueuedListener(listener) {
            @Override
            protected void queue(Runnable runnable) {
                Gdx.app.postRunnable(runnable);
            }
        });
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
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (lPressed) {
            player.moveX(-2);
        }
        if (rPressed) {
            player.moveX(2);
        }
        if (uPressed) {
            player.moveY(2);
        }
        if (dPressed) {
            player.moveY(-2);
        }
    }
}
