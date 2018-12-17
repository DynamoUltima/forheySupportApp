package app.com.forheypanel.model;


import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayram on 11/17/15.
 */


public class Order {
    public  String tag,message,error_msg;
    public int success,error;
    public String server_code,pickup_date,pickup_point,client_location,client_phone,client_name,status,created_at,order_type;
    public String name,phone, location,email;

//    public ArrayList<Options>order_list=new ArrayList<Options>();

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

    public class OrderArrayList {
//        public ArrayList<Order>
    }

    public class OrderList{
       public String tag,message;
       public int success,error;
       public ArrayList<Options>order_list;
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

    public class Referal{
       public int gcm_success,phone_success,success,error;
       public String success_message,error_msg;

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
