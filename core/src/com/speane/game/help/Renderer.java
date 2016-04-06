package com.speane.game.help;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.speane.game.entities.MovingObject;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public class Renderer {
    private Batch batch;
    private BitmapFont font;

    public Renderer(Batch batch) {
        this.batch = batch;
        font = new BitmapFont();
        font.getData().scale(2);
    }

    public void draw(MovingObject entity, Texture texture) {
        int width = entity.getWidth();
        int height = entity.getHeight();
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

    public void showMessage(String message) {
        font.draw(batch, message, Settings.DESKTOP_SCREEN_WIDTH / 2 - 100, Settings.DESKTOP_SCREEN_HEIGHT / 2 + 100);
    }
}
