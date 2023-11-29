package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class Connect {
	public static Connection getConnection(String host, String server_name, String port, String db_name, String db_username, String db_password) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver//" + host +"\\" + server_name+ ":" + port +"/" + db_name;
			Connection conn = DriverManager.getConnection(url, db_username, db_password);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws SQLException {
		
	}
}
