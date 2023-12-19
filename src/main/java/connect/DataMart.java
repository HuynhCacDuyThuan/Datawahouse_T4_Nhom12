package connect;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DataMart {
/**
 * Kết nối database
 * @return
 * @throws SQLException
 */

	public static Connection connection() throws SQLException, IOException {
		Properties configProps = new Properties();
		InputStream input = Control.class.getClassLoader().getResourceAsStream("config.properties");

		if (input == null) {
			System.out.println("error");
		}

		try {
			configProps.load(input);
			String url = configProps.getProperty("database2.url");
			String username = configProps.getProperty("database.username");
			String password = configProps.getProperty("database.password");

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("Không tìm thấy JDBC Driver");
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    public static void main(String[] args) throws SQLException, IOException  {
		System.out.println(DataMart.connection());
		
	}
}
