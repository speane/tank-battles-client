package com.speane.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.speane.game.help.TextureManager.TANK_TEXTURE;

/**
 * Created by Speane on 08.03.2016.
 */
public class Tank extends MovingObject {
    public int ID;
    private int lives;
    private int score;
    private ArrayList<Bullet> bullets;

    private State state;

    public Tank(float x, float y, float rotation) {
        this.position = new Vector2(x, y);
        this.rotation = rotation;
        this.width = TANK_TEXTURE.getRegionWidth();
        this.height = TANK_TEXTURE.getRegionHeight();

        bullets = new ArrayList<Bullet>();
        lives = 3;
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

    public void hit() {
        lives--;
        if (lives <= 0) {
            state = State.DEAD;
        }
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }
}
