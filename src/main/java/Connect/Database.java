package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Database {
/**
 * Kết nối database
 * @return
 * @throws SQLException
 */
	public static Connection connection() throws SQLException {
		try {
			String url = "jdbc:sqlserver://localhost\\SQLEXPRESSS05:1433;databaseName=mart;"
					+ "encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2";
			String host = "test";
			String pass = "thuan1301";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(url, host, pass);
			return conn;
		} catch (ClassNotFoundException e) {
			System.out.println("-------");
			e.printStackTrace();
		}
		return null;
	}
    public static void main(String[] args) throws SQLException {
		System.out.println(Database.connection());
		
	}
}
