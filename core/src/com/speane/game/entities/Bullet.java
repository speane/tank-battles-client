package com.speane.game.entities;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet {
    private float rotation;
    private float bulletSpeed = 3.f;

    public Bullet(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    private float x;

    public float getY() {
        return y;
    }

    private float y;
    public void move() {
        this.x -= bulletSpeed * MathUtils.sinDeg(rotation);
        this.y += bulletSpeed * MathUtils.cosDeg(rotation);
    }

    public float getRotation() {
        return rotation;
    }
}
