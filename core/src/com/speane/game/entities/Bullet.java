package com.speane.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static com.speane.game.help.TextureManager.BULLET_TEXTURE;

/**
 * Created by Speane on 10.03.2016.
 */
public class Bullet extends MovingObject {
    public Bullet(TextureRegion texture, float x, float y, float rotation) {
        super(texture, x, y, rotation);

        float START_MOVE_X_SPEED = 0;
        float START_MOVE_Y_SPEED = 10;
        float START_ROTATE_SPEED = 3.f;

        this.moveVector = new Vector2(START_MOVE_X_SPEED, START_MOVE_Y_SPEED);
        this.rotateSpeed = START_ROTATE_SPEED;
        this.moveVector.rotate(rotation);
        this.width = BULLET_TEXTURE.getRegionWidth();
        this.height = BULLET_TEXTURE.getRegionHeight();
    }
}
