package controller;

import Connect.Control;
import Connect.DataMart;
import Connect.DataWahouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transfer {
    /**
     *
     * @param sourceConnection
     * @return
     * @throws SQLException
     */
  public ResultSet getDataAgrate(Connection sourceConnection) throws SQLException {
        String query = "select * from Agregate";
        PreparedStatement statement = sourceConnection.prepareStatement(query);
        return statement.executeQuery();
    }
    // chuyen data
    public void insertDataMart(Connection targetConnection, ResultSet resultSet, Connection update, String date) throws SQLException {
        String mergeQuery = "MERGE INTO datamart AS target " +
                "USING (VALUES (?, ?, ?, ?, ?, ?)) " +
                "AS source (id, Date, Region, Province, PrizeName, WinningNumbers) " +
                "ON target.id = source.id " +
                "WHEN MATCHED THEN " +
                "UPDATE SET Date = source.Date, Region = source.Region, Province = source.Province, PrizeName = source.PrizeName, WinningNumbers = source.WinningNumbers " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (id, Date, Region, Province, PrizeName, WinningNumbers) VALUES (source.id, source.Date, source.Region, source.Province, source.PrizeName, source.WinningNumbers);";
        try (PreparedStatement statement = targetConnection.prepareStatement(mergeQuery)) {
            targetConnection.setAutoCommit(false);
            while (resultSet.next()) {
                statement.setInt(1, resultSet.getInt("id"));
                statement.setString(2, resultSet.getString("Date"));
                statement.setString(3, resultSet.getString("Region"));
                statement.setString(4, resultSet.getString("Province"));
                statement.setString(5, resultSet.getString("PrizeName"));
                statement.setInt(6, resultSet.getInt("WinningNumbers"));
                statement.executeUpdate();
            }

            targetConnection.commit();
            updateData(update, "success", date);

        } catch (SQLException e) {
            targetConnection.rollback();
            updateData(update, "error", date);
            throw e;
        } finally {
            if (targetConnection != null) {
                targetConnection.setAutoCommit(true);
            }
        }
    }

    /**
     * cap nhap trang thai, truyen du lieu va data mart
     * @param connection
     * @param newValue
     * @param date
     * @throws SQLException
     */
    public void updateData(Connection connection, String newValue, String date) throws SQLException {
        // Sử dụng PreparedStatement để tránh các vấn đề về bảo mật và hiệu suất
        String sql = "UPDATE controls_logs SET status = ? WHERE date_create = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newValue); // Set the value for the first parameter
            preparedStatement.setString(2, date);     // Set the value for the second parameter

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void main(String[] args) {
      Transfer tranfer = new Transfer();
       DataWahouse dataWahouse = new DataWahouse();
        DataMart mart = new DataMart();
        Control c = new Control();
        try {
            Connection connectionMart =mart.connection();
            Connection conWasehouse =dataWahouse.connection();
            ResultSet resultSet =tranfer.getDataAgrate(conWasehouse);
            tranfer.insertDataMart(connectionMart,resultSet , c.connection() , "8/12/2023" );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
