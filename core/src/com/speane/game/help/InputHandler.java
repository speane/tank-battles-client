package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.transfers.MoveTank;
import com.speane.game.transfers.ShootTank;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class InputHandler {
    private Tank tank;
    private NetworkManager networkManager;
    private TiledMap tiledMap;

    public InputHandler(Tank tank, NetworkManager networkManager, TiledMap tiledMap) {
        this.tank = tank;
        this.networkManager = networkManager;
        this.tiledMap = tiledMap;
    }

    public void queryInput() {
        boolean moved = false;
        boolean rotated = false;
        int oldX = tank.getX();
        int oldY = tank.getY();

        Direction moveDirection = Direction.FORWARD;
        Direction rotateDirection = Direction.LEFT;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

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

        if (moved) {
            if (CollisionDetector.collidesWithLayer((TiledMapTileLayer)tiledMap.getLayers().get("indestructible"),
                    tank.getCollisionModel())) {
                tank.setPostion(oldX, oldY);
            }
        }

        if (spacePressed) {
            Bullet bullet = tank.shoot();
            ShootTank shootTank = new ShootTank();
            shootTank.rotation = bullet.getRotation();
            shootTank.x = bullet.getX();
            shootTank.y = bullet.getY();
            networkManager.shoot(shootTank);
            Resourses.shootSound.play();
        }

        if (moved || rotated) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = tank.getX();
            moveTank.y = tank.getY();
            moveTank.rotation = tank.getRotation();
            networkManager.move(moveTank);
        }
    }
}
