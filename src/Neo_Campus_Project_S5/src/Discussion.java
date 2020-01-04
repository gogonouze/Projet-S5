import java.util.TreeSet;

public class Discussion {
	private String name;
	private TreeSet<Message> messages= new TreeSet<Message>();
	TreeSet<User> groups = new TreeSet<User>() ;
	private int id_Conv;
	
	public Discussion(String name, int id_Conv) {
		super();
		this.name = name;
		this.id_Conv = id_Conv;
	}
	public String getName() {
		return name;
	}
	public TreeSet<Message> getMessages() {
		return messages;
	}
	public TreeSet<User> getGroups() {
		return groups;
	}
	public int getId_Conv() {
		return id_Conv;
	}
	
}
