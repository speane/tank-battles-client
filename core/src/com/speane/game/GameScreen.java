package com.speane.game;

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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Tank player;

    private SpriteBatch batch;
    private Texture tankTexture;
    private Texture enemyTexture;

    private Client client;

    ArrayList<Tank> enemies;

    @Override
    public void show() {
        super.show();

        player = new Tank(0, 0);
        enemies = new ArrayList<Tank>();

        addEnemy(70, 233);
        addEnemy(90, 80);

        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");
        enemyTexture = new Texture("enemy.png");

        initClient();
    }

    private void initClient() {
        client = new Client();
        Kryo kryo = client.getKryo();
        kryo.register(SomeRequest.class);


        client.start();
        try {
            client.connect(5000, "localhost", 7777);
        } catch (IOException e) {
            System.out.println("Unable to connect");
        }
        client.addListener(new Listener() {
            @Override
            public void received(Connection c, Object o) {
                /*if (o instanceof TankMove) {
                    System.out.println("TankMove");
                    player.moveY(((Movement) o).dY);
                    System.out.println(((Movement) o).dX);
                    player.moveX(((Movement) o).dX);
                }*/
                if (o instanceof SomeRequest) {
                    /*System.out.println(o);
                    System.out.println(((TankMove) o).dX);
                    System.out.println(((TankMove) o).dY);
                    player.moveX(((TankMove) o).dX);
                    player.moveY(((TankMove) o).dY);*/
                    System.out.println(((SomeRequest) o).text);
                    System.out.println(((SomeRequest) o).x);
                    System.out.println(((SomeRequest) o).y);
                    player.moveX(((SomeRequest) o).x);
                    player.moveY(((SomeRequest) o).y);
                }
            }
        });
    }

    private void addEnemy(int x, int y) {
        enemies.add(new Tank(x, y));
    }


    @Override
    public void render(float delta) {
        super.render(delta);

        queryInput();

        clearScreen();
        draw();
    }

    private void draw() {
        batch.begin();

        drawEnemies();
        drawTank(player);

        batch.end();
    }

    private void drawEnemies() {
        for (Tank enemy : enemies) {
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
