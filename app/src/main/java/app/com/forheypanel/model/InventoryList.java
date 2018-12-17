package app.com.forheypanel.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class InventoryList {

    public String tag,message;
    public int success,error;
    public ArrayList<Inventory> clientInventory;
    public WashNFold weight;
    public String error_msg;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = "";

        result.append(((Object) this).getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        Field[] fields = ((Object) this).getClass().getDeclaredFields();

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
