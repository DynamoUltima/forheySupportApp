package app.com.forheypanel.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayram on 2/17/17.
 */

public class OrderList {
    public String tag,message;
    public int success,error;
    public ArrayList<IdvOrder> order_list;
    public ArrayList<HeyGirlOptions>assignees;
    public ArrayList<Facility>assigned_fac;
    public String error_msg;
    public ArrayList<HeyGirlOptions> order_admin;

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
