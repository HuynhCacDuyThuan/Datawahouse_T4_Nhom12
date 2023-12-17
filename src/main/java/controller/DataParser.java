package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.module.Configuration;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



import model.Prize;
import model.PrizeInfo;

public class DataParser {
	/**
	 * phuong thuc lấy ngày giờ
	 * 
	 * @return
	 */
	public static String currentDateGetter(String date) {

		LocalDate currentDate = LocalDate.now(ZoneId.of("GMT+7"));

		// Format the date as "ddMMyyyy"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(date);
		String formattedDate = currentDate.format(formatter);
		return formattedDate;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		String dirpath = "csv/";
		LocalDate ngayHienTai = LocalDate.now();
		String filePath = dirpath + "KQSX_"+ngayHienTai + ".csv";
       File  file = new File(filePath);

			ArrayList<PrizeInfo> prizeInfoList = XosoCrawler.getAllPrize();

			// Perform the writing operation
			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true),
					StandardCharsets.UTF_8)) {
				// Uncomment the line below if you want to add UTF-8 BOM to the file
				writer.append('\ufeff');

				// Don't overwrite the header if the file is empty
				if (file.length() == 0) {
					writer.append("Date,Region,Province,PrizeName,WinningNumbers\n");
				}

				for (PrizeInfo pi : prizeInfoList) {
					String region = pi.getRegion();
					String date1 = pi.getDate();
					String province = pi.getProvince() == null ? "XSMB" : pi.getProvince();

					// Check if the date is already present in the CSV file for the current
					// PrizeInfo

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
			} catch (IOException e) {
				System.err.println("Lỗi khi ghi vào tệp CSV: " + e.getMessage());
			}
		}

/**
 * kiem tra ngay ton tai trong file chưa
 * @param file
 * @param date
 * @return
 * @throws IOException
 */


	/**
	 * 
	 * @param prizeArray
	 */
	private static void printArrayList(String[] prizeArray) {
		for (String element : prizeArray) {
			System.out.println(element);
		}
	}

}
