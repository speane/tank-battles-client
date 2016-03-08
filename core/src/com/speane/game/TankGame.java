package com.speane.game;

import com.badlogic.gdx.Game;

public class TankGame
		extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
