package com.speane.game;

import com.badlogic.gdx.Game;
import com.speane.game.screens.StartScreen;

public class TankGame extends Game {

	@Override
	public void create() {
		//setScreen(new GameScreen());
		setScreen(new StartScreen(this));
	}
}
