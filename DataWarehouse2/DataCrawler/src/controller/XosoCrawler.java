package controller;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Prize;
import model.PrizeInfo;

public class XosoCrawler {

	public static Elements connectToSource() throws IOException {
		String url = "https://kqxs.vn/";
		Document document = Jsoup.connect(url).get();

		Elements rows = document.select("div.block-result");
		return rows;
	}

	public static ArrayList<PrizeInfo> crawlXSMB(Element e) {
		Elements tableRows = e.getElementsByTag("tr");
		PrizeInfo pi = new PrizeInfo();
		pi.setRegion("Miền Bắc");
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

	public static ArrayList<PrizeInfo> crawlXSMT_N(Element e, String region) {
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

	public static ArrayList<PrizeInfo> getAllPrize() throws IOException {
		Elements rows = connectToSource();

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
		getAllPrize();
	}
}
