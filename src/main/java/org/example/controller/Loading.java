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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Loading {

    public static void main(String[] args) throws SQLException, IOException, CsvException {
        Connection conn_control = DBConnect.getConnection("DESKTOP-KM92DV1", "1433",
                "control", "sa", "123abc");
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


    public static void insertLog(Connection conn, int id, String path, String description, String status) throws SQLException {
        String query = "insert into controls_logs values (?,?,?,?,?,?)";
        PreparedStatement pm = conn.prepareStatement(query);
        pm.setString(1,path);
        pm.setInt(2, id);
        pm.setString(3,description);
        pm.setString(4, status);
        pm.setDate(5, (java.sql.Date) new Date());
        pm.setDate(6,(java.sql.Date) new Date());
    }

    public static void loadingCSVToStaging(Connection conn_control, Connection conn_staging) throws SQLException, IOException, CsvException {

        // Lấy dòng data log mới nhất
        String pathQuery = "select top 1 id, path" +
                "from controls_configurations " +
                "order by id desc";
        try ( PreparedStatement getPath = conn_control.prepareStatement(pathQuery)) {

            ResultSet datalog = getPath.executeQuery();
            datalog.next();
            // Lấy file csv
            String filepath = datalog.getString("path");
            // Lay id
            int id = datalog.getInt("id");
            // Lấy status
            String sttQuery= "select top 1 status from controls_logs where control_id = ? order by id desc";
            PreparedStatement getStt = conn_control.prepareStatement(sttQuery);
            ResultSet rs = getStt.executeQuery();
            rs.next();
            String status = rs.getString("status");

            // Kiem tra da extract xong
            if (status.equals("E")) {
                File file = new File(filepath);
                if (file.exists()) {
                    // update bắt đầu load
                    insertLog(conn_control,id, filepath, "Bắt đầu Load", "SL");
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
                                insertLog(conn_control, id, filepath, "Success Load", "SL");
                            }
                        }
                    }
                } else {
                    // Lỗi update fail load
                    insertLog(conn_control, id, filepath, "Fail Load", "FL");
                }
            }
        }
    }
}
