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
import java.util.LinkedList;

public class Server implements Runnable{
	private static final int PORT = 8952;
	Socket socket;
	ServerSocket server;
	//cr√©e le serveur
	public Server(){
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
											String user="";
											String ip_Adress="";
											String temp="";
											for(char car : input.toCharArray()) {
												if(car=='@') {
													user=temp;
													temp="";
												}
												else {
													temp.concat(Character.toString(car));
												}
											}
											ip_Adress=temp;
											connect_user(user,ip_Adress);
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
													//message envoy√© le serveur va retransmettre le message
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
	
	protected void retransmettreMessage(String user, String discussion, String message) {
		Socket socket;
		PrintWriter output = null;
		for( User client : getDiscussion(discussion).getGroup().group ){
			if(client.getNameUser()!=user && isconnected(client.getId())) {
				try {
					socket = new Socket(getIp(client.getNameUser()),PORT);//"192.168.43.95", PORT);
					output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				output.println(message);
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

	private Discussion getDiscussion(String discussion) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void refresh(String user) {
		//renvoie tout les message non lus renvoy√©s par getAllUnviewedMessage sous la forme "@Envoyeur@Discussion@contenu@Date" 
		LinkedList<String> unviewedmessage=getAllUnviewedMessage(user);
		Socket socket;
		PrintWriter output = null;
		for(String message : unviewedmessage) {
				try {
					socket = new Socket(getIp(user),PORT);//"192.168.43.95", PORT);
					output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				output.println(message);
				updateStatus(message,user);
			
		}
		
		
	}

	private void updateStatus(String message, String user) {
		// cet user a lu ce message
		
	}

	private String getIp(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	private LinkedList<String> getAllUnviewedMessage(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void updateLeaveConv(String user, String discussion) {
		// TODO Auto-generated method stub
		
	}

	protected void updateBDDMessage(String user, String group, String message) {
		// ajout message ‡ bdd, si il y est dej‡ le message est lu
	}

	protected void deleteuserGBDD(String user, String group) {
		// delete un user d'un groupe
		
	}

	protected void adduserGBDD(String user, String group) {
		// ajoute un user ‡ un groupe
		
	}

	protected void adduserBDD(String user, String group) {
		// ajoute un user
		
	}

	protected void connect_user(String user, String ip_Adress) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args){
		Server c = new Server();
		Thread t = new Thread(c);
		t.start();
	}
}
