package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.forheypanel.database.LocalDatabase;
import app.com.forheypanel.R;
import app.com.forheypanel.adapter.NotificationAdapter;
import app.com.forheypanel.model.NotificationDataModel;

/**
 * Created by nayram on 6/1/15.
 */
public class NotificationList extends BaseActivity {
    ListView lvNotification;
    ProgressDialog progressDialog;
    LocalDatabase db;
    ArrayList<NotificationDataModel>dataModels;
    String userEmail;
    SharedPreferences mpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        lvNotification=(ListView)findViewById(R.id.lvNotification);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        db=new LocalDatabase(this);
        mpref=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        userEmail=mpref.getString("email", "Default");

        getNotList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getNotList(){
        db.open();
        lvNotification.setAdapter(new NotificationAdapter(this,db.getAllNotifications(userEmail)));
        db.close();

    }

    public static class MyNotificationHolder{
        public TextView tvTitle,tvMessage;
        public NotificationDataModel model;
        public String orderid;
        public ImageView imgNew;
        public int id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.menu_clear){
            db.open();
           int res=db.clearNotifications();
            db.close();
            if (res>0){
                getNotList();
            }

        }else{
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
