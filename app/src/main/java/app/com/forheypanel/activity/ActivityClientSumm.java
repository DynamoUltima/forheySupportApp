package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.com.forheypanel.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nayram on 12/5/15.
 */
public class ActivityClientSumm extends BaseActivity {

    @Bind(R.id.tvName)
    TextView tvName;

    @Bind(R.id.tvLocation)
    TextView tvLocation;

    @Bind(R.id.tvEmail)
    TextView tvEmail;

    @Bind(R.id.tvPhone)
    TextView tvPhone;

    @Bind(R.id.tvMore)
    TextView tvMore;

    @Bind(R.id.btn_Orders)
    Button btnOrders;

    @Bind(R.id.btnCancel)
    Button btnCancel;

    @Bind(R.id.tvOrders)
    TextView tvOrders;

    @Bind(R.id.tvCancel)
    TextView tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sum);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Bundle bundle=getIntent().getExtras();

        if (bundle != null){
            tvEmail.setText(bundle.getString("email",""));
            tvLocation.setText(bundle.getString("location",""));
            tvName.setText(bundle.getString("name",""));
            tvPhone.setText(bundle.getString("phone",""));
            final int cancel=bundle.getInt("no_cancel");
            final int orders=bundle.getInt("no_orders");

            tvCancel.setText(String.valueOf(cancel));
            tvOrders.setText(String.valueOf(orders));

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cancel > 0) {
                        Bundle group = new Bundle();
                        group.putString("type", "cancel");
                        group.putString("email", tvEmail.getText().toString());
                        Intent intent = new Intent(ActivityClientSumm.this, ActivityCustOrders.class);
                        intent.putExtras(group);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ActivityClientSumm.this, "Sorry, this customer has no orders", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orders > 0) {
                        Bundle group = new Bundle();
                        group.putString("type", "all_orders");
                        group.putString("email", tvEmail.getText().toString());
                        Intent intent = new Intent(ActivityClientSumm.this, ActivityCustOrders.class);
                        intent.putExtras(group);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ActivityClientSumm.this, "Sorry, this customer has no orders", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=bundle.getInt("client_id");
                    Bundle group=new Bundle();
                    group.putInt("client_id", id);
                    Intent intent=new Intent(ActivityClientSumm.this,ActivityClient.class);
                    intent.putExtras(group);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
