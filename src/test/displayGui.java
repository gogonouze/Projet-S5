package test;

import gui.Gui;
import gui.Launcher;
import gui.gui_element.PopUp_JoinGroup;
import gui.gui_element.PopUp_NewAccount;
import gui.gui_element.PopUp_NewDiscussion;
import gui.gui_element.PopUp_NewGroup;
import object.Client;
import object.User;

public class displayGui {

	public static void main(String[] args) {
		User user = new Client("oui");
		
		Gui.launch(user);
		Launcher.launch(user);
		PopUp_JoinGroup.launch(user);
		PopUp_NewGroup.launch(user);
		PopUp_NewAccount.launch(user);
		PopUp_NewDiscussion.launch(user);
		
		

	}

}
