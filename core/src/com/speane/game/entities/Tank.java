package com.speane.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.speane.game.help.Resourses;

import java.util.ArrayList;

/**
 * Created by Speane on 08.03.2016.
 */
public class Tank extends MovingObject {
    public int ID;

    private ArrayList<Bullet> bullets;

    private State state;

    public Tank(float x, float y, float rotation) {
        this.position = new Vector2(x, y);
        this.rotation = rotation;
        this.width = Resourses.tankTexture.getWidth();
        this.height = Resourses.tankTexture.getHeight();

        bullets = new ArrayList<Bullet>();

        state = State.ALIVE;

        rotateSpeed = 2.5f;
        moveVector = new Vector2(0, 3);
        moveVector.rotate(rotation);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public Bullet shoot() {
        Bullet bullet = new Bullet(
                position.x + 35 - 50 * MathUtils.sinDeg(rotation),
                position.y + 40 + 50 * MathUtils.cosDeg(rotation),
                rotation
        );
        System.out.println(position.x + " " + position.y + " " + rotation);
        bullets.add(bullet);
        return bullet;
    }

    public Bullet shoot(Bullet bullet) {
        bullets.add(bullet);
        return bullet;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
