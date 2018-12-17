package app.com.forheypanel.model;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class Inventory {

    String item,noOfItems,type;
    String no_of_items;
    int id;
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
