package object;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Discussion {
	private String name;
	private NavigableSet<Message> messages = new TreeSet<>();
	private Group group;
	private int id_Conv;
	
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
	public void setGroup(Group group) {
		this.group = group;
	}
	
}
