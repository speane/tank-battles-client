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
            System.err.println(e);
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
                if (o instanceof MoveTank) {
                    moveTankEvent((MoveTank) o);
                }
                else if (o instanceof CreatePlayer) {
                    createPlayerEvent((CreatePlayer) o);
                }
                else if (o instanceof ShootTank) {
                    shootTankEvent((ShootTank) o);
                }
                else if (o instanceof SendMessage) {
                    sendMessageEvent((SendMessage) o);
                }
                else if (o instanceof DeadTank) {
                    deadTankEvent((DeadTank) o);
                }
                else if (o instanceof HitTank) {
                    hitTankEvent((HitTank) o);
                }
                else if (o instanceof LevelUp) {
                    levelUpEvent((LevelUp) o);
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

    private void moveTankEvent(MoveTank moveTank) {
        Tank enemy = enemies.get(moveTank.id);
        enemy.setPosition(moveTank.x, moveTank.y);
        enemy.setRotation(moveTank.rotation);
    }

    private void createPlayerEvent(CreatePlayer newPlayer) {
        Tank newTank = new Tank(ENEMY_TANK_TEXTURE, newPlayer.x, newPlayer.y, newPlayer.rotation);
        newTank.setLevel(newPlayer.level);
        newTank.setHealthPoints(newPlayer.healthPoints);
        enemies.put(newPlayer.id, newTank);
        gameScreen.getPlayerNames().put(newPlayer.id, newPlayer.name);
    }

    private void shootTankEvent(ShootTank shootTank) {
        Tank tank = enemies.get(shootTank.id);
        tank.shoot(new Bullet(BULLET_TEXTURE, shootTank.x, shootTank.y, shootTank.rotation));
    }

    private void sendMessageEvent(SendMessage sendMessage) {
        gameScreen.getChatMessages().addFirst(gameScreen.getPlayerNames().get(sendMessage.id)
                + ": " + sendMessage.message);
        if (gameScreen.getChatMessages().size > 5) {
            gameScreen.getChatMessages().removeLast();
        }
    }

    private void deadTankEvent(DeadTank deadTank) {
        enemies.remove(deadTank.id);
        if (!enemies.containsKey(deadTank.killerID)) {
            gameScreen.addScore(Config.SCORE_FOR_KILL);
        }
    }

    private void hitTankEvent(HitTank hitTank) {
        if (enemies.containsKey(hitTank.shooterID)) {
            enemies.get(hitTank.shooterID).hit(enemies.get(hitTank.id));
        }
        else {
            gameScreen.getPlayer().hit(enemies.get(hitTank.id));
            gameScreen.addScore(Config.SCORE_FOR_HIT);
        }
    }

    private void levelUpEvent(LevelUp levelUp) {
        enemies.get(levelUp.id).levelUp(levelUp.level);
        enemies.get(levelUp.id).addHealthPoints(levelUp.healthPoints);
    }

    public <T> void sendEvent(T event) {
        client.sendTCP(event);
    }

    public void close() {
        client.close();
    }
}