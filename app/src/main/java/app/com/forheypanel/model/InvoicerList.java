package app.com.forheypanel.model;

import com.google.gson.annotations.SerializedName;

public class InvoicerList {
    @SerializedName("item")
    private String item;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("type_of_service")
    private String type_of_service;
    @SerializedName("itemCode")
    private String itemCode;

    public InvoicerList(String item, String quantity, String type_of_service, String itemCode) {
        this.item = item;
        this.quantity = quantity;
        this.type_of_service = type_of_service;
        this.itemCode = itemCode;
    }



    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType_of_service() {
        return type_of_service;
    }

    public void setType_of_service(String type_of_service) {
        this.type_of_service = type_of_service;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }


}
