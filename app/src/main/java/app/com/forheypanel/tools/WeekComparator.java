package app.com.forheypanel.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import app.com.forheypanel.model.Options;

/**
 * Created by nayram on 11/20/15.
 */
public class WeekComparator implements Comparator<Options> {

    @Override
    public int compare(Options options1, Options options2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd");
            Date d1=sdf.parse(options1.pickup_date);
            Date d2=sdf.parse(options2.pickup_date);
            int result=getWeekOfYear(d1)-getWeekOfYear(d2);
            if (result ==0){
                result=d1.compareTo(d2);
            }
            return result;
        }catch (ParseException ex){
            ex.printStackTrace();
            return 0;
        }
//            int result=getWeekOfYear(options.pickup_date)

    }

    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
