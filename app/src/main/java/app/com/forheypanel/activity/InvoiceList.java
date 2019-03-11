package app.com.forheypanel.activity;

public class InvoiceList {
    String item,noOfItems,type;
    String no_of_items;
    int id;
    String price;

    public String getPrice() {
        return price;
    }

    public InvoiceList(String item, String noOfItems, String type, String no_of_items, String price) {
        this.item = item;
        this.noOfItems = noOfItems;
        this.type = type;
        this.no_of_items = no_of_items;
        this.price = price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int success;
    public int error;


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNoOfITems() {
        if (noOfItems !=null && !noOfItems.isEmpty())
            return noOfItems;
        else return no_of_items;
    }

    public void setNoOfItems(String noOfItems) {
        this.noOfItems = noOfItems;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
