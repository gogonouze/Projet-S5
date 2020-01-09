package test;

import object.Client;
import object.Discussion;
import object.Gui;
import object.Server;
import object.User;

public class Main {

	public static void main(String[] args) {
		System.out.println("test\n");
		User arouf = new Client("Arouf");
		arouf.create_account(arouf.getNameUser(),"Gangsta");
		arouf.createGroup("alo");
		arouf.requestGroup();
		arouf.createConversation("Fan d'arouf?", "Viva Arouf", arouf.getAllGroup().get(0));
	}

}
