package com.speane.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.speane.game.TankGame;
import com.speane.game.help.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Settings.DESKTOP_SCREEN_WIDTH;
		config.height = Settings.DESKTOP_SCREEN_HEIGHT;
		TexturePacker.process("../assets", "../assets", "tank_battles_assets");
		new LwjglApplication(new TankGame(), config);
	}
}
