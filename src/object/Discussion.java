package object;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Discussion implements Comparable<Discussion>{
	private String name;
	private TreeSet<Message> messages = new TreeSet<>();
	private List<User> group = new ArrayList<>();
	private int id_Conv;
	
	public void setId_Conv(int id_Conv) {
		this.id_Conv = id_Conv;
	}

	public Discussion(String name, Group group, Message message) {
		this.name = name;
		this.group = group.getGroup();
		messages.add(message);
	}
	
	public Discussion(String name, Group group, Message message, int id_Conv) {
		this.name = name;
		this.group = group.getGroup();
		this.id_Conv = id_Conv;
		messages.add(message);
	}
	
	public int getId_Conv() {
		return id_Conv;
	}

	public Discussion(String name, TreeSet<Message> messages, List<User> group, int id_Conv) {
		super();
		this.name = name;
		this.messages = messages;
		this.group = group;
		this.id_Conv = id_Conv;
	}
	
	public int compareTo(Discussion d) {
		return getId()-d.getId(); 
		
	}
	public boolean equals (Object obj) {
		if(obj!=null && obj instanceof Discussion) {
			Discussion other=(Discussion) obj;
			return this.id_Conv==other.id_Conv;
			
		}
		return false;
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
	public void debug_addMessage(Message message) {
		messages.add(message);
	}

	@Override
	public String toString() {
		return "Discussion [name=" + name + ", messages=" + messages + ", group=" + group + ", id_Conv=" + id_Conv
				+ "]";
	}
	public String toStringBis() {
		String retval ="";
		for(User user : group) {
			retval = retval+ user.getId()+"@";
		}
		return name+"@"+id_Conv+"@"+retval;
	}
	
}
