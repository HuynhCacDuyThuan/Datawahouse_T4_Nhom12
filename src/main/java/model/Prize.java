package model;

import java.util.ArrayList;

/**
 * class prize ( giai thuong)
 */
public class Prize {
	public String prizeName;
	public ArrayList<String> winningNumbers;

	public Prize(String prizeName, ArrayList<String> winningNumbers) {
		super();
		this.prizeName = prizeName;
		this.winningNumbers = winningNumbers;
	}

	public Prize() {
		super();
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public ArrayList<String> getWinningNumbers() {
		return winningNumbers;
	}

	public void setWinningNumbers(ArrayList<String> winningNumbers) {
		this.winningNumbers = winningNumbers;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Prize prizeName=").append(prizeName).append(", winningNumbers=").append(winningNumbers.toString());

		// Thêm mỗi số trúng vào chuỗi
		for (String number : winningNumbers) {
			sb.append(number).append(", ");
		}

		// Xóa dấu phẩy cuối cùng nếu có ít nhất một số trúng
		if (!winningNumbers.isEmpty()) {
			sb.delete(sb.length() - 2, sb.length());
		}

		sb.append("");
		return sb.toString();
	}

}
