package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.com.forheypanel.service.SupportService;
import app.com.forheypanel.tools.JSONParser;
import app.com.forheypanel.R;
import app.com.forheypanel.fragment.DatePickerFragment;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nayram on 7/3/15.
 */
public class PlaceClientOrder extends BaseActivity implements TimeRangeFragment.TimeRange {

    Date date=new Date();
    int deldate,delmonth,delyear,delfromhour,deltohour,delfrommin,deltomin,pkfromhour,pktohour,pkfrommin,pktomin,pkdate,pkmonth,pkyear;
    String pickupDateStr="",pickupStart="",pickupEnd="",loc="",pickupPoint="home";
    Calendar pickupDay=Calendar.getInstance();
    Calendar cal=Calendar.getInstance();
    private SharedPreferences mprefs;
    private List<ClientDataModel>clientData;

    EditText pickupDate,pickupTime,deliveryDate,deliveryTime,etNote,etNoteTaken,recCode,etPrCode,etPhone,etLocation;
    AutoCompleteTextView etName;
    RadioButton rbHome,rbOffice;

    Calendar calendar=Calendar.getInstance();
    Calendar delCalendar=Calendar.getInstance();
    String promoMessage="",promoCode="";

    View layoutView,promoView;
    private TextView tvmessage;
    private MaterialDialog errorMessage,promotionDialog;
    private Button btnClose,btnSend;

    String credEmail,dateString="";

    MaterialDialog mDialog,recommendationBox;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order_xml);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayUseLogoEnabled(true);
        if (actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        pickupDate=(EditText)findViewById(R.id.etPickup_date);
        pickupTime=(EditText)findViewById(R.id.etPickup_time);
        deliveryTime=(EditText)findViewById(R.id.etDelivery_time);
        deliveryDate=(EditText)findViewById(R.id.etDelivery_date);
        rbHome=(RadioButton)findViewById(R.id.rbHome);
        rbOffice=(RadioButton)findViewById(R.id.rbOffice);
        etNote=(EditText)findViewById(R.id.etNote);
        btnSend=(Button)findViewById(R.id.btnPlaceOrder);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etLocation=(EditText)findViewById(R.id.etLocation);
        etName=(AutoCompleteTextView)findViewById(R.id.etName);
        pDialog=new ProgressDialog(this);

        LayoutInflater mNotInflater = (LayoutInflater)
                this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

        layoutView=mNotInflater.inflate(R.layout.layout_dialog, null);
        errorMessage=new MaterialDialog(this);
        tvmessage=(TextView)layoutView.findViewById(R.id.tvStateInfo);
        btnClose=(Button)layoutView.findViewById(R.id.btnStateClose);
        errorMessage.setView(layoutView);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessage.dismiss();
            }
        });
      // etName.setAdapter(new clientAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));

        etName.setAdapter(new  clientAutoCompleteAdapter(this));

        final Calendar c = Calendar.getInstance();

        Date date1=new Date();
        Date delDate=new Date();
        Calendar initialCal=Calendar.getInstance();
        if(initialCal.get(Calendar.HOUR_OF_DAY)<7){
            pkyear = initialCal.get(Calendar.YEAR);
            pkmonth = initialCal.get(Calendar.MONTH);
            pkdate = initialCal.get(Calendar.DATE);
            pkmonth=pkmonth+1;
            deldate=pkdate;
            delyear=pkyear;
            delmonth=pkmonth;
            pkfromhour= 7;
            pkfrommin= 00;
            pktomin=00;
            pktohour=8;
            delfromhour=16;
            delfrommin=00;
            deltohour=19;
            deltomin=00;
        }else if(initialCal.get(Calendar.HOUR_OF_DAY)<10){
            pkyear = initialCal.get(Calendar.YEAR);
            pkmonth = initialCal.get(Calendar.MONTH);
            pkdate = initialCal.get(Calendar.DATE);
            pkmonth=pkmonth+1;
            deldate=pkdate;
            delyear=pkyear;
            delmonth=pkmonth;
            delDate.setDate(deldate);
            pkfromhour=date1.getHours()+1;
            pkfrommin= date1.getMinutes();
            date.setHours(pkfromhour+1);
            pktohour=pkfromhour+1;
            pktomin= date.getMinutes();
            delfromhour=16;
            delfrommin=00;

            deltohour=19;
            deltomin=00;
        }else if( initialCal.get(Calendar.HOUR_OF_DAY)==10 ||initialCal.get(Calendar.HOUR_OF_DAY)> 10 && initialCal.get(Calendar.HOUR_OF_DAY)<18){
            int day=initialCal.get(Calendar.DATE);
            pkyear = initialCal.get(Calendar.YEAR);
            pkmonth = initialCal.get(Calendar.MONTH);
            pkdate = initialCal.get(Calendar.DATE);
            day++;
            delDate.setDate(day);

            pkmonth=pkmonth+1;

            deldate=delDate.getDate();
            delyear=c.get(Calendar.YEAR);
            delmonth=delDate.getMonth()+1;
            pkfromhour= date1.getHours()+1;
            pkfrommin= date1.getMinutes();
            pktomin=date1.getMinutes();
            pktohour=pkfromhour+1;
            delfromhour=16;
            delfrommin=00;
            deltohour=19;
            deltomin=00;
        }else{
            int day=initialCal.get(Calendar.DATE);
            day=day+1;
            initialCal.set(Calendar.DATE,day);
            pkyear = initialCal.get(Calendar.YEAR);
            pkmonth = initialCal.get(Calendar.MONTH);
            pkdate = initialCal.get(Calendar.DATE);
            pkmonth=pkmonth+1;

            deldate=pkdate;
            delyear=pkyear;
            delmonth=pkmonth;

            pkfromhour= 7;
            pkfrommin= 00;
            pktomin=00;
            pktohour=8;
            delfromhour=16;
            delfrommin=00;
            deltohour=17;
            deltomin=00;
        }

        Date date2=new Date();
        pickupTime.setText(showTime(pkfromhour,pkfrommin)+" - "+showTime(pktohour,pktomin));
        pickupStart=showTime(pkfromhour, pkfrommin);
        pickupEnd=showTime(pktohour, pktomin);
        pickupDay=Calendar.getInstance();

        pickupDay.set(Calendar.YEAR,pkyear);
        int mmh=pkmonth-1;
        System.out.println("month "+mmh);
        pickupDay.set(Calendar.MONTH,mmh);
        pickupDay.set(Calendar.DATE, pkdate);
        Date wkDate=new Date(pkdate,mmh,pkyear);
        int comp =wkDate.getDay();
        System.out.println("day " + wkDate.getDay()+" "+comp);




        String dayOfTheWeek="";
        String mn = (String) android.text.format.DateFormat.format("MMM", date);

        if ( pickupDay.get(Calendar.DAY_OF_WEEK) !=Calendar.SUNDAY ){
            System.out.println("Not sunday "+pickupDay.get(Calendar.DAY_OF_WEEK));
            System.out.println("Not sunday "+pickupDay.get(Calendar.DATE)+" "+pickupDay.get(Calendar.MONTH)+" "+pickupDay.get(Calendar.YEAR));
            System.out.println("today "+date.getDate()+" pkdate "+pkdate);
            if (date.getDate()<pkdate){
                pickupDate.setText("Tomorrow"+"  "+pkdate+"/"+mn);
                pickupDateStr="Tomorrow"+"  "+pkdate+"/"+mn;
                dateString=(String) android.text.format.DateFormat.format("EEEE",date);


            }else{
                pickupDate.setText("Today"+"  "+pkdate+"/"+mn);
                pickupDateStr="Today"+"  "+pkdate+"/"+mn;
                dateString=(String) android.text.format.DateFormat.format("EEEE",date);
            }
        }else {
            System.out.println("today "+date.getDate()+" pkdate "+pkdate);
            pkdate=pkdate+1;
            int tdmnth=pkmonth-1;
            initialCal.set(Calendar.DATE,pkdate);
            initialCal.set(Calendar.MONTH,tdmnth);
            initialCal.set(Calendar.YEAR, pkyear);
            pkyear = initialCal.get(Calendar.YEAR);
            pkmonth = initialCal.get(Calendar.MONTH);
            pkmonth=pkmonth+1;
            pkdate = initialCal.get(Calendar.DATE);

            pickupDay.set(Calendar.YEAR, pkyear);

            pickupDay.set(Calendar.MONTH,pkmonth-1);
            pickupDay.set(Calendar.DATE, pkdate);

            if (date.getDate()<pkdate){
                pickupDate.setText("Tomorrow"+"  "+pkdate+"/"+mn);
                pickupDateStr="Tomorrow"+"  "+pkdate+"/"+mn;
                dateString=(String) android.text.format.DateFormat.format("EEEE",date);


            }else{
                pickupDate.setText("Today"+"  "+pkdate+"/"+mn);
                pickupDateStr="Today"+"  "+pkdate+"/"+mn;
                dateString=(String) android.text.format.DateFormat.format("EEEE",date);
            }
            System.out.println("date pk " + pkdate);

        }

        delCalendar=Calendar.getInstance();
        delCalendar.set(Calendar.DATE,deldate);
        delCalendar.set(Calendar.MONTH,delmonth-1);
        delCalendar.set(Calendar.YEAR, delyear);

        if (delCalendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
            System.out.println("Not sunday");
            if(date2.getDate()==deldate){
                System.out.println("hours date "+date2.getHours());
            /*if(date2.getHours()<16){
                delfromhour=16;
                delfrommin=00;
                deltohour=17;
                deltomin=00;
                deldate=pkdate;
                delyear=pkyear;
                delmonth=pkmonth;
            }else if (date2.getHours()> 16) {
                delfromhour=pkfromhour+1;
                delfrommin=pkfrommin;
                deltohour=delfromhour+1;
                deltomin=00;
                deldate=pkdate;
                delyear=pkyear;
                delmonth=pkmonth;
            }*/

                deliveryTime.setText(showTime(delfromhour,delfrommin)+" - "+showTime(deltohour,deltomin));
                dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", delDate);
                mn = (String) android.text.format.DateFormat.format("MMM", delDate);
                deliveryDate.setText("Today  "+deldate+"/"+mn);
            }else{
                deliveryTime.setText(showTime(delfromhour,delfrommin)+" - "+showTime(deltohour,deltomin));
                dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", delDate);
                mn = (String) android.text.format.DateFormat.format("MMM", delDate);
                deliveryDate.setText("Tomorrow"+"  "+deldate+"/"+mn);
            }
        }else{
            System.out.println(" sunday");

            deldate=deldate+1;
            delCalendar=Calendar.getInstance();
            delCalendar.set(Calendar.DATE,deldate);
            delCalendar.set(Calendar.MONTH, delmonth -1);
            delCalendar.set(Calendar.YEAR,delyear);
            delmonth=delCalendar.get(Calendar.MONTH);
            delmonth=delmonth+1;
            delyear=delCalendar.get(Calendar.YEAR);
            if(date2.getDate()==(deldate)){
                System.out.println("hours date "+date2.getHours());

                deliveryTime.setText(showTime(delfromhour,delfrommin)+" - "+showTime(deltohour,deltomin));
                dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", delDate);
                mn = (String) android.text.format.DateFormat.format("MMM", delDate);
                deliveryDate.setText("Today  "+deldate+"/"+mn);
            }else{
                deliveryTime.setText(showTime(delfromhour,delfrommin)+" - "+showTime(deltohour,deltomin));
                dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", delDate);
                mn = (String) android.text.format.DateFormat.format("MMM", delDate);
                deliveryDate.setText("Tomorrow"+"  "+deldate+"/"+mn);
            }


        }

        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callFagDialog("pickup");


            }

        });

        deliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callFagDialog("delivery");

            }
        });

        pickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentManager fm = getSupportFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("type", "pickup");
                    args.putString("state", "New");
                    TimeRangeFragment frag = TimeRangeFragment.newInstance("Setup pick up time range");
                    frag.setArguments(args);
                    frag.show(fm, "pick_time_range");

            }
        });

        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentManager fm = getSupportFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("type", "delivery");
                    args.putString("state", "New");
                    args.putInt("date",deldate);
                    args.putInt("month", delmonth);
                    args.putInt("year", delyear);
                    TimeRangeFragment frag = TimeRangeFragment.newInstance("Setup delivery time range");
                    frag.setArguments(args);
                    frag.show(fm, "deliver_time_range");

            }
        });

        rbHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    pickupPoint = "home";
                }
            }
        });

        rbOffice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    pickupPoint = "office";
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPhone.getText().toString().isEmpty()){
                    if (etPhone.getText().toString().length()<10){
                        setNotifier("Please check the length of the phone number");
                    }else{
                        new PlaceOrderTask().execute();
                    }
                }else{
                    setNotifier("Please enter phone number");
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTimeRange(String fromTime, String toTime, String Type, int fromHour, int fromMinute,int toHour, int toMin) {
        if(Type.equals("pickup")){
            Date pickupDte=new Date();
            Date current=new Date();
            pickupDte.setHours(fromHour);
            pickupDte.setMinutes(fromMinute);
            pickupDte.setDate(pkdate);
            pickupDte.setMonth(pkmonth);
            pickupDte.setYear(pkyear);

            if(pickupDte.getTime()< current.getTime() && current.equals(pickupDte)){
                setNotifier("Pickup time should not be before current time");
            }else{
                pickupTime.setText(fromTime+" - "+toTime);
                pickupStart=fromTime;
                pickupEnd=toTime;
                pkfromhour=fromHour;
                pkfrommin=fromMinute;
                pktohour=toHour;
                pktomin=toMin;
            }


        }else{

            deliveryTime.setText(fromTime + " - " + toTime);
            delfrommin=fromMinute;
            delfromhour=fromHour;
            deltohour=toHour;
            deltomin=toMin;
        }
    }

    public void callFagDialog(String type){
        DialogFragment newFragment = new DatePickerFragment();
        Bundle args=new Bundle();


        args.putString("type",type);
        args.putString("state","New");

        if(type.contains("delivery")){

            args.putInt("deldate",deldate);
            args.putInt("delmonth",delmonth-1);
            args.putInt("delyear",delyear);
            args.putInt("pkdate",pickupDay.get(Calendar.DATE));
            args.putInt("pkmonth",pickupDay.get(Calendar.MONTH));
            args.putInt("pkyear",pickupDay.get(Calendar.YEAR));
        }else{
            System.out.println("Before date "+pkdate+" month "+pkmonth+" year "+pkyear);


            args.putInt("date",pickupDay.get(Calendar.DATE));
            args.putInt("month",pickupDay.get(Calendar.MONTH));
            args.putInt("year", pickupDay.get(Calendar.YEAR));

            args.putInt("pk-date",pkdate);
            args.putInt("pk-month",pkmonth-1);
            args.putInt("pk-year",pkyear);
        }

        newFragment.setArguments(args);


        newFragment.show(this.getSupportFragmentManager(), type + "_+DateFragment");
    }

    @Override
    public void setDate(String date, int month, int day, int year, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        if(type.equals("pickup")){
            pkdate=day;
            pkmonth=month;
            pkyear=year;
            pickupDate.setText(date);
           /* pickupDay=Calendar.getInstance();
            pickupDay.set(Calendar.DATE,pkdate);
            pickupDay.set(Calendar.MONTH,pkmonth);
            pickupDay.set(Calendar.YEAR,pkyear);*/
            String strDay=year+"/"+month+"/"+day;
            System.out.println("pick up date "+strDay);
            Date dt=null;
            try{
                dt = formatter.parse(strDay);
                dateString=(String) android.text.format.DateFormat.format("EEEE",dt);
            }catch (ParseException e){
                e.printStackTrace();
            }
            pickupDateStr=date;
            deldate=day+1;


            Date d = null;
            delCalendar=Calendar.getInstance();

            delCalendar.set(Calendar.DATE, deldate);
            delCalendar.set(Calendar.MONTH,pkmonth-1);
            System.out.println("date "+delCalendar.get(Calendar.YEAR)+"/"+delCalendar.get(Calendar.MONTH)+"/"+delCalendar.get(Calendar.DATE));
            //delCalendar.set(Calendar.MONTH,pkmonth);
            int mnth=delCalendar.get(Calendar.MONTH)+1;
            String strThatDay=delCalendar.get(Calendar.YEAR)+"/"+mnth+"/"+delCalendar.get(Calendar.DATE);
            if (delCalendar.get(Calendar.MONTH) == Calendar.JANUARY||delCalendar.get(Calendar.MONTH)==Calendar.MARCH||
                    delCalendar.get(Calendar.MONTH)==Calendar.MAY||delCalendar.get(Calendar.MONTH)==Calendar.JULY||
                    delCalendar.get(Calendar.MONTH)==Calendar.AUGUST||delCalendar.get(Calendar.MONTH)==Calendar.OCTOBER||
                    delCalendar.get(Calendar.MONTH)==Calendar.DECEMBER){
                // setNotifier("more than 4 weeks");
                // delCalendar.set(Calendar.DATE,1);



            }

            System.out.println("Real month ");

            if (delCalendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
                System.out.println("Not sunday"+delCalendar.get(Calendar.DATE)+" month "+mnth);
                try{

                    d=null;
                    d = formatter.parse(strThatDay);//catch exception
                    calendar=Calendar.getInstance();
                    calendar.set(Calendar.DATE,delCalendar.get(Calendar.DATE));
                    calendar.set(Calendar.MONTH,delCalendar.get(Calendar.MONTH));
                    calendar.set(Calendar.YEAR,delCalendar.get(Calendar.YEAR));
                    deldate=calendar.get(Calendar.DATE);
                    delmonth=calendar.get(Calendar.MONTH)+1;
                    delyear=calendar.get(Calendar.YEAR);
                    String dayoftheweek=(String) android.text.format.DateFormat.format("EEEE",d);

                    String mn=(String) android.text.format.DateFormat.format("MMM", d);
                    String fullDate="";
                    fullDate=dayoftheweek+"  "+deldate+"/"+mn;
                    deliveryDate.setText(fullDate);

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }else {
                delCalendar=Calendar.getInstance();
                deldate++;
                delCalendar.set(Calendar.DATE,deldate);
                delCalendar.set(Calendar.MONTH,pkmonth-1);

                  /* delCalendar.set(Calendar.MONTH,month);
                delCalendar.set(Calendar.YEAR,year);*/
                mnth=delCalendar.get(Calendar.MONTH)+1;
                strThatDay=delCalendar.get(Calendar.YEAR)+"/"+mnth+"/"+delCalendar.get(Calendar.DATE);
                // strThatDay=delCalendar.get(Calendar.YEAR)+"/"+mnth+"/"+delCalendar.get(Calendar.DATE);
                System.out.println("sunday"+delCalendar.get(Calendar.DAY_OF_WEEK));
                try{
                    d = formatter.parse(strThatDay);//catch exception
                    calendar=Calendar.getInstance();
                    calendar.set(Calendar.DATE,deldate);
                    calendar.set(Calendar.MONTH,delCalendar.get(Calendar.MONTH));
                    calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                    deldate=calendar.get(Calendar.DATE);
                    delmonth=calendar.get(Calendar.MONTH)+1;
                    delyear=calendar.get(Calendar.YEAR);
                    String dayoftheweek=(String) android.text.format.DateFormat.format("EEEE",d);

                    String mn=(String) android.text.format.DateFormat.format("MMM", d);
                    String fullDate="";
                    fullDate=dayoftheweek+"  "+deldate+"/"+mn;
                    deliveryDate.setText(fullDate);

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }



        }else{
            deldate=day;
            delmonth=month;
            delyear=year;
            deliveryDate.setText(date);
        }
    }

    private void setNotifier(String param){
        //   Toast.makeText(this,param,Toast.LENGTH_SHORT).show();
        tvmessage.setText(param);
        errorMessage.show();
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

    class PlaceOrderTask extends AsyncTask<Void,Void,String>{

        JSONParser jparser=new JSONParser();
        String url= SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
        private  String KEY_SUCCESS = "success";
        private  String KEY_ERROR = "error";
        private  String KEY_ERROR_MSG = "error_msg";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Placing Order..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!= null && !s.isEmpty()){
                try{
                    JSONObject jobj=new JSONObject(s);
                    int success=jobj.getInt(KEY_SUCCESS);
                    Log.e("PlaceOrder","Succees "+success);

                    if(success ==1){
                        //JSONObject json_user = jobj.getJSONObject("user");
                        Bundle group=new Bundle();
                        group.putString("OrderId", jobj.getString("order_code"));
                        group.putString("ClassName", "HomeClass");
                        group.putString("OrderType", "Offline");

                        Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
                        intent.putExtras(group);
                        startActivity(intent);

                    }else{
                        setNotifier(jobj.getString(KEY_ERROR_MSG));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }else{
                setNotifier("failed to add order");
            }

            pDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("tag","add_offline_order"));
            args.add(new BasicNameValuePair("pickup_point",pickupPoint));
            args.add(new BasicNameValuePair("pickup_date",pkyear+"/"+pkmonth+"/"+pkdate));
            args.add(new BasicNameValuePair("pick_from_time",showTime(pkfromhour, pkfrommin)));
            args.add(new BasicNameValuePair("pick_to_time",showTime(pktohour, pktomin)));
            args.add(new BasicNameValuePair("note", etNote.getText().toString()));
            args.add(new BasicNameValuePair("delivery_date",delyear+"/"+delmonth+"/"+deldate));
            args.add(new BasicNameValuePair("delivery_from_time",showTime(delfromhour,delfrommin)));
            args.add(new BasicNameValuePair("delivery_to_time",showTime(deltohour,deltomin)));
            args.add(new BasicNameValuePair("status","0"));
            args.add(new BasicNameValuePair("official",credEmail));
            args.add(new BasicNameValuePair("created_at",calendar.getTime().toString()));
            args.add(new BasicNameValuePair("pickup_to_hour",String.valueOf(pktohour)));
            args.add(new BasicNameValuePair("pickup_to_minute",String.valueOf(pktomin)));
            args.add(new BasicNameValuePair("pickup_from_hour",String.valueOf(pkfromhour)));
            args.add(new BasicNameValuePair("pickup_from_minute",String.valueOf(pkfrommin)));
            args.add(new BasicNameValuePair("delivery_from_hour",String.valueOf(delfromhour)));
            args.add(new BasicNameValuePair("delivery_from_min",String.valueOf(delfrommin)));
            args.add(new BasicNameValuePair("delivery_to_hour",String.valueOf(deltohour)));
            args.add(new BasicNameValuePair("delivery_to_min",String.valueOf(deltomin)));
            args.add(new BasicNameValuePair("phone",etPhone.getText().toString()));


            String json=jparser.makeHttpRequest(url, "POST", args);
            System.out.println("json item " + json);
            return json;
        }
    }

    class ClientDataModel{
        String name,phone,location;

        public ClientDataModel(String name, String phone, String location) {
            this.name = name;
            this.phone = phone;
            this.location = location;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class clientAutoCompleteAdapter extends ArrayAdapter<ClientDataModel> implements Filterable{
        ArrayList<ClientDataModel> items;

        public clientAutoCompleteAdapter(Context context) {
            super(context,android.R.layout.simple_dropdown_item_1line);

            this.items=new ArrayList<ClientDataModel>();
        }

        @Override
        public ClientDataModel getItem(int position) {

            return items.get(position);
        }



        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.client_info_row, null);
            }

           final ClientDataModel l = items.get(position);

            if (l != null) {

                TextView name = (TextView) v.findViewById(R.id.tvName);
                TextView location = (TextView) v.findViewById(R.id.tvLocation);
                TextView phone = (TextView) v.findViewById(R.id.tvPhone);

                if (name != null){
                    name.setText(l.getName());
                }
                if (location != null){
                    location.setText(l.getLocation());
                }
                if (phone != null){
                    phone.setText(l.getPhone());
                }
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etName.setText(l.getName());
                    etPhone.setText(l.getPhone());
                    etLocation.setText(l.getLocation());
                }
            });

            return v;


        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint != null) {
                            try{
                                items=new LoadClientData().execute(new String[]{constraint.toString()}).get();
                                System.out.println("Client name "+items.get(0).getName());
                            }catch (Exception e){

                            }
                        Log.e("PlaceClientOrder",String.valueOf(items.size()));
                        filterResults.values = items;
                        filterResults.count = items.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence contraint,
                                              FilterResults results) {
                    if(results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return myFilter;


        }

        private class LoadClientData extends AsyncTask<String,Void,ArrayList<ClientDataModel>>{
            JSONParser jparser=new JSONParser();
            String url=SupportService.domain+"forhey_mobile_scripts/access_credentials.php";
            private  String KEY_SUCCESS = "success";
            private  String KEY_ERROR = "error";
            private  String KEY_ERROR_MSG = "error_msg";

            @Override
            protected void onPostExecute(ArrayList<ClientDataModel> clientDataModels) {
               // super.onPostExecute(clientDataModels);

            }

            @Override
            protected ArrayList<ClientDataModel> doInBackground(String... params) {
                ArrayList<ClientDataModel> clientList=new ArrayList<ClientDataModel>();
                List<NameValuePair> args = new ArrayList<NameValuePair>();
                args.add(new BasicNameValuePair("tag","SearchClient"));
                args.add(new BasicNameValuePair("name",params[0]));
                String json=jparser.makeHttpRequest(url, "POST", args);

                System.out.println("json item " + json);
                try{
                    JSONObject jobj=new JSONObject(json);
                    int success=jobj.getInt(KEY_SUCCESS);
                    Log.e("PlaceOrder","Succees "+success);


                    if(success ==1){
                        //JSONObject json_user = jobj.getJSONObject("user");
                        JSONArray array= jobj.getJSONArray("client_data");
                        for (int i=0; i<array.length();i++){
                            JSONObject jsonObject=array.getJSONObject(i);
                            clientList.add(new ClientDataModel(jsonObject.getString("name"),
                                    jsonObject.getString("phone_number"),jsonObject.getString("location")));
                        }


                    }else{
                        //setNotifier(jobj.getString(KEY_ERROR_MSG));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return clientList;
            }
        }
    }



}
