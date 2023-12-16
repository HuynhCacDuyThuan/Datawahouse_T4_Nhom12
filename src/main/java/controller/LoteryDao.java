package controller;


import Connect.DataMart;
import model.Lottery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoteryDao {
public static ArrayList<Lottery> lotteries (String date,String mien) throws SQLException {
    Connection connection = DataMart.connection();

    ArrayList<Lottery> list = new ArrayList<>();
    String sql = "SELECT Region, Province, PrizeName, WinningNumbers FROM datamart WHERE Date = ? AND Region = ?";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, date);
    statement.setString(2, mien);
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
        Lottery lottery = new Lottery(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
    list.add(lottery);
    }
    return list;
}
    public static int countDomain(String domain, String date) {
        int result = 0;

        try {
            Connection connection = DataMart.connection();
            String query = "  SELECT COUNT(DISTINCT Province) AS doamin\n" +
                    "FROM datamart\n" +
                    "WHERE  Region = ? AND Date = ? ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, domain);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();

            // Assuming you have a single result, so you can use an if statement
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // Handle the exception (e.g., log or throw a custom exception)
            e.printStackTrace();
        }

        return result;
    }
    public static ArrayList<Lottery> select(String domain, String date, String prize) {
        ArrayList<Lottery> list = new ArrayList<>();
        try {
            Connection connection = DataMart.connection();

            String query = "SELECT  Region, Province, PrizeName, WinningNumbers FROM datamart " +
                    "WHERE Region = ? AND Date = ? AND PrizeName = ?;";
            PreparedStatement statement = connection.prepareStatement(query);

            // Sử dụng tham số để truyền giá trị
            statement.setString(1, domain);
            statement.setString(2, date);
            statement.setString(3, prize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Lottery lottery = new Lottery(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                list.add(lottery);
            }

        } catch (SQLException e) {
            // Xử lý ngoại lệ
            e.printStackTrace();
        }
        return list;
    }

    /**
     * lấy tên các giải
     * @param domain
     * @param date
     * @return
     */
    public static ArrayList<Lottery> arrayList(String domain, String date) {
        ArrayList<Lottery> list = new ArrayList<>();
        try {
            Connection connection = DataMart.connection();
            String query = "SELECT DISTINCT Province FROM datamart " +
                    "WHERE  Region = ? AND Date = ? ;";
            PreparedStatement statement = connection.prepareStatement(query);

            // Sử dụng tham số để truyền giá trị
            statement.setString(1, domain);
            statement.setString(2, date);


            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Lottery l = new Lottery(resultSet.getString(1));
                list.add(l);
            }
        } catch (SQLException e) {
            // Xử lý ngoại lệ
            e.printStackTrace();
        }
        return list;
    }
    public static void main(String[] args) throws SQLException {
        LoteryDao loteryDao = new LoteryDao();
        loteryDao.lotteries("26-11-2023", "Miền Nam");
        System.out.println(loteryDao.lotteries("26-11-2023", "Miền Nam"));
        System.out.println(LoteryDao.countDomain("Miền Nam" ,"26-11-2023"));
        System.out.println(LoteryDao.select("Miền Nam" ,"26-11-2023","Đặc Biệt"));
        System.out.println(LoteryDao.arrayList("Miền Nam" ,"26-11-2023"));
    }

}
