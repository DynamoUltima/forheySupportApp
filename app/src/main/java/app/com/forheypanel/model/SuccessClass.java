package app.com.forheypanel.model;

import java.lang.reflect.Field;

/**
 * Created by nayram on 12/10/15.
 */
public class SuccessClass {
    public int success,error;
    public String laundry="",dry_cleaning="",num_of_laundry,num_of_dry_cleaning;
    public String error_msg,success_message;

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
