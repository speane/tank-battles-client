package com.speane.game.entities;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet {
    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    private float x;

    public float getY() {
        return y;
    }

    private float y;
    public void moveX(int dX) {
        x += dX;
    }
    public void moveY(int dY) {
        y += dY;
    }
}
