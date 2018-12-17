package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Referal;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.ConnectionDetector;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/30/15.
 */
public class ActivityLatestReferral extends BaseActivity{

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.layout_frame)
    LinearLayout layout;

    @Bind(R.id.btn_add)
    Button add;

    @Bind(R.id.tvMessage)
    TextView tvMessage;

    @Bind(R.id.tvValue)
    TextView tvValue;

    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_latest_referral);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        connectionDetector=new ConnectionDetector(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityLatestReferral.this,AddReferralValue.class);
                startActivityForResult(intent, 5);
            }
        });

        if (connectionDetector.isConnectingToInternet()){
            progressWheel.setVisibility(View.VISIBLE);
            loadReferral();
        }else{
            Toast.makeText(getApplicationContext(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_referral,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==5){
            if (connectionDetector.isConnectingToInternet()){

                progressWheel.setVisibility(View.VISIBLE);
                loadReferral();
            }else{
                Toast.makeText(getApplicationContext(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.mn_refresh){
            if (connectionDetector.isConnectingToInternet()){
                progressWheel.setVisibility(View.VISIBLE);
                loadReferral();
            }else{
                Toast.makeText(getApplicationContext(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
            }
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void loadReferral(){
        App.supportService.getLatestReferal("getLatestReferal").enqueue(new Callback<Referal>() {
            @Override
            public void onResponse(Call<Referal> call, Response<Referal> response) {
                progressWheel.setVisibility(View.GONE);
                if (response.body().success==1){
                    layout.setVisibility(View.VISIBLE);
                    tvMessage.setText(response.body().message);
                    if (response.body().type==1){
                        tvValue.setText("GHC"+response.body().col_value);
                    }else{
                        tvValue.setText(response.body().col_value+"%");
                    }
                }else {
                    Toast.makeText(getApplicationContext(),response.body().error_msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Referal> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Sorry, failed to load data",Toast.LENGTH_SHORT).show();

            }
        });


    }
}
