package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.transfers.*;

import java.io.IOException;
import java.util.Map;

import static com.speane.game.help.Config.*;
import static com.speane.game.help.Messages.CONNECTION_FAILED_MESSAGE;
import static com.speane.game.help.TextureManager.BULLET_TEXTURE;
import static com.speane.game.help.TextureManager.ENEMY_TANK_TEXTURE;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class NetworkManager {
    private Client client;
    private Map<Integer, Tank> enemies;
    public NetworkManager(Map<Integer, Tank> enemies) {
        this.enemies = enemies;
        initNetwork();
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
        kryo.register(HitTank.class);
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
                    Tank newTank = new Tank(ENEMY_TANK_TEXTURE, newPlayer.x, newPlayer.y, newPlayer.rotation);
                    newTank.ID = newPlayer.id;
                    enemies.put(newPlayer.id, newTank);
                }
                else if (o instanceof ShootTank) {
                    ShootTank shootTank = (ShootTank) o;
                    Tank tank = enemies.get(shootTank.id);
                    tank.shoot(new Bullet(BULLET_TEXTURE, shootTank.x, shootTank.y, shootTank.rotation));
                    System.out.println("Shoot " + ((ShootTank) o).id);
                }
                else if (o instanceof DeadTank) {
                    DeadTank deadTank = (DeadTank) o;
                    System.out.println("Killer: " + deadTank.killerID);
                    enemies.remove(deadTank.id);
                }
                else if (o instanceof HitTank) {
                    HitTank hitTank = (HitTank) o;
                    enemies.get(hitTank.id).subHealthPoints(hitTank.damage);
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
}
