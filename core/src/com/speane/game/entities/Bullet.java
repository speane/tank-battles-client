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
        this.moveVector = new Vector2(0, 10);
        this.rotateSpeed = 3.f;
        this.moveVector.rotate(rotation);
        this.width = BULLET_TEXTURE.getRegionWidth();
        this.height = BULLET_TEXTURE.getRegionHeight();
    }
}
