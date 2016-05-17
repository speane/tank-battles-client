package com.speane.game.help;

import com.speane.game.entities.Bullet;
import com.speane.game.entities.GameObject;
import com.speane.game.entities.Tank;

import java.util.Iterator;
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

    public void checkCollisions() {
        for (Tank firstEnemy : enemies.values()) {
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
                    player.hit();
                    Resourses.hitSound.play();
                }
            }
            iterator = player.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                if (isCollision(bullet, firstEnemy)) {
                    iterator.remove();
                    firstEnemy.hit();
                    player.addScore(200);
                    Resourses.hitSound.play();
                }
            }
        }
    }

    public boolean isOutOfField(GameObject object) {
        float x = object.getPosition().x;
        float y = object.getPosition().y;
        return  x > Config.DESKTOP_SCREEN_WIDTH ||
                x < 0 ||
                y > Config.DESKTOP_SCREEN_HEIGHT ||
                x < 0;
    }

    public boolean isCollision(GameObject first, GameObject second) {
        float left1 = first.getPosition().x;
        float rigth1 = left1 + first.getWidth();
        float bottom1 = first.getPosition().y;
        float top1 = bottom1 + first.getHeight();

        float left2 = second.getPosition().x;
        float right2 = left2 + second.getWidth();
        float bottom2 = second.getPosition().y;
        float top2 = bottom2 + second.getHeight();

        return (left1 < right2 &&
                left2 < rigth1 &&
                bottom1 < top2 &&
                bottom2 < top1);
    }
}
