package com.speane.game.entities;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by Speane on 08.03.2016.
 */
public class Tank {
    public int ID;

    private State state;

    private float x;
    private float y;
    private float rotation;
    private float speed = 2.f;
    private float rotationSpeed = 1.f;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    private ArrayList<Bullet> bullets;

    public Bullet shoot() {
        Bullet bullet = new Bullet(
                x + 35 - 50 * MathUtils.sinDeg(rotation),
                y + 40 + 50 * MathUtils.cosDeg(rotation),
                rotation
        );
        bullets.add(bullet);
        return bullet;
    }

    public Bullet shoot(Bullet bullet) {
        bullets.add(bullet);
        return bullet;
    }

    public Tank(float x, float y, float rotation) {
        bullets = new ArrayList<Bullet>();
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        state = State.ALIVE;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void rotateLeft() {
        this.rotation += rotationSpeed;
        if (rotation > 360) {
            rotation = rotation - 360;
        }
    }

    public void rotateRight() {
        this.rotation -= rotationSpeed;
        if (rotation < 0) {
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
        this.x += speed * MathUtils.sinDeg(rotation);
        this.y -= speed * MathUtils.cosDeg(rotation);
    }
    public void moveForward() {
        this.x -= speed * MathUtils.sinDeg(rotation);
        this.y += speed * MathUtils.cosDeg(rotation);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
