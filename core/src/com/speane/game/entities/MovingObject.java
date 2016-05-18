package com.speane.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.speane.game.entities.moving.Direction;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public abstract class MovingObject extends GameObject {
    protected float rotateSpeed;
    protected Vector2 moveVector;

    public MovingObject(TextureRegion texture, float x, float y, float rotation) {
        super(texture, x, y, rotation);
    }

    public void move(Direction direction) {
        switch (direction) {
            case FORWARD:
                position.add(moveVector);
                break;
            case BACKWARD:
                position.sub(moveVector);
                break;
        }
        updateCollisionModel();
    }

    public void rotate(Direction direction) {
        switch (direction) {
            case LEFT:
                moveVector.rotate(rotateSpeed);
                rotation += rotateSpeed;
                break;
            case RIGHT:
                rotation -= rotateSpeed;
                moveVector.rotate(-rotateSpeed);
                break;
        }
        updateCollisionModel();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
