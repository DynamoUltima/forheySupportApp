package app.com.forheypanel.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.TimeRangeFragment;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 7/3/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

    private Calendar minDate,pkCalendar,defCal;
    private Calendar maxDate,dlCalendar;
    int mainDate,mainYear,mainMonth;
    final Calendar c = Calendar.getInstance();
    final Calendar cal= Calendar.getInstance();
    private java.text.DateFormat mTitleDateFormat;
    View layoutView;
    private TextView tvmessage;
    private MaterialDialog errorMessage;
    private Button btnClose;

    TimeRangeFragment.TimeRange mCallback;
    TimeRangeFragment.EditTimeRage mEditCallback;


    public DatePickerFragment() {
        mTitleDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int yr=0;
        int mth=0;

        int dy=0;
        pkCalendar=Calendar.getInstance();
        dlCalendar=Calendar.getInstance();
        defCal=Calendar.getInstance();

        LayoutInflater mInflater = (LayoutInflater)
                getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        layoutView=mInflater.inflate(R.layout.layout_dialog, null);
        errorMessage=new MaterialDialog(getActivity());
        tvmessage=(TextView)layoutView.findViewById(R.id.tvStateInfo);
        btnClose=(Button)layoutView.findViewById(R.id.btnStateClose);
        errorMessage.setView(layoutView);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessage.dismiss();
            }
        });

        String type=getArguments().getString("type");
        if(!type.contains("pickup")){
            System.out.println("delivey");
            pkCalendar.set(Calendar.DATE, getArguments().getInt("pkdate"));
            pkCalendar.set(Calendar.MONTH, getArguments().getInt("pkmonth"));
            pkCalendar.set(Calendar.YEAR, getArguments().getInt("year"));


            dlCalendar.set(Calendar.DATE, getArguments().getInt("deldate"));
            dlCalendar.set(Calendar.MONTH, getArguments().getInt("delmonth"));
            dlCalendar.set(Calendar.YEAR, getArguments().getInt("delyear"));
            if (pkCalendar.getTime().equals(dlCalendar.getTime())){
                yr=pkCalendar.get(Calendar.YEAR);
                mth=pkCalendar.get(Calendar.MONTH);
                dy=pkCalendar.get(Calendar.DATE);
            }else {
                yr=dlCalendar.get(Calendar.YEAR);
                mth=dlCalendar.get(Calendar.MONTH);
                dy=dlCalendar.get(Calendar.DATE);
            }

            mainDate=dy;
            mainMonth=mth;
            mainYear = yr;

        }else {
            System.out.println("pickup");
            pkCalendar.set(Calendar.DATE, getArguments().getInt("date"));
            pkCalendar.set(Calendar.MONTH, getArguments().getInt("month"));
            pkCalendar.set(Calendar.YEAR, getArguments().getInt("year"));

            /*defCal.set(Calendar.DATE, getArguments().getInt("pk-date"));
            defCal.set(Calendar.MONTH, getArguments().getInt("pk-month"));
            defCal.set(Calendar.YEAR, getArguments().getInt("pk-year"));*/
            yr=pkCalendar.get(Calendar.YEAR);
            mth=pkCalendar.get(Calendar.MONTH);
            dy=pkCalendar.get(Calendar.DATE);


            mainYear=defCal.get(Calendar.YEAR);
            mainMonth=defCal.get(Calendar.MONTH);
            mainDate=defCal.get(Calendar.DATE);


        }





        Date date12=new Date();
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), this, yr,mth,dy);
        DatePicker dp=datePickerDialog.getDatePicker();
        c.set(Calendar.YEAR,yr);
        c.set(Calendar.MONTH,mth);
        c.set(Calendar.DATE,dy);
        dp.setMinDate(c.getTimeInMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        try {
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DATE, dayOfMonth);
            if (c.get(Calendar.DAY_OF_WEEK) !=Calendar.SUNDAY){
                cal.set(Calendar.YEAR,mainYear);
                cal.set(Calendar.MONTH,mainMonth);
                cal.set(Calendar.DATE,mainDate);
                String strTha1tDay=year+"/"+monthOfYear+"/"+dayOfMonth;
                String strMinDate=mainYear+"/"+mainMonth+"/"+mainDate;

                System.out.println(" Min Date "+strMinDate);
                System.out.println(" Selected Date "+strTha1tDay);

                if(!c.getTime().before( cal.getTime()) ){
                    String type=getArguments().getString("type");
                    monthOfYear=monthOfYear+1;
                    String strThatDay=year+"/"+monthOfYear+"/"+dayOfMonth;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    Date d = null;
                    try {
                        d = formatter.parse(strThatDay);//catch exception
                        String dayoftheweek=(String) android.text.format.DateFormat.format("EEEE",d);
                        String mn=(String) android.text.format.DateFormat.format("MMM", d);
                        String fullDate;
                        if(type.contains("pickup")){
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE));
                            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
                            calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                            int day = calendar.get(Calendar.DATE);

                            if (calendar.getTime().equals(cal.getTime())){
                                fullDate="Today"+"  "+dayOfMonth+"/"+mn;
                            }else {

                                fullDate=dayoftheweek+"  "+dayOfMonth+"/"+mn;
                            }

                        }else{
                            fullDate=dayoftheweek+"  "+dayOfMonth+"/"+mn;
                        }




                        String state=getArguments().getString("state");
                        if(state.contains("Edit")){
                            mEditCallback=(TimeRangeFragment.EditTimeRage)getActivity();
                            mEditCallback.setEditDate(fullDate,monthOfYear,dayOfMonth,year,type);
                        }else{
                            mCallback=(TimeRangeFragment.TimeRange) getActivity();
                            mCallback.setDate(fullDate,monthOfYear,dayOfMonth,year,type);
                        }

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    Confirmation();

                }
            }else{
                String type=getArguments().getString("type");
                if(type.contains("pickup")){
                    // Toast.makeText(getActivity().getApplicationContext(),"Sorry, You can't set a pickup on sundays",Toast.LENGTH_SHORT).show();
                    tvmessage.setText("Sorry, You can't set pickups on sundays");
                    errorMessage.show();
                }else{
                    //Toast.makeText(getActivity().getApplicationContext(),"Sorry, You can't set deliveries on sundays",Toast.LENGTH_SHORT).show();
                    tvmessage.setText("Sorry, You can't set  deliveries on sundays");
                    errorMessage.show();
                }
            }




        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement Date Picker");
        }
    }

    public void Confirmation(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Date Selection");


        // Setting Dialog Message
        mainMonth++;
        alertDialog.setMessage("The date must not be before "+mainDate+"/"+mainMonth+"/"+mainYear);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });



        // Showing Alert Message
        alertDialog.show();
    }
}
