package controller;

import Connect.Control;
import Connect.DataMart;
import Connect.DataWahouse;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Transfer {
    /**
     *
     * @param sourceConnection
     * @return
     * @throws SQLException
     */
    public ResultSet getDataAggregate(Connection sourceConnection) throws SQLException {
        String query = "SELECT x.id , d.date, i.name, pr.name, p.name, x.number_winning " +
                "FROM date_dim d " +
                "JOIN xo_so_fact x ON d.id = x.id_date " +
                "JOIN domain_dim i ON x.id_domain = i.id " +
                "JOIN prize_dim p ON x.id_prize = p.id " +
                "JOIN province_dim pr ON x.id_province = pr.id " +
                "WHERE date_expired IS NULL";
        PreparedStatement statement = sourceConnection.prepareStatement(query);
        return statement.executeQuery();
    }
    // chuyen  dữ liệu trong data wahouse vào data mart
    public void insertDataMart(Connection targetConnection, Connection update, Connection source) throws SQLException {
        ResultSet resultSet = getDataAggregate(source);
        int id_log = getid(update) + 1;
        int id_Control = getid_Control(update);
        String insertQuery = "INSERT INTO datamart(id, Date, Region, Province, PrizeName, WinningNumbers) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = targetConnection.prepareStatement(insertQuery)) {
            targetConnection.setAutoCommit(false);
            boolean hasInsertedData = false;
            while (resultSet.next()) {
                statement.setString(1, resultSet.getString("id"));
                /**
                 * định dạng ngày tháng năm khi chuyển qua
                 *
                 */
                java.sql.Date originalDate = resultSet.getDate(2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(originalDate);
                statement.setString(2, formattedDate);
                statement.setString(3, resultSet.getString(3));
                statement.setString(4, resultSet.getString(4));
                statement.setString(5, resultSet.getString(5));
                statement.setString(6, resultSet.getString(6)); // Assuming WinningNumbers is a string, change to setInt if it's an integer.
                statement.addBatch();
            }
            int[] batchResult = statement.executeBatch();
            for (int count : batchResult) {
                if (count > 0) {
                    hasInsertedData = true;
                    break;
                }
            }
            if (hasInsertedData) {
                targetConnection.commit();
                insertLogs(update, id_log, id_Control, "Dữ liệu chuyển từ warehouse sang mart thành công!", "SU");
            } else {
                targetConnection.rollback();
                insertLogs(update, id_log, id_Control, "Không có dữ liệu mới để chuyển từ warehouse sang mart.", "ER");
            }

            updateDate_Expire(source);
        } catch (SQLException e) {
            System.out.println("loi");
            targetConnection.rollback();
            insertLogs(update, id_log, id_Control, "Dữ liệu chuyển từ warehouse sang mart không thành công!", "EF");
            throw e;
        } finally {
            if (targetConnection != null) {
                targetConnection.setAutoCommit(true);
            }
            if (resultSet != null) {
                resultSet.close();
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


    /**
     * Cập nhập ngày hết hạn sau khi insert vào thành công
     * @param args
     */
    public void updateDate_Expire(Connection connection) throws SQLException {
        String sql = "UPDATE xo_so_fact SET date_expired = GETDATE()";

        try (Statement statement = connection.createStatement()) {
            int rowsUpdated = statement.executeUpdate(sql);

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
    // lấy giá trị id lon nhat
    public int getid(Connection connection) throws SQLException {
        String sql ="SELECT MAX(id) as id FROM [control].[dbo].[controls_logs]";
        int maxId = -1; // Giá trị mặc định nếu không tìm thấy id nào

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Chỉ cần di chuyển tới hàng đầu tiên vì MAX(id) chỉ trả về một hàng
            if (rs.next()) {
                maxId = rs.getInt("id");
            }
        }

        return maxId; // Trả về id lớn nhất tìm được hoặc -1 nếu không có dữ liệu
    }

    /**
     * lấy gi control lon nhat
     * @param connection
     * @return
     * @throws SQLException
     */
    public int getid_Control(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id) FROM controls_configurations"; // Sửa lại đây
        int maxId = -1; // Giá trị mặc định nếu không tìm thấy id nào

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Chỉ cần di chuyển tới hàng đầu tiên vì MAX(id) chỉ trả về một hàng
            if (rs.next()) {
                maxId = rs.getInt(1); // Sử dụng chỉ số cột nếu không có tên cột
            }
        }

        return maxId;
    }

    /**
     * THÊM VÀO LOG GHI CHUYEN DƯ LIEU
     * @param args
     */
    public void insertLogs(Connection connection, int id, int id_control, String description, String status) {
        String sql = "INSERT INTO controls_logs (id, name, control_id, description, status, date_create, date_update) " +
                "VALUES (?, 'KQSX_log', ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số cho PreparedStatement
            statement.setInt(1, id);
            statement.setInt(2, id_control);
            statement.setString(3, description);
            statement.setString(4, status);

            // Thực thi câu lệnh insert
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new log has been inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
//            ResultSet resultSet =tranfer.getDataAggregate(conWasehouse,"2023-12-15");
//         tranfer.insertDataMart(connectionMart,"2023-12-15" , c.connection() ,conWasehouse);
          tranfer.getid(c.connection());

         int id =tranfer.getid(c.connection());
         id++;

            System.out.println(id);
            int id_Control = tranfer.getid_Control(c.connection());
            id_Control ++;
            System.out.println(id_Control);
            tranfer.insertDataMart(connectionMart, c.connection(), conWasehouse);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
