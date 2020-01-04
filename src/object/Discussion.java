package object;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Discussion {
	String name;
	NavigableSet<Message> messages = new TreeSet<>();
	Group group;
	int id_Conv;
	
	public Discussion(String name, int id, Group group, Message message) {
		this.name = name;
		id_Conv = id;
		this.group = group;
		messages.add(message);
	}

	public NavigableSet<Message> getMessages() {
		return messages;
	}
	
	public int getId() {
		return id_Conv;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public String getName() {
		return name;
	}
}
