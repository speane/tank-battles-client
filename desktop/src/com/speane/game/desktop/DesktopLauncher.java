package com.speane.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.speane.game.TankGame;
import com.speane.game.help.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Settings.DESKTOP_SCREEN_WIDTH;
		config.height = Settings.DESCTOP_SCREEN_HEIGHT;
		Settings.PORT = Integer.parseInt(arg[Settings.PORT_INDEX]);
		Settings.SERVER_IP = arg[Settings.SERVER_IP_INDEX];
		new LwjglApplication(new TankGame(), config);
	}
}
