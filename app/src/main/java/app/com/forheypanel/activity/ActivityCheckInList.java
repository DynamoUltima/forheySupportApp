package app.com.forheypanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.model.HeyGirlOptions;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 11/26/15.
 */
public class ActivityCheckInList extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView rv;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        ButterKnife.bind(this);
        setTitle("Today's Check Ins");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        progressWheel.setVisibility(View.VISIBLE);
        App.supportService.todaysCheckin("get_today_check_in").enqueue(new Callback<HeyGirlOptions.ListHeyGirlOptions>() {
            @Override
            public void onResponse(Call<HeyGirlOptions.ListHeyGirlOptions> call, Response<HeyGirlOptions.ListHeyGirlOptions> response) {
                progressWheel.setVisibility(View.GONE);
                if (response.body().success==1)
                    rv.setAdapter(new HeyGirlAdapter(response.body().checkin_list));
                else{
                    Toast.makeText(ActivityCheckInList.this,response.body().error_msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeyGirlOptions.ListHeyGirlOptions> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                Toast.makeText(ActivityCheckInList.this,"Sorry, failed to load data",Toast.LENGTH_SHORT).show();

            }
        });


    }

    class HeyGirlAdapter extends RecyclerView.Adapter<HeyGirlAdapter.ViewHolder>{
        
        ArrayList<HeyGirlOptions> items;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public final View itemView;
            public final TextView tvName;
            public final TextView tvTime;
            public final CircleImageView imageView;


            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                tvName=(TextView)itemView.findViewById(R.id.tvName);
                tvTime=(TextView)itemView.findViewById(R.id.tvTimeStamp);
                imageView=(CircleImageView)itemView.findViewById(R.id.profile_image);
            }
        }

        public HeyGirlAdapter (ArrayList<HeyGirlOptions> items ){
            this.items=items;

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_heygirl_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Picasso.with(holder.imageView.getContext())
                    .load(items.get(position).image)
                    .into(holder.imageView);
            holder.tvName.setText(items.get(position).name);
            holder.tvTime.setText(items.get(position).chk_time);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent notificationIntent = new Intent(view.getContext(), ActivityHeyGirl.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("name", holder.tvName.getText().toString());
                    bundle.putString("email", items.get(position).email);
                    bundle.putString("image", items.get(position).name);
//        bundle.putBoolean("history", true);
                    //bundle.putString("OrderId",server_code);
                    notificationIntent.putExtras(bundle);
                    startActivity(notificationIntent);
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
