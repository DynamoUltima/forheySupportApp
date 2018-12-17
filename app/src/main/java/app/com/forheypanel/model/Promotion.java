package app.com.forheypanel.model;

import java.util.ArrayList;

/**
 * Created by nayram on 12/29/15.
 */
public class Promotion {
    public String success_message,error_msg,code,message,last_update,promo_value;
    public int success,error,active,promo_type,id;


    public class RawPromotion{
       public String success_message,error_msg;
       public ArrayList<Promotion>promotions;
       public int success,error;

    }
}