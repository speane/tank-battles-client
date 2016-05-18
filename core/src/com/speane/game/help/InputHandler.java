package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.transfers.MoveTank;
import com.speane.game.transfers.ShootTank;
import javafx.scene.shape.Rectangle;

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
        float oldX = tank.getPosition().x;
        float oldY = tank.getPosition().y;

        Rectangle newCollsionModel = new Rectangle(
                tank.getCollisionModel().getX(),
                tank.getCollisionModel().getY(),
                tank.getCollisionModel().getWidth(),
                tank.getCollisionModel().getHeight());

        Direction moveDirection = Direction.FORWARD;
        Direction rotateDirection = Direction.LEFT;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (lPressed) {
            tank.mo
        }
        if (rPressed) {
            rotateDirection = Direction.RIGHT;
            rotated = true;
        }
        if (uPressed) {
            moveDirection = Direction.FORWARD;
            moved = true;
        }
        if (dPressed) {
            moveDirection = Direction.BACKWARD;
            moved = true;
        }

        if (moved) {
            tank.move(moveDirection);
        }
        if (rotated) {
            tank.
        }

        if (spacePressed) {
            Bullet bullet = tank.shoot();
            ShootTank shootTank = new ShootTank();
            shootTank.rotation = bullet.getRotation();
            shootTank.x = bullet.getPosition().x;
            shootTank.y = bullet.getPosition().y;
            networkManager.shoot(shootTank);
            Resourses.shootSound.play();
        }

        if (moved) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = tank.getPosition().x;
            moveTank.y = tank.getPosition().y;
            moveTank.rotation = tank.getRotation();
            networkManager.move(moveTank);
        }
    }
}
