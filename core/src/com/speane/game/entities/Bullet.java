package com.speane.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.speane.game.help.Resourses;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet extends MovingObject {
    public Bullet(float x, float y, float rotation) {
        this.position = new Vector2(x, y);
        this.moveVector = new Vector2(0, 5);
        this.rotation = rotation;
        this.rotateSpeed = 3.f;
        this.moveVector.rotate(rotation);
        this.width = Resourses.bulletTextTexture.getWidth();
        this.height = Resourses.bulletTextTexture.getHeight();
    }
}
