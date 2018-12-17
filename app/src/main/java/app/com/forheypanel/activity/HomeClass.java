package app.com.forheypanel.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.goka.blurredgridmenu.BlurredGridMenuConfig;
//import com.goka.blurredgridmenu.Config;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;


import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.fragment.AllOrders;
import app.com.forheypanel.fragment.DeliveryFragment;
import app.com.forheypanel.fragment.ListClass;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.adapter.OrderlistAdater;
import app.com.forheypanel.fragment.PickupFragment;
import app.com.forheypanel.R;

/**
 * Created by nayram on 4/24/15.
 */
public class HomeClass extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    ArrayList<OrderDataModel> dataModels;
    ListView listView;
    SharedPreferences mprefs;
    private GridMenuFragment mGridMenuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_viewpager);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

       final android.app.ActionBar actionBar=getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(true);
       // actionBar.setDisplayHomeAsUpEnabled(true);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.hide();
        BlurredGridMenuConfig
                .build(new BlurredGridMenuConfig.Builder()
                        .radius(1)
                        .downsample(1)
                        .overlayColor(Color.parseColor("#000000")));



        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        createGridMenu();

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
           android.support.v4.app.FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main_frame, mGridMenuFragment);
            tx.addToBackStack(null);
            tx.commit();

        }else{



                finish();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void setListData(ArrayList<OrderDataModel> param){
        this.dataModels=param;
        listView.setAdapter(new OrderlistAdater(this,param));

    }

    public void createGridMenu(){
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Add Client", R.drawable.ic_action_user_add));
        menus.add(new GridMenu("Place Order", R.drawable.ic_action_user_add));
        menus.add(new GridMenu("Notifications", R.drawable.ic_action_user_add));
        menus.add(new GridMenu("Search", R.drawable.ic_action_user_add));
        menus.add(new GridMenu("Add Referral", R.drawable.ic_action_referrals));
        menus.add(new GridMenu("Logout", R.drawable.ic_action_user_add));
        int backgroundResource=R.drawable.forrhey_lady;
        mGridMenuFragment=GridMenuFragment.newInstance(backgroundResource);


        mGridMenuFragment.setupMenu(menus);
        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {
                /*Toast.makeText(context, "Title:" + gridMenu.getTitle() + ", Position:" + position,
                        Toast.LENGTH_SHORT).show();*/
            }
        });
       /* mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(com.goka.blurredgridmenu.MenuItem menuItem) {
//                Toast.makeText(getApplicationContext(), menuItem.title, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DummySectionFragment();
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    fragment= new ListClass();
                    break;

                case 1:
                    fragment= new PickupFragment();
                    break;
                case 2:
                    fragment= new DeliveryFragment();
                    break;
                case 3:
                    fragment= new AllOrders();
                    break;
                default:
                    // The other sections of the app are dummy placeholders.
//                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title="";
            switch (position){
                case 0:
                    title="New Order";
                    break;
                case 1:
                    title="Today's Pickup";
                    break;
                case 2:
                    title="Today's Delivery";
                    break;
                case 3:
                    title="All Orders";
                    break;


            }
            return title;
        }
    }

    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.new_list, container, false);



            return rootView;
        }
    }

    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
           /* ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }



}
