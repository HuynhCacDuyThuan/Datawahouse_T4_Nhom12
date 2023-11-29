package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import connect.Connect;

public class Loading {
	public static void main(String[] args) throws SQLException, IOException, CsvException {
		Connection conn_control = Connect.getConnection("localhost","DESKTOP-KM92DV1", "1433", "control_database", "root", "");
		Connection conn_staging = Connect.getConnection("localhost","DESKTOP-KM92DV1", "1433", "staging", "root", "");
		
		loadingCSVToStaging(conn_control, conn_staging);
		
		conn_control.close();
		conn_staging.close();
	}
	//Lấy dòng data log cuối cùng
	public static ResultSet getLog(Connection conn) throws SQLException {
		String pathQuery = "select top 1 * from data_log order by id desc";
		PreparedStatement getPath = conn.prepareStatement(pathQuery);
		ResultSet rs = getPath.executeQuery();
		return rs;
	}

	//Update status
	public static void updateStatus(Connection conn, ResultSet resultSet, String status) throws SQLException {
		String query = "UPDATE data_log SET status = ?";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, status);
		preparedStatement.executeUpdate();
	}

	public static void loadingCSVToStaging(Connection conn_control, Connection conn_staging) throws SQLException, IOException, CsvException {
		
		ResultSet datalog = getLog(conn_control);
		String filepath = datalog.getString("destination");
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
						PreparedStatement insert = conn_staging.prepareStatement(insertQuery);
						insert.executeUpdate();
						insertData = "";
					}
					// phần còn lại
					else if (i == allData.size() - 1) {
						insertData = insertData.substring(0, insertData.length() - 1);//xóa dấu phẩy
						String insertQuery = "insert into staging_data_table values " + insertData;
						PreparedStatement insert = conn_staging.prepareStatement(insertQuery);
						insert.executeUpdate();
						// update success load
						updateStatus(conn_control, datalog, "SL");
					}
				}
			} else {
				// Lỗi update fail load
				updateStatus(conn_control, datalog, "FL");
			}
		}
	}
}
