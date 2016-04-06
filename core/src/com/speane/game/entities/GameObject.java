package com.speane.game.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public abstract class GameObject {
    protected Vector2 position;

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }
}
