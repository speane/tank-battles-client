package com.speane.game.help;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Evgeny Shilov on 17.05.2016.
 */
public class TextureManager {
    public static TextureRegion TANK_TEXTURE;
    public static TextureRegion DEAD_TANK_TEXTURE;
    public static TextureRegion ENEMY_TANK_TEXTURE;
    public static TextureRegion BULLET_TEXTURE;
    public static TextureRegion START_MENU_BACKGROUND_TEXTURE;
    public static TextureRegion PLAY_BUTTON_TEXTURE;
    public static TextureRegion PLAY_BUTTON_PRESSED_TEXTURE;

    public static void init(TextureAtlas atlas) {
        TANK_TEXTURE = atlas.findRegion("tank");
        DEAD_TANK_TEXTURE = atlas.findRegion("enemy");
        ENEMY_TANK_TEXTURE = atlas.findRegion("deadtank");
        BULLET_TEXTURE = atlas.findRegion("bullet");
        PLAY_BUTTON_TEXTURE = atlas.findRegion("play_button");
        PLAY_BUTTON_PRESSED_TEXTURE = atlas.findRegion("play_button_pressed");
        START_MENU_BACKGROUND_TEXTURE = atlas.findRegion("start_screen_background_image");
    }
}
