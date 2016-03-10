package com.speane.game.entities;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet {
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    private int x;

    public int getY() {
        return y;
    }

    private int y;
    public void moveX(int dX) {
        x += dX;
    }
    public void moveY(int dY) {
        y += dY;
    }
}
