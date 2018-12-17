package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.PromotionsAdapter;
import app.com.forheypanel.model.Promotion;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by nayram on 12/29/15.
 */
public class ActivityListPromos extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView rv;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.frame_layout)
    RelativeLayout layout;

    String TAG=getClass().getName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_promotions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.mn_add){
            Intent intent=new Intent(ActivityListPromos.this,ActivityPromotion.class);
            startActivityForResult(intent, 5);
        }else if (item.getItemId()==R.id.mn_refresh){
            refresh();
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==5){
            refresh();
        }
    }

    void refresh(){
        ConnectionDetector connectionDetector=new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()){
            progressWheel.setVisibility(View.VISIBLE);
            App.supportService.getPromotions("getAllPromotions").enqueue(new Callback<Promotion.RawPromotion>() {
                @Override
                public void onResponse(Call<Promotion.RawPromotion> call, retrofit2.Response<Promotion.RawPromotion> response) {
                    progressWheel.setVisibility(View.GONE);
                    if (response.body().success==1){
                        PromotionsAdapter promo_adapter=new PromotionsAdapter(ActivityListPromos.this,response.body().promotions);
                        rv.setAdapter(promo_adapter);
                    }else{
                        Toast.makeText(getApplicationContext(),response.body().error_msg,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Promotion.RawPromotion> call, Throwable t) {
                    t.printStackTrace();
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Sorry, failed to load data",Toast.LENGTH_SHORT).show();

                }
            });
            /*App.supportService.getPromotions("getAllPromotions", new Callback<Promotion.RawPromotion>() {
                @Override
                public void onResponse(Call<Promotion.RawPromotion> call, Response<Promotion.RawPromotion> response) {
                    progressWheel.setVisibility(View.GONE);
                    if (response.body().success==1){
                        PromotionsAdapter promo_adapter=new PromotionsAdapter(ActivityListPromos.this,response.body().promotions);
                        rv.setAdapter(promo_adapter);
                    }else{
                        Toast.makeText(getApplicationContext(),response.body().error_msg,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Promotion.RawPromotion> call, Throwable t) {
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Sorry, failed to load data",Toast.LENGTH_SHORT).show();
                }
            });*/


        }else{
            Toast.makeText(getApplicationContext(),"No internet connection!!",Toast.LENGTH_SHORT).show();
        }
    }
}
