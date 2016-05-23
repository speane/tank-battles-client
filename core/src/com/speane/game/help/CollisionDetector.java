package com.speane.game.help;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.GameObject;
import com.speane.game.entities.Tank;
import com.speane.game.entities.network.transfers.DeadTank;
import com.speane.game.entities.network.transfers.HitTank;
import com.speane.game.screens.GameScreen;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Evgeny Shilov on 06.04.2016.
 */
public class CollisionDetector {
    private Tank player;
    private Map<Integer, Tank> enemies;
    private GameScreen gameScreen;
    private NetworkManager networkManager;

    public CollisionDetector(GameScreen gameScreen) {
        this.player = gameScreen.getPlayer();
        this.enemies = gameScreen.getEnemies();
        this.networkManager = gameScreen.getNetworkManager();
        this.gameScreen = gameScreen;
    }

    public static boolean collidesWithLayer(TiledMapTileLayer layer, Rectangle collisionModel) {
        float x = collisionModel.getX();
        float y = collisionModel.getY();
        float width = collisionModel.getWidth();
        float height = collisionModel.getHeight();
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
        for (Integer firstKey : enemies.keySet()) {
            Tank firstEnemy = enemies.get(firstKey);
            Iterator<Bullet> bulletIterator = firstEnemy.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                boolean bulletRemoved = false;
                for (Tank secondEnemy : enemies.values()) {
                    if (firstEnemy != secondEnemy) {
                        if (isCollision(bullet, secondEnemy)) {
                            bulletIterator.remove();
                            bulletRemoved = true;
                            break;
                        }
                    }
                }
                if (!bulletRemoved && isCollision(bullet, player)) {
                    bulletIterator.remove();
                    firstEnemy.hit(player);

                    int PLAYER_INIT_ID = 0;
                    HitTank hitTank = new HitTank(PLAYER_INIT_ID, firstEnemy.getDamage(), firstKey);
                    networkManager.sendEvent(hitTank);

                    if (player.isDead()) {
                        networkManager.sendEvent(new DeadTank(PLAYER_INIT_ID, firstKey));
                        gameScreen.setGameOver(true);
                        return;
                    }
                }
            }
            Iterator<Bullet> playerBulletIterator = player.getBullets().iterator();
            while (playerBulletIterator.hasNext()) {
                Bullet bullet = playerBulletIterator.next();
                if (isCollision(bullet, firstEnemy)) {
                    playerBulletIterator.remove();
                }
            }
        }
    }

    private boolean isCollision(GameObject first, GameObject second) {
        float left1 = first.getX();
        float right1 = left1 + first.getWidth();
        float bottom1 = first.getY();
        float top1 = bottom1 + first.getHeight();

        float left2 = second.getX();
        float right2 = left2 + second.getWidth();
        float bottom2 = second.getY();
        float top2 = bottom2 + second.getHeight();

        return (left1 < right2 &&
                left2 < right1 &&
                bottom1 < top2 &&
                bottom2 < top1);
    }
}
