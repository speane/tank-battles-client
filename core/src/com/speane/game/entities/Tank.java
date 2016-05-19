package com.speane.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.speane.game.help.TextureManager.BULLET_TEXTURE;

/**
 * Created by Evgeny Shilov on 08.03.2016.
 */
public class Tank extends MovingObject {
    private int healthPoints;
    private int level;
    private int damage;
    private ArrayList<Bullet> bullets;

    public Tank(TextureRegion texture, float x, float y, float rotation) {
        super(texture, x, y, rotation);

        bullets = new ArrayList<Bullet>();
        healthPoints = 100;
        damage = 20;
        setLevel(1);

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
                (int) (position.x + 35 - 50 * MathUtils.sinDeg(rotation)),
                (int) (position.y + 40 + 50 * MathUtils.cosDeg(rotation)),
                rotation
        );
        bullets.add(bullet);
        return bullet;
    }

    public void hit(Tank tank) {
        System.out.println(tank.getHealthPoints());
        System.out.println(damage);
        tank.subHealthPoints(damage);
        System.out.println(tank.getHealthPoints());
    }

    public Bullet shoot(Bullet bullet) {
        bullets.add(bullet);
        return bullet;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void subHealthPoints(int hp) {
        healthPoints -= hp;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        damage = level * 20;
        System.out.println("NEW LEVEL " + level + " damage: " + damage);
    }

    public int getDamage() {
        return damage;
    }

    public void levelUp(int levels) {
        setLevel(getLevel() + levels);
        healthPoints += 50;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}
