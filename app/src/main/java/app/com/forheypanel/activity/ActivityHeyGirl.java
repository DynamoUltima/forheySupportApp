package app.com.forheypanel.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import app.com.forheypanel.R;
import app.com.forheypanel.fragment.FragmentNextWeek;
import app.com.forheypanel.fragment.FragmentThisWeek;
import app.com.forheypanel.model.Order;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nayram on 11/24/15.
 */
public class ActivityHeyGirl extends BaseActivity {


    @Bind(R.id.profile_image)
    ImageView profile_image;

    @Bind(R.id.main_content)
    CoordinatorLayout main_content;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    TabLayout tabs;

    String email,TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hey_girl);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        String image=bundle.getString("image", null);
        String name=bundle.getString("name",null);
        email=bundle.getString("email",null);
        if (image!=null){
            Picasso.with(this)
                    .load(image)
                    .into(profile_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

        }

        if (name !=null){
            setTitle(name);
        }

        setupViewPager();

    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        Fragment thisWeekFrag=new FragmentThisWeek();
        Fragment nextWeekFrag=new FragmentNextWeek();

        Log.e(TAG,"EMAIL "+email);
        Bundle bundle=new Bundle();
        bundle.putString("email",email);
        thisWeekFrag.setArguments(bundle);
        nextWeekFrag.setArguments(bundle);

        adapter.addFragment(thisWeekFrag, "This Week");
        adapter.addFragment(nextWeekFrag,"Next Week");

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
