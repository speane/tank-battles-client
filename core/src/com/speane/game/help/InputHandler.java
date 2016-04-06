package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Networker networker;

    public InputHandler(Tank tank, Networker networker) {
        this.tank = tank;
        this.networker = networker;
    }

    public void queryInput() {
        boolean moved = false;

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (lPressed) {
            tank.rotate(Direction.LEFT);
            moved = true;
        }
        if (rPressed) {
            tank.rotate(Direction.RIGHT);
            moved = true;
        }
        if (uPressed) {
            tank.move(Direction.FORWARD);
            moved = true;
        }
        if (dPressed) {
            tank.move(Direction.BACKWARD);
            moved = true;
        }

        if (spacePressed) {
            Bullet bullet = tank.shoot();
            ShootTank shootTank = new ShootTank();
            shootTank.rotation = bullet.getRotation();
            shootTank.x = bullet.getPosition().x;
            shootTank.y = bullet.getPosition().y;
            networker.shoot(shootTank);
            Resourses.shootSound.play();
        }

        if (moved) {
            MoveTank moveTank = new MoveTank();
            moveTank.x = tank.getPosition().x;
            moveTank.y = tank.getPosition().y;
            moveTank.rotation = tank.getRotation();
            networker.move(moveTank);
        }
    }
}
