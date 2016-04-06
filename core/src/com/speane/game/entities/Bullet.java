package com.speane.game.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet extends MovingObject {
    public Bullet(float x, float y, float rotation) {
        this.position = new Vector2(x, y);
        this.moveVector = new Vector2(0, 5);
        this.rotation = rotation;
        this.rotateSpeed = 3.f;
        moveVector.rotate(rotation);
    }
}
