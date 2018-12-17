package app.com.forheypanel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import app.com.forheypanel.R;
import app.com.forheypanel.fragment.FragmentAccount;
import app.com.forheypanel.fragment.FragmentCheckIn;
import app.com.forheypanel.fragment.FragmentOrders;
import app.com.forheypanel.fragment.FragmentSchedule;
import app.com.forheypanel.tools.CheckInCallback;
import app.com.forheypanel.tools.Helper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nayram on 11/21/15.
 */
public class ActivityHome extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,CheckInCallback{

    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private TextView tvTitle;
    private ImageView profileHeaderImg;
    SharedPreferences mpref;
    CircleImageView profileImage;


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_container);
        initViews();
        initToolbar();
        setTitle("Schedule");
        setContentFragment(new FragmentSchedule());

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_white);

    }

    private void initViews() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View header=inflater.inflate(R.layout.header_nav,null);
        profileImage=(CircleImageView)header.findViewById(R.id.profileImage);
        tvTitle=(TextView)header.findViewById(R.id.tvTitle);
        mpref=getSharedPreferences("Credentials",Context.MODE_PRIVATE);
        String image=mpref.getString("image",null);
        String name=mpref.getString("name",null);
        if (name!=null){
            tvTitle.setText(name);
        }

        if (image!=null)
            if (!image.isEmpty())
                Picasso.with(this)
                    .load(image)
                    .placeholder(getResources().getDrawable(R.drawable.ic_action_person))
                    .into(profileImage);
        navigationView.addHeaderView(header);

    }

    public void setContentFragment(Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.mn_orders:
                drawerlayout.closeDrawers();
                setTitle("Orders");
                setContentFragment(new FragmentOrders());
                break;
            case R.id.mn__schedule:
                drawerlayout.closeDrawers();
                setTitle("Schedule");
                setContentFragment(new FragmentSchedule());
                break;
            case R.id.mn_account:
                drawerlayout.closeDrawers();
                setTitle("Account");
                setContentFragment(new FragmentAccount());
                break;
           /* case R.id.settings:

                break;*/
            case R.id.mn_checkin:
                drawerlayout.closeDrawers();
                setTitle("Tap To Check In");
                setContentFragment(new FragmentCheckIn());
                break;
            case R.id.mn_logout:
                drawerlayout.closeDrawers();
                Helper.setLogout(getApplicationContext());
                Intent logout=new Intent(getApplicationContext(),MainClass.class);
                startActivity(logout);
                finish();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCheckInCallback() {
        switch (Helper.getLastView(ActivityHome.this)){
            case "AllOrders":
                setTitle("Orders");
                setContentFragment(new FragmentOrders());
                break;
            case "Schedule":
                setTitle("Schedule");
                setContentFragment(new FragmentSchedule());
                break;
            default:
                setTitle("Schedule");
                setContentFragment(new FragmentSchedule());
                break;
        }
    }
}
