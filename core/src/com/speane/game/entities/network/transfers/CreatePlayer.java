package com.speane.game.entities.network.transfers;

/**
 * Created by Speane on 10.03.2016.
 */
public class CreatePlayer implements NetworkTransfer {
    public int id;
    public float x;
    public float y;
    public float rotation;
    public int level;
    public int healthPoints;
    public String name;

    public CreatePlayer() {

    }

    public CreatePlayer(int id, float x, float y, float rotation, int level, int healthPoints, String name) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.level = level;
        this.healthPoints = healthPoints;
        this.name = name;
    }
}
