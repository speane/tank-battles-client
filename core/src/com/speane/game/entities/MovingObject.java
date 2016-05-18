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
    protected float rotation;

    public MovingObject(TextureRegion texture, Vector2 position, float rotation) {
        super(texture, position);
        this.rotation = rotation;
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
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
