package app.com.forheypanel.activity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InvoicerList;

public class Invoicer {
    private String total_num_of_items;
    private String order_id;
    private String total_cost;
    private String promo;
    private ArrayList<GarmentItems> items;

    private String success;
    private String message;

    public Invoicer(String total_num_of_items, String order_id, String total_cost, String promo, ArrayList<GarmentItems> items) {
        this.total_num_of_items = total_num_of_items;
        this.order_id = order_id;
        this.total_cost = total_cost;
        this.promo = promo;
        this.items = items;
    }

    //
//    public int getTotal_num_of_items() {
//        return total_num_of_items;
//    }
//
//    public void setTotal_num_of_items(int total_num_of_items) {
//        this.total_num_of_items = total_num_of_items;
//    }
//
//    public String getOrder_id() {
//        return order_id;
//    }
//
//    public void setOrder_id(String order_id) {
//        this.order_id = order_id;
//    }
//
//    public String getPromo() {
//        return promo;
//    }
//
//    public void setPromo(String promo) {
//        this.promo = promo;
//    }
//
//    public String getTotal_cost() {
//        return total_cost;
//    }
//
//    public void setTotal_cost(String total_cost) {
//        this.total_cost = total_cost;
//    }
//
//    public ArrayList<GarmentItems> getItems() {
//        return items;
//    }
//
//    public void setItems(ArrayList<GarmentItems> items) {
//        this.items = items;
//    }
    public String getSuccess() {
        return success;
    }


    public String getMessage() {
        return message;
    }

//    public void setMessage(int message) {
//        this.message = message;
//    }
//    private String success;

//    public Invoicer(int total_num_of_items, String order_id,String total_cost, String promo,ArrayList<GarmentItems> items) {
//        this.total_num_of_items = total_num_of_items;
//        this.order_id = order_id;
//        this.promo = promo;
//        this.total_cost = total_cost;
//        this.items = items;
//    }

//    @Override
//    public String toString() {
//        return "Invoicer{" +
//                "noOfItems='" + total_num_of_items + '\'' +
//                ", order_id='" + order_id + '\'' +
//                ", promo='" + promo + '\'' +
//                ", total_cost=" + total_cost +
//              ", success='" + success + '\'' +
//                '}';
//    }


//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(String success) {
//        this.success = success;
//    }


//    @SerializedName("items")
//    public ArrayList<InvoicerList> clientInventory;


}
