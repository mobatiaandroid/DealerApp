package com.mobatia.vkcsalesapp.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mobatia.vkcsalesapp.activities.DashboardFActivity;
import com.mobatia.vkcsalesapp.activities.VKCSplashActivity;
import com.mobatia.vkcsalesapp.manager.VKCBitmapCache;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.DealersShopModel;
import com.mobatia.vkcsalesapp.model.QuickSizeModel;
import com.mobatia.vkcsalesapp.model.RedeemReportModel;
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel;
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel;
import com.mobatia.vkcsalesapp.util.ConnectivityReceiver;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import androidx.multidex.MultiDex;


public class AppController extends Application implements Serializable,
        java.lang.Thread.UncaughtExceptionHandler {

    private static final long serialVersionUID = 1L;

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public static String articleNumber;
    private static AppController mInstance;
    public static List<String> listDealers;
    private static Context mContext;
    public static ArrayList<DealersShopModel> dealersModels = new ArrayList<DealersShopModel>();
    public static ArrayList<SubDealerOrderDetailModel> subDealerOrderDetailList = new ArrayList<SubDealerOrderDetailModel>();
    public static ArrayList<SubDealerOrderDetailModel> TempSubDealerOrderDetailList = new ArrayList<SubDealerOrderDetailModel>();
    public static List<SubDealerOrderListModel> subDealerModels = new ArrayList<SubDealerOrderListModel>();
    public static ArrayList<SubDealerOrderDetailModel> subDealerorderList = new ArrayList<SubDealerOrderDetailModel>();
    public static ArrayList<CartModel> cartArrayList = new ArrayList<CartModel>();
    public static List<String> listErrors = new ArrayList<String>();
    public static int status = 0;
    public static boolean isCart = false;
    public static boolean isSubmitError = false;
    public static boolean isEditable = true;
    public static boolean isDealerList = false;
    public static int selectedProductPosition = 0;
    public static boolean isCalledApiOnce = false;
    public static ArrayList<CartModel> cartArrayListSelected = new ArrayList<CartModel>();
    /**
     * The mClass
     */
    public static ArrayList<QuickSizeModel> arrayListSize = new ArrayList<QuickSizeModel>();
    public static String product_id = "";
    public static String category_id = "";
    public static String p_id = "";
    public static String size = "";
    public static String color = "";
    public static int delPosition = 0;
    private final Class<?> mClass;
    /**
     * The mIntent
     */
    public static boolean isClickedCartButton = true;
    public static ListView mNotificationList;
    private Intent mIntent;
    public static boolean isClickedCartItem = false;
    public static int listScrollTo = 0;
    public static boolean isClickedCartAdapter;
    public static ArrayList<EditText> custoSizeArray = new ArrayList<EditText>();
    public static DashboardFActivity dashboard_activity = new DashboardFActivity();
    public static boolean isSelectedDealer;
    public static ArrayList<String> tempmainCatArrayList = new ArrayList<String>();
    public static ArrayList<String> tempsubCatArrayList = new ArrayList<String>();
    public static ArrayList<String> tempsizeCatArrayList = new ArrayList<String>();
    public static ArrayList<String> tempbrandCatArrayList = new ArrayList<String>();
    public static ArrayList<String> temppriceCatArrayList = new ArrayList<String>();
    public static ArrayList<String> tempcolorCatArrayList = new ArrayList<String>();
    public static ArrayList<String> tempofferCatArrayList = new ArrayList<String>();
    public static String[] navMenuTitles;
    public static String[] categoryIdList;
    public static ArrayList<RedeemReportModel> listRedeemReport = new ArrayList<>();
    public static String userType;
    public static String mDealerCount;
    public static String mRoleId;

    @Override
    public void onCreate() {
        super.onCreate();

        listDealers = new ArrayList<String>();
        mInstance = this;
        //SugarContext.init(this);
       /* RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);*/
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }

    public AppController() {
        // TODO Auto-generated constructor stub
        this.mContext = null;
        this.mClass = null;
    }

    public AppController(Context mContext) {

        this.mContext = mContext;
        this.mClass = null;
    }

    public AppController(Context mContext, Class<?> mClass) {

        this.mContext = mContext;
        this.mClass = mClass;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static Context getContext() {

        return mContext;

    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new VKCBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
     * .Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        // TODO Auto-generated method stub
        StringWriter mStringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(mStringWriter));
        System.out.println(mStringWriter);

        mIntent = new Intent(mContext, VKCSplashActivity.class);
        String s = mStringWriter.toString();
        Log.e("LOG", s);
        // you can use this String to know what caused the exception and in
        // which Activity
        mIntent.putExtra("uncaught Exception",
                "Exception is: " + mStringWriter.toString());
        mIntent.putExtra("uncaught stacktrace", s);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(mIntent);
        System.out.println("error " + s);
        // for restarting the Activity
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
