package com.speane.game.entities.network.transfers;

/**
 * Created by Evgeny Shilov on 05.04.2016.
 */
public class DeadTank {
    public int id;
    public int killerID;

    public DeadTank() {

    }

    public DeadTank(int id, int killerID) {
        this.id = id;
        this.killerID = killerID;
    }
}
