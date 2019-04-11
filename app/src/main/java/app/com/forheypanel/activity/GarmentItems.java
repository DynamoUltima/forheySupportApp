package app.com.forheypanel.activity;

public class GarmentItems {

    //        @SerializedName("item")
    String item;

    //        @SerializedName("quantity")
    int quantity;

    //        @SerializedName("type_of_Service")
    String type_of_service;

    String itemCode;

    public GarmentItems(String item, int quantity, String type_of_service, String itemCode) {
        this.item = item;
        this.quantity = quantity;
        this.itemCode = itemCode;
        this.type_of_service = type_of_service;
    }

//        @Override
//        public String toString() {
//            return "{" +
//                    "item='" + item + '\'' +
//                    ", quantity='" + quantity + '\'' +
//                    ", type_of_Service='" + type_of_Service + '\'' +
//                    ", itemCode='" + itemCode + '\'' +
//                    '}';
//        }

}
