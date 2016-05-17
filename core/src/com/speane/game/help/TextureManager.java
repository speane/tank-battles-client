package com.speane.game.help;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Evgeny Shilov on 17.05.2016.
 */
public class TextureManager {
    public TextureRegion tankTexture;
    public TextureRegion deadTankTexture;
    public TextureRegion enemyTankTexture;
    public TextureRegion bulletTexture;

    public TextureManager(TextureAtlas atlas) {
        tankTexture = atlas.findRegion("tank");
        deadTankTexture = atlas.findRegion("deadtank");
        enemyTankTexture = atlas.findRegion("enemy");
        bulletTexture = atlas.findRegion("bullet");
    }
}
