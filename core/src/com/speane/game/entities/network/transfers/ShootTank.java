package com.speane.game.entities.network.transfers;

/**
 * Created by Speane on 10.03.2016.
 */
public class ShootTank implements NetworkTransfer {
    public int id;
    public float rotation;
    public float x;
    public float y;

    public ShootTank() {

    }

    public ShootTank(int id, float rotation, float x, float y) {
        this.id = id;
        this.rotation = rotation;
        this.x = x;
        this.y = y;
    }
}
