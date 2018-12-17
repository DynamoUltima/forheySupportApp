package app.com.forheypanel.dashboard.search.tools;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.util.Log;

import java.util.Calendar;

import app.com.forheypanel.activity.TimeRangeFragment;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TimeRangeFragment.TimeRange mCallback;
    long time;
    String title="";
    String TAG="";

    public DatePicker() {
    }

    public static DatePicker newInstance(String title,long time){
        DatePicker dp=new DatePicker();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putLong("time",time);
        dp.setArguments(args);
        return dp;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mCallback=(TimeRangeFragment.TimeRange) getActivity();

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, year, month, day);
        title = getArguments().getString("title", "Set Date");
        time=getArguments().getLong("time",0);

        if (title.equalsIgnoreCase("From - Date"))
        {
            Log.d(TAG,String.valueOf(time));
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        }

        if (title.equalsIgnoreCase("To - Date"))
        {
            Log.d(TAG,String.valueOf(time));
            datePickerDialog.getDatePicker().setMinDate(time);
        }
        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        month=month+1;
        mCallback.setDate(year+"-"+month+"-"+day,month,day,year,title);
    }
}
