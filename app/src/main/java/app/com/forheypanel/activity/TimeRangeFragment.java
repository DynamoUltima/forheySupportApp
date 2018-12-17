package app.com.forheypanel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import app.com.forheypanel.R;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 1/13/15.
 */
public class TimeRangeFragment extends DialogFragment implements TimePickerFragment.OnTimeSelected {
    EditText fromTime,toTime;
    TimeRange mCallback;
    EditTimeRage mEditCallback;
    int deldate,delmonth,delyear,delfromhour,deltohour,delfrommin,deltomin,pkfromhour,pktohour,pkfrommin,pktomin,pkdate,pkmonth,pkyear;
    int fromHour,fromMin,toHour,toMin;
    String parentType;
    final Calendar totime=Calendar.getInstance();
    View layoutView;
    private TextView tvmessage;
    private MaterialDialog errorMessage;
    private Button btnClose;


    public TimeRangeFragment(){

    }

    public static TimeRangeFragment newInstance(String title){
        TimeRangeFragment frag=new TimeRangeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public interface TimeRange{
        public void setTimeRange(String fromTime,String toTime,String Type,int fromhour,int fromminute,int toHour,int toMin);
        public void setDate(String date,int month,int day,int year,String type);

    }
    public interface EditTimeRage{
        public void setEditDate(String date,int month,int day,int year,String type);
        public void setEditTimeRange(String fromTime,String toTime,String Type,int fromhour,int fromminute,int toHour,int toMin);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final Calendar c = Calendar.getInstance();
        final Calendar totime=Calendar.getInstance();
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
        Date date=new Date();
        Date delDate=new Date();
        parentType=getArguments().getString("type");

        if(parentType.contains("delivery")){
            int dDate=getArguments().getInt("date");
            int dMonth=getArguments().getInt("month");
            dMonth--;
            int dyear=getArguments().getInt("year");
            date.setDate(dDate);
            date.setMonth(dMonth);

            delfromhour=16;
            delfrommin=00;
            deltohour=18;
            deltomin=00;

            //date.setYear();
            // Toast.makeText(getActivity(),"date "+date.getDate()+" month "+date.getMonth(),Toast.LENGTH_SHORT).show();
            // Toast.makeText(getActivity(),"Del date "+delDate.getDate()+" month "+delDate.getMonth(),Toast.LENGTH_LONG).show();

            System.out.println("date " + date.getDate() + " month " + date.getMonth() + " year " + date.getYear());
            if(delDate.getDate()==date.getDate() && delDate.getMonth()== date.getMonth() && delDate.getYear()==date.getYear()) {
                pkfromhour = 8;
                pkfrommin = 00;
                pktomin = 00;
                pktohour = 9;
                delfromhour = 16;
                delfrommin = 00;
                deltohour = 17;
                deltomin = 00;
            }
        }else if(date.getHours()<8 ){
            pkfromhour= 8;
            pkfrommin= 00;
            pktomin=00;
            pktohour=9;
            delfromhour=pkfromhour;
            delfrommin=pkfrommin;
            deltohour=pktohour;
            deltomin=pktomin;
        }else if(date.getHours()<10){

            pkfromhour=date.getHours()+1;
            pkfrommin= date.getMinutes();
            date.setHours(pkfromhour+1);
            pktohour=date.getHours();
            pktomin= date.getMinutes();
            delfromhour=delDate.getHours();
            delfrommin=delDate.getMinutes();
            delDate.setHours(delfromhour+1);
            deltohour=delDate.getHours();
            deltomin=delDate.getMinutes();
        }else if(date.getHours()> 17){
            pkfromhour= 8;
            pkfrommin= 00;
            pktomin=00;
            pktohour=9;
            delfromhour=pkfromhour;
            delfrommin=pkfrommin;
            deltohour=pktohour;
            deltomin=pktomin;

        }else{

            pkfromhour=date.getHours()+1;
            pkfrommin= date.getMinutes();
            date.setHours(pkfromhour+1);
            pktohour=date.getHours();
            pktomin= date.getMinutes();
            delfromhour=delDate.getHours();
            delfrommin=delDate.getMinutes();
            delDate.setHours(delfromhour+1);
            deltohour=delDate.getHours();
            deltomin=delDate.getMinutes();


        }

        try {
            String state=getArguments().getString("state");
            if(state.contains("Edit")){
                mEditCallback=(EditTimeRage)getActivity();
            }else{
                mCallback=(TimeRange) getActivity();
            }
        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement Time Picker");
        }
        final String title = getArguments().getString("title", "Set Time");
        builder.setTitle(title);
        builder.setPositiveButton("Done",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type=getArguments().getString("type");
                if (type.contains("delivery")){
                    fromHour=delfromhour;
                    fromMin=delfrommin;
                    toHour=deltohour;
                    toMin=deltomin;
                }else{
                    fromHour=pkfromhour;
                    fromMin=pktomin;
                    toHour=pktohour;
                    toMin=pktomin;
                }
                String state=getArguments().getString("state");
                if(state.contains("Edit")){
                    mEditCallback.setEditTimeRange(fromTime.getText().toString(),toTime.getText().toString(),type,fromHour,fromMin,toHour,toMin);
                }else{
                    mCallback.setTimeRange(fromTime.getText().toString(),toTime.getText().toString(),type,fromHour,fromMin,toHour,toMin);
                }
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View rootView=inflater.inflate(R.layout.time_range_layout,null);
        fromTime=(EditText)rootView.findViewById(R.id.etFromTime);
        toTime=(EditText)rootView.findViewById(R.id.etToTime);
        // if(delDate.getDate()==date.getDate() && delDate.getMonth()== date.getMonth() && delDate.getYear()==date.getYear()){
        if(parentType.contains("delivery")){

            fromTime.setText(showTime(delfromhour,delfrommin));

            //toTime.setText(showTime(deltohour,deltomin));
            if(date.getHours()<10){
                toTime.setText(showTime(19,00));
            }else{
                toTime.setText(showTime(deltohour,deltomin));
            }
        }else{
            fromTime.setText(showTime(pkfromhour,pkfrommin));
            toTime.setText(showTime(pktohour,pktomin));
        }

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFagDialog("FromTime");
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  callFagDialog("ToTime");
            }
        });
        builder.setView(rootView);
        return builder.create();
    }
    @Override
    public void TimeSelected(String time,String type,int hr,int min) {
        totime.set(Calendar.HOUR, hr);
        totime.set(Calendar.MINUTE,min);
        totime.add(Calendar.MINUTE,60);
        Date dd=new Date();
        Date delv=new Date();
        Date d=new Date();
        d.setHours(hr+1);
        d.setMinutes(min);

        if(parentType.contains("delivery")){
            int dDate=getArguments().getInt("date");
            int dMonth=getArguments().getInt("month");
            dMonth--;
            int dyear=getArguments().getInt("year");

            delv.setDate(dDate);
            delv.setMonth(dMonth);
            delv.setYear(dyear);


            System.out.println("date " + delv.getDate() + " month " + delv.getMonth() + " year " + delv.getYear());

            System.out.println("date2 " + dd.getDate() + " month2 " + dd.getMonth() + " year2 " + dd.getYear());

            System.out.println("hour "+hr);
            if ( dd.getDate()==delv.getDate() && dd.getMonth()== delv.getMonth() && hr < 16){
                // Toast.makeText(getActivity(),"delivery starts at 4 pm",Toast.LENGTH_LONG).show();
                toTime.setText(showTime(19,00));
                fromTime.setText(showTime(delfromhour,delfrommin));
                tvmessage.setText("delivery starts at 4 pm");
                errorMessage.show();

            }else{
                delfromhour=hr;
                delfrommin=min;
                deltohour=d.getHours();
                //Toast.makeText(getActivity(),"Hour "+String.valueOf(deltohour),Toast.LENGTH_LONG).show();
                deltomin=d.getMinutes();
                toTime.setText(showTime(deltohour,deltomin));
                fromTime.setText(time);
            }

        }else{
            pkfromhour=hr;
            pkfrommin=min;
            pktohour=d.getHours();
            pktomin=d.getMinutes();
            toTime.setText(showTime(pktohour,pktomin));
            fromTime.setText(time);
        }
    }

    public void callFagDialog(String tpe){
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args=new Bundle();
        args.putString("parent",parentType);
        if (tpe.equals("ToTime")){
            args.putString("Type","ToTime");

        }else {
            args.putString("Type","FromTime");
        }

        newFragment.setArguments(args);
        newFragment.setTargetFragment(this,0);
        newFragment.show(getActivity().getSupportFragmentManager(), "to_timePicker");
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
}
