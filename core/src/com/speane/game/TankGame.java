package com.speane.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.speane.game.screens.StartScreen;

public class TankGame extends Game {
	private AssetManager assetManager = new AssetManager();

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
