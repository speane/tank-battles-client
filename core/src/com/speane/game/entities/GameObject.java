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
    protected float width;
    protected float height;

    public GameObject(TextureRegion texture, float x, float y) {
        this.texture = texture;
        position = new Vector2(x, y);
        collisionModel = new Rectangle(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
    }

    public int getX() {
        return (int) position.x;
    }

    public int getY() {
        return (int) position.y;
    }

    public void setX(int x) {
        position.x = x;
    }

    public void setY(int y) {
        position.y = y;
    }

    public void setPostion(float x, float y) {
        position.x = x;
        position.y = y;
        updateCollisionModel();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getCollisionModel() {
        return collisionModel;
    }

    protected void updateCollisionModel() {
        collisionModel.setX(position.x);
        collisionModel.setY(position.y);
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
