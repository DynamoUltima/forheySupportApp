package app.com.forheypanel.service;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.model.Client;
import app.com.forheypanel.model.HeyGirlOptions;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.model.InventoryList;
import app.com.forheypanel.model.InventorySummary;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.model.OrderBill;
import app.com.forheypanel.model.OrderList;
import app.com.forheypanel.model.PriceCartegory;
import app.com.forheypanel.model.PriceItem;
import app.com.forheypanel.model.Promotion;
import app.com.forheypanel.model.Referal;
import app.com.forheypanel.model.Results;
import app.com.forheypanel.model.SuccessClass;
import app.com.forheypanel.model.Transaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nayram on 11/17/15.
 */
public interface SupportService  {

    public static final String domain = "http://www.forhey.com/";
    public static final String forhey_domain = "https://forhey.com/";

     String prefix="forhey_mobile_scripts";

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call getAllOrders(@Field("tag") String tag, Callback<Order.OrderList> results);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <Order.OrderList> getOrdersByDate(@Field("tag") String tag,@Field("date") String date,@Field("email") String email);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <Order.OrderList> getOrdersByAssigned(@Field("tag") String tag,@Field("email") String email);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/support_checkin.php")
    Call <Order> check_in(@Field("tag") String tag,@Field("email") String email);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <HeyGirlOptions.ListHeyGirlOptions> todaysCheckin(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Client.RawClientData> getClientdata(@Field("tag") String tag,@Field("name") String name);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Client> getClientdataById(@Field("tag") String tag,@Field("id") int id);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Client.RawClientData> getAllCustomers(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Order.OrderList> getAllCustOrders(@Field("tag") String tag,@Field("email") String email);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <Order.Referal> updateReferalReward(@Field("tag") String tag,@Field("value") String value,@Field("message") String message,
                             @Field("category") int type,@Field("txtMsg") boolean txtMsg,@Field("notMsg") boolean notMsg);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <PriceCartegory.ListPriceCart> getPriceCategories(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <PriceItem.ListPriceItem> getItemPrices(@Field("tag") String tag,@Field("id") int id);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <SuccessClass> updateItemPrices(@Field("tag") String tag,@Field("id") int id,@Field("laundry") String laundry,
                          @Field("dry_cleaning") String dryCleaning,@Field("wash_n_fold") String wash,
                          @Field("visible") int visible);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <SuccessClass> updateWash_n_fold(@Field("tag") String tag,@Field("id") int id,
                           @Field("price") String price);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <Results> addPromotion(@Field("tag") String tag,@Field("value") String value,@Field("promo_type") int type,
                      @Field("code") String code,@Field("push_noti") boolean noti,@Field("active") int active,
                      @Field("txtMsg") boolean txtMsg,@Field("message") String message);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Promotion.RawPromotion> getPromotions(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Referal> getLatestReferal(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/SearchClass.php")
    Call <Referal> updatePromotion(@Field("tag") String tag,@Field("id") int id,@Field("active") int active);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <Results> sendOrderBill(@Field("tag") String tag,@Field("server_code") String server_code,@Field("service_cost") String service_cost,
                       @Field("promotion") String promotion,@Field("bookingFee") String bookingFee,@Field("regid") String regid,
                       @Field("total") String total,@Field("referral_ids") String ids);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call <OrderBill> generateBill(@Field("tag") String tag, @Field("server_code") String server_code,
                      @Field("service_cost") String service_cost);


    @FormUrlEncoded
    @POST("forhey_mobile_scripts/dashboard_get.php")
    Call <Transaction> getTransactionData(@Field("tag") String tag);

    @GET("forhey_mobile_scripts/clientInventory.php")
    Call <InventoryList>getInventory(@Query("orderId") String orderId,@Query("tag") String tag);

    @GET("forhey_mobile_scripts/clientInventory.php")
    Call <InventoryList>getInventoryList(@Query("orderId") String orderId, @Query("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/clientInventory.php")
    Call <Inventory>addInventory (@Field("orderId") String orderId,
                                     @Field("item") String item, @Field("noOfItems") String number,
                                     @Field("type") String type,@Field("tag") String tag,@Field("price") double price);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/clientInventory.php")
    Call<Inventory>updateInventory(@Field("id") int invId,@Field("item") String item,@Field("noOfItems") String no,
                                   @Field("type") String type,@Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/clientInventory.php")
    Call<Inventory> deleteInventory(@Field("id") int invId , @Field("tag") String tag);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/access_credentials.php")
    Call<OrderList> getOrdersByServerCode(@Field("tag") String tag, @Field("server_code") String id);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/support_api.php")
    Call<Results> updateOrderStatus(@Field("tag") String tag,@Field("server_code") String id,@Field("status") String status,
                                    @Field("medium") String medium,@Field("notification") String notification,@Field("phone") String phone);

    @GET("forhey_mobile_scripts/clientInventory.php")
    Call<InventorySummary> getInventorySummary(@Query("tag") String tag,@Query("orderId") String orderId);

    @FormUrlEncoded
    @POST("forhey_mobile_scripts/clientInventory.php")
    Call<InventorySummary> addWashNFold(@Field("tag") String tag,@Field("orderId") String orderId, @Field("weight") String weight);


    @POST("forhey_mobile_scripts/clientInventory.php")
    Call <InventoryList>sendInventory(@Field("orderId") String orderId,@Field("tag") String tag,@Field("clientInventory[]") ArrayList<Inventory> arrayList);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SupportService.forhey_domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
