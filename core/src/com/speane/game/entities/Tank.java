package com.speane.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.speane.game.help.TextureManager.*;

/**
 * Created by Evgeny Shilov on 08.03.2016.
 */
public class Tank extends MovingObject {
    public int ID;
    private int lives;
    private int score;
    private ArrayList<Bullet> bullets;

    private State state;

    public Tank(TextureRegion texture, Vector2 position, float rotation) {
        super(texture, position, rotation);

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
                BULLET_TEXTURE,
                    new Vector2(
                        position.x + 35 - 50 * MathUtils.sinDeg(rotation),
                        position.y + 40 + 50 * MathUtils.cosDeg(rotation)),
                rotation
        );
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
