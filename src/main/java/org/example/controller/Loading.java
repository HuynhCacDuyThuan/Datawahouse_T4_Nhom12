package org.example.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.connect.DBConnect;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Loading {

    public static void main(String[] args) throws SQLException, IOException, CsvException {
        Connection conn_control = DBConnect.getConnection("DESKTOP-KM92DV1", "1433",
                "control_database", "sa", "123abc");
        Connection conn_staging = DBConnect.getConnection("DESKTOP-KM92DV1", "1433",
                "staging", "sa", "123abc");
        try {
            assert conn_control != null;

            loadingCSVToStaging(conn_control, conn_staging);
        } catch (SQLException | IOException | CsvException e) {
            e.printStackTrace();
        } finally {
            // Add logging to check the connection status before closing
            System.out.println("Closing conn_control");
            if (conn_control != null) {
                conn_control.close();
            }

            System.out.println("Closing conn_staging");
            if (conn_staging != null) {
                conn_staging.close();
            }
        }

    }

    //Update status
    public static void updateStatus(Connection conn, ResultSet resultSet, String status) throws SQLException {
        String query = "UPDATE data_log SET status = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, status);
        preparedStatement.executeUpdate();
    }

    public static void loadingCSVToStaging(Connection conn_control, Connection conn_staging) throws SQLException, IOException, CsvException {

        // Lấy dòng data log mới nhất
        String pathQuery = "select top 1 * from data_log order by id desc";
        try ( PreparedStatement getPath = conn_control.prepareStatement(pathQuery)) {

            ResultSet datalog = getPath.executeQuery();
            datalog.next();
            // Lấy file csv
            String filepath = datalog.getString("destination");

            // Lấy status
            String status = datalog.getString("status");

            // Kiem tra da extract xong
            if (status.equals("SE")) {
                File file = new File(filepath);
                if (file.exists()) {
                    // update bắt đầu load
                    updateStatus(conn_control, datalog, "PL");
                    String insertData = "";
                    CSVReader reader = new CSVReader(new FileReader(file));
                    List<String[]> allData = reader.readAll();

                    // Bỏ dòng đầu
                    for (int i = 1; i < allData.size(); i++) {
                        String[] row = allData.get(i);
                        insertData += "(";
                        for (int j = 0; j < row.length; j++) {
                            if (j == row.length - 1) {
                                insertData += "'" + row[j] + "'),";
                            } else
                                insertData += "'" + row[j] + "', ";
                        }

                        // insert 1 lần không được quá 1k dòng
                        if (i % 999 == 0) {
                            insertData = insertData.substring(0, insertData.length() - 1);//xóa dấu phẩy
                            String insertQuery = "insert into staging_data_table values " + insertData;
                            try (PreparedStatement insert = conn_staging.prepareStatement(insertQuery)) {
                                insert.executeUpdate();
                            }
                            insertData = "";
                        }
                        // phần còn lại
                        else if (i == allData.size() - 1) {
                            insertData = insertData.substring(0, insertData.length() - 1);//xóa dấu phẩy
                            String insertQuery = "insert into staging_data_table values " + insertData;
                            try (PreparedStatement insert = conn_staging.prepareStatement(insertQuery)) {
                                insert.executeUpdate();
                                // update success load
                                updateStatus(conn_control, datalog, "SL");
                            }
                        }
                    }
                } else {
                    // Lỗi update fail load
                    updateStatus(conn_control, datalog, "FL");
                }
            }
        }
    }
}
