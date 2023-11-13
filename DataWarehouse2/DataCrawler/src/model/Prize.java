package model;

import java.util.ArrayList;

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
		return "Prize [prizeName=" + prizeName + ", winningNumbers=" + winningNumbers + "]";
	}

}
