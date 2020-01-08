package test;

import object.Client;
import object.Discussion;
import object.Gui;
import object.Server;
import object.User;

public class Main {

	public static void main(String[] args) {
		System.out.println("test\n");
		User gerard = new Client("gerard");
		User arouf = new Client("Arouf");
		gerard.create_account(gerard.getNameUser(), "NarutoRunner");
		arouf.create_account(arouf.getNameUser(),"Gangsta");
		gerard.createGroup("alo");
		arouf.requestGroup();
		arouf.createConversation("Fan d'arouf?", "Viva Arouf", arouf.getAllGroup().get(0));
		System.out.println(gerard.getDiscussions().first().getMessages().first().getMessage());
	}

}
