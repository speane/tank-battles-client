package com.speane.game;

/**
 * Created by Speane on 09.03.2016.
 */
public class SomeRequest {
    public SomeRequest(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }
    public SomeRequest() {}
    public String text;
    public int x;
    public int y;
}