package app.com.forheypanel.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nayram on 8/10/15.
 */
public class FactorDateClass {
    Calendar calendar=Calendar.getInstance();
    Calendar calToday=Calendar.getInstance();

    public FactorDateClass() {
    }

    public String factorDate(String params){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dayOfTheWeek="",mn="",res="";

        Date d=null;
        try{

            d = formatter.parse(params);//catch exception
            calendar.setTime(formatter.parse(params));
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);

            if (calendar.get(Calendar.YEAR)<calToday.get(Calendar.YEAR)) {
                res = dayOfTheWeek + " " + d.getDate() + " " + mn+" "+calendar.get(Calendar.YEAR);
            }else{
                res = dayOfTheWeek + " " + d.getDate() + " " + mn;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        return res;
    }

    public String formatDate(String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dayOfTheWeek="",mn="",res="";

        Date d=null;
        try{

            d = formatter.parse(date);//catch exception
            calendar.setTime(formatter.parse(date));
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", d);
            mn = (String) android.text.format.DateFormat.format("MMM", d);

            if (calendar.get(Calendar.YEAR)<calToday.get(Calendar.YEAR)) {
                res = dayOfTheWeek + " " + d.getDate() + " " + mn+" "+calendar.get(Calendar.YEAR);
            }else{
                res = dayOfTheWeek + " " + d.getDate() + " " + mn;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        return res;
    }
}
