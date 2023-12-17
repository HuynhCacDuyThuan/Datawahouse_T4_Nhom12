package controller;

import Connect.Control;
import model.Prize;
import model.PrizeInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Access {
    /**
     * Truy cap vao controls_configurations
     * @param connection
     * @return
     * @throws SQLException
     */
    public ResultSet accessControls_configurations(Connection connection) throws SQLException {
        String query = "  select * FROM [control].[dbo].[controls_configurations]";
        PreparedStatement statement =  connection.prepareStatement(query);
        return statement.executeQuery();
    }

    /**
     * Truy cap vao  controls_logs
     * @param connection
     * @return
     * @throws SQLException
     */
    public ResultSet accessControls_logs(Connection  connection) throws SQLException {
        String query = "  select *   FROM [control].[dbo].[controls_logs]";
        PreparedStatement statement =  connection.prepareStatement(query);
        return statement.executeQuery();
    }

    /**
     * Lấy url với ngay lon nhat
     * @param connection
     * @return
     * @throws SQLException
     */
    public String getUrl(Connection connection) throws SQLException {
        // Lấy ResultSet từ phương thức accessControls_configurations
        ResultSet resultSet = accessControls_configurations(connection);
        String url = null;

        // Tạo một biến để giữ ngày lớn nhất
        LocalDate maxDate = LocalDate.MIN;

        // Duyệt qua ResultSet để tìm URL với ngày lớn nhất
        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("date").toLocalDate(); // Chuyển java.sql.Date thành LocalDate
            if (date.isAfter(maxDate)) {
                maxDate = date;
                url = resultSet.getString("url");
            }
        }

        // Đóng ResultSet
        resultSet.close();


        return url;
    }
    public int getIdconfigurations(Connection connection) throws SQLException {
        // Lấy ResultSet từ phương thức accessControls_configurations
        ResultSet resultSet = accessControls_configurations(connection);

        // Tạo biến để giữ giá trị id lớn nhất
        int maxId = -1;

        // Duyệt qua ResultSet để tìm id lớn nhất
        while (resultSet.next()) {
            int currentId = resultSet.getInt("id");
            if (currentId > maxId) {
                maxId = currentId;
            }
        }

        // Đóng ResultSet
        resultSet.close();

        // Trả về id lớn nhất tìm được hoặc -1 nếu không tìm thấy
        return maxId;
    }
    /**
     * lấy id logs
     */
    public int getIdLogs(Connection connection) throws SQLException {
        // Lấy ResultSet từ phương thức accessControls_configurations
        ResultSet resultSet = accessControls_logs(connection);

        // Tạo biến để giữ giá trị id lớn nhất
        int maxId = -1;

        // Duyệt qua ResultSet để tìm id lớn nhất
        while (resultSet.next()) {
            int currentId = resultSet.getInt("id");
            if (currentId > maxId) {
                maxId = currentId;
            }
        }

        // Đóng ResultSet
        resultSet.close();

        // Trả về id lớn nhất tìm được hoặc -1 nếu không tìm thấy
        return maxId;
    }
    /**
     * Kết nối đến source
     * @param connection
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public  Elements connectToSource(Connection connection) throws IOException, SQLException {
        String url = getUrl(connection)  ;
        Document document = Jsoup.connect(url).get();
        Elements rows = document.select("div.block-result");
        return rows;
    }

    /**
     * EXTRAC DỮ LIÊU
     * @param args
     * @throws SQLException
     */
    public  ArrayList<PrizeInfo> crawlXSMB(Element e) {
        // Trích xuất thông tin ngày từ thẻ h2
        String dateText = e.select("caption > h2").text();

        if (dateText.contains("Thứ Ba")) {
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Ba", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Ba", "").trim();
        }
        if (dateText.contains("Thứ Hai")) {
            dateText = dateText.replace("Xổ số Miền BắcThứ Hai", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Hai", "").trim();
        }
        if (dateText.contains("Thứ Tư")) {
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Tư", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Tư", "").trim();
        }
        if (dateText.contains("Thứ năm")) {
            dateText = dateText.replace("Xổ số Miền Bắc Thứ năm", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ năm", "").trim();
        }
        if (dateText.contains("Thứ Sáu")) {
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Sáu", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Sáu", "").trim();
        }
        if (dateText.contains("Thứ Bảy")) {
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Bảy", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Thứ Bảy", "").trim();
        }
        if (dateText.contains("Chủ Nhật")) {
            dateText = dateText.replace("Xổ số Miền Bắc Chủ Nhật", "").trim();
            dateText = dateText.replace("Xổ số Miền Bắc Chủ Nhật", "").trim();
        }

        // Tiếp tục trích xuất thông tin giải thưởng
        Elements tableRows = e.getElementsByTag("tr");
        PrizeInfo pi = new PrizeInfo();
        pi.setRegion("Miền Bắc");
        pi.setDate(dateText);
        ArrayList<Prize> prizelist = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Prize p = new Prize();
            Element tr = tableRows.get(i);
            String prize = tr.getElementsByTag("td").get(0).text();
            p.setPrizeName(prize);

            ArrayList<String> pNum = new ArrayList<>();
            String number = tr.getElementsByTag("td").get(1).text();
            String[] numbers = number.split(" . ");
            for (String num : numbers) {
                pNum.add(num);
            }
            p.setWinningNumbers(pNum);

            prizelist.add(p);
        }

        pi.setPrizes(prizelist);
        ArrayList<PrizeInfo> piList = new ArrayList<PrizeInfo>();
        piList.add(pi);
        return piList;
    }

    /***
     *
     * @param e
     * @param region
     * @return
     */
    public  ArrayList<PrizeInfo> crawlXSMT_N(Element e, String region) {
        /**
         * lấy nngày
         */
        String dateText = e.select("caption > h2").text();
        if (dateText.contains("Thứ Ba")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Ba", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Ba", "").trim();
        }
        if (dateText.contains("Thứ Hai")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Hai", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Hai", "").trim();
        }
        if (dateText.contains("Thứ Tư")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Tư", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Tư", "").trim();
        }
        if (dateText.contains("Thứ Năm")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Năm", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Năm", "").trim();
        }
        if (dateText.contains("Thứ năm")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ năm", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ năm", "").trim();
        }
        if (dateText.contains("Thứ Sáu")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Sáu", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Sáu", "").trim();
        }
        if (dateText.contains("Thứ Bảy")) {
            dateText = dateText.replace("Xổ số Miền Nam Thứ Bảy", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Thứ Bảy", "").trim();
        }
        if (dateText.contains("Chủ Nhật")) {
            dateText = dateText.replace("Xổ số Miền Nam Chủ Nhật", "").trim();
            dateText = dateText.replace("Xổ số Miền Trung Chủ Nhật", "").trim();
        }
        Elements tableRows = e.getElementsByTag("tr");
        int amount = Integer.parseInt(e.getElementsByClass("quantity-of-number").get(0).attr("data-quantity"));
//		System.out.println(amount);
        ArrayList<PrizeInfo> piList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            PrizeInfo pi = new PrizeInfo();
            pi.setRegion(region);
            piList.add(pi);
        }
        ArrayList<Prize> pList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                Element tr = tableRows.get(i);
                Elements provinces = tr.getElementsByTag("td").get(1).getElementsByTag("span");
                for (int j = 0; j < amount; j++) {
                    Element span = provinces.get(j);
                    String province = span.text();
                    piList.get(j).setProvince(province);
                    piList.get(j).setDate(dateText);
                }
            } else {
                int quantity = e.getElementsByClass("quantity-of-number").get(i).getElementsByTag("span").size();

                Element tr = tableRows.get(i);
                String prize = tr.getElementsByTag("td").get(0).text();

                for (int j = 0; j < amount; j++) {
                    Prize p = new Prize();
                    p.setPrizeName(prize);

                    ArrayList<String> prizeList = new ArrayList<>();
                    p.setWinningNumbers(prizeList);
                    pList.add(p);
                }

                Elements prizesNum = tr.getElementsByTag("td").get(1).getElementsByTag("span");
                int k = 0;
                for (int j = 0; j < quantity; j++) {
                    String prizeText = prizesNum.get(j).attr("data-value");
                    pList.get(k + (i - 1) * amount).winningNumbers.add(prizeText);
                    k++;
                    if (k == amount)
                        k = 0;
                }

            }
        }
        ArrayList<ArrayList<Prize>> pList2 = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            pList2.add(new ArrayList<Prize>());
        }
        int k = 0;
        for (int i = 0; i < pList.size(); i++) {
            pList2.get(k).add(pList.get(i));
            k++;
            if (k == amount)
                k = 0;
        }
        for (int i = 0; i < amount; i++) {
            piList.get(i).setPrizes(pList2.get(i));
        }
        return piList;
    }

    /**
     *
     * @return tra giai theo theo id miền
     * @throws IOException
     */
    public  ArrayList<PrizeInfo> getAllPrize(Connection connection) throws IOException , SQLException{

        Elements rows = connectToSource(connection);

        ArrayList<PrizeInfo> pi = new ArrayList<>();
        for (Element e : rows) {
            if (e.id().equals("mien-bac")) {
                pi.addAll(crawlXSMB(e));
            }
            if (e.id().equals("mien-trung")) {
                pi.addAll(crawlXSMT_N(e, "Miền Trung"));
            }
            if (e.id().equals("mien-nam")) {
                pi.addAll(crawlXSMT_N(e, "Miền Nam"));
            }
        }
        return pi;
    }

    /**
     * Lưu dữ liệu vào file csv
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public void saveDataCsv(Connection connection) throws IOException , SQLException{
        int  id_Control = getIdconfigurations(connection)+1 ;
        int id_log = getIdLogs(connection)+1;
        String dirpath = "csv/";
        LocalDate ngayHienTai = LocalDate.now();
        String filePath = dirpath + "KQSX_"+ngayHienTai + ".csv";
        File file = new File(filePath);
        ArrayList<PrizeInfo> prizeInfoList = getAllPrize(connection);

        // Perform the writing operation
        if (!file.exists()) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true),
                    StandardCharsets.UTF_8)) {
                // Uncomment the line below if you want to add UTF-8 BOM to the file
                writer.append('\ufeff');

                // Write the header as the file is new
                writer.append("Date,Region,Province,PrizeName,WinningNumbers\n");

                for (PrizeInfo pi : prizeInfoList) {
                    String region = pi.getRegion();
                    String date1 = pi.getDate();
                    String province = pi.getProvince() == null ? "XSMB" : pi.getProvince();

                    for (Prize p : pi.getPrizes()) {
                        ArrayList<String> prizeData = new ArrayList<>();
                        prizeData.add(date1);
                        prizeData.add(region);
                        prizeData.add(province);
                        prizeData.add(p.getPrizeName());

                        // Thêm từng số của giải vào dòng mới
                        for (String winningNumber : p.getWinningNumbers()) {
                            ArrayList<String> prizeDataWithNumber = new ArrayList<>(prizeData);
                            prizeDataWithNumber.add(winningNumber);
                            String csvLine = String.join(",", prizeDataWithNumber);
                            writer.append(csvLine).append("\n");
                        }

                    }

                }
                /**
                 * Thêm vào control và log khi lưu dữ liệu vào file csv thanh cong
                 */
                insertData(connection,  id_Control,filePath, getUrl(connection));
                insertLogs(connection, id_log,"KQSX_"+ngayHienTai +"_log" , id_Control, "extract data vào csv  thành công!", "E");
            } catch (IOException e) {
                insertLogs(connection, id_log,"KQSX_"+ngayHienTai +"_log" , id_Control-1, "extract data vào csv không thành công!", "FE");
            }
        } else {
            insertLogs(connection, id_log,"KQSX_"+ngayHienTai +"_log" , id_Control-1, "extract data vào csv không thành công!", "FE");
        }

    }

    /**
     * Thêm dưx liêu vao
     * @param connection
     * @param id controls_logs
     * @param id_control
     * @param description
     * @param status
     */
    public void insertLogs(Connection connection, int id, String file, int id_control, String description, String status) {
        LocalDate ngayHienTai = LocalDate.now();
        String sql = "INSERT INTO controls_logs (id, name, control_id, description, status, date_create, date_update) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số cho PreparedStatement
            statement.setInt(1, id);
            statement.setString(2, file);
            statement.setInt(3, id_control);
            statement.setString(4, description);
            statement.setString(5, status);

            // Thực thi câu lệnh insert
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new log has been inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * thêm dữu liệu vào dbo.controls_configurations
     * @param args
     * @throws SQLException
     *
     */
    public void insertData(Connection connection, int id, String path, String url) throws SQLException {
        // Câu lệnh SQL cho việc chèn dữ liệu
        String insertSQL = "INSERT INTO dbo.controls_configurations (id ,date, path, url, username, pass, flag) VALUES ( ?,CURRENT_TIMESTAMP, ?, ?, 'test', 'thuan123', 1);";

        // Tạo một PreparedStatement để chèn dữ liệu
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Thiết lập giá trị cho các tham số
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2, path);
            preparedStatement.setString(3, url);


            // Thực hiện câu lệnh chèn dữ liệu
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Chèn dữ liệu thành công!");
            } else {
                System.out.println("Không có dòng nào được chèn.");
            }
        }
    }
    public static void main(String[] args) throws  SQLException , IOException{
        Access access = new Access();
        Control  c = new Control();


        System.out.println(access.getIdconfigurations(c.connection()));
      access.saveDataCsv(c.connection());

    }
}
