package com.speane.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.speane.game.entities.MovingObject;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public class Renderer {
    private Batch batch;

    public Renderer(Batch batch) {
        this.batch = batch;
    }

    public void draw(MovingObject entity, Texture texture) {
        int width = texture.getWidth();
        int height = texture.getHeight();
        Vector2 coordinates = entity.getPosition();
        float rotation = entity.getRotation();
        batch.draw(
                texture,
                coordinates.x,
                coordinates.y,
                width / 2,
                height / 2,
                width,
                height,
                1,
                1,
                rotation,
                0,
                0,
                width,
                height,
                false,
                false
        );
    }
}
