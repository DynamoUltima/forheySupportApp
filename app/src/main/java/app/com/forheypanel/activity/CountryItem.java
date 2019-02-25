package app.com.forheypanel.activity;

public class CountryItem {

    private String countryName;
    private double laundry;
    private double dryCleaning;
    private double pressOnly;

    public CountryItem(String countryName, double laundry, double dryCleaning, double pressOnly) {
        this.countryName = countryName;
        this.laundry = laundry;
        this.dryCleaning= dryCleaning;
        this.pressOnly= pressOnly;
    }

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

}
