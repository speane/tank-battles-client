package com.speane.game.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public class Resourses {
    public static Texture tankTexture;
    public static Texture enemyTankTexture;
    public static Texture bulletTextTexture;
    public static Texture deadTankTexture;
    public static Music backgroundMusic;
    public static Sound hitSound;
    public static Sound shootSound;

    static {
        tankTexture = new Texture("tank.png");
        enemyTankTexture = new Texture("enemy.png");
        bulletTextTexture = new Texture("bullet.png");
        deadTankTexture = new Texture("deadtank.png");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("blast.wav"));
    }
}
