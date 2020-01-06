package test;

import object.Client;
import object.Discussion;
import object.Gui;
import object.User;

public class Main {

	public static void main(String[] args) {
		User user = new Client("alphonse");
		user.debug_addDiscussion(new Discussion("aled", 0, null, null));
		
		
		Gui.launch(user);
	}

}
