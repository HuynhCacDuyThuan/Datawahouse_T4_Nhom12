package org.example.connect;

import java.sql.*;

public class DBConnect {
    public static Connection getConnection(String server, String port, String db_name, String user, String pass) {
        try {
            // Create a variable for the connection string.
            String connectionUrl = "jdbc:sqlserver://" + server +
                    ":" + port + ";databaseName=" + db_name + ";user="
                    + user + ";password=" + pass + ";encrypt=true;trustServerCertificate=true";

            // Return the connection without using try-with-resources
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
