package com.speane.game;

import com.badlogic.gdx.Game;
import com.speane.game.screens.GameScreen;

public class TankGame
		extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
