package model;

public class Lottery {

    private String Region;
    private String  Province;
    private String PrizeName;
    private String WinningNumbers;

    public Lottery(String region, String province, String prizeName, String winningNumbers) {
        Region = region;
        Province = province;
        PrizeName = prizeName;
        WinningNumbers = winningNumbers;
    }

    public Lottery(String Province) {
        this.Province = Province;
    }

    public String getRegion() {
        return Region;
    }

    public String getProvince() {
        return Province;
    }

    public String getPrizeName() {
        return PrizeName;
    }

    public String getWinningNumbers() {
        return WinningNumbers;
    }

    @Override
    public String toString() {
        return "Lottery{" +
                "Region='" + Region + '\'' +
                ", Province='" + Province + '\'' +
                ", PrizeName='" + PrizeName + '\'' +
                ", WinningNumbers='" + WinningNumbers + '\'' +
                '}';
    }
}
