package object;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Server implements Runnable{
	private static final int PORT = 8952;
	Socket socket;
	ServerSocket server;
	HashMap<Integer, BufferedWriter> communication= new HashMap<Integer, BufferedWriter>(); 
	//crÃ©e le serveur
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
										if(input.startsWith("@Connection@")){
											input = input.replaceFirst("@Connection@","");;
											String user="";
											String temp="";
											String password="";
											String id ="";
											for(char car : input.toCharArray()) {
												if(car=='@') {
													user=password;
													password=temp;
													temp="";
												}
												else {
													temp=temp+String.valueOf(car);
												}
											}
											id=temp;
											
											connect_user(user,password,id,socket);
										}
										else {
											//tentative de rejoindre un groupe
											if(input.startsWith("@joinGroup@")){
												input=input.replaceFirst("@joinGroup@", "");
												String user="";
												String temp="";
												String group="";
												for(char car : input.toCharArray()) {
													if(car=='@') {
														user=temp;
														temp="";
													}
													else {
														temp=temp+String.valueOf(car);
													}
												}
												group=temp;
												adduserGBDD(atoi(user),atoi(group));
											}
											else {
												//quitte un groupe
												if(input.startsWith("@leaveGroup@")){
													input=input.replaceFirst("@leaveGroup@", "");
													String user="";
													String temp="";
													String group="";
													for(char car : input.toCharArray()) {
														if(car=='@') {
															user=temp;
															temp="";
														}
														else {
															temp=temp+String.valueOf(car);
														}
													}
													group=temp;
													deleteuserGBDD(atoi(user),atoi(group));
												}
												else {
													//message envoyÃ© le serveur va retransmettre le message
													if(input.startsWith("@Message@")){
														input=input.replaceFirst("@Message@", "");
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
																temp=temp+String.valueOf(car);
															}
														}
														message=temp;
														int i=updateBDDMessage(getUser(atoi(user)),getDiscussion(atoi(discussion)),message);
														communication.get(atoi(user)).write(i+"\n");
														communication.get(atoi(user)).flush();
												}
													else {
														//Quitte une conversation
														if(input.startsWith("@LeaveC@")){
															input=input.replaceFirst("@LeaveC@", "");
															String user="";
															String temp="";
															String discussion="";
															for(char car : input.toCharArray()) {
																if(car=='@') {
																	user=temp;
																	temp="";
																}
																else {
																	temp=temp+String.valueOf(car);
																}
															}
															discussion=temp;
															updateLeaveConv(atoi(user),atoi(discussion));

													}
														//CrÃ©er une conversation
														if(input.startsWith("@NeWMessage@")) {
															input=input.replaceFirst("@NeWMessage@", "");
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
																			nbdot++;
																			temp="";
																		}
																		else {
																			group=getGroupBDD(atoi(temp));
																			temp="";

																		}
																	}
																}
																else {
																	temp=temp+String.valueOf(car);
																}
																
															}
															message=temp;
															group.getGroup().add(getUser(atoi(user)));
															int i =addDiscussion(discussion,group);
															int j=updateBDDMessage(getUser(atoi(user)),getDiscussion(atoi(discussion)),message);
															
															communication.get(atoi(user)).write(i+"@"+j+"\n");
															communication.get(atoi(user)).flush();
														}
														else {
															if(input.startsWith("@Rdiscussion@")) {
																input=input.replaceFirst("@Rdiscussion@", "");
																String user="";
																String temp="";
																String discussion="";
																for(char car : input.toCharArray()) {
																	if(car=='@') {
																		user=temp;
																		temp="";
																	}
																	else {
																		temp=temp+String.valueOf(car);
																	}
																}
																discussion=temp;
																giveDiscussion(getDiscussion(atoi(discussion)),user);
																
															}
															else {
																if(input.startsWith("@Refresh@")){
																	input=input.replaceFirst("@Refresh@", "");
																	refresh(input);
																}
																else {
																	if(input.startsWith("@ack@")) {
																		input=input.replaceFirst("@ack@", "");
																		updateStatus(input);
																	}
																	else {
																		if(input.startsWith("@createAccount@")) {
																			input=input.replaceFirst("@createAccount@", "");
																			String user="";
																			String temp="";
																			String password="";
																			for(char car : input.toCharArray()) {
																				if(car=='@') {
																					user=temp;
																					temp="";
																				}
																				else {
																					temp=temp+String.valueOf(car);
																				}
																			}
																			password=temp;
																			create_user(user,password,socket);
																		}
																		else {
																			if(input.startsWith("@requestGroup@")) {
																				input=input.replaceFirst("@requestGroup@", "");
																				sendAllGroup(atoi(input));
																			}
																			else {
																				if(input.startsWith("@disconnect@")) {
																					input=input.replaceFirst("@disconnect@", "");
																					disconnectUser(atoi(input));
																				}
																				else {
																					if(input.startsWith("@createGroup@")) {
																						input=input.replaceFirst("@createGroup@", "");
																						String nameGroup="";
																						String temp="";
																						int id_user=0;
																						for(char car : input.toCharArray()) {
																							if(car=='@') {
																								nameGroup=temp;
																								temp="";
																							}
																							else {
																								temp=temp+String.valueOf(car);
																							}
																						}
																						id_user=atoi(temp);
																						addGroup(nameGroup,id_user);
																					}
																					else {
																						if(input.startsWith("@RName@")) {
																							input=input.replaceFirst("@RName@", "");
																							int id_user=0;
																							int idToKnow=0;
																							String temp="";
																							for(char car : input.toCharArray()) {
																								if(car=='@') {
																									id_user=atoi(temp);
																									temp="";
																								}
																								else {
																									temp=temp+String.valueOf(car);
																								}
																							}
																							idToKnow=atoi(temp);
																							sendUser(id_user,idToKnow);
																						}
																					}
																				}
																			}
																		}
																	}
																}
																
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
						} catch (IOException e) {e.printStackTrace();}
					}


					

					
				});
				t.start();
			}
		}catch (IOException e){e.printStackTrace();}
	}
	



	protected void sendUser(int id_user, int idToKnow) {
		try {
			communication.get(id_user).write(getUser(idToKnow).getNameUser()+"\n");
			communication.get(id_user).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	protected void disconnectUser(int id) {
		
	}
	
	private void addGroup(String nameGroup, int id_user) {
		int id_group = addGroupBDD(nameGroup);
		adduserGBDD(id_user, id_group);
		try {
			communication.get(id_user).write(id_group+"\n");
			communication.get(id_user).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



	protected void sendAllGroup(int id) {
		List<Group> zbreh= getAllGroup();
		if(zbreh!=null) {
			for(Group arouf : zbreh) {
				try {
						communication.get(id).write(arouf.BetterToString()+"\n");
						communication.get(id).flush();
					}
				 catch (IOException e) {
				e.printStackTrace();
			}
				}
			
		}
		try {
			communication.get(id).write(".\n");
			communication.get(id).flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	protected void create_user(String user, String password, Socket s) {
		Integer id = adduserBDD(user, password);
		try {
			communication.put(id, new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			communication.get(id).write(id.toString()+"\n");
			communication.get(id).flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	protected void giveDiscussion(Discussion discussion, String user) {
		int id_user=atoi(user);
		try {
				communication.get(id_user).write(discussion.toStringBis()+"\n");
				communication.get(id_user).flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	// renvoie l'id de la discussion
	protected int addDiscussion(String discussion, Group group) {
		
		Connection con;
		int idU;
		int id = 0;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT NbD FROM bdd_projet_s5.discussion");
			if (rst.next()) {
				id = rst.getInt("NbD") + 1;
			}
			else {
				id = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO discussion (IdD, Name) VALUES ('" + id + "', '" + discussion + "')");
			stmt.executeUpdate("UPDATE discussion SET nbD = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (User u : group.getGroup()) {
			idU = u.getId();
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO appartenirud (IdU, IdD) VALUES ('" + idU + "', '" + id + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
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
		int id_user=atoi(user);
		//renvoie tout les message non lus renvoyÃ©s par getAllUnviewedMessage sous la forme "Envoyeur@Discussion@Date@contenu" 
		LinkedList<String> unviewedmessage=getAllMessage(id_user);
		if(!unviewedmessage.isEmpty()) {
			for(String message : unviewedmessage) {
				try {
						communication.get(id_user).write(message+"\n");
						communication.get(id_user).flush();
					}
				 catch (IOException e) {
				e.printStackTrace();
			}
				}
			
		}
		try {
			communication.get(id_user).write(".\n");
			communication.get(id_user).flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	private void updateStatus(String message) {
		
	}
	// Les deux String doivent correspondre aux id. String user sert Ã  rien
	/*private void updateStatus(String message) {
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
	}*/

private LinkedList<String> getAllMessage(int id) {
		
		Connection con;
		
		LinkedList<String> l = new LinkedList<>();
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU, " + "IdD FROM bdd_projet_s5.appartenirud");
			while (rst.next()) {
				int idU = rst.getInt("IdU");
				if (idU == id) {
					int idD = rst.getInt("IdD");
					try {
						Statement stmt2 = con.createStatement();
						ResultSet rst2 = stmt2.executeQuery("SELECT IdM, " + "IdU, " + "IdD, " + "Content, " + "Time FROM bdd_projet_s5.message");
						while (rst2.next()) {
							int idD2 = rst2.getInt("IdD");
							if (idD2 == idD) {
								int idM = rst2.getInt("IdM");
								int idU2 = rst2.getInt("IdU");
								if (idU2 != idU) {
									try {
										Statement stmt3 = con.createStatement();
										ResultSet rst3 = stmt3.executeQuery("SELECT Name, " + "IdU FROM bdd_projet_s5.user");
										while (rst3.next()) {
											int idU3 = rst3.getInt("IdU");
											if (idU3 == idU2) {
												//String name = rst3.getString("Name");
												String content = rst2.getString("Content");
												String time = rst2.getString("Time");
												String chaine = "@" + idU2 + "@" + idD + "@" + time + "@" + idM + "@" + content	;
												l.add(chaine);
											}
										}
										
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							}
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return l;
	}

	protected void updateLeaveConv(int idUser, int idDiscussion) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM appartenirud WHERE IdU = " + idUser + " AND IdD = " + idDiscussion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void updateRejoinConv(int idUser, int idDiscussion) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO appartenirud (IdU, IdD) VALUES ('" + idUser + "', '" + idDiscussion + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// renvoie l'id du message
	protected int updateBDDMessage(User user, Discussion discussion, String message) {
		
		Connection con;
		int nbm = 0;
		Message m = new Message(message);
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT NbM FROM bdd_projet_s5.message");
			if (rst.next()) {
				nbm = rst.getInt("NbM") + 1;
			}
			else {
				nbm = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO message (IdM, Content, IsRead, Time, IdU, IdD) VALUES ('" + nbm + "', '" + message + "', '" + 0 + "', '" + m.getDateCreation() + "', '" + user.getId() + "', '" + discussion.getId() + "')");
			stmt.executeUpdate("UPDATE message SET nbM = " + nbm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nbm;
	}

	protected Group getGroupBDD(int id) {
		
		Group g = null;
		String name = "";
		List<User> group =new ArrayList<User>();
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdG ," + "Name FROM bdd_projet_s5.groupe");
			while (rst.next()) {
				int idG = rst.getInt("IdG");
				if (idG == id) {
					name = rst.getString("Name");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "IdG FROM bdd_projet_s5.appartenirug");
			while (rst.next()) {
				int idG = rst.getInt("IdG");
				if (idG == id) {
					group.add(getUser(rst.getInt("IdU")));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		g = new Group(name, id, group);
		
		return g;
	}
	
	protected void deleteuserGBDD(int idUser, int idGroup) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM appartenirug WHERE IdU = " + idUser + " AND IdG = " + idGroup);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void adduserGBDD(int idUser, int idGroup) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO appartenirug (IdU, IdG) VALUES ('" + idUser + "', '" + idGroup + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// renvoie l'id de l'user ajouté
	protected  int adduserBDD(String user, String password) {
		
		Connection con;
		int nbu = 0;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT NbU FROM bdd_projet_s5.user");
			if (rst.next()) {
				nbu = rst.getInt("NbU") + 1;
			}
			else {
				nbu = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO user (IdU, Name, Password, IsConnected) VALUES ('" + nbu + "', '" + user + "', '" + password + "', '" + 1 +"')");
			stmt.executeUpdate("UPDATE user SET nbU = " + nbu);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nbu;
	}

	// renvoie l'id du groupe ajouté
	protected int addGroupBDD(String name) {
		
		Connection con;
		int nbg = 0;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT NbG FROM bdd_projet_s5.groupe");
			if (rst.next()) {
				nbg = rst.getInt("NbG") + 1;
			}
			else {
				nbg = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO groupe (IdG, Name) VALUES ('" + nbg + "', '" + name + "')");
			stmt.executeUpdate("UPDATE groupe SET nbG = " + nbg);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nbg;
	}
	
	protected void connect_user(String user, String password, String id, Socket s) throws IOException {
		List<Integer> id_users=getUser(user);
		Integer id_user=0;
		for(Integer i : id_users) {
			if(matchpassword(i,password)) {
				id_user=i;
				boolean is_present =false;
				is_present=communication.containsKey(id_user);
				if (is_present) {
					communication.replace(id_user, new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
					
				}
				else {
					communication.put(id_user, new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
				}
			try {
					communication.get(id_user).write(id_user.toString()+"\n");
					communication.get(id_user).flush();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	if(id_user==0) {
			BufferedWriter temp = new BufferedWriter((new OutputStreamWriter(s.getOutputStream())));
			temp.write("WrongPassword\n");
			temp.flush();
		}
	}
	 private List<Integer> getUser(String name) {
	        
	        List<Integer> lu = new ArrayList<>();
	        Connection con;
	        
	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
	            Statement stmt = con.createStatement();
	            ResultSet rst = stmt.executeQuery("SELECT IdU ," + "Name FROM bdd_projet_s5.user");
	            while (rst.next()) {
	                String n = rst.getString("Name");
	                if (n.equals( name)) {
	                    int idU = rst.getInt("IdU");
	                    lu.add(idU);
	                }
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return lu;
	    }
	public List<Group> getAllGroup(){
		
		List<Group> lg = new ArrayList<>();
		List<User> lu;
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdG ," + "Name FROM bdd_projet_s5.groupe");
			while (rst.next()) {
				int idG = rst.getInt("IdG");
				String name = rst.getString("Name");
				lu = new ArrayList<>();
				try {
					Statement stmt2 = con.createStatement();
					ResultSet rst2 = stmt2.executeQuery("SELECT IdU ," + "IdG FROM bdd_projet_s5.appartenirug");
					while (rst2.next()) {
						int idG2 = rst2.getInt("IdG");
						if (idG2 == idG) {
							int idU = rst2.getInt("IdU");
							lu.add(getUser(idU));
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Group g = new Group(name, idG, lu);
				lg.add(g);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lg;
	}

	
	protected boolean matchpassword(int id_user, String password_test){
		return password_test.equals(getPassword(id_user));
	}
	
	private String getPassword(int id_user) {
		
		String password = "";
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "Password FROM bdd_projet_s5.user");
			while (rst.next()) {
				int idU = rst.getInt("IdU");
				if (idU == id_user) {
					password = rst.getString("Password");
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return password;
	}
	public static void launch(){
		Server c = new Server();
		Thread t = new Thread(c);
		t.start();
	}
}