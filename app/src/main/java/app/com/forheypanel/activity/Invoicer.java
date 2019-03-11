package app.com.forheypanel.activity;

import java.util.ArrayList;

import app.com.forheypanel.model.Inventory;

public class Invoicer {

    private String username;
    private String order_id;
    public ArrayList<Inventory> clientInventory;

    public Invoicer(String username, String order_id, ArrayList<Inventory> clientInventory) {
        this.username = username;
        this.order_id = order_id;
        this.clientInventory = clientInventory;
    }

}
