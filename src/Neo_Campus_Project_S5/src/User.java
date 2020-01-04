import java.io.*;
import java.net.*;
import java.util.TreeSet;

public class User {
	private String nameUser;
	private static final int PORT = 8952;
	Socket socket;
	public String getNameUser() {
		return nameUser;
	}
	ServerSocket server;
	PrintWriter output;
	TreeSet<Group> groups = new TreeSet<Group>() ;
	TreeSet<Discussion> discussions= new TreeSet<Discussion>();
	public void connect() {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.println("@Connection@"+nameUser+InetAddress.getLocalHost().toString());
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
		output.println("@joinGroup@"+nameUser+"@"+groupe.getiD_group());
		
	}
	public void leaveGroup(Group groupe) {
		groups.remove(groupe);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@leaveGroup@"+nameUser+"@"+groupe.getiD_group());
		
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
		output.println("@Message@"+nameUser+"@"+discussion.getId_Conv()+"@"+temp.getMessage());
	}
	public void createConversation( String message, Group group) {
		Message temp = new Message(message);
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@NeWMessage@"+nameUser+"@"+"@"+temp.getMessage()+"@"+group.getGroup().toString());
	}
	public void leaveConversation (Discussion conversation) {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@LeaveC@"+nameUser+"@"+conversation.getId_Conv());
		this.discussions.remove(conversation);
		
	}
	

}
