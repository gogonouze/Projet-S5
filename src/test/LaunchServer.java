package test;

import gui.GuiServer;
import object.Server;

public class LaunchServer {
	public static void main(String[] args) {
		Server.launch();
		GuiServer.launch();
	}
}
