package app.com.forheypanel.service;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.activity.GarmentItems;
import app.com.forheypanel.activity.Invoicer;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.InvoicerList;
import app.com.forheypanel.model.Order;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupportApiService {
    public static final String domain = "https://support.forhey.com/api/";


    @GET("order/search")
    Call<ArrayList<Order>> getOrdersByDate(@Query("from") String from, @Query("to") String to);

    @GET("order/inventory")
    Call<InventoryList> getInventoryByServerCode(@Query("order_id") String id);

    //    @POST("orders/genInvoice")
//    Call <Invoicer>sendInventory(@Body Invoicer invoicer);

//    @Headers("Content-Type:application/json")
//    @FormUrlEncoded
//    @POST("orders/genInvoice")
//    Call<Invoicer> sendInventory(@Field("total_num_of_items") int total_num_of_items,
//                                 @Field("order_id") String order_id,
//                                 @Field("total_cost") String total_cost,
//                                 @Field("promo") String promo,
//                                 @Field("items") ArrayList<GarmentItems> items
//    );

   // @FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("orders/genInvoice")
    Call <Invoicer>sendInventory(@Body Invoicer invoicer);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
