package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pnikosis.materialishprogress.ProgressWheel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.CategoryGridAdapter;
import app.com.forheypanel.adapter.PriceGridAdapter;
import app.com.forheypanel.model.PriceCartegory;
import app.com.forheypanel.model.PriceItem;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/9/15.
 */
public class ActivityPrices extends BaseActivity{

    @Bind(R.id.recyclerview)
    RecyclerView rv;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.frame_layout)
    RelativeLayout layout;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);
        Bundle bundle=getIntent().getExtras();

        if (bundle!=null){
            id=bundle.getInt("cart_id");
            if (new ConnectionDetector(this).isConnectingToInternet()){
                progressWheel.setVisibility(View.VISIBLE);
                loadPrices();

            }else{
                Toast.makeText(rv.getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==120){
            progressWheel.setVisibility(View.VISIBLE);
            loadPrices();
        }
    }

    void loadPrices(){
        App.supportService.getItemPrices("getItemsByCartegories",id).enqueue(new Callback<PriceItem.ListPriceItem>() {
            @Override
            public void onResponse(Call<PriceItem.ListPriceItem> call, Response<PriceItem.ListPriceItem> response) {
                progressWheel.setVisibility(View.GONE);
                if (response.body().success==1)
                    rv.setAdapter(new PriceGridAdapter(response.body().items,ActivityPrices.this));
                else
                    Toast.makeText(ActivityPrices.this,response.body().error_msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PriceItem.ListPriceItem> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                Toast.makeText(ActivityPrices.this,"Sorry, failed to load data",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
