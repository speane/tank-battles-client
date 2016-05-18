package com.speane.game.score;

/**
 * Created by Evgeny Shilov on 18.05.2016.
 */
public class GameScore {
    private int score;

    public void upScore(int delta) {
        score += delta;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "" + score;
    }
}
