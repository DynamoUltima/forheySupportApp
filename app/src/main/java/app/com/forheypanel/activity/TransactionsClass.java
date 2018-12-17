package app.com.forheypanel.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Transaction;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 5/26/16.
 */
public class TransactionsClass extends BaseActivity {

    @Bind(R.id.ll_app_orders)
    LinearLayout ll_app_orders;

    @Bind(R.id.ll_web_orders)
    LinearLayout ll_web_orders;

    @Bind(R.id.ll_order_deliveries)
    LinearLayout ll_order_deliveries;

    @Bind(R.id.ll_orders_cancelled)
    LinearLayout ll_order_cancelled;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Bind(R.id.tvAppOrdersNum)
    TextView tvAppOrdersNum;

    @Bind(R.id.tvWebOrdersNum)
    TextView tvWebOrderNum;

    @Bind(R.id.tvOrdDel)
    TextView tvOrdDel;

    @Bind(R.id.tvOrderCancel)
    TextView tvOrderCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_layout);
        ButterKnife.bind(this);
        progressWheel.setVisibility(View.VISIBLE);
        App.supportService.getTransactionData("get_numbers").enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                progressWheel.setVisibility(View.GONE);
                tvAppOrdersNum.setText(String.valueOf(response.body().app_orders));
                tvWebOrderNum.setText(String.valueOf(response.body().web_orders));
                tvOrdDel.setText(String.valueOf(response.body().orders_delivered));
                tvOrderCancel.setText(String.valueOf(response.body().orders_cancelled));
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Toast.makeText(TransactionsClass.this,"Sorry, failed to load data",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
}
