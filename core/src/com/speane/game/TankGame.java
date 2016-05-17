package com.speane.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.speane.game.screens.StartScreen;

public class TankGame extends Game {
	private AssetManager assetManager = new AssetManager();
	private String playerName;

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
