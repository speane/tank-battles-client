package com.speane.game;

/**
 * Created by Speane on 08.03.2016.
 */
public class Tank {
    private int x;
    private int y;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void moveX(int dX) {
        this.x += dX;
    }

    public void moveY(int dY) {
        this.y += dY;
    }
}
