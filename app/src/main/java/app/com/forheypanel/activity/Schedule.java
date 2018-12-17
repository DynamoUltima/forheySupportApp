package app.com.forheypanel.activity;

import app.com.forheypanel.R;
import app.com.forheypanel.fragment.AllOrders;
import app.com.forheypanel.fragment.FragmentNextWeek;
import app.com.forheypanel.fragment.FragmentThisWeek;
import app.com.forheypanel.fragment.ListClass;
import app.com.forheypanel.fragment.PickupFragment;
import app.com.forheypanel.model.Options;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by nayram on 11/17/15.
 */
public class Schedule extends AppCompatActivity implements MaterialTabListener {

    @Bind(R.id.tabHost)
    MaterialTabHost tabHost;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.main_frame)
    FrameLayout main_frame;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    ViewPagerAdapter adapter;
    String TAG=getClass().getName();
    ArrayList<Options>arrayThisWeek=new ArrayList<Options>();
    ArrayList<Options>arrayNextWeek=new ArrayList<Options>();
    ArrayList<String>listthisWeek=new ArrayList<String>();
    ArrayList<String>listNextWeek=new ArrayList<String>();
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_list);
        ButterKnife.bind(this);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }


    @Override
    public void onTabSelected(MaterialTab tab) {

    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }



    boolean  verifyThisWeek(String date){
        for (String data:listthisWeek){
            if (data==date){
                return true;
            }
        }
        return false;
    }

    boolean verifyNextWeek(String date){
        for (String data:listNextWeek){
            if (data==date){
                return true;
            }
        }
        return false;
    }
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment=new FragmentThisWeek();
                    break;
                case 1:
                    fragment=new FragmentNextWeek();
                    break;
                default:
                    // The other sections of the app are dummy placeholders.
                    // Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(HomeTabActivity.DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title="";
            switch (position){
                case 0:
                    title="This Week";
                    break;
                case 1:
                    title="Next Week";
                    break;

            }
            return title;
        }

    }
    public static class WeekComparator implements Comparator<Options> {

       /* @Override
        public int compare(Date o1, Date o2) {
            int result = getWeekOfYear(o1) - getWeekOfYear(o2);
            if (result == 0) {
                result = o1.compareTo(o2);
            }
            return result;
        }*/

        @Override
        public int compare(Options options1, Options options2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd");
                Date d1=sdf.parse(options1.pickup_date);
                Date d2=sdf.parse(options2.pickup_date);
                int result=getWeekOfYear(d1)-getWeekOfYear(d2);
                if (result ==0){
                    result=d1.compareTo(d2);
                }
                return result;
            }catch (ParseException ex){
                ex.printStackTrace();
                return 0;
            }
//            int result=getWeekOfYear(options.pickup_date)

        }
    }

    protected static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
