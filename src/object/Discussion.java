package object;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Discussion {
	private String name;
	private TreeSet<Message> messages = new TreeSet<>();
	private List<User> group = new ArrayList<>();
	private int id_Conv;
	
	public Discussion(String name, int id, Group group, Message message) {
		this.name = name;
		id_Conv = id;
		this.group = group.getGroup();
		messages.add(message);
	}
	
	public Discussion(String name, TreeSet<Message> messages, List<User> group, int id_Conv) {
		super();
		this.name = name;
		this.messages = messages;
		this.group = group;
		this.id_Conv = id_Conv;
	}

	public TreeSet<Message> getMessages() {
		return messages;
	}
	
	public int getId() {
		return id_Conv;
	}
	
	public List<User> getGroup() {
		return group;
	}
	
	public String getName() {
		return name;
	}
	public void setGroup(Group group) {
		this.group = group.getGroup();
	}

	@Override
	public String toString() {
		return "Discussion [name=" + name + ", messages=" + messages + ", group=" + group + ", id_Conv=" + id_Conv
				+ "]";
	}
	
}
