package com.speane.game.entities.network.transfers;

/**
 * Created by Speane on 10.03.2016.
 */
public class MoveTank {
    public int id;
    public float x;
    public float y;
    public float rotation;

    public MoveTank() {

    }

    public MoveTank(int id, int x, int y, int rotation) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }
}
