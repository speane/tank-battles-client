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
import com.speane.game.entities.Bullet;
import com.speane.game.entities.State;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.help.Settings;
import com.speane.game.rendering.Renderer;
import com.speane.game.resourses.Resourses;
import com.speane.game.transfers.CreatePlayer;
import com.speane.game.transfers.DeadTank;
import com.speane.game.transfers.MoveTank;
import com.speane.game.transfers.ShootTank;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.speane.game.help.Messages.CONNECTION_FAILED_MESSAGE;
import static com.speane.game.help.Settings.*;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Tank player;
    private SpriteBatch batch;
    private Renderer renderer;
    private Client client;
    private Map<Integer, Tank> enemies;

    @Override
    public void show() {
        loadResources();
        initEntities();
        initNetwork();
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
        Resourses.tankTexture = new Texture("tank.png");
        Resourses.enemyTankTexture = new Texture("enemy.png");
        Resourses.bulletTextTexture = new Texture("bullet.png");
        Resourses.deadTankTexture = new Texture("deadtank.png");
        renderer = new Renderer(batch);
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
        kryo.register(DeadTank.class);
    }

    private void initNetworkListener() {
        Listener listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
                if (o instanceof MoveTank) {
                    MoveTank moveTank = (MoveTank) o;
                    Tank enemy = enemies.get(moveTank.id);
                    enemy.setPosition(moveTank.x, moveTank.y);
                    enemy.setRotation(moveTank.rotation);
                }
                else if (o instanceof CreatePlayer) {
                    CreatePlayer newPlayer = (CreatePlayer) o;
                    Tank newTank = new Tank(newPlayer.x, newPlayer.y, newPlayer.rotation);
                    newTank.ID = newPlayer.id;
                    enemies.put(newPlayer.id, newTank);
                }
                else if (o instanceof ShootTank) {
                    ShootTank shootTank = (ShootTank) o;
                    Tank tank = enemies.get(shootTank.id);
                    tank.shoot(new Bullet(shootTank.x, shootTank.y, shootTank.rotation));
                    System.out.println("Shoot " + ((ShootTank) o).id);
                }
                else if (o instanceof DeadTank) {
                    DeadTank deadTank = (DeadTank) o;
                    System.out.println("Killer: " + deadTank.killerID);
                    enemies.get(deadTank.id).setState(State.DEAD);
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

    @Override
    public void render(float delta) {
        queryInput();
        updateAllBullets();
        clearScreen();
        draw();
    }

    private void queryInput() {
        boolean moved = false;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (lPressed) {
            player.rotate(Direction.LEFT);
            moved = true;
        }
        if (rPressed) {
            player.rotate(Direction.RIGHT);
            moved = true;
        }
        if (uPressed) {
            player.move(Direction.FORWARD);
            moved = true;
        }
        if (dPressed) {
            player.move(Direction.BACKWARD);
            moved = true;
        }

        if (spacePressed) {
            Bullet bullet = player.shoot();
            ShootTank shootTank = new ShootTank();
            shootTank.rotation = bullet.getRotation();
            shootTank.x = bullet.getPosition().x;
            shootTank.y = bullet.getPosition().y;
            client.sendTCP(shootTank);
        }

        if (moved) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = player.getPosition().x;
            moveTank.y = player.getPosition().y;
            moveTank.rotation = player.getRotation();
            client.sendTCP(moveTank);
        }
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
            /*else if ((tank != player) && isHit(bullet.getPosition().x, bullet.getPosition().y)) {
                iterator.remove();
                player.setState(State.DEAD);
                DeadTank deadTank = new DeadTank();
                deadTank.bulletNumber = tank.getBullets().indexOf(bullet);
                deadTank.killerID = tank.ID;
                System.out.println("Killer: " + deadTank.killerID);
                client.sendTCP(deadTank);
            }*/
        }
    }

    /*private boolean isHit(float x, float y) {
        return ((x > player.getX()) && (x < player.getX() + tankTexture.getWidth())
                && (y > player.getY() && (y < player.getY() + tankTexture.getHeight())));
    }*/

    private boolean isOutOfScreen(float x, float y) {
        return ((x > Settings.DESKTOP_SCREEN_WIDTH)
                || (x < 0)
                || (y > Settings.DESKTOP_SCREEN_HEIGHT)
                || (y < 0));
    }


    private void draw() {
        batch.begin();

        drawEnemies();
        if (player.getState() == State.ALIVE) {
            //drawTank(player);
            drawTank(player, Resourses.tankTexture);
        }
        else {
            //drawDeadTank(player);
            drawTank(player, Resourses.deadTankTexture);
        }

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
