package com.speane.game.help;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        font.setColor(Color.BLUE);
    }

    public void drawText(String text, float x, float y) {
        font.draw(batch, text, x, y);
    }

    public void draw(MovingObject entity) {
        batch.draw(
                entity.getTexture(),
                entity.getX(),
                entity.getY(),
                entity.getTexture().getRegionWidth() / 2,
                entity.getTexture().getRegionHeight() / 2,
                entity.getTexture().getRegionWidth(),
                entity.getTexture().getRegionHeight(),
                1,
                1,
                entity.getRotation()
        );
    }
}
