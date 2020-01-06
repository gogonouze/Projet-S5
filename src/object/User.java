package object;
import java.io.*;
import java.net.*;
import java.util.NavigableSet;
import java.util.TreeSet;

import java.io.*;
import java.net.*;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.sql.PooledConnection;

public abstract class User implements Runnable{
	private String name;
	ServerSocket server;
	PrintWriter output;
	NavigableSet<Group> groups = new TreeSet<Group>() ;
	NavigableSet<Discussion> discussions= new TreeSet<Discussion>();
	private static final int PORT = 8952;
	private int PORT_RECEPTION;
	Socket socket;
	InetAddress a;
	public String getNameUser() {
		return name;
	}
	public User(String name,Port_Pool p) {
		this.name = name;
		if(p!=null) {
		PORT_RECEPTION=p.selectPort();
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			server = new ServerSocket(PORT_RECEPTION);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			 a =InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	public void createReception() {
	}
	public void run() {
		try{
			while(!server.isClosed()){
				socket = server.accept();
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try{
							BufferedReader plec = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							boolean socketOuvert = true;
							while (socketOuvert) {
								try{
									String input = plec.readLine();
									if(input != null)
									{
										System.out.println(name);
										System.out.println("Message recu : "+input);
										//si cas de connection
										if(input.startsWith("@Message@")){
											input.replaceFirst("@Message@", "");
												String user="";
												String temp="";
												String discussion="";
												String message="";
												for(char car : input.toCharArray()) {
													if(car=='@') {
														user=discussion;
														discussion=temp;
														temp="";
													}
													else {
														temp.concat(Character.toString(car));
													}
												}
												message=temp;
												boolean exist_discussion=false;
												for(Discussion conv : discussions) {
													if(conv.getName()==discussion) {
														conv.getMessages().add(new Message(message));
														exist_discussion=true;
													}
												}
												if(!exist_discussion) {
													Discussion conv= new Discussion(discussion, 0, null, new Message(message));
													discussions.add(conv);
													request_discussion(discussion);
												}

											}
										else {
											if(input.startsWith("@NewD@")){
												input.replaceFirst("@Message@", "");
												Group group= new Group();
												String temp="";
												String discussion="";
												String id_conv="";
												int nb_dot=0;
												for(char car : input.toCharArray()) {
													if(car=='@') {
														if(nb_dot==0) {
															discussion=temp;
															temp="";
															nb_dot++;
														}
														else {
															if(nb_dot==1) {
																id_conv=temp;
																nb_dot++;
																temp="";
															}
															else {
																group.group.add(new Client(temp,null));
																temp="";

															}
														}
													}
													else {
														temp.concat(Character.toString(car));
													}
												}
												for(Discussion conv : discussions) {
													if (conv.getName()==discussion) {
														conv.group=group;
														conv.id_Conv=atoi(id_conv);
													}
												}
											}
										}
					
														
										}
								}catch(SocketException se){
									socketOuvert=false;
								}
							}
							socket.close();
						} catch (IOException e) {e.printStackTrace();}
					}

				});
				t.start();
			}
		}catch (IOException e){e.printStackTrace();}
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
	protected void request_discussion(String discussion) {
		output.println("@Rdiscussion@"+name+"@"+discussion);
		
	}
	public void connect() {
			output.println("@Connection@"+name+"@"+PORT_RECEPTION);
	}
	public void joinGroup(Group groupe) {
		groups.add(groupe);
		output.println("@joinGroup@"+name+"@"+groupe.getiD_group());
		
	}
	public void leaveGroup(Group groupe) {
		groups.remove(groupe);
		output.println("@leaveGroup@"+name+"@"+groupe.getiD_group());
		
	}
	public void sendMessage(String message ,Discussion discussion) {
		Message temp = new Message(message);
		discussion.getMessages().add(temp);
		output.println("@Message@"+name+"@"+discussion.getId()+"@"+temp.getMessage());
	}
	public void createConversation( String message, String name_conv ,Group group) {
		Message temp = new Message(message);
		output.println("@NeWMessage@"+name_conv+"@"+name+"@"+"@"+group.toString()+"@"+temp.getMessage());
	}
	public void leaveConversation (Discussion conversation) {
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
	public int getPort() {
		// TODO Auto-generated method stub
		return PORT_RECEPTION;
	}
	
}
