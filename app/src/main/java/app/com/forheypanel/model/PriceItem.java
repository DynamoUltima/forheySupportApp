package app.com.forheypanel.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by nayram on 12/9/15.
 */
public class PriceItem {
    public String item_name,image,error_msg;
    public int success,id,error,visible;
    public boolean wash_n_fold;
    public String price="",laundry="0.00",dry_cleaning="0.00";
    public String category="";

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

    public class ListPriceItem {

        public int success,error;
        public String error_msg;
        public ArrayList<PriceItem>items;

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
