package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.State;
import com.speane.game.entities.Tank;
import com.speane.game.transfers.CreatePlayer;
import com.speane.game.transfers.DeadTank;
import com.speane.game.transfers.MoveTank;
import com.speane.game.transfers.ShootTank;

import java.io.IOException;
import java.util.Map;

import static com.speane.game.help.Messages.CONNECTION_FAILED_MESSAGE;
import static com.speane.game.help.Settings.*;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class Networker {
    private Client client;
    private Map<Integer, Tank> enemies;
    public Networker(Map<Integer, Tank> enemies) {
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

    public void shoot(ShootTank shootTank) {
        client.sendTCP(shootTank);
    }

    public void move(MoveTank moveTank) {
        client.sendTCP(moveTank);
    }
}
