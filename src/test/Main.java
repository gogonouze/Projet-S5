package test;

import object.Client;
import object.Discussion;
import gui.Launcher;
import object.Server;
import object.User;

public class Main {

	public static void main(String[] args) {
		User arouf = new Client("Arouf");
		User me = new Client("Romain");
		Launcher.launch(arouf);
		Launcher.launch(me);
	}

}
