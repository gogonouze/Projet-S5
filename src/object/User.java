package object;
import java.io.*;
import java.net.*;
import java.util.NavigableSet;
import java.util.TreeSet;


public abstract class User implements Runnable{
	private int id;
	private String name;
	OutputStreamWriter output;
	BufferedReader input ;
	NavigableSet<Group> groups = new TreeSet<Group>() ;
	NavigableSet<Discussion> discussions= new TreeSet<Discussion>();
	private static final int PORT = 8952;
	private int PORT_RECEPTION;
	Socket socket;
	Timer t= new Timer();
	public User(String name) {
		this.name = name;
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			socket.getKeepAlive();
			output = new OutputStreamWriter(socket.getOutputStream());
			input =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	public User(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static int getPort() {
		return PORT;
	}

	public String getNameUser() {
		return name;
	}

		public int atoi(String str) {
			if (str == null || str.length() < 1)
				return 0;

			// trim white spaces
			str = str.trim();

			char flag = '+';

			// check negative or positive
			int i = 0;
			if (str.charAt(0) == '-') {
				flag = '-';
				i++;
			} else if (str.charAt(0) == '+') {
				i++;
			}
			// use double to store result
			double result = 0;

			// calculate value
			while (str.length() > i && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				result = result * 10 + (str.charAt(i) - '0');
				i++;
			}

			if (flag == '-')
				result = -result;

			// handle max and min
			if (result > Integer.MAX_VALUE)
				return Integer.MAX_VALUE;

			if (result < Integer.MIN_VALUE)
				return Integer.MIN_VALUE;

			return (int) result;
		}

	protected void request_discussion(String discussion) throws IOException {
		output.write("@Rdiscussion@"+name+"@"+discussion);
		
	}

	public void connect() {
		String command="";	
		command="@Connection@"+name;
				try {
					output.write(command +"\n");
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		String reponse="";
		try {
			reponse = input.readLine();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		t.schedule(new Refresh(),1,5);
	}
	
	public void joinGroup(Group groupe) {
		groups.add(groupe);
		try {
			output.write("@joinGroup@"+name+"@"+groupe.getiD_group());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void leaveGroup(Group groupe) {
		groups.remove(groupe);
		try {
			output.write("@leaveGroup@"+name+"@"+groupe.getiD_group());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String message ,Discussion discussion) {
		Message temp = new Message(message);
		discussion.getMessages().add(temp);
		output.println("@Message@"+getPort()+"@"+discussion.getId()+"@"+temp.getMessage());
	}
	
	public void createConversation( String message, String name_conv ,Group group) {
		Message temp = new Message(message);
		try {
			output.write("@NeWMessage@"+name_conv+"@"+name+"@"+"@"+group.toString()+"@"+temp.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void leaveConversation (Discussion conversation) {
		try {
			output.write("@LeaveC@"+name+"@"+conversation.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.discussions.remove(conversation);
		
	}

	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}

	public NavigableSet<Discussion> getDiscussions() {
		return discussions;
	}
	
	public void debug_addDiscussion(Discussion discussion) {
		discussions.add(discussion);
	}
	private class Refresh extends TimerTask {

		@Override
		public void run() {
			String command="@Refresh@"+name;
					try {
						output.write(command +"\n");
						output.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			String reponse="";
			while(!reponse.equals(".")) {
				try {
					reponse = input.readLine();
					System.out.println(reponse);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}
