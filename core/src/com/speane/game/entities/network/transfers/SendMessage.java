package com.speane.game.entities.network.transfers;

/**
 * Created by Evgeny Shilov on 23.05.2016.
 */
public class SendMessage implements NetworkTransfer {
    public int id;
    public String message;

    public SendMessage() {

    }

    public SendMessage(int id, String message) {
        this.message = message;
        this.id = id;
    }
}
