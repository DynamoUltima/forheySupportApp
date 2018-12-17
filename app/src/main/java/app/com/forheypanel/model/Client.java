package app.com.forheypanel.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayram on 12/3/15.
 */

public class Client {
    public String name;
    public int user_id;
    public String location;
    public String email;
    public String phone;
    public String home_location,home_other_location;
    public String home_street_name;
    public String home_house_number;
    public String home_reference;
    public String home_latitude;
    public String home_longitude;
    public String office_location,office_other_location;
    public String company_name;
    public String office_street_name;
    public String office_house_number;
    public String office_reference;
    public String office_latitude;
    public String error_msg;
    public String office_longitude;
    public String tag;
    public int num_of_orders,num_of_cancelled;
    public int success,error;


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

    public class RawClientData{
            public String tag;
            public int success,error;
            public ArrayList<Client>client;
        public String error_msg;

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
}
