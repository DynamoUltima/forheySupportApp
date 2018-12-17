package app.com.forheypanel.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayram on 2/17/17.
 */

public class IdvOrder {
    public  String tag,message,error_msg;
    public int success,error;
    public String server_code,pickup_date,pickup_point,pick_from_time,note,
            assigned_to,promotion,comment,payment,fold,press,dry_clean,
            press_only,home_service,cancel_message,payment_status,
            receipt_url,home_location,home_otherlocation,home_street_name,
            home_latitude,home_longitude,home_reference,home_housenumber,
            office_otherlocation,office_street_name,office_latitude,
            office_longitude,office_reference,office_company_name,office_housenumber,
            client_location,client_phone,client_name,
            status,created_at,order_type,client_gcm;
    public String name,phone, location,medium;
    public String office_location;
    public String amount;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = "";

        result.append(((Object) this).getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = ((Object) this).getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append(" ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }

        result.append("}");

        return result.toString();
    }



}
