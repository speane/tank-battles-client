package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.network.transfers.*;
import com.speane.game.screens.GameScreen;

import java.io.IOException;
import java.util.Map;

import static com.speane.game.help.Config.*;
import static com.speane.game.help.TextureManager.BULLET_TEXTURE;
import static com.speane.game.help.TextureManager.ENEMY_TANK_TEXTURE;

//import static com.speane.game.help.Messages.CONNECTION_FAILED_MESSAGE;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class NetworkManager {
    private Client client;
    private Map<Integer, Tank> enemies;
    private GameScreen gameScreen;

    public NetworkManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.enemies = gameScreen.getEnemies();
        initNetwork();
    }
    private void initNetwork() {
        client = new Client();
        registerClasses();
        new Thread(client).start();
        try {
            client.connect(CLIENT_WAIT_TIMEOUT, SERVER_HOST, PLAY_PORT);
        } catch (IOException e) {
            System.out.println("CONNECTION FAILED");
        }
        initNetworkListener();
    }

    private void registerClasses() {
        Kryo kryo = client.getKryo();
        kryo.register(MoveTank.class);
        kryo.register(CreatePlayer.class);
        kryo.register(ShootTank.class);
        kryo.register(DeadTank.class);
        kryo.register(HitTank.class);
        kryo.register(LevelUp.class);
        kryo.register(SendMessage.class);
    }

    private void initNetworkListener() {
        Listener listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
                System.out.println(o.getClass());
                if (o instanceof MoveTank) {
                    MoveTank moveTank = (MoveTank) o;
                    Tank enemy = enemies.get(moveTank.id);
                    enemy.setPosition(moveTank.x, moveTank.y);
                    enemy.setRotation(moveTank.rotation);
                }
                else if (o instanceof CreatePlayer) {
                    CreatePlayer newPlayer = (CreatePlayer) o;
                    Tank newTank = new Tank(ENEMY_TANK_TEXTURE, newPlayer.x, newPlayer.y, newPlayer.rotation);
                    newTank.setLevel(newPlayer.level);
                    newTank.setHealthPoints(newPlayer.healthPoints);
                    enemies.put(newPlayer.id, newTank);
                    gameScreen.getPlayerNames().put(newPlayer.id, newPlayer.name);
                    for (Integer key : enemies.keySet()) {
                        System.out.println("KEY: " + key + " enemy: " + enemies.get(key));
                    }
                }
                else if (o instanceof ShootTank) {
                    ShootTank shootTank = (ShootTank) o;
                    Tank tank = enemies.get(shootTank.id);
                    tank.shoot(new Bullet(BULLET_TEXTURE, shootTank.x, shootTank.y, shootTank.rotation));
                    System.out.println("Shoot " + ((ShootTank) o).id);
                }
                else if (o instanceof SendMessage) {
                    SendMessage sendMessage = (SendMessage) o;
                    gameScreen.getChatMessages().addFirst(gameScreen.getPlayerNames().get(sendMessage.id)
                            + ": " + sendMessage.message);
                    if (gameScreen.getChatMessages().size > 5) {
                        gameScreen.getChatMessages().removeLast();
                    }
                }
                else if (o instanceof DeadTank) {
                    DeadTank deadTank = (DeadTank) o;
                    System.out.println("Killer: " + deadTank.killerID);

                    enemies.remove(deadTank.id);
                    if (!enemies.containsKey(deadTank.killerID)) {
                        gameScreen.addScore(Config.SCORE_FOR_KILL);
                    }
                }
                else if (o instanceof HitTank) {
                    HitTank hitTank = (HitTank) o;
                    if (enemies.containsKey(hitTank.shooterID)) {
                        enemies.get(hitTank.shooterID).hit(enemies.get(hitTank.id));
                    }
                    else {
                        gameScreen.getPlayer().hit(enemies.get(hitTank.id));
                        gameScreen.addScore(Config.SCORE_FOR_HIT);
                    }
                    /*enemies.get(hitTank.id).subHealthPoints(hitTank.damage);
                    if (enemies.get(hitTank.id).isDead()) {
                        enemies.remove(hitTank.id);
                    }*/
                }
                else if (o instanceof LevelUp) {
                    LevelUp levelUp = (LevelUp) o;
                    System.out.println("lvlup: " + levelUp.id + " " + levelUp.level);
                    enemies.get(levelUp.id).levelUp(levelUp.level);
                    enemies.get(levelUp.id).addHealthPoints(levelUp.healthPoints);
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

    public <T> void sendEvent(T event) {
        client.sendTCP(event);
    }

    public void shoot(ShootTank shootTank) {
        client.sendTCP(shootTank);
    }

    public void move(MoveTank moveTank) {
        client.sendTCP(moveTank);
    }

    public void close() {
        client.close();
    }
}