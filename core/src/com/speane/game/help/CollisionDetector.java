package com.speane.game.help;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.GameObject;
import com.speane.game.entities.Tank;
import com.speane.game.screens.GameScreen;
import com.speane.game.transfers.DeadTank;
import com.speane.game.transfers.HitTank;

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
        Iterator<Integer> firstEnemyKeyIterator = enemies.keySet().iterator();
        while (firstEnemyKeyIterator.hasNext()) {
            int firstKey = firstEnemyKeyIterator.next();
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
                    HitTank hitTank = new HitTank();
                    hitTank.damage = firstEnemy.getDamage();
                    hitTank.shooterID = firstKey;
                    networkManager.sendEvent(hitTank);

                    if (player.isDead()) {
                        DeadTank deadTank = new DeadTank();
                        deadTank.killerID = firstKey;
                        networkManager.sendEvent(deadTank);
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


        /*Iterator<Tank> enemyIterator = enemies.values().iterator();
        while (enemyIterator.hasNext()) {
            Tank enemy = enemyIterator.next();
            Iterator<Bullet> bulletIterator = enemy.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (isCollision(player, bullet)) {
                    bulletIterator.remove();
                    enemy.hit(player);
                    if (player.isDead()) {
                        networkManager.sendEvent(new DeadTank());
                        gameScreen.setGameOver(true);
                        return;
                    }
                    else {
                        HitTank hitTank = new HitTank();
                        hitTank.damage = enemy.getDamage();
                        networkManager.sendEvent(hitTank);
                    }
                }
            }
            Iterator<Bullet> playerBulletIterator = player.getBullets().iterator();
            while (playerBulletIterator.hasNext()) {
                Bullet bullet = playerBulletIterator.next();
                if (isCollision(bullet, enemy)) {
                    int oldScore = gameScreen.getScore();
                    gameScreen.setScore(oldScore
                            + ((int) (Config.SCORE_FOR_HIT / ((double) player.getLevel() / enemy.getLevel()))));
                    playerBulletIterator.remove();
                    if (enemy.isDead()) {
                        gameScreen.setScore(oldScore +
                                ((int) (Config.SCORE_FOR_KILL / ((double) player.getLevel() / enemy.getLevel()))));
                    }
                    gameScreen.setNextLevelScore(gameScreen.getNextLevelScore() + gameScreen.getScore() - oldScore);
                    if (gameScreen.getNextLevelScore() >= Config.LEVEL_UP_SCORE) {
                        int levelsToUp = gameScreen.getNextLevelScore() / Config.LEVEL_UP_SCORE;
                        player.levelUp(levelsToUp);
                        player.addHealthPoints(Config.HEALTH_POINTS_FOR_LEVEL_UP);
                        LevelUp levelUp = new LevelUp();
                        levelUp.level = levelsToUp;
                        levelUp.healthPoints = Config.HEALTH_POINTS_FOR_LEVEL_UP;
                        networkManager.sendEvent(levelUp);
                        gameScreen.setNextLevelScore(gameScreen.getNextLevelScore() % Config.LEVEL_UP_SCORE);
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
