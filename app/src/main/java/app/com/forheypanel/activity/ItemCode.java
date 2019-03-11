package app.com.forheypanel.activity;

public class ItemCode {
    private String dryCleaningCode;
    private String laundryCode;
    private String washNFoldCode;
    private String pressOnlyCode;

    public ItemCode(String dryCleaningCode, String laundryCode, String washNFoldCode, String pressOnlyCode) {
        this.dryCleaningCode = dryCleaningCode;
        this.laundryCode = laundryCode;
        this.washNFoldCode = washNFoldCode;
        this.pressOnlyCode = pressOnlyCode;
    }

    public String getDryCleaningCode() {
        return dryCleaningCode;
    }

    public void setDryCleaningCode(String dryCleaningCode) {
        this.dryCleaningCode = dryCleaningCode;
    }

    public String getLaundryCode() {
        return laundryCode;
    }

    public void setLaundryCode(String laundryCode) {
        this.laundryCode = laundryCode;
    }

    public String getWashNFoldCode() {
        return washNFoldCode;
    }

    public void setWashNFoldCode(String washNFoldCode) {
        this.washNFoldCode = washNFoldCode;
    }

    public String getPressOnlyCode() {
        return pressOnlyCode;
    }

    public void setPressOnlyCode(String pressOnlyCode) {
        this.pressOnlyCode = pressOnlyCode;
    }
}
