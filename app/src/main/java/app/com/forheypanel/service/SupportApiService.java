package app.com.forheypanel.service;

import java.util.ArrayList;

import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.Order;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SupportApiService {
    public static final String domain = "https://support.forhey.com/api/";


    @GET("order/search")
    Call<ArrayList<Order>> getOrdersByDate(@Query("from") String from, @Query("to") String to);

    @GET("order/inventory")
    Call<InventoryList> getInventoryByServerCode(@Query("order_id") String id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
