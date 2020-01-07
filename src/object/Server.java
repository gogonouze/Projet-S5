package object;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Server implements Runnable{
	
	private static final int PORT = 8952;
	Socket socket;
	ServerSocket server;
	
	//crée le serveur
	public Server(){
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
										System.out.println("Message recu : "+input);
										//si cas de connection
										if(input.startsWith("@Connection@")){
											input.replaceFirst("@Connection@", "");
											String user=input;
											connect_user(user);
											refresh(user);
										}
										else {
											//tentative de rejoindre un groupe
											if(input.startsWith("@joinGroup@")){
												input.replaceFirst("@joinGroup@", "");
												String user="";
												String temp="";
												String group="";
												for(char car : input.toCharArray()) {
													if(car=='@') {
														user=temp;
														temp="";
													}
													else {
														temp.concat(Character.toString(car));
													}
												}
												group=temp;
												adduserGBDD(user,group);
											}
											else {
												//quitte un groupe
												if(input.startsWith("@leaveGroup@")){
													input.replaceFirst("@leaveGroup@", "");
													String user="";
													String temp="";
													String group="";
													for(char car : input.toCharArray()) {
														if(car=='@') {
															user=temp;
															temp="";
														}
														else {
															temp.concat(Character.toString(car));
														}
													}
													group=temp;
													deleteuserGBDD(user,group);
												}
												else {
													//message envoyé le serveur va retransmettre le message
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
														updateBDDMessage(user,discussion,message);
														retransmettreMessage(user,discussion,message);

												}
													else {
														//Quitte une conversation
														if(input.startsWith("@LeaveC@")){
															input.replaceFirst("@LeaveC@", "");
															String user="";
															String temp="";
															String discussion="";
															for(char car : input.toCharArray()) {
																if(car=='@') {
																	user=temp;
																	temp="";
																}
																else {
																	temp.concat(Character.toString(car));
																}
															}
															discussion=temp;
															updateLeaveConv(user,discussion);

													}
														//Créer une conversation
														if(input.startsWith("@NeWMessage@")) {
															input.replaceFirst("@NeWMessage@", "");
															Group group= new Group();
															String user="";
															String temp="";
															String discussion="";
															String message="";
															int nbdot =0;
															for(char car : input.toCharArray()) {
																if(car=='@') {
																	if(nbdot==0) {
																		discussion=temp;
																		temp="";
																		nbdot++;
																	}
																	else {
																		if(nbdot==1) {
																			user=temp;
																			group.group.add(getUser(atoi(user)));
																			nbdot++;
																			temp="";
																		}
																		else {
																			group.group.add(getUser(atoi(temp)));
																			temp="";

																		}
																	}
																}
																else {
																	temp.concat(Character.toString(car));
																}
															}
															message=temp;
															addDiscussion(discussion,group);
															updateBDDMessage(user,discussion,message);
															retransmettreMessage(user,discussion,message);
														}
														else {
															if(input.startsWith("@Rdiscussion@")) {
																input.replaceFirst("@Rdiscussion@", "");
																String user="";
																String temp="";
																String discussion="";
																for(char car : input.toCharArray()) {
																	if(car=='@') {
																		user=temp;
																		temp="";
																	}
																	else {
																		temp.concat(Character.toString(car));
																	}
																}
																discussion=temp;
																giveDiscussion(getDiscussion(atoi(discussion)),user);
																
															}
														}
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
	
	protected void giveDiscussion(Discussion discussion, String user) {
		Socket socket;
		PrintWriter output = null;
		try {
			socket = new Socket(InetAddress.getLocalHost(),getUser(atoi(user)).getPort());//"192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.println("@NewD@"+discussion.toString());
	}
	
	// J'ai ajoute des atoi et remplace getId par getPort. Du coup dans user y a l'id/port et les ports sont differents en theorie
	protected void retransmettreMessage(String user, String discussion, String message) {
		Socket socket;
		PrintWriter output = null;
		for( User client : getDiscussion(atoi(discussion)).getGroup() ){
			if(client.getNameUser()!=user && isconnected(client.getPort())) {
				try {
					socket = new Socket(InetAddress.getLocalHost(),getUser(atoi(user)).getPort());//"192.168.43.95", PORT);
					output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				output.println("@Message@"+user+"@"+discussion+"@"+message);
				updateStatus(message,user);
				
			}
		}
		
	}

	private boolean isconnected(int id) {
		
		Connection con;
		Boolean connected = false;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "IsConnected FROM bdd_projet_s5.user");
			
			while (rst.next()) {
				int idU = rst.getInt("IdU");
				if (id == idU) {
					if (rst.getInt("IsConnected") == 1){
						connected = true;
					}
					else {
						connected = false;
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connected;
	}
	
	// Permet de recuperer un message identifie avec son id dans la base de donnees
	private Message getMessage(int id) {
		
		Message m = null;
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdM ," + "Content ," + "IsRead ," + "Time ," + "NbVu FROM bdd_projet_s5.message");
			while (rst.next()) {
				int idM = rst.getInt("IdM");
				if (idM == id) {
					String content = rst.getString("Content");
					int isRead = rst.getInt("IsRead");
					String time = rst.getString("Time");
					int nbVu = rst.getInt("NbVu");
					Status status = null;
					if (isRead == 0) {
						status = Status.wait;
					} else if (isRead == 1) {
						status = Status.received;
					} else {
						status = Status.viewed;
					}
					m = new Message(content, status, time, idM, nbVu);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return m;
	}
	
	private Discussion getDiscussion(int id) {
		
		Discussion d = null;
		String name = "";
		TreeSet<Message> messages = new TreeSet<>();
		List<User> group = new ArrayList<>() ;
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdD ," + "Name FROM bdd_projet_s5.discussion");
			while (rst.next()) {
				int idD = rst.getInt("IdD");
				if (idD == id) {
					name = rst.getString("Name");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdD ," + "IdM FROM bdd_projet_s5.message");
			while (rst.next()) {
				int idD = rst.getInt("IdD");
				if (idD == id) {
					messages.add(getMessage(rst.getInt("IdM")));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "IdD FROM bdd_projet_s5.appartenirud");
			while (rst.next()) {
				int idD = rst.getInt("IdD");
				if (idD == id) {
					group.add(getUser(rst.getInt("IdU")));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		d = new Discussion(name, messages, group, id);
		
		return d;
	}
	
	protected void addDiscussion(String discussion, Group group, int id) {
		
		Connection con;
		int idU;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO discussion (IdD, Name) VALUES ('" + id + "', '" + discussion + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (User u : group.getGroup()) {
			idU = u.getPort();
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO appartenirud (IdU, IdD) VALUES ('" + idU + "', '" + id + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// actuellement tous les User sont des clients
	private User getUser(int id) {
		
		User u = null;
		String name;
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "Name FROM bdd_projet_s5.user");
			while (rst.next()) {
				int idU = rst.getInt("IdU");
				if (idU == id) {
					name = rst.getString("Name");	
					u = new Client(name, id);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return u;
	}
	
	protected void refresh(String user) {
		//renvoie tout les message non lus renvoyés par getAllUnviewedMessage sous la forme "@Envoyeur@Discussion@contenu@Date" 
		LinkedList<String> unviewedmessage=getAllUnviewedMessage(user);
		Socket socket;
		PrintWriter output = null;
		if(unviewedmessage!=null) {
			for(String message : unviewedmessage) {
				try {
					socket = new Socket(InetAddress.getLocalHost(),getUser(atoi(user)).getPort());//"192.168.43.95", PORT);
					output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				output.println("@Message@"+user+"@"+message);
				updateStatus(message,user);
			}
				
		}

	}

	// Les deux String doivent correspondre aux id. String user sert � rien
	private void updateStatus(String message, String user) {
		int idm = atoi(message);
		int idd = 0;
		Message m = getMessage(idm);
		System.out.println("getmessage nbvu = " + m.getNbVu());
		Connection con;
		Connection con2;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdM ," + "IdD FROM bdd_projet_s5.message");
			while (rst.next()) {
				int idM = rst.getInt("IdM");
				if (idM == m.getId()) {
					idd = rst.getInt("IdD");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (m.received(getDiscussion(idd).getGroup().size())) {
			System.out.println("dans le if");
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE message SET IsRead = 1 WHERE IdM = " + idm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Dans le else");
			try {
				con2 = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
				Statement stmt = con2.createStatement();
				stmt.executeUpdate("UPDATE message SET NbVu = " + m.getNbVu() + " WHERE IdM = " + idm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String getIp(String user) {
		// TODO Auto-generated method stub
		// C'est quoi cette Ip ?
		return null;
	}

	private LinkedList<String> getAllUnviewedMessage(String user) {
		// TODO Auto-generated method stub
		// C'est pas personnel les message non vu 
		return null;
	}

	protected void updateLeaveConv(String user, String discussion) {
		// TODO Auto-generated method stub
		// Mais c'est une conv de groupe, tu dois pas quiter le groupe ?
	}

	protected void updateBDDMessage(String user, String group, String message) {
		// ajout message à bdd, si il y est dejà le message est lu
		// Il est lu pour tout le monde
	}

	protected void deleteuserGBDD(String user, String group) {
		// delete un user d'un groupe
		
	}

	protected void adduserGBDD(String user, String group) {
		// ajoute un user à un groupe
		
	}

	protected void adduserBDD(String user, String group) {
		// ajoute un user, pas dans la bdd ? On l'ajoute
		// Pourquoi group ?
		
	}

	protected void connect_user(String user, String ip_Adress) {
		// TODO Auto-generated method stub
		// Pourquoi ip_Adress ?
		
	}

	public static void main(String[] args){
		Server c = new Server();
		Thread t = new Thread(c);
		t.start();
	}
}
