package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.entities.network.transfers.MoveTank;
import com.speane.game.entities.network.transfers.ShootTank;
import com.speane.game.screens.GameScreen;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class InputHandler {
    private Tank tank;
    private NetworkManager networkManager;
    private TiledMap tiledMap;
    private GameScreen gameScreen;

    public InputHandler(Tank tank, NetworkManager networkManager, TiledMap tiledMap, GameScreen gameScreen) {
        this.tank = tank;
        this.gameScreen = gameScreen;
        this.networkManager = networkManager;
        this.tiledMap = tiledMap;
    }

    public void queryInput() {
        boolean moved = false;
        boolean rotated = false;
        int oldX = tank.getX();
        int oldY = tank.getY();

        boolean escPressed = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (escPressed) {
            gameScreen.setGameOver(true);
            return;
        }
        if (lPressed) {
            tank.rotate(Direction.LEFT);
            rotated = true;
        }
        if (rPressed) {
            tank.rotate(Direction.RIGHT);
            rotated = true;
        }
        if (uPressed) {
            tank.move(Direction.FORWARD);
            moved = true;
        }
        if (dPressed) {
            tank.move(Direction.BACKWARD);
            moved = true;
        }

        String INDESTRUCTIBLE_LAYER_NAME = "indestructible";
        String IMPASSABLE_LAYER_NAME = "impassable";
        if (moved) {
            if (CollisionDetector.collidesWithLayer((TiledMapTileLayer)tiledMap.getLayers().get(INDESTRUCTIBLE_LAYER_NAME),
                    tank.getCollisionModel()) ||
                    CollisionDetector.collidesWithLayer((TiledMapTileLayer)tiledMap.getLayers().get(IMPASSABLE_LAYER_NAME),
                            tank.getCollisionModel())) {
                tank.setPosition(oldX, oldY);
            }
        }

        int PLAYER_INIT_ID = 0;

        if (spacePressed) {
            Bullet bullet = tank.shoot();
            ShootTank shootTank = new ShootTank(PLAYER_INIT_ID, bullet.getRotation(), bullet.getX(), bullet.getY());
            networkManager.sendEvent(shootTank);

            //Resourses.shootSound.play();
        }

        if (moved || rotated) {
            MoveTank moveTank = new MoveTank(PLAYER_INIT_ID, tank.getX(), tank.getY(), tank.getRotation());
            networkManager.sendEvent(moveTank);
        }
    }
}
