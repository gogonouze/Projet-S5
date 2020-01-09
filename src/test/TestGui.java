package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import object.Client;
import object.Discussion;
import gui.Gui;
import object.Message;
import object.User;
import object.Group;

public class TestGui {

	public static void main(String[] args) {
		User user = new Client("alphonse", 0) ;
		
		List<User> users = new ArrayList<>();
		users.add(user);
		Group group = new Group("alo", 12, users);
		
		Discussion d = new Discussion("alo", group, new Message("pute",user.getId(),1), 1);
		
		Message m = new Message("ta daronne la chaudasse",user.getId(),2);
		
		d.debug_addMessage(m);
		
		user.debug_addDiscussion(new Discussion("oh", group, new Message("",user.getId(),3)));
		
		user.debug_addDiscussion(d);
		
		d.debug_addMessage(new Message("fisbustier",user.getId(),4));
		d.debug_addMessage(new Message("henri le courtoix",user.getId(),5));
		
		
		System.out.println(d.toString());
		
		user.debug_addDiscussion(new Discussion("ah", group, new Message(""), 3));
		user.debug_addDiscussion(new Discussion("putaing", group, new Message("alo sale chien"),142));
		
		Gui.launch(user);
	}

}
