package app.com.forheypanel.activity;

public class CountryItem {

    private String countryName;
    private double laundry;
    private double dryCleaning;
    private double pressOnly;
    private String dryCleaningCode;
    private String laundryCode;
    private String washNFoldCode;
    private String pressOnlyCode;



    public CountryItem(String countryName, double laundry, double dryCleaning, double pressOnly, String dryCleaningCode, String laundryCode,  String pressOnlyCode) {
        this.countryName = countryName;
        this.laundry = laundry;
        this.dryCleaning = dryCleaning;
        this.pressOnly = pressOnly;
        this.dryCleaningCode = dryCleaningCode;
        this.laundryCode = laundryCode;
        this.pressOnlyCode = pressOnlyCode;
    }

//    public CountryItem(String countryName, double laundry, double dryCleaning, double pressOnly) {
//        this.countryName = countryName;
//        this.laundry = laundry;
//        this.dryCleaning= dryCleaning;
//        this.pressOnly= pressOnly;
//    }

    public String getCountryName() {
        return countryName;
    }

    public double getLaundry() {
        return laundry;
    }

    public double getDryCleaning() {
        return dryCleaning;
    }

    public double getPressOnly() {
        return pressOnly;
    }

    public String getDryCleaningCode() {
        return dryCleaningCode;
    }

    public String getLaundryCode() {
        return laundryCode;
    }



    public String getPressOnlyCode() {
        return pressOnlyCode;
    }

}
