package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Client;
import app.com.forheypanel.tools.AdapterSearchClient;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/5/15.
 */
public class ActivityAllCustomers extends BaseActivity{

    @Bind(R.id.recyclerview)
    RecyclerView rv;
    String TAG=getClass().getName();

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));


        progressWheel.setVisibility(View.VISIBLE);
        loadCustomers();


    }

    void loadCustomers(){
        App.supportService.getAllCustomers("getAllUsers").enqueue(new Callback<Client.RawClientData>() {
            @Override
            public void onResponse(Call<Client.RawClientData> call, Response<Client.RawClientData> response) {
                progressWheel.setVisibility(View.GONE);
                if (response.body().success==1){
                    AdapterSearchClient adapterSearchClient=new AdapterSearchClient(response.body().client,ActivityAllCustomers.this);
                    adapterSearchClient.notifyDataSetChanged();
                    rv.setAdapter(adapterSearchClient);
                }else{
                    Toast.makeText(ActivityAllCustomers.this,response.body().error_msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client.RawClientData> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                Toast.makeText(ActivityAllCustomers.this,"Sorry, failed to load data",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_customers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.mn_search){
            startActivity(new Intent(ActivityAllCustomers.this,ActivitySearchClient.class));
        }else if (item.getItemId()==R.id.mn_refresh){
            progressWheel.setVisibility(View.VISIBLE);
            loadCustomers();



        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
