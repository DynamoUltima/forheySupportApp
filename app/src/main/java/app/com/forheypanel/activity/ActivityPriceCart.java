package app.com.forheypanel.activity;

import android.os.Bundle;

import com.pnikosis.materialishprogress.ProgressWheel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.CategoryGridAdapter;
import app.com.forheypanel.model.PriceCartegory;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/9/15.
 */
public class ActivityPriceCart extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView rv;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.frame_layout)
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);

        if (new ConnectionDetector(this).isConnectingToInternet()){
            progressWheel.setVisibility(View.VISIBLE);
            App.supportService.getPriceCategories("getCartegories").enqueue(new Callback<PriceCartegory.ListPriceCart>() {
                @Override
                public void onResponse(Call<PriceCartegory.ListPriceCart> call, Response<PriceCartegory.ListPriceCart> response) {
                    progressWheel.setVisibility(View.GONE);
                    if (response.body().success==1)
                        rv.setAdapter(new CategoryGridAdapter(rv.getContext(),response.body().cartegory));
                    else
                        Toast.makeText(rv.getContext(),response.body().error_msg,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PriceCartegory.ListPriceCart> call, Throwable t) {
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(rv.getContext(),"Sorry, failed to load data",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(rv.getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
