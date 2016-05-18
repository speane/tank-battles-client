package com.speane.game.help;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.speane.game.entities.GameObject;
import com.speane.game.entities.Tank;
import javafx.scene.shape.Rectangle;

import java.util.Map;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class CollisionDetector {
    private Tank player;
    private Map<Integer, Tank> enemies;

    public CollisionDetector(Map<Integer, Tank> enemies, Tank player) {
        this.player = player;
        this.enemies = enemies;
    }

    public static boolean collidesWithLayer(TiledMapTileLayer layer, Rectangle collisionModel) {
        float x = (float) collisionModel.getX();
        float y = (float) collisionModel.getY();
        float width = (float) collisionModel.getWidth();
        float height = (float) collisionModel.getHeight();
        int tileWidth = (int) layer.getTileWidth();
        int tileHeight = (int) layer.getTileHeight();

        int left = MathUtils.floor(x / tileWidth);
        int right = MathUtils.floor((x + width) / tileWidth);
        int bottom = MathUtils.floor(y / tileHeight);
        int top = MathUtils.floor((y + height) / tileHeight);

        for (int i = left; i <= right; i++) {
            for (int j = bottom; j <= top; j++) {
                if (layer.getCell(i, j) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    public void checkCollisions() {
        /*for (Tank firstEnemy : enemies.values()) {
            Iterator<Bullet> iterator = firstEnemy.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                for (Tank secondEnemy : enemies.values()) {
                    if (firstEnemy.ID != secondEnemy.ID && isCollision(bullet, secondEnemy)) {
                        iterator.remove();
                        secondEnemy.hit();
                        Resourses.hitSound.play();
                    }
                }

                if (isCollision(bullet, player)) {
                    iterator.remove();
                    //player.hit();
                    Resourses.hitSound.play();
                }
            }
            iterator = player.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                if (isCollision(bullet, firstEnemy)) {
                    iterator.remove();
                    //firstEnemy.hit();
                    //player.addScore(200);
                    Resourses.hitSound.play();
                }
            }
        }*/


    }

    public boolean isOutOfField(GameObject object) {
        float x = object.getX();
        float y = object.getY();
        return  x > Config.DESKTOP_SCREEN_WIDTH ||
                x < 0 ||
                y > Config.DESKTOP_SCREEN_HEIGHT ||
                x < 0;
    }

    public boolean isCollision(GameObject first, GameObject second) {
        float left1 = first.getX();
        float rigth1 = left1 + first.getWidth();
        float bottom1 = first.getY();
        float top1 = bottom1 + first.getHeight();

        float left2 = second.getX();
        float right2 = left2 + second.getWidth();
        float bottom2 = second.getY();
        float top2 = bottom2 + second.getHeight();

        return (left1 < right2 &&
                left2 < rigth1 &&
                bottom1 < top2 &&
                bottom2 < top1);
    }
}
