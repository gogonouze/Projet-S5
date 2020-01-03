package test;

import java.sql.*;

public class BddTest {

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
	}

}
