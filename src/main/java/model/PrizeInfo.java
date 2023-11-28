package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * class prizeinfo
 */
public class PrizeInfo {
	public String region;
	public String province;
	private String date;
	public ArrayList<Prize> prizes;

	public PrizeInfo(String region, String province, ArrayList<Prize> prizes) {
		super();
		this.region = region;
		this.province = province;
		this.prizes = prizes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PrizeInfo region=").append(", Date=").append(date).append(", region=").append(region)
				.append(", province=").append(province).append(", prizes=").append(prizes.toString());

		// Thêm mỗi giải thưởng vào chuỗi
		for (Prize prize : prizes) {
			sb.append(prize).append(", ");
		}

		// Xóa dấu phẩy cuối cùng nếu có ít nhất một giải thưởng
		if (!prizes.isEmpty()) {
			sb.delete(sb.length() - 2, sb.length());
		}

		sb.append("");
		return sb.toString();
	}

	public PrizeInfo() {
		super();
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public ArrayList<Prize> getPrizes() {
		return prizes;
	}

	public void setPrizes(ArrayList<Prize> prizes) {
		this.prizes = prizes;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
