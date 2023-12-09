package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 */
public class DataWahouse {
    public static Connection connection() throws SQLException {
        Connection connection = null;
        try {
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESSS05:1433;databaseName=datawahouse;"
                    + "encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2";
            String host = "test";
            String pass = "thuan1301";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           connection = DriverManager.getConnection(url, host, pass);
            return connection;
        } catch (ClassNotFoundException e) {
            /**
             * ket noi loi
             */
            System.out.println("error" + e.getMessage());
            connection.close();
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws SQLException {
        System.out.println(DataWahouse.connection());

    }
}
