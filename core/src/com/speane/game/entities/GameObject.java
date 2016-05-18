package com.speane.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import javafx.scene.shape.Rectangle;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public abstract class GameObject {
    protected TextureRegion texture;
    protected Rectangle collisionModel;
    protected Vector2 position;
    protected int width;
    protected int height;

    public GameObject(TextureRegion texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
        collisionModel = new Rectangle(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        collisionModel.setX(x);
        collisionModel.setY(y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getCollisionModel() {
        return collisionModel;
    }

    protected void updateCollisionModel() {
        collisionModel.setX(position.x);
        collisionModel.setY(position.y);
    }
}
