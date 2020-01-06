package test;

import java.sql.*;

import object.Message;
import object.Status;

public class BddTest {

	private static void getMessage(int id) {
		
		Message m = null;
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdM ," + "Content ," + "IsRead ," + "Time FROM bdd_projet_s5.message");
			while (rst.next()) {
				int idM = rst.getInt("IdM");
				if (idM == id) {
					String content = rst.getString("Content");
					int isRead = rst.getInt("IsRead");
					String time = rst.getString("Time");
					Status status = null;
					if (isRead == 0) {
						status = Status.wait;
					} else if (isRead == 1) {
						status = Status.received;
					} else {
						status = Status.viewed;
					}
					m = new Message(content, status, time, idM);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(m.toString());
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
	
	public static void main(String[] args) {
		
		Connection con;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/bdd_projet_s5", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT IdG ," + "Name FROM bdd_projet_s5.group");
			
			while (rst.next()) {
				String s = rst.getString("Name");
				int i = rst.getInt("IdG");
				System.out.println("Name = " + s + " IdG = " + i);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		isconnected(1);
		getMessage(1);
		
	}
	
}
