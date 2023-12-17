package view;

import com.toedter.calendar.JDateChooser;
import controller.LoteryDao;
import model.Lottery;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Gui extends JFrame implements ActionListener {
    private JDateChooser dateChooser = new JDateChooser();
    JPanel container = new JPanel();
    JLabel lbsoxo = new JLabel("Kết quả số xố ngày");

    JLabel date = new JLabel();
    JLabel lbname = new JLabel("KẾT QUẢ SỔ XỐ MIỀN NAM");
    private JTable table;
    private JTable calendarTable;
    private DefaultTableModel tableModel;

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public Gui() throws SQLException {
        getContentPane().add(container);
        setLayoutManager();
        addComponentsToContainer();
        setLocationAndSize();
        addTableAdmin();
        addActionEvent();
    }

    /**
     * cập nhập kết quả sổ xố khi chọn thời gian khác
     */
    public void addActionEvent() {
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                Date selectedDate = dateChooser.getDate();
                if (selectedDate != null) {
/**
 * cap nhap ket qua khi chọn ngày khác
 */

                    // formart ngay thang nam
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    String formattedDate = dateFormat.format(selectedDate);
                    System.out.println(formattedDate);
/**
 * Cập nhập ngày vào  lable date
 */
                    ArrayList<Lottery> arrayList = LoteryDao.arrayList("Miền Nam", formattedDate);
                    int  countDoMain = LoteryDao.countDomain("Miền Nam", formattedDate);
                    tableModel = new DefaultTableModel(9, countDoMain +1);
                    for (int i = 1; i <= arrayList.size(); i++) {
                       table.getColumnModel().getColumn(i).setHeaderValue(arrayList.get(i-1).getProvince());
                    }

                    /**
                     *ghi tên các Giai
                     *
                     */
                    ArrayList<Lottery> giaidb =LoteryDao.select("Miền Nam", formattedDate,"Đặc Biệt");
                    ArrayList<Lottery> giaiNhat = LoteryDao.select("Miền Nam",  formattedDate,"Giải Nhất");
                    ArrayList<Lottery> giaiNhi = LoteryDao.select("Miền Nam",  formattedDate,"Giải Nhì");
                    ArrayList<Lottery> giaiBA = LoteryDao.select("Miền Nam",  formattedDate,"Giải Ba");
                    ArrayList<Lottery> giaiTu= LoteryDao.select("Miền Nam", formattedDate,"Giải Tư");
                    ArrayList<Lottery> giaiNam= LoteryDao.select("Miền Nam",  formattedDate,"Giải Năm");
                    ArrayList<Lottery> giaiSau = LoteryDao.select("Miền Nam", formattedDate,"Giải Sáu");
                    ArrayList<Lottery> giaiBay = LoteryDao.select("Miền Nam",  formattedDate,"Giải Bảy");
                    ArrayList<Lottery> giaiTam= LoteryDao.select("Miền Nam",  formattedDate,"Giải Tám");
//tạo các html để xuông dòng
                    String result = "<html>";
                    String result1 = "<html>";
                    String result2 = "<html>";
                    String result3 = "<html>";
                    String result4 = "<html>";
                    String result5 = "<html>";
                    String result6= "<html>";
                    String result7 = "<html>";
                    String result8 = "<html>";
                    String result9 = "<html>";
                    String result10 = "<html>";
                    String result11= "<html>";
                    /***
                     * Hiên thi thông tin
                     * điều chỉnh độ cao
                     */
                    int rowHeight = 50;
                    int rowHeight1 = 70;
                    int rowHeight2 = 100;
                    int rowHeight4 = 120;
                    int rowHeight3= 70;
                    int rowIndexToSetHeight = 2;
                    int rowIndexToSetHeight1 = 4;
                    int rowIndexToSetHeight3= 3;
                    int rowIndexToSetHeight5= 5;
                    int rowIndexToSetHeight6= 6;
                    int rowIndexToSetHeight7= 7;
                    int numberOfRowsToAdd =2;
                    int rowIndexToSetHeight2 = 5;
                    table.setRowHeight(rowIndexToSetHeight, rowHeight);
                    table.setRowHeight(rowIndexToSetHeight1, rowHeight4);
                    table.setRowHeight(rowIndexToSetHeight3, rowHeight);
                    table.setRowHeight(rowIndexToSetHeight2, rowHeight);
                    table.setRowHeight(rowIndexToSetHeight5, rowHeight);
                    table.setRowHeight(rowIndexToSetHeight6, rowHeight);
                    table.setRowHeight(rowIndexToSetHeight7, rowHeight);
                    /**
                     * Ghi gia tri giai vao bang
                     */
                    for (int i = 1; i <= giaidb.size(); i++) {

                        double giaiDacBietValue = Double.parseDouble(giaidb.get(i-1).getWinningNumbers());
                        table.setValueAt((int) giaiDacBietValue, 8, i);
                    }
                    // giai nhat
                    for (int i = 1; i <= giaiNhat.size(); i++) {
                        double giainhat =Double.parseDouble(giaiNhat.get(i-1).getWinningNumbers());
//            result = giaiNhat.get(i-1).getWinningNumbers().toString() ;
                        table.setValueAt((int) giainhat, 7, i);
                    }
                    /**
                     * Giải Nhì
                     */
                    for (int i = 1; i <= giaiNhi.size(); i++) {
                        double giainhi =Double.parseDouble( giaiNhi.get(i-1).getWinningNumbers());
                        table.setValueAt((int) giainhi, 6, i);
                    }
/**
 * Giai Ba
 */
                    for (int i = 0; i < 2; i++) {
                        String numberString = giaiBA.get(i).getWinningNumbers();
                        // Tách chuỗi bởi dấu chấm và lấy phần đầu tiên
                        String wholeNumberPart = numberString.split("\\.")[0];
                        result2 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result2, 5, 1);
                    for (int i = 2; i < 4; i++) {
                        String numberString = giaiBA.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result3 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result3, 5, 2);

                    for (int i = 4; i < 6; i++) {
                        String numberString = giaiBA.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result4 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result4, 5, 3);
/**
 * Giai Tu
 */
                    for (int i = 0; i < 7; i++) {
                        String numberString = giaiTu.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result5 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result5, 4, 1);

                    for (int i = 7; i < 14; i++) {
                        String numberString = giaiTu.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result6 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result6, 4, 2);

                    for (int i = 14; i < 21; i++) {
                        String numberString = giaiTu.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result7 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result7, 4, 3);
                    /**
                     * Giai năm
                     *
                     */
                    for (int i = 1; i <= giaiNam.size(); i++) {
                        double giainam = Double.parseDouble(giaiNam.get(i-1).getWinningNumbers());
                        table.setValueAt((int)giainam, 3, i);
                    }
                    /**
                     * Giai Sau
                     */
                    for (int i = 0; i < 3; i++) {
                        String numberString = giaiSau.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result8 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result8, 2, 1);

                    for (int i = 3; i < 6; i++) {
                        String numberString = giaiSau.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result9 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result9, 2, 2);

                    for (int i = 6; i < 9; i++) {
                        String numberString = giaiSau.get(i).getWinningNumbers();
                        String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
                        result10 += wholeNumberPart + "<br>";
                    }
                    table.setValueAt(result10, 2, 3);
                    /**
                     * Giai bay
                     */
                    for (int i = 1; i <= giaiBay.size(); i++) {
                        double bay =Double.parseDouble(giaiBay.get(i-1).getWinningNumbers());
                        table.setValueAt((int)bay, 1, i);
                    }
                    /**
                     * Giai Tam
                     */
                    for (int i = 1; i <= giaiTam.size(); i++) {
                        double tam=Double.parseDouble(giaiTam.get(i-1).getWinningNumbers());
                        table.setValueAt((int)tam, 0, i);
                    }


                }}
        });
    }

    public void setLayoutManager() {
        container.setLayout(null);

    }

    public void addComponentsToContainer() {
        container.add(dateChooser);
        container.add(lbsoxo);
        container.add(date);
        container.add(lbname);
    }

    public void setLocationAndSize() {
        dateChooser.setBounds(20, 40, 300, 20);
        dateChooser.setDateFormatString("dd-MM-yyyy");
        lbsoxo.setBounds(20, 17, 294, 13);
        date.setBounds(140, 17, 294, 13);
        lbname.setBounds(130, 70, 250, 34);
    }

    private void addTableAdmin() throws SQLException {
        String result = "<html>";
        String result1 = "<html>";
        String result2 = "<html>";
        String result3 = "<html>";
        String result4 = "<html>";
        String result5 = "<html>";
        String result6= "<html>";
        String result7 = "<html>";
        String result8 = "<html>";
        String result9 = "<html>";
        String result10 = "<html>";
        String result11= "<html>";
        LoteryDao loteryDao = new LoteryDao();

        int  countDoMain = LoteryDao.countDomain("Miền Nam",  loteryDao.maxDate());
        date.setText(       loteryDao.formatDate(loteryDao.maxDate()));
        tableModel = new DefaultTableModel(9, countDoMain +1);
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setHeaderValue("Giải");
        table.getTableHeader().repaint();
        /**
         *ghi tên các đài
         *
         */

        ArrayList<Lottery> arrayList = LoteryDao.arrayList("Miền Nam",  loteryDao.maxDate());
        for (int i = 1; i <= arrayList.size(); i++) {

            table.getColumnModel().getColumn(i).setHeaderValue(arrayList.get(i-1).getProvince());
            System.out.println(arrayList.get(i-1).getProvince());

        }
        /**
         * Ghi cá giải
         */
        for (int i = 0; i < 9; i++) {


            tableModel.setValueAt("Giải  Tám", 0, 0);
            tableModel.setValueAt("Giải Bảy", 1, 0);
            tableModel.setValueAt("Giải Sáu", 2, 0);
            tableModel.setValueAt("Giải Năm", 3, 0);
            tableModel.setValueAt("Giải Tư", 4, 0);
            tableModel.setValueAt("Giải Ba", 5, 0);
            tableModel.setValueAt("Giải Nhì", 6, 0);
            tableModel.setValueAt("Giải Nhất", 7, 0);
            tableModel.setValueAt("Giải Đặc Biệt", 8, 0);

        }
        //LAY DU LIEU
        ArrayList<Lottery> giaidb =LoteryDao.select("Miền Nam",loteryDao.maxDate(),"Đặc Biệt");

        ArrayList<Lottery> giaiNhat = LoteryDao.select("Miền Nam", loteryDao.maxDate() ,"Giải Nhất");
        ArrayList<Lottery> giaiNhi = LoteryDao.select("Miền Nam",  loteryDao.maxDate(),"Giải Nhì");
        ArrayList<Lottery> giaiBA = LoteryDao.select("Miền Nam", loteryDao.maxDate(),"Giải Ba");
        ArrayList<Lottery> giaiTu= LoteryDao.select("Miền Nam", loteryDao.maxDate(),"Giải Tư");
        ArrayList<Lottery> giaiNam= LoteryDao.select("Miền Nam",  loteryDao.maxDate(),"Giải Năm");
        ArrayList<Lottery> giaiSau = LoteryDao.select("Miền Nam", loteryDao.maxDate(),"Giải Sáu");
        ArrayList<Lottery> giaiBay = LoteryDao.select("Miền Nam",  loteryDao.maxDate(),"Giải Bảy");
        ArrayList<Lottery> giaiTam= LoteryDao.select("Miền Nam",  loteryDao.maxDate(),"Giải Tám");
/**
 * điều chỉnh chiều cao
 */
        int rowHeight = 50;
        int rowHeight1 = 70;
        int rowHeight2 = 100;
        int rowHeight4 = 120;
        int rowHeight3= 70;
        int rowIndexToSetHeight = 2;
        int rowIndexToSetHeight1 = 4;
        int rowIndexToSetHeight3= 3;
        int rowIndexToSetHeight5= 5;
        int rowIndexToSetHeight6= 6;
        int rowIndexToSetHeight7= 7;
        int numberOfRowsToAdd =2;
        int rowIndexToSetHeight2 = 5;
       table.setRowHeight(rowIndexToSetHeight, rowHeight);
        table.setRowHeight(rowIndexToSetHeight1, rowHeight4);
        table.setRowHeight(rowIndexToSetHeight3, rowHeight);

        table.setRowHeight(rowIndexToSetHeight2, rowHeight);
        table.setRowHeight(rowIndexToSetHeight5, rowHeight);
        table.setRowHeight(rowIndexToSetHeight6, rowHeight);
        table.setRowHeight(rowIndexToSetHeight7, rowHeight);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 120, 400, 800);
        container.add(scrollPane);
        /**
         * Ghi gia tri giai vao bang
         */
        for (int i = 1; i <= giaidb.size(); i++) {
            double giaiDacBietValue = Double.parseDouble(giaidb.get(i-1).getWinningNumbers());
            tableModel.setValueAt((int) giaiDacBietValue, 8, i);
        }
        // giai nhat
        for (int i = 1; i <= giaiNhat.size(); i++) {
            double giainhat =Double.parseDouble(giaiNhat.get(i-1).getWinningNumbers());
//            result = giaiNhat.get(i-1).getWinningNumbers().toString() ;
            tableModel.setValueAt((int) giainhat, 7, i);
        }
        /**
         * Giải Nhì
         */
        for (int i = 1; i <= giaiNhi.size(); i++) {
            double giainhi =Double.parseDouble( giaiNhi.get(i-1).getWinningNumbers());
            tableModel.setValueAt((int) giainhi, 6, i);
        }
/**
 * Giai Ba
 */
        for (int i = 0; i < 2; i++) {
            String numberString = giaiBA.get(i).getWinningNumbers();
            // Tách chuỗi bởi dấu chấm và lấy phần đầu tiên
            String wholeNumberPart = numberString.split("\\.")[0];
            result2 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result2, 5, 1);
        for (int i = 2; i < 4; i++) {
            String numberString = giaiBA.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result3 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result3, 5, 2);

        for (int i = 4; i < 6; i++) {
            String numberString = giaiBA.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result4 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result4, 5, 3);
/**
 * Giai Tu
 */
        for (int i = 0; i < 7; i++) {
            String numberString = giaiTu.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result5 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result5, 4, 1);

        for (int i = 7; i < 14; i++) {
            String numberString = giaiTu.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result6 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result6, 4, 2);

        for (int i = 14; i < 21; i++) {
            String numberString = giaiTu.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result7 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result7, 4, 3);
        /**
         * Giai năm
         *
         */
        for (int i = 1; i <= giaiNam.size(); i++) {
           double giainam = Double.parseDouble(giaiNam.get(i-1).getWinningNumbers());
            tableModel.setValueAt((int)giainam, 3, i);
        }
        /**
         * Giai Sau
         */
        for (int i = 0; i < 3; i++) {
            String numberString = giaiSau.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result8 += wholeNumberPart + "<br>";
        }
           tableModel.setValueAt(result8, 2, 1);

        for (int i = 3; i < 6; i++) {
            String numberString = giaiSau.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result9 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result9, 2, 2);

        for (int i = 6; i < 9; i++) {
            String numberString = giaiSau.get(i).getWinningNumbers();
            String wholeNumberPart = numberString.split("\\.")[0]; // Chia chuỗi và lấy phần trước dấu chấm
            result10 += wholeNumberPart + "<br>";
        }
        tableModel.setValueAt(result10, 2, 3);
        /**
         * Giai bay
         */
        for (int i = 1; i <= giaiBay.size(); i++) {
            double bay =Double.parseDouble(giaiBay.get(i-1).getWinningNumbers());
            tableModel.setValueAt((int)bay, 1, i);
        }
        /**
         * Giai Tam
         */
        for (int i = 1; i <= giaiTam.size(); i++) {
            double tam=Double.parseDouble(giaiTam.get(i-1).getWinningNumbers());
            tableModel.setValueAt((int)tam, 0, i);
        }

    }

    public static void main(String[] a) throws SQLException {
        Gui frame = new Gui();
        frame.setTitle("Sổ xố kiến thiết");
        frame.setVisible(true);
        frame.setBounds(100, 100, 430, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}