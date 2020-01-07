package test;

import object.Client;
import object.Discussion;
import object.Gui;
import object.User;

public class Main {

	public static void main(String[] args) {
		User user = new Client("alphonse", null);
		user.debug_addDiscussion(new Discussion("lol", 15, null, null));
		user.debug_addDiscussion(new Discussion("alpha", 1, null, null));
	}

}
