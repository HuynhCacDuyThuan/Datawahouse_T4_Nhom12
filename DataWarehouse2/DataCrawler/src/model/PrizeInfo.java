package model;

import java.util.ArrayList;

public class PrizeInfo {
	public String region;
	public String province;
	public ArrayList<Prize> prizes;

	public PrizeInfo(String region, String province, ArrayList<Prize> prizes) {
		super();
		this.region = region;
		this.province = province;
		this.prizes = prizes;
	}

	@Override
	public String toString() {
		return "PrizeInfo [region=" + region + ", province=" + province + ", prizes=" + prizes + "]";
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

}
