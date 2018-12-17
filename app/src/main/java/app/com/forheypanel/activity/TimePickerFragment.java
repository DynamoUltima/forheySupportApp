package app.com.forheypanel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import app.com.forheypanel.R;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 1/14/15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    String type="",parentType="";
    String format = "";
    OnTimeSelected mCallback;
    int hr,min;

    View layoutView;
    private TextView tvmessage;
    private MaterialDialog errorMessage;
    private Button btnClose;

    public TimePickerFragment() {

    }




    public interface OnTimeSelected{
        public void TimeSelected(String time,String type,int hr,int min);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        type=getArguments().getString("Type");
        parentType=getArguments().getString("parent");
        if (type.contains("ToTime"))
            minute=minute+30*1000;
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



        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                !DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        try {
            mCallback=(OnTimeSelected) getTargetFragment();
            System.err.println("hour of the day "+hourOfDay+" minute "+minute+" type "+type);
            hr=hourOfDay;
            min=minute;
            Date date=new Date();
            Date refDelDate=new Date();
            Date endDelDate=new Date();
            Calendar today=Calendar.getInstance();
            Calendar refCalendar=Calendar.getInstance();
            Calendar endCalendar=Calendar.getInstance();

            Date endPikDate=new Date();
            Date startPickDate=new Date();
            refDelDate.setHours(16);
            refDelDate.setMinutes(00);
            endDelDate.setHours(19);
            endDelDate.setMinutes(00);
            endPikDate.setHours(18);
            endPikDate.setMinutes(30);

            date.setHours(hourOfDay);
            date.setMinutes(minute);

            startPickDate.setHours(7);
            startPickDate.setMinutes(00);


            if (parentType.contains("delivery")){
                if (date.getTime()<refDelDate.getTime()){
                    logTime("Sorry, Delivery starts at 4 pm each day");
                }else if (date.getTime()>endDelDate.getTime()){
                    logTime("Sorry, Delivery ends at 7 pm");
                }else{
                    mCallback.TimeSelected(showTime(hourOfDay,minute),type,hourOfDay,minute);
                }
            }else{
                if (date.getTime()<startPickDate.getTime()){
                    logTime("Sorry, Pickup starts at 7 am");
                }else if(date.getTime()>endPikDate.getTime()){
                    logTime("Sorry, Pickup ends at 6:30 pm");
                }else{
                    mCallback.TimeSelected(showTime(hourOfDay,minute),type,hourOfDay,minute);
                }

            }



        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement Time Picker");


        }



    }

    public String showTime(int hour, int min) {
        String result;
        String format,stMin=String.valueOf(min);
        if (stMin.length()==1){
            stMin='0'+stMin;
        }
        if (hour == 0) {
            hour += 12;
            format = "am";
        } else if (hour == 12) {
            format = "pm";
        } else if (hour > 12) {
            hour -= 12;
            format = "pm";
        } else {
            format = "am";
        }
        result= String.valueOf(new StringBuilder().append(hour).append(" : ").append(stMin).append(" ").append(format));
        return  result;
    }

    public void logTime(String param){
        // Toast.makeText(getActivity(),param,Toast.LENGTH_LONG).show();
        tvmessage.setText(param);
        errorMessage.show();
    }

}
