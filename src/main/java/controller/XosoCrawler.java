package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Prize;
import model.PrizeInfo;

public class XosoCrawler {

	public static Elements connectToSource(String date) throws IOException {

		String url = "https://kqxs.vn/?date=" + date;
		Document document = Jsoup.connect(url).get();
		Elements rows = document.select("div.block-result");
		return rows;
	}

	public static ArrayList<PrizeInfo> crawlXSMB(Element e) {
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
		if (dateText.contains("Thứ Năm")) {
			dateText = dateText.replace("Xổ số Miền Bắc Thứ Năm", "").trim();
			dateText = dateText.replace("Xổ số Miền Bắc Thứ Năm", "").trim();
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
	public static ArrayList<PrizeInfo> crawlXSMT_N(Element e, String region) {
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
	public static ArrayList<PrizeInfo> getAllPrize(String date) throws IOException {

		Elements rows = connectToSource(date);

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

	public static void main(String[] args) throws IOException {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = dateFormat.format(date);
		System.out.println(formattedDate);
		for (int i = 0; i < getAllPrize(formattedDate).size(); i++) {
			System.out.println(getAllPrize(formattedDate).get(i));
		}
	}

}