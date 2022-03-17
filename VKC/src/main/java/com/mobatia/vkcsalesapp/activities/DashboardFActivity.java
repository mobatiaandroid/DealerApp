package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcsalesapp.adapter.NavDrawerExpandableListAdapter;
import com.mobatia.vkcsalesapp.adapter.NavDrawerExpandableListAdapter.OnExploreListener;
import com.mobatia.vkcsalesapp.adapter.NavDrawerExpandableListAdapter.OnItemClickListener;
import com.mobatia.vkcsalesapp.adapter.NavDrawerListAdapter;
import com.mobatia.vkcsalesapp.adapter.SearchAdapter;
import com.mobatia.vkcsalesapp.appdialogs.AppExitDialog;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.fragments.ComplaintFragment;
import com.mobatia.vkcsalesapp.fragments.ContactUsFragment;
import com.mobatia.vkcsalesapp.fragments.CreditPaymentFragment;
import com.mobatia.vkcsalesapp.fragments.DealersRetailersAndDateFilter;
import com.mobatia.vkcsalesapp.fragments.FeedbackFragment;
import com.mobatia.vkcsalesapp.fragments.GiftRewardReportFragment;
import com.mobatia.vkcsalesapp.fragments.HomeFragment;
import com.mobatia.vkcsalesapp.fragments.LocationFragment;
import com.mobatia.vkcsalesapp.fragments.MyCustomersFragment;
import com.mobatia.vkcsalesapp.fragments.NotificationListFragment;
import com.mobatia.vkcsalesapp.fragments.ProductListNew_Fragment;
import com.mobatia.vkcsalesapp.fragments.Quick_Order_Fragment;
import com.mobatia.vkcsalesapp.fragments.RecentOrdersFragment;
import com.mobatia.vkcsalesapp.fragments.RedeemReportFragment;
import com.mobatia.vkcsalesapp.fragments.RedeemedListFragment;
import com.mobatia.vkcsalesapp.fragments.SalesHeadOrderList;
import com.mobatia.vkcsalesapp.fragments.SalesOrderFragment;
import com.mobatia.vkcsalesapp.fragments.SalesOrderStatusListFragment;
import com.mobatia.vkcsalesapp.fragments.SchemePointsFragment;
import com.mobatia.vkcsalesapp.fragments.SearchListFragment;
import com.mobatia.vkcsalesapp.fragments.SubDealerOrderList;
import com.mobatia.vkcsalesapp.fragments.SubdealerOrderStatusFragment;
import com.mobatia.vkcsalesapp.fragments.TransactionHistoryFragment;
import com.mobatia.vkcsalesapp.fragments.VKCDealersListView;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.model.CategoryModel;
import com.mobatia.vkcsalesapp.model.NavDrawerItem;
import com.mobatia.vkcsalesapp.model.SortCategory;
import com.mobatia.vkcsalesapp.util.ConnectivityReceiver;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashboardFActivity extends AppCompatActivity implements
        VKCDbConstants, ConnectivityReceiver.ConnectivityReceiverListener {

    private DrawerLayout mDrawerLayout;
    ExpandableListView lvExp;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private String[] categoryIdList;
    public static String categoryId;
    public static int categorySelectionPosition = -1;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private static int mNavDrawerItemIndex = 0;
    private List<String> items;
    SearchView searchView;
    String userType;
    int mDealerCount, mRoleId;
    public String key = "";

    public static DashboardFActivity dashboardFActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardFActivity = this;
        AppController.product_id = "";
        setContentView(R.layout.activity_dashboard_f);
        mTitle = mDrawerTitle = getTitle();
        AppPrefenceManager.saveFilterDataCategory(dashboardFActivity, "");
        AppPrefenceManager.saveFilterDataSize(dashboardFActivity, "");
        AppPrefenceManager.saveFilterDataBrand(dashboardFActivity, "");
        AppPrefenceManager.saveFilterDataColor(dashboardFActivity, "");
        AppPrefenceManager.saveFilterDataPrice(dashboardFActivity, "");
        AppPrefenceManager.saveFilterDataOffer(dashboardFActivity, "");
        setIconTitle();
        Thread.setDefaultUncaughtExceptionHandler((UncaughtExceptionHandler) new AppController(
                this, DashboardFActivity.class));

        checkDatabase();


        initArray();
        init(savedInstanceState);
        initProductDetails();

    }

    private void setIconTitle() {

        navMenuTitles = AppController.navMenuTitles;
        categoryIdList = AppController.categoryIdList;
        userType = AppPrefenceManager.getUserType(dashboardFActivity);
        mDealerCount = Integer.parseInt(AppPrefenceManager
                .getDealerCount(dashboardFActivity));
        if (AppPrefenceManager.getUserType(dashboardFActivity).equals("")) {
            mRoleId = 0;
        } else {
            mRoleId = Integer.parseInt(AppPrefenceManager
                    .getUserType(dashboardFActivity));
        }

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

    }

    protected void checkDatabase() {
        DatabaseHelper myDbHelper = new DatabaseHelper(DashboardFActivity.this,
                DBNAME);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
            myDbHelper.close();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    private void initArray() {

        System.out.println("21012015:" + userType);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // Home
        navDrawerItems.add(new NavDrawerItem("Home", navMenuIcons
                .getResourceId(0, -1), "-1"));

        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
                    .getResourceId(i + 1, -1), categoryIdList[i]));
            // System.out.println("NAVMENu"+navMenuTitles.length);
        }
        navDrawerItems.add(new NavDrawerItem("Locate Us", R.drawable.brand,
                "-3"));
        navDrawerItems.add(new NavDrawerItem("Contact Us", R.drawable.location,
                "-5"));

        // App Version
        navDrawerItems.add(new NavDrawerItem("Version : " + getVersion(),
                R.color.transparent, "-7"));
        // navDrawerItems.add(new NavDrawerItem("Version 1.2"));
        navMenuIcons.recycle();
    }

    @SuppressLint("NewApi")
    private void init(Bundle savedInstanceState) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvExp = (ExpandableListView) findViewById(R.id.lvExp);
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) lvExp
                .getLayoutParams();
        params.width = width;
        lvExp.setLayoutParams(params);
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);

        HashMap<NavDrawerItem, ArrayList<NavDrawerItem>> hashMapNavDrawerItemChild = new HashMap<NavDrawerItem, ArrayList<NavDrawerItem>>();
        SortCategory sortCategory[] = VKCSplashActivity.sortCategoryGlobal
                .getSortCategorys();
        System.out.println("13022015 SortCategory size:" + sortCategory.length);
        lvExp.setGroupIndicator(null);
        ArrayList<NavDrawerItem> itemsHome = new ArrayList<NavDrawerItem>();
        hashMapNavDrawerItemChild.put(navDrawerItems.get(0), itemsHome);

        for (int j = 0; j < sortCategory.length; j++) {

            ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
            ArrayList<CategoryModel> filterArrayList = sortCategory[j]
                    .getCategoryModels();
            for (int i = 0; i < filterArrayList.size(); i++) {
                CategoryModel categoryModel = filterArrayList.get(i);
                NavDrawerItem drawerItem = new NavDrawerItem();
                drawerItem.setTitle(categoryModel.getName());
                drawerItem.setId(categoryModel.getId());
                items.add(drawerItem);
            }
            hashMapNavDrawerItemChild.put(navDrawerItems.get(j + 1), items);
        }

        NavDrawerExpandableListAdapter expandableListAdapter = new NavDrawerExpandableListAdapter(
                this, navDrawerItems, hashMapNavDrawerItemChild,
                new OnExploreListener() {

                    @Override
                    public void onExpandGroup(int position) {
                        // TODO Auto-generated method stub
                        lvExp.expandGroup(position, true);
                    }

                    @Override
                    public void onCollapseGrope(int position) {
                        // TODO Auto-generated method stub
                        lvExp.collapseGroup(position);
                    }
                });
        expandableListAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemSelected(String id) {
                AppPrefenceManager.saveSubCategoryId(DashboardFActivity.this,
                        id + "");
                goToProductList(Integer.parseInt(id));
            }
        });
        lvExp.setAdapter(expandableListAdapter);
        lvExp.setBackgroundColor(Color.parseColor("#DDFFFFFF"));
        setActionBar();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.backbutton, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // Setting, Refresh and Rate App
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.closeDrawers();

        beginFragmentTransaction();
        // set
        if (savedInstanceState == null) {
            if (AppPrefenceManager.getDealerCount(this).equals("0")
                    && AppPrefenceManager.getUserType(this).equals("7"))
            // && mRoleId == 7
            {
                displayView(-4);
            } else {
                displayView(-1);
            }

        }

    }

    private void goToProductList(int position) {
        // display view for selected item
        mDrawerLayout.closeDrawers();
        mNavDrawerItemIndex = position;

        AppPrefenceManager.saveListingOption(DashboardFActivity.this, "0");
        if (position == -1) {
            displayView(position);
        } else if (position == 1) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 2) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 3) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 4) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 5) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == -2) {
            AppPrefenceManager.saveListingOption(DashboardFActivity.this, "3");
            displayView(position);
        } else if (position == -4) {
            displayView(position);
        } else if (position == -3) {
            displayView(position);
        } else if (position == -5) {
            displayView(position);
        } else if (position == -7) {
        } else {// 09 /01/2015
            displayView(position);
            AppPrefenceManager.saveListingOption(DashboardFActivity.this, "5");
        }
    }

    @SuppressLint("NewApi")
    public void setActionBar() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();



    }

    private boolean exitFlag = true;

    private void setHomeToFront() {

        beginFragmentTransaction();
        detachAllFragment();
        exitFlag = true;
        if (mHomeFragment == null) {
            mFragmentTransaction.add(R.id.frame_container, new HomeFragment(),
                    "Home");
        } else {

            mFragmentTransaction.attach(mHomeFragment);
        }
        mFragmentTransaction.commit();
        setTitle(navMenuTitles[0]);
        mDrawerLayout.closeDrawer(lvExp);
    }

    private void showExirDialog(String str) {
        AppExitDialog appExitDialog = new AppExitDialog(
                DashboardFActivity.this, str);
        appExitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        appExitDialog.setCancelable(true);
        appExitDialog.show();

    }

    @Override
    public void onBackPressed() {

        if (AppController.isDealerList) {
            displayView(-1);
        } else {
            if (exitFlag) {
                showExirDialog("");
            } else {
                setHomeToFront();
            }
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (!isConnected) {
            showSnack(isConnected);
        }
    }

    private class SlideMenuClickListener implements
            ExpandableListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected item
            mNavDrawerItemIndex = position;
            AppPrefenceManager.saveListingOption(DashboardFActivity.this, "0");
            if (position == 0) {
                displayView(position);
            } else if (position == 1) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 2) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 3) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 4) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 5) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == navMenuTitles.length + 1) {
                AppPrefenceManager.saveListingOption(DashboardFActivity.this,
                        "3");
                categorySelectionPosition = position - 2;
                categoryId = categoryIdList[position - 2];
                displayView(position);
            } else if (position == navMenuTitles.length + 2) {
                categorySelectionPosition = position - 3;
                categoryId = categoryIdList[position - 3];
                displayView(position);
            }
            if (position == navMenuTitles.length + 3) {
                categorySelectionPosition = position - 4;
                categoryId = categoryIdList[position - 4];
                displayView(position);
            }
        }
    }

    HomeFragment mHomeFragment;
    // StoreFragment mStoreFActivity;
    ProductListNew_Fragment mProductFragment;
    // ProductListFragment mProductFragment;
    SearchListFragment mSearchFragment;
    LocationFragment mLocationFragment;
    VKCDealersListView mVkcDealersListView;
    ComplaintFragment mComplaintFragment;
    FeedbackFragment mFeedbackFragment;
    SalesOrderFragment mSalesOrderFragment;
    SubDealerOrderList mSubdealerOrderFragment;
    SubdealerOrderStatusFragment mPendingOrderFragment;
    DealersRetailersAndDateFilter dealersRetailersAndDateFilter;
    ContactUsFragment mContactUsFragment;
    CreditPaymentFragment creditPaymentStatusFragment;
    RecentOrdersFragment mRecentOrderFragment;
    SalesOrderStatusListFragment salesOrderStatusListFragment;
    SalesHeadOrderList msalesHeadOrderFragment;
    NotificationListFragment mNotificationsFragment;
    Quick_Order_Fragment mQuickOrderFragment;
    SchemePointsFragment mSchemePoints;
    TransactionHistoryFragment mTransactionHistory;
    MyCustomersFragment mCustomersFragment;
    RedeemedListFragment mRedeemListFragment;
    RedeemReportFragment mRedeemReportFragment;
    GiftRewardReportFragment mGiftRewardFragment;

    private void beginFragmentTransaction() {

        mFragmentManager = getSupportFragmentManager();

        mHomeFragment = (HomeFragment) mFragmentManager
                .findFragmentByTag("Home");
        mProductFragment = (ProductListNew_Fragment) mFragmentManager
                .findFragmentByTag("Category");
        mCustomersFragment = (MyCustomersFragment) mFragmentManager
                .findFragmentByTag("Customers");
        mSearchFragment = (SearchListFragment) mFragmentManager
                .findFragmentByTag("Search");
        mLocationFragment = (LocationFragment) mFragmentManager
                .findFragmentByTag("Locate Us");
        mVkcDealersListView = (VKCDealersListView) mFragmentManager
                .findFragmentByTag("VKC dealers");
        mContactUsFragment = (ContactUsFragment) mFragmentManager
                .findFragmentByTag("Contact Us");
        mComplaintFragment = (ComplaintFragment) mFragmentManager
                .findFragmentByTag("Complaint");
        mFeedbackFragment = (FeedbackFragment) mFragmentManager
                .findFragmentByTag("FeedBack");
        mSalesOrderFragment = (SalesOrderFragment) mFragmentManager
                .findFragmentByTag("Sales");
        mSubdealerOrderFragment = (SubDealerOrderList) mFragmentManager
                .findFragmentByTag("SubDealerOrderList");
        dealersRetailersAndDateFilter = (DealersRetailersAndDateFilter) mFragmentManager
                .findFragmentByTag("DealersRetailersAndDateFilter");
        creditPaymentStatusFragment = (CreditPaymentFragment) mFragmentManager
                .findFragmentByTag("CreditPaymentStatusFragment");
        salesOrderStatusListFragment = (SalesOrderStatusListFragment) mFragmentManager
                .findFragmentByTag("SalesOrder");
        mPendingOrderFragment = (SubdealerOrderStatusFragment) mFragmentManager
                .findFragmentByTag("PendingOrder");
        mRecentOrderFragment = (RecentOrdersFragment) mFragmentManager
                .findFragmentByTag("RecentOrder");
        msalesHeadOrderFragment = (SalesHeadOrderList) mFragmentManager
                .findFragmentByTag("SalesHead");
        mNotificationsFragment = (NotificationListFragment) mFragmentManager
                .findFragmentByTag("Notifications");
        mQuickOrderFragment = (Quick_Order_Fragment) mFragmentManager
                .findFragmentByTag("QuickOrder");
        mSchemePoints = (SchemePointsFragment) mFragmentManager
                .findFragmentByTag("SchemePoints");
        mTransactionHistory = (TransactionHistoryFragment) mFragmentManager
                .findFragmentByTag("History");
        mRedeemListFragment = (RedeemedListFragment) mFragmentManager
                .findFragmentByTag("RedeemList");
        mRedeemReportFragment = (RedeemReportFragment) mFragmentManager
                .findFragmentByTag("RedeemReport");
        mGiftRewardFragment = (GiftRewardReportFragment) mFragmentManager
                .findFragmentByTag("GiftReward");
        mFragmentTransaction = mFragmentManager.beginTransaction();

    }

    private void detachAllFragment() {
        if (mHomeFragment != null) {
            mFragmentTransaction.detach(mHomeFragment);
        }
        if (mProductFragment != null) {
            mFragmentTransaction.detach(mProductFragment);
            AppPrefenceManager.saveProductListSortOption(
                    DashboardFActivity.this, "0");
        }
        if (mSearchFragment != null) {
            mFragmentTransaction.detach(mSearchFragment);
        }
        if (mLocationFragment != null) {
            mFragmentTransaction.detach(mLocationFragment);
        }
        if (mVkcDealersListView != null) {
            mFragmentTransaction.detach(mVkcDealersListView);
        }
        if (mComplaintFragment != null) {
            mFragmentTransaction.detach(mComplaintFragment);
        }
        if (mFeedbackFragment != null) {
            mFragmentTransaction.detach(mFeedbackFragment);
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction.detach(mSalesOrderFragment);
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction.detach(mSalesOrderFragment);
        }
        if (dealersRetailersAndDateFilter != null) {
            mFragmentTransaction.detach(dealersRetailersAndDateFilter);
        }
        if (creditPaymentStatusFragment != null) {
            mFragmentTransaction.detach(creditPaymentStatusFragment);
        }
        if (salesOrderStatusListFragment != null) {
            mFragmentTransaction.detach(salesOrderStatusListFragment);
        }
        if (mSubdealerOrderFragment != null) {
            mFragmentTransaction.detach(mSubdealerOrderFragment);
        }
        if (mPendingOrderFragment != null) {
            mFragmentTransaction.detach(mPendingOrderFragment);
        }
        if (mContactUsFragment != null) {
            mFragmentTransaction.detach(mContactUsFragment);
        }
        if (mRecentOrderFragment != null) {
            mFragmentTransaction.detach(mRecentOrderFragment);
        }
        if (msalesHeadOrderFragment != null) {
            mFragmentTransaction.detach(msalesHeadOrderFragment);
        }
        if (mNotificationsFragment != null) {
            mFragmentTransaction.detach(mNotificationsFragment);
        }
        if (mQuickOrderFragment != null) {
            mFragmentTransaction.detach(mQuickOrderFragment);
        }
        if (mSchemePoints != null) {
            mFragmentTransaction.detach(mSchemePoints);
        }

        if (mTransactionHistory != null) {
            mFragmentTransaction.detach(mTransactionHistory);
        }
        if (mCustomersFragment != null) {
            mFragmentTransaction.detach(mCustomersFragment);
        }
        if (mRedeemListFragment != null) {
            mFragmentTransaction.detach(mRedeemListFragment);
        }
        if (mRedeemReportFragment != null) {
            mFragmentTransaction.detach(mRedeemReportFragment);
        }
        if (mGiftRewardFragment != null) {
            mFragmentTransaction.detach(mGiftRewardFragment);
        }
    }

    public interface DisplayVIewListener {
        public void setDisplayViewListener(int i);
    }

    public void setDisplayView() {
        displayView(2);
    }

    public void goToSearchWithKey(String key) {

        this.key = key;
        displayView(-2);

    }

    public String[] getcategoryIdList() {
        Log.v("LOG", "22122014 getcategoryIdList in display view");
        return categoryIdList;
    }

    public void displayView(int position) {
        // update the main content with called Fragment
        beginFragmentTransaction();
        detachAllFragment();
        Fragment fragment = null;
        if (position == 0) {
            exitFlag = true;
        } else {
            exitFlag = false;
        }

        if (position == -1) {

            if (mHomeFragment == null) {
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", "VALUE");
                homeFragment.setArguments(bundle);
                mFragmentTransaction.add(R.id.frame_container, homeFragment,
                        "Home");
                exitFlag = true;
            } else {

                mFragmentTransaction.attach(mHomeFragment);
            }

        } else if (position == -2) {
            if (mSearchFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new SearchListFragment(), "Search");
            } else {

                mFragmentTransaction.attach(mSearchFragment);
            }

        } else if (position == -3) {
            if (mLocationFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new LocationFragment(), "Locate Us");
            } else {

                mFragmentTransaction.attach(mLocationFragment);
            }
        } else if (position == -5) {

            if (mContactUsFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new ContactUsFragment(), "Contact Us");
            } else {

                mFragmentTransaction.attach(mContactUsFragment);
            }
        } else if (position == -4) {

            if (mVkcDealersListView == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new VKCDealersListView(), "VKC dealers");
            } else {

                mFragmentTransaction.attach(mVkcDealersListView);
            }
        } else if (position == R.id.feedback) {
            if (mFeedbackFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new FeedbackFragment(), "FeedBack");
            } else {

                mFragmentTransaction.attach(mFeedbackFragment);
            }

        } else if (position == R.id.notifications) {
            if (mNotificationsFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new NotificationListFragment(), "Notifications");
            } else {

                mFragmentTransaction.attach(mNotificationsFragment);
            }

        } else if (position == R.id.complaint) {
            if (mComplaintFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new ComplaintFragment(), "Complaint");
            } else {

                mFragmentTransaction.attach(mComplaintFragment);
            }
        } else if (position == R.id.quick_order) {
            if (mQuickOrderFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new Quick_Order_Fragment(), "QuickOrder");
            } else {

                mFragmentTransaction.attach(mQuickOrderFragment);
            }
        } else if (position == R.id.schemePoints) {
            if (mSchemePoints == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new SchemePointsFragment(), "SchemePoints");
            } else {
                mFragmentTransaction.attach(mSchemePoints);
            }
        } else if (position == R.id.logout) {

            AppPrefenceManager.saveUserType(dashboardFActivity, "");
            AppPrefenceManager.saveUserId(dashboardFActivity, "");
            AppPrefenceManager.saveIsCredit(dashboardFActivity, "no");
            AppPrefenceManager.saveLoginStatusFlag(dashboardFActivity, "false");
            AppPrefenceManager.saveSelectedUserName(dashboardFActivity, "");
            AppPrefenceManager.saveSelectedUserId(dashboardFActivity, "");
            AppPrefenceManager.saveCustomerCategory(dashboardFActivity, "");
            AppPrefenceManager.setIsCallPendingAPI(dashboardFActivity, true);
            AppPrefenceManager.saveIsGroupMember(dashboardFActivity, "0");
            AppPrefenceManager.saveCustomerId(dashboardFActivity, "");
            AppController.cartArrayListSelected.clear();
            AppController.cartArrayList.clear();
            clearDb();
            Intent intent = new Intent(DashboardFActivity.this,
                    VKCSplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (position == R.id.switchAccount) {

            startActivity(new Intent(DashboardFActivity.this,
                    GroupMemberActivity.class));
            finish();

        } else if (position == R.id.create_new) {
            if (mSalesOrderFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderFragment(), "Sales"); // For Order submit
            } else {
                mFragmentTransaction.attach(mSalesOrderFragment);
            }
        } else if (position == R.id.salesHeadOrders) {
            if (msalesHeadOrderFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new SalesHeadOrderList(), "SalesHead"); // For Order

            } else {

                mFragmentTransaction.attach(msalesHeadOrderFragment);
            }
        } else if (position == R.id.credit) {

            if (creditPaymentStatusFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new CreditPaymentFragment(),
                        "CreditPaymentStatusFragment");
            } else {

                mFragmentTransaction.attach(creditPaymentStatusFragment);
            }

        } else if (position == R.id.redeemList) {
            if (mRedeemListFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new RedeemedListFragment(), "RedeemList"); // For Order
                // submit
            } else {

                mFragmentTransaction.attach(mRedeemListFragment);
            }
        } else if (position == R.id.salesorder) {

            if (salesOrderStatusListFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderStatusListFragment(), "SalesOrder");
            } else {

                mFragmentTransaction.attach(salesOrderStatusListFragment);
            }

        } else if (position == R.id.subdealerorder) {

            if (mSubdealerOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SubDealerOrderList(), "SubDealerOrderList");
            } else {


                mFragmentTransaction.attach(mSubdealerOrderFragment);
            }

        } else if (position == R.id.pendingorder) {

            if (mSubdealerOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SubdealerOrderStatusFragment(), "PendingOrder");
            } else {


                mFragmentTransaction.attach(mPendingOrderFragment);
            }

        } else if (position == R.id.salesmngmt) {

            if (salesOrderStatusListFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderStatusListFragment(), "SalesOrder");
            } else {

                mFragmentTransaction.attach(salesOrderStatusListFragment);
            }

        } else if (position == R.id.transactionHistory) {

            if (mTransactionHistory == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new TransactionHistoryFragment(), "History");
            } else {

                mFragmentTransaction.attach(mTransactionHistory);
            }

        } else if (position == R.id.redeemReport) {

            if (mRedeemReportFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new RedeemReportFragment(), "RedeemReport");
            } else {

                mFragmentTransaction.attach(mRedeemReportFragment);
            }

        } else if (position == R.id.giftRewardReport) {

            if (mGiftRewardFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new GiftRewardReportFragment(), "GiftReward");
            } else {

                mFragmentTransaction.attach(mGiftRewardFragment);
            }

        } else if (position == R.id.recentOrder) {

            if (mRecentOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new RecentOrdersFragment(), "RecentOrder");
            } else {

                mFragmentTransaction.attach(mRecentOrderFragment);
            }

        } else if (position == R.id.myCustomers) {

            if (mCustomersFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new MyCustomersFragment(), "Customers");
            } else {

                mFragmentTransaction.attach(mCustomersFragment);
            }

        } else if (position == 22) {
            if (mSalesOrderFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderFragment(), "Sales"); // For Order submit
            } else {

                mFragmentTransaction.attach(mSalesOrderFragment);
            }
        } else if (position == 11) {
            if (mProductFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new ProductListNew_Fragment(), "Category");

            } else {

                mFragmentTransaction.attach(mProductFragment);
            }
        } else {

            if (mProductFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new ProductListNew_Fragment(), "Category");

            } else {

                mFragmentTransaction.attach(mProductFragment);
            }

        }

        mFragmentTransaction.commit();

        if (position < navMenuTitles.length && position > 0) {
            setTitle(navMenuTitles[position]);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem userName = menu.findItem(R.id.userName);

        SpannableString s = new SpannableString(
                AppPrefenceManager.getUserName(this));
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#cb181f")), 0,
                s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, AppPrefenceManager
                .getUserName(this).length(), Spannable.SPAN_PARAGRAPH);
        userName.setTitle(s);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar actions click
        switch (item.getItemId()) {

            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerLayout)) {
                    mDrawerLayout.closeDrawer(mDrawerLayout);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.complaint:
                displayView(R.id.complaint);
                return true;
            case R.id.feedback:
                displayView(R.id.feedback);
                return true;
            case R.id.logout:
                mNavDrawerItemIndex = R.id.logout;
                displayView(R.id.logout);
                return true;
            case R.id.create_new:
                mNavDrawerItemIndex = R.id.create_new;
                displayView(R.id.create_new);
                return true;
            case R.id.credit:
                mNavDrawerItemIndex = R.id.credit;
                displayView(R.id.credit);
                return true;

            case R.id.salesorder:
                mNavDrawerItemIndex = R.id.salesorder;
                displayView(R.id.salesorder);
                return true;
            case R.id.salesmngmt:
                mNavDrawerItemIndex = R.id.salesmngmt;
                displayView(R.id.salesmngmt);
                return true;

            case R.id.subdealerorder:
                mNavDrawerItemIndex = R.id.subdealerorder;
                displayView(R.id.subdealerorder);
                return true;
            case R.id.pendingorder:
                mNavDrawerItemIndex = R.id.pendingorder;
                displayView(R.id.pendingorder);
                return true;
            case R.id.recentOrder:
                mNavDrawerItemIndex = R.id.recentOrder;
                displayView(R.id.recentOrder);
                return true;
            case R.id.notifications:
                mNavDrawerItemIndex = R.id.notifications;
                displayView(R.id.notifications);
                return true;
            case R.id.quick_order:
                mNavDrawerItemIndex = R.id.quick_order;
                displayView(R.id.quick_order);
                return true;
            case R.id.salesHeadOrders:
                mNavDrawerItemIndex = R.id.salesHeadOrders;
                displayView(R.id.salesHeadOrders);
                return true;
            case R.id.schemePoints:
                mNavDrawerItemIndex = R.id.schemePoints;
                displayView(R.id.schemePoints);
                return true;

            case R.id.transactionHistory:
                mNavDrawerItemIndex = R.id.transactionHistory;
                displayView(R.id.transactionHistory);
                return true;

            case R.id.myCustomers:
                mNavDrawerItemIndex = R.id.myCustomers;
                displayView(R.id.myCustomers);
                return true;
            case R.id.redeemReport:
                mNavDrawerItemIndex = R.id.redeemReport;
                displayView(R.id.redeemReport);
                return true;
            case R.id.giftRewardReport:
                mNavDrawerItemIndex = R.id.giftRewardReport;
                displayView(R.id.giftRewardReport);
                return true;
            case R.id.redeemList:
                mNavDrawerItemIndex = R.id.redeemList;
                displayView(R.id.redeemList);
                return true;
            case R.id.switchAccount:
                mNavDrawerItemIndex = R.id.switchAccount;
                displayView(R.id.switchAccount);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (AppPrefenceManager.getIsCredit(dashboardFActivity)
                .equalsIgnoreCase("yes")) {
            menu.findItem(R.id.credit).setVisible(true);
        } else {
            menu.findItem(R.id.credit).setVisible(false);
        }

        if (AppPrefenceManager.getUserType(dashboardFActivity).equals("4")) {
            menu.findItem(R.id.salesmngmt).setVisible(false);
            menu.findItem(R.id.salesorder).setVisible(false);
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.recentOrder).setVisible(false);
            // Bibin
            menu.findItem(R.id.schemePoints).setVisible(false);
            menu.findItem(R.id.transactionHistory).setVisible(false);
            menu.findItem(R.id.myCustomers).setVisible(false);
            menu.findItem(R.id.redeemList).setVisible(false);
            menu.findItem(R.id.redeemReport).setVisible(false);
            menu.findItem(R.id.giftRewardReport).setVisible(false);

            menu.findItem(R.id.salesHeadOrders).setVisible(true);
        } else if (AppPrefenceManager.getUserType(dashboardFActivity).equals(
                "6")) {
            // menu.findItem(R.id.salesorder).setTitle("SalesManagement");
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.subdealerorder).setVisible(true);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
        } else if (AppPrefenceManager.getUserType(dashboardFActivity).equals(
                "7")) {
            menu.findItem(R.id.recentOrder).setVisible(true);
            menu.findItem(R.id.salesorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(true);
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
            menu.findItem(R.id.schemePoints).setVisible(false);
            menu.findItem(R.id.transactionHistory).setVisible(false);
            menu.findItem(R.id.myCustomers).setVisible(false);
            menu.findItem(R.id.redeemList).setVisible(false);
            menu.findItem(R.id.redeemReport).setVisible(false);
            menu.findItem(R.id.giftRewardReport).setVisible(false);
        } else {
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
        }
        if (AppPrefenceManager.getIsGroupMember(dashboardFActivity).equals("1")) {
            menu.findItem(R.id.switchAccount).setVisible(true);
        } else {
            menu.findItem(R.id.switchAccount).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }


    // History
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadHistory(String query, boolean flagNoItem) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor

            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "VKC Sandals"};

            MatrixCursor cursor = new MatrixCursor(columns);
            if (!flagNoItem) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))) {
                        temp[0] = i;
                        temp[1] = items.get(i);
                        // replaced s with i as s not used anywhere.

                        cursor.addRow(temp);
                    }

                }
            }

            // SearchView

            searchView.setSuggestionsAdapter(new SearchAdapter(this, cursor,
                    items));
            searchView.setVerticalScrollBarEnabled(false);
        }
    }

    public void initProductDetails() {
        items = new ArrayList<String>();
        items.add("VKC sandals");
        items.add("VKC 2306 Sandals");
        items.add("VKC Pride 3108 Sandals");
        items.add("VKC 3311 Slippers");
        items.add("VKC 3306 Slippers");
        items.add("VKC 3385 Slippers");
        items.add("VKC 3390 Slippers");
    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(
                dashboardFActivity);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        // AppController.isSelectedDealer=false;
        super.onRestart();

    }

    private String getVersion() {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(getPackageName(),
                    0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return packageinfo.versionName.toString();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}
