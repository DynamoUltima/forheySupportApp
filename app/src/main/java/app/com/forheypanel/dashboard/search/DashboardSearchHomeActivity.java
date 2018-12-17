package app.com.forheypanel.dashboard.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.com.forheypanel.R;
import app.com.forheypanel.activity.BaseActivity;
import app.com.forheypanel.activity.TimeRangeFragment;
import app.com.forheypanel.dashboard.search.adapter.SearchOrdersByDateAdapter;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.dashboard.search.tools.DatePicker;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardSearchHomeActivity extends BaseActivity implements TimeRangeFragment.TimeRange {

    SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-M-dd");

   @Bind(R.id.toolbar_dashboard)
    Toolbar toolbar;

   @Bind(R.id.edtFrom)
    EditText edtFrom;

   @Bind(R.id.editTo)
   EditText edtTo;

   @Bind(R.id.pBar)
    ProgressBar pBar;

   @Bind(R.id.recycSearch)
    RecyclerView recycSearch;


   @OnClick(R.id.edtFrom)
   public void showFromDate(){

       Calendar calendar= Calendar.getInstance();

       DatePicker newFragment =  DatePicker.newInstance("From - Date",calendar.getTimeInMillis());

       newFragment.show(getSupportFragmentManager(), "datePicker");
   }

   @OnClick(R.id.editTo)
   public void showToDate(){

       if (!edtFrom.getText().toString().isEmpty()){

           Calendar calendar= Calendar.getInstance();

           try {

               Date date = parserSDF.parse(edtFrom.getText().toString());

               calendar.setTime(date);
               DatePicker datePicker=DatePicker.newInstance("To - Date",calendar.getTimeInMillis());
               datePicker.show(getSupportFragmentManager(),"datePicker");
           } catch (ParseException e) {
               e.printStackTrace();
           }    
       }else {
           Toast.makeText(this, "First choose add date from which to start the search", Toast.LENGTH_SHORT).show();
       }

   }

   @OnClick(R.id.searchFab)
   public void search(){
       if (!edtFrom.getText().toString().isEmpty()
               && !edtTo.getText().toString().isEmpty())
       {
           pBar.setVisibility(View.VISIBLE);
           App.supportApiService.getOrdersByDate(

                   edtFrom.getText().toString(),edtTo.getText().toString()

           ).enqueue(new Callback<ArrayList<Order>>() {
               @Override
               public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {

                   pBar.setVisibility(View.GONE);

                   if (response.isSuccessful())
                   {
                        if (response.body().size()>0){

                            recycSearch.setAdapter(new SearchOrdersByDateAdapter(response.body(),getApplicationContext()));

                        }else {

                            Toast.makeText(DashboardSearchHomeActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                        }
                   }else {

                       Toast.makeText(DashboardSearchHomeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                   pBar.setVisibility(View.GONE);
                   t.printStackTrace();
                   Toast.makeText(DashboardSearchHomeActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
               }
           });
       }else{
           Toast.makeText(this, "Provide date search parameters", Toast.LENGTH_SHORT).show();
       }
   }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_inventory_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search");
        recycSearch.setLayoutManager(new LinearLayoutManager(recycSearch.getContext()));
        recycSearch.setHasFixedSize(true);
        recycSearch.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       finish();

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTimeRange(String fromTime, String toTime, String Type, int fromhour, int fromminute, int toHour, int toMin) {

    }

    @Override
    public void setDate(String date, int month, int day, int year, String type) {
        if (type.equalsIgnoreCase("From - Date"))
        {
            edtFrom.setText(date);
        }else{
            edtTo.setText(date);
        }
    }


}
