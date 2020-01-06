package object;
import java.io.*;
import java.net.*;
import java.util.NavigableSet;
import java.util.TreeSet;

public abstract class User {
	private int id;
	private String name;
	private ServerSocket server;
	private PrintWriter output;
	private NavigableSet<Group> groups = new TreeSet<>() ;
	private NavigableSet<Discussion> discussions= new TreeSet<>();
	private static final int PORT = 8952;
	private Socket socket;
	
	public User(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getNameUser() {
		return name;
	}
	
	public void connect() {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.println("@Connection@"+name+InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void joinGroup(Group groupe) {
		groups.add(groupe);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@joinGroup@"+name+"@"+groupe.getiD_group());
		
	}
	
	public void leaveGroup(Group groupe) {
		groups.remove(groupe);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@leaveGroup@"+name+"@"+groupe.getiD_group());
		
	}
	
	public void sendMessage(String message ,Discussion discussion) {
		Message temp = new Message(message);
		discussion.getMessages().add(temp);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@Message@"+name+"@"+discussion.getId()+"@"+temp.getMessage());
	}
	
	public void createConversation( String message, Group group) {
		Message temp = new Message(message);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@NeWMessage@"+name+"@"+"@"+temp.getMessage()+"@"+group.getGroup().toString());
	}
	
	public void leaveConversation (Discussion conversation) {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@LeaveC@"+name+"@"+conversation.getId());
		this.discussions.remove(conversation);
		
	}

	public NavigableSet<Discussion> getDiscussions() {
		return discussions;
	}
	
	public Discussion getDiscussion( ) {
		/* TODO */ 
		return null;
	}
	
	public void debug_addDiscussion(Discussion discussion) {
		discussions.add(discussion);
	}
	
}
