package test;

import gui.Launcher;
import object.Client;

public class LaunchClient {
	public static void main(String[] args) {
		Launcher.launch(new Client(null));
	}
	
}
