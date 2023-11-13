package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import model.Prize;
import model.PrizeInfo;

public class DataParser {
	public static String currentDateGetter() {
		LocalDate currentDate = LocalDate.now(ZoneId.of("GMT+7"));

		// Format the date as "ddMMyyyy"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		String formattedDate = currentDate.format(formatter);
		return formattedDate;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<PrizeInfo> prizeInfoList = XosoCrawler.getAllPrize();
		String dirpath= "D:\\DataWarehouse\\data\\";
		File file = new File(dirpath+"kqxs_" + currentDateGetter() + ".csv");
		try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
			for (PrizeInfo pi : prizeInfoList) {
				String[] region = { pi.getRegion() };
				writer.writeNext(region);
				String[] province = { pi.getProvince() == null ? "XSMB" : pi.getProvince() };
				writer.writeNext(province);
				for (Prize p : pi.getPrizes()) {
					ArrayList<String> prize = new ArrayList<String>();
					prize.add(p.getPrizeName());
					prize.addAll(p.getWinningNumbers());
					
					String[] prizeArray = new String[prize.size()];
					for (int i=0; i < prize.size();i++) {
						prizeArray[i] = prize.get(i);
					}
					writer.writeNext(prizeArray);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
