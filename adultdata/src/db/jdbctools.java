package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class jdbctools {

	private static String dbUrl = "jdbc:mysql://localhost:3306/adult";
	private static String dbUser = "root";
	private static String dbPassword = "123456";
	private static String jdbcName = "com.mysql.jdbc.Driver";
	private static Connection conn = null;

	public Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}