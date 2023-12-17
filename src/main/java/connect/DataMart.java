package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataMart {
/**
 * Kết nối database
 * @return
 * @throws SQLException
 */
	public static Connection connection() throws SQLException {
		Connection conn = null;
		try {
			String url = "jdbc:sqlserver://localhost\\SQLEXPRESSS05:1433;databaseName=mart;"
					+ "encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2";
			String host = "test";
			String pass = "thuan1301";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		 conn = DriverManager.getConnection(url, host, pass);
			return conn;
		} catch (ClassNotFoundException e) {
			System.out.println("error" + e.getMessage());
			conn.close();
			e.printStackTrace();
		}
		return null;
	}
    public static void main(String[] args) throws SQLException {
		System.out.println(DataMart.connection());
		
	}
}
