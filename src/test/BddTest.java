package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import object.Client;
import object.Discussion;
import object.Group;
import object.Message;
import object.Status;
import object.User;

public class BddTest {
	
	public static int atoi(String str) {
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
	
	private static Message getMessage(int id) {
		
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
	
	private static void isconnected(int id) {
		
		Connection con;
		Boolean connected = false;
		String name = "aucun";
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdU ," + "IsConnected ," + "Name FROM bdd_projet_s5.user");
			
			while (rst.next()) {
				int idU = rst.getInt("IdU");
				if (id == idU) {
					if (rst.getInt("IsConnected") == 1){
						connected = true;
					}
					else {
						connected = false;
					}
					name = rst.getString("Name");
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (connected) {
			System.out.println("L'utilisateur " + name + " est connecté.");
		}
		else {
			System.out.println("L'utilisateur " + name + " n'est pas connecté.");
		}
	}
	
	private static User getUser(int id) {
		
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
	
	private static Discussion getDiscussion(int id) {
		
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

	private static int addDiscussion(String discussion, Group group) {
		
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
	
	private static void updateStatus(String message) {
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
	
	private static int adduserBDD(String user, String password) {
		
		Connection con;
		int nbu = 0;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT NbU FROM bdd_projet_s5.user");
			if (rst.next()) {
				System.out.println("Dans le if");
				nbu = rst.getInt("NbU") + 1;
			}
			else {
				System.out.println("Dans le else");
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
	
	private static int updateBDDMessage(User user, Discussion discussion, String message) {
		
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
	
	private static int addGroupBDD(String name) {
		
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
	
	private static void adduserGBDD(int idUser, int idGroup) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO appartenirug (IdU, IdG) VALUES ('" + idUser + "', '" + idGroup + "')");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void deleteuserGBDD(int idUser, int idGroup) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM appartenirug WHERE IdU = " + idUser + " AND IdG = " + idGroup);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Group getGroupBDD(int id) {
		
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
	
	private static void updateLeaveConv(int idUser, int idDiscussion) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM appartenirud WHERE IdU = " + idUser + " AND IdD = " + idDiscussion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void updateRejoinConv(int idUser, int idDiscussion) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO appartenirud (IdU, IdD) VALUES ('" + idUser + "', '" + idDiscussion + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static LinkedList<String> getAllMessage(int id) {
		
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
	
	private static List<Group> getAllGroup(){
		
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
	
	private static String getPassword(int id_user) {
		
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
	
	public static void main(String[] args) {
		

		System.out.println(getAllMessage(2).toString());
	}
	
}
