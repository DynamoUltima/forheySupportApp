package app.com.forheypanel.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.dashboard.search.DashboardSearchHomeActivity;
import app.com.forheypanel.fragment.AllOrders;
import app.com.forheypanel.fragment.ListClass;
import app.com.forheypanel.model.OrderDataModel;
import app.com.forheypanel.fragment.PickupFragment;
import app.com.forheypanel.R;
import app.com.forheypanel.model.Transaction;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by nayram on 6/24/15.
 */
public class HomeTabActivity  extends BaseActivity implements MaterialTabListener {

    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter adapter;
    ArrayList<OrderDataModel> dataModels;
    ListView listView;
    SharedPreferences mprefs;
    private GridMenuFragment mGridMenuFragment;
    String  TAG="hOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tab_layout);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        mprefs=getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.forrhey_lady);
        createGridMenu();

        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager );

        // init view pager
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
                            .setTabListener(this)
            );

        }

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
        Log.e(TAG,"BEFORE MEND");
        if (id == R.id.action_search) {
            Log.e(TAG, "Witjom MEND");
            android.support.v4.app.FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main_frame, mGridMenuFragment);
            tx.addToBackStack(null);
            tx.commit();

        }else{
            Log.e(TAG,"finish MEND");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

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
                    fragment= new AllOrders();
                    break;
                default:
                    // The other sections of the app are dummy placeholders.
                    // Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
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
                    title="All Orders";
                    break;


            }
            return title;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode==RESULT_OK){
                ListClass.instance.onActivityResult(requestCode,resultCode,data);
                PickupFragment.instance.onActivityResult(requestCode,resultCode,data);
                AllOrders.instance.onActivityResult(requestCode,resultCode,data);
            }
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

    public void createGridMenu(){

        List<GridMenu> menus = new ArrayList<>();

        menus.add(new GridMenu("Add Client", R.drawable.ic_action_user_add));
        menus.add(new GridMenu("Prices", R.drawable.ic_label_outline_white_24dp));
        menus.add(new GridMenu("Customers", R.drawable.ic_person_outline_white_24dp));
        menus.add(new GridMenu("Referral", R.drawable.ic_action_referrals));
        menus.add(new GridMenu("Promotions", R.drawable.ic_redeem_white_24dp));
        menus.add(new GridMenu("Notifications", R.drawable.ic_notifications_active_white_24dp));
        menus.add(new GridMenu("Search", R.drawable.ic_search_white_24dp));
        menus.add(new GridMenu("Check Ins", R.drawable.ic_schedule_white_24dp));
        menus.add(new GridMenu("Dashboard",R.drawable.ic_dashboard_white_24dp));
        menus.add(new GridMenu("Logout", R.drawable.ic_action_logout));

        mGridMenuFragment.setupMenu(menus);

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {

                switch (gridMenu.getTitle()){
                    case "Add Client":
                        startActivity(new Intent(getApplicationContext(),AddClient.class));
                        break;
                    case "Place Order":
                        startActivity(new Intent(getApplicationContext(),PlaceClientOrder.class));
                        break;
                    case "Notifications":
                        startActivity(new Intent(getApplicationContext(), NotificationList.class));
                        break;
                    case "Search":
                        Bundle bundle=new Bundle();
                        bundle.putString("ClassName", "SearchClass");
                        Intent intent1=new Intent(getApplicationContext(),DashboardSearchHomeActivity.class);
//                        intent1.putExtras(bundle);
                        startActivity(new Intent(getApplicationContext(),DashboardSearchHomeActivity.class));
                        break;
                    case "Referral":
                        Intent intent=new Intent(getApplicationContext(),ActivityLatestReferral.class);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "Logout":
                        SharedPreferences.Editor mEditor = mprefs.edit();
                        mEditor.putBoolean("isLoggedIn", false);
                        mEditor.commit();
                        Intent logout=new Intent(getApplicationContext(),MainClass.class);
                        startActivity(logout);
                        finish();
                        break;
                    case "Check Ins":
                        startActivity(new Intent(getApplicationContext(),ActivityCheckInList.class));
                        break;
                    case "Customers":
                        startActivity(new Intent(getApplicationContext(),ActivityAllCustomers.class));
                        break;
                    case "Prices":
                        startActivity(new Intent(getApplicationContext(),ActivityPriceCart.class));
                        break;
                    case "Dashboard":
                        startActivity(new Intent(getApplicationContext(), TransactionsClass.class));
                        break;
                    case "Promotions":
                        startActivity(new Intent(getApplicationContext(),ActivityListPromos.class));
                        break;
                }
            }
        });

    }
}

