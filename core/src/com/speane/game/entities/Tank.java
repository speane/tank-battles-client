package com.speane.game.entities;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by Speane on 08.03.2016.
 */
public class Tank {
    private float x;
    private float y;
    private float rotation;
    private final float speed = 2.f;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    private ArrayList<Bullet> bullets;

    public void shoot() {
        Bullet bullet = new Bullet((x + 35), y + 100);
        bullets.add(bullet);
    }

    public Tank(float x, float y, float rotation) {
        bullets = new ArrayList<Bullet>();
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void rotate(float delta) {
        this.rotation += delta;
        if (rotation > 360) {
            rotation = rotation - 360;
        }
        else if (rotation < 0) {
            rotation = rotation + 360;
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void moveBackward() {
        this.x -= speed * MathUtils.sinDeg(rotation);
        this.y += speed * MathUtils.cosDeg(rotation);
    }
    public void moveForward() {
        this.x += speed * MathUtils.sinDeg(rotation);
        this.y -= speed * MathUtils.cosDeg(rotation);
    }
}
