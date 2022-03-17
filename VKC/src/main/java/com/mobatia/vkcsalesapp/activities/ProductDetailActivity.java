/**
 *
 */
package com.mobatia.vkcsalesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.adapter.ColorGridAdapter;
import com.mobatia.vkcsalesapp.adapter.ListImageAdapter;
import com.mobatia.vkcsalesapp.adapter.SizeGridAdapter;
import com.mobatia.vkcsalesapp.adapter.ViewpagerAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.customview.CustomProgressBar;
import com.mobatia.vkcsalesapp.customview.HorizontalListView;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DataBaseManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog;
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.BrandTypeModel;
import com.mobatia.vkcsalesapp.model.CartModel;
import com.mobatia.vkcsalesapp.model.CaseModel;
import com.mobatia.vkcsalesapp.model.ColorModel;
import com.mobatia.vkcsalesapp.model.PendingQuantityModel;
import com.mobatia.vkcsalesapp.model.ProductImages;
import com.mobatia.vkcsalesapp.model.ProductModel;
import com.mobatia.vkcsalesapp.model.SizeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductDetailActivity extends AppCompatActivity implements
        VKCUrlConstants, OnClickListener, VKCJsonTagConstants, VKCDbConstants,
        OnItemClickListener {


    private HorizontalListView mHorizontalListView;
    private HorizontalListView listViewColor, listViewSize;
    private ListImageAdapter mListAdapter;
    private ViewpagerAdapter mViewPagerAdapter;
    private ColorGridAdapter colorGridAdapter;
    private SizeGridAdapter sizeGridAdapter;
    private RelativeLayout mRelImage;
    private RelativeLayout mRelativText;
    RelativeLayout relativSecondSec;
    private LinearLayout mRelBottom, mFirstView;
    private ImageView mImgArrowRight;
    private ImageView mImgArrowLeft;
    private Activity mActivity;
    int width;
    int creditPrice;
    int height;
    float x1, x2;
    float y1, y2;
    boolean isClickedAnimation;
    TextView txtlikeCount;
    TextView txtNameText;
    TextView txtViewPrice, txtCartCount, txtDescription, txtCatName;
    RelativeLayout relShare, relCart;
    public static String selectedFromSizeList;
    public static String selectedFromColorList;
    public static String selectedIDFromSizeList;
    public static String selectedIDFromColorList;
    boolean isClicked;
    int likeCount = 0;
    private ViewPager mImagePager;
    ArrayList<ProductModel> productModels;
    float priceTotal;
    DisplayManagerScale displayManagerScale;
    ProductModel productModel = new ProductModel();
    Button buttonLike;
    private DataBaseManager databaseManager;
    private EditText edtQuantity;
    private ArrayList<ProductImages> imageUrls;
    private ArrayList<ColorModel> colorArrayList;
    private ArrayList<SizeModel> sizeArrayList;
    private ArrayList<CaseModel> caseArrayList;
    private ArrayList<ProductImages> newArrivalArrayList;
    private ArrayList<PendingQuantityModel> mPendingList;
    private TextView edtPendQuantity;
    TableLayout tableLayout;
    ArrayList<EditText> editList;
    ArrayList<String> listTemp;
    int tableCount;
    int tempSize;
    private float cartPrice;
    int cartValue;
    boolean isShowMessage;
    boolean isInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_new);
        final ActionBar abar = getSupportActionBar();
        editList = new ArrayList<EditText>();
        tableCount = 0;
        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Product Details");
        abar.setCustomView(viewActionBar, params);
        AppController.p_id = "";
        abar.setDisplayShowCustomEnabled(true);
        isClickedAnimation = false;
        isShowMessage = false;
        mActivity = this;
        String productid = AppController.product_id;
        // System.out.println("Pid :" + AppController.p_id);
        AppController.isClickedCartAdapter = false;
        DatabaseHelper database = new DatabaseHelper(mActivity,"VKC");
        database.openDataBase();
        if (productid.equals("")) {
            productModel = (ProductModel) getIntent().getExtras()
                    .getSerializable("MODEL");

            initialiseUI();
            AppController.product_id = productModel.getmProductName();
            AppController.category_id = productModel.getCategoryId();
            getCartDataForTable();
            createTable();
            LikeCountApi();
            getProducts(AppController.product_id);
        } else {

            initialiseUI();
            getCartDataForTable();
            LikeCountApi();
            getProducts(productid);

        }

    }

    private void productDetailStatus() {
        final VKCInternetManager manager = new VKCInternetManager(
                GET_SALES_ORDER_STATUS);

        String name[] = {"user_id", "product_id"};
        String value[] = {AppPrefenceManager.getUserId(mActivity),
                productModel.getId()};
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                try {
                    JSONObject jsonObj = new JSONObject(successResponse);
                    JSONArray jsonArray = jsonObj
                            .getJSONArray("productSalesOrderStatus");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        System.out.println("pending12 " + jObj);
                        jObj.opt("id");
                        jObj.opt("CusId");
                        jObj.opt("productSapId");
                        jObj.opt("CusName");
                        jObj.opt("MaterialNo");
                        jObj.opt("OrderDate");
                        jObj.opt("OrderQty");
                        jObj.opt("PendingQty");
                        edtPendQuantity.setText(jObj.opt("PendingQty")
                                .toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub

            }
        });

    }


    private void increasePopularCount() {
        String name[] = {"product_id"};
        String values[] = {productModel.getId()};
        final VKCInternetManager manager = new VKCInternetManager(
                POPULARITY_COUNT_URL);
        manager.getResponsePOST(ProductDetailActivity.this, name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub

                        productDetailStatus();
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    /*
     * Method Name:setLayoutDimension() Parameter:nill Description:Set layout
     * according to device height and width
     */

    private void setLayoutDimension() {
        displayManagerScale = new DisplayManagerScale(mActivity);
        width = displayManagerScale.getDeviceWidth();
        height = displayManagerScale.getDeviceHeight();
        mRelImage.getLayoutParams().height = (int) (height * 0.40);
        relativSecondSec.getLayoutParams().height = (int) (height * .40);
        mRelativText.getLayoutParams().height = (int) (height * .12);
        mRelBottom.getLayoutParams().height = (int) (height * .08);

    }

    /*
     * Method Name:initialiseUI() Parameter:nill Description:Initialise UI
     * elements
     */

    private void initialiseUI() {

        databaseManager = new DataBaseManager(mActivity);
        relativSecondSec = (RelativeLayout) findViewById(R.id.relativSecondSec);
        mHorizontalListView = (HorizontalListView) findViewById(R.id.listView);
        listViewColor = (HorizontalListView) findViewById(R.id.listViewColor);
        listViewSize = (HorizontalListView) findViewById(R.id.listViewSize);
        txtlikeCount = (TextView) findViewById(R.id.txtLikeCount);
        tableLayout = (TableLayout) findViewById(R.id.matrixLayout);
        mRelImage = (RelativeLayout) findViewById(R.id.relImage);
        mImgArrowRight = (ImageView) findViewById(R.id.imgArrowRight);
        mImgArrowLeft = (ImageView) findViewById(R.id.imgArrowLeft);
        edtQuantity = (EditText) findViewById(R.id.edtViewQty);
        edtPendQuantity = (TextView) findViewById(R.id.edtViewQtyOneData);
        buttonLike = (Button) findViewById(R.id.btnLike);
        mRelativText = (RelativeLayout) findViewById(R.id.relativText);
        mRelBottom = (LinearLayout) findViewById(R.id.relBottomLayout);
        mImagePager = (ViewPager) findViewById(R.id.imagePager);
        txtNameText = (TextView) findViewById(R.id.txtNameText);
        txtViewPrice = (TextView) findViewById(R.id.txtViewPrice);
        txtDescription = (TextView) findViewById(R.id.txtCatText);
        txtCatName = (TextView) findViewById(R.id.txtCatName);

        // txtNameText = (TextView) findViewById(R.id.txtNameText);
        mFirstView = (LinearLayout) findViewById(R.id.firstView);
        // txtNameText.setText("VKC " /* + productModel.getId() */+ " "
        // + productModel.getmProductName());
        productModels = new ArrayList<ProductModel>();
        relShare = (RelativeLayout) findViewById(R.id.relShare);
        relCart = (RelativeLayout) findViewById(R.id.relCart);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        txtCartCount = (TextView) findViewById(R.id.txtCartSize);
        setListeners();

        setCartQuantity();
        setLayoutDimension();
        setActionBar();
        colorArrayList = new ArrayList<ColorModel>();
        sizeArrayList = new ArrayList<SizeModel>();
        caseArrayList = new ArrayList<CaseModel>();
        newArrivalArrayList = new ArrayList<ProductImages>();
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        relativeParams.addRule(RelativeLayout.ABOVE, R.id.relBottomLayout);
        buttonLike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.btnLike) {
                    isClicked = !isClicked; // toggle the boolean flag
                    v.setBackgroundResource(isClicked ? R.drawable.likepress
                            : R.drawable.like);
                    LikeApi(v);

                }
            }
        });
        mRelativText.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                // TODO Auto-generated method stub

                switch (touchevent.getAction()) {
                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN: {
                        x1 = touchevent.getX();
                        y1 = touchevent.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = touchevent.getX();
                        y2 = touchevent.getY();

                        // if left to right sweep event on screen
                        if (x1 < x2) {
                            // Toast.makeText(this, "Left to Right Swap Performed",
                            // Toast.LENGTH_LONG).show();
                        }

                        // if right to left sweep event on screen
                        if (x1 > x2) {
                            // Toast.makeText(this, "Right to Left Swap Performed",
                            // Toast.LENGTH_LONG).show();
                        }

                        // if UP to Down sweep event on screen
                        if (y1 < y2) {
                            final Animation slide_down = AnimationUtils
                                    .loadAnimation(getApplicationContext(),
                                            R.anim.slide_down);

                            mFirstView.startAnimation(slide_down);
                            relativSecondSec.startAnimation(slide_down);
                            slide_down
                                    .setAnimationListener(new AnimationListener() {

                                        @Override
                                        public void onAnimationStart(
                                                Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationRepeat(
                                                Animation animation) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onAnimationEnd(
                                                Animation animation) {
                                            // TODO Auto-generated method stub
                                            isClickedAnimation = false;
                                            mFirstView.setVisibility(View.VISIBLE);
                                            relativSecondSec
                                                    .setVisibility(View.VISIBLE);
                                        }
                                    });

                            // Toast.makeText(this, "UP to Down Swap Performed",
                            // Toast.LENGTH_LONG).show();
                        }

                        // if Down to UP sweep event on screen
                        if (y1 > y2) {
                            // Toast.makeText(this, "Down to UP Swap Performed",
                            // Toast.LENGTH_LONG).show();
                            final Animation slide_up = AnimationUtils
                                    .loadAnimation(getApplicationContext(),
                                            R.anim.slide_up);
                            /*
                             * if(!isClickedAnimation) {
                             */
                            mFirstView.startAnimation(slide_up);
                            relativSecondSec.startAnimation(slide_up);
                            // }

                            mFirstView.setVisibility(View.GONE);
                            relativSecondSec.setVisibility(View.GONE);
                            // isClickedAnimation=true;
                            slide_up.setAnimationListener(new AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    // TODO Auto-generated method stub

                                }
                            });

                        }
                        break;
                    }
                }
                return false;
            }

        });
    }

    private void setListeners() {
        relShare.setOnClickListener(this);
        relCart.setOnClickListener(this);
        mImgArrowRight.setOnClickListener(this);
        mImgArrowLeft.setOnClickListener(this);
        listViewSize.setOnItemClickListener(this);
        txtCartCount.setOnClickListener(this);
        mRelativText.setOnClickListener(this);
    }

    private void setHorizontalListAction(HorizontalListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // mImagePager.setCurrentItem(position);
                AppController.product_id = newArrivalArrayList.get(position)
                        .getProductName();
                AppController.category_id = newArrivalArrayList.get(position)
                        .getCatId();
                System.out.println("Selected Id :"
                        + newArrivalArrayList.get(position).getProductName());
                System.out.println("Selected Id Cat :"
                        + newArrivalArrayList.get(position).getCatId());
                getCartDataForTable();
                getProducts(AppController.product_id);

            }
        });

    }

    private void setColorGridClickListener(HorizontalListView listView) {
        listView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                ArrayList<ProductImages> imageUrls = new ArrayList<ProductImages>();
                ArrayList<ProductImages> imageUrlsTemp = new ArrayList<ProductImages>();
                ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
                imageUrls = productModel.getProductImages();
                colorArrayList = productModel.getProductColor();

                for (int i = 0; i < imageUrls.size(); i++) {

                    if (imageUrls
                            .get(i)
                            .getColorModel()
                            .getColorcode()
                            .equals(colorArrayList.get(position).getColorcode())) {
                        imageUrlsTemp.add(imageUrls.get(i));
                    }

                }

                mListAdapter = new ListImageAdapter(mActivity, imageUrlsTemp);
                mHorizontalListView.setAdapter(mListAdapter);

                mViewPagerAdapter = new ViewpagerAdapter(mActivity,
                        imageUrlsTemp);
                mImagePager.setAdapter(mViewPagerAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        // listViewColor.set
        listViewColor.setSelection(0);

    }

    private void shareIntent(String link) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(emailIntent, getApplicationContext()
                .getResources().getString(R.string.app_name)));
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == relShare) {

            shareIntent("http://vkcgroup.com/");

        } else if (v == relCart) {
            isInserted = false;
            if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
                if (AppPrefenceManager.getCustomerCategory(mActivity)
                        .equals("")
                        && AppPrefenceManager.getSelectedUserId(mActivity)
                        .equals("")) {
                    VKCUtils.showtoast(mActivity, 41);
                } else {
                    AddToCart addToCart = new AddToCart();
                    addToCart.execute();
                }
            } else {
                System.out.println("Product id---" + AppController.p_id);

                AddToCart addToCart = new AddToCart();
                addToCart.execute();
            }

        } else if (v == mImgArrowRight) {

            mImagePager.setCurrentItem(mImagePager.getCurrentItem() + 1);

        } else if (v == mImgArrowLeft) {

            mImagePager.setCurrentItem(mImagePager.getCurrentItem() - 1);

        } else if (v == txtCartCount) {
            startActivity(new Intent(ProductDetailActivity.this,
                    CartActivity.class));
            finish();

        } else if (v == mRelativText) {

        }

    }

    private class AddToCart extends AsyncTask<Void, Void, Void> {

        final CustomProgressBar pDialog = new CustomProgressBar(mActivity,
                R.drawable.loading);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            priceTotal = 0;
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getCartData();
            getCartValue();
            if (productModel != null) {

                listTemp = new ArrayList<String>();
                if (!AppController.p_id.equals("")) {

                }

                if (editList.size() > 0) {
                    // Sum of price value
                    for (int j = 0; j < editList.size(); j++) {
                        String qty = editList.get(j).getText().toString()
                                .trim();
                        long itemCount = 0;

                        try {
                            // the String to int conversion happens here
                            itemCount = Long.parseLong(qty);

                            // print out the value after the conversion
                        } catch (NumberFormatException nfe) {
                            System.out.println("NumberFormatException: "
                                    + nfe.getMessage());
                        }
                        priceTotal = priceTotal + (itemCount * cartPrice);
                    }
                    if ((cartValue + priceTotal) > creditPrice) {

                        isShowMessage = true;
                    } else {
                        isShowMessage = false;
                        insertToDb();
                    }
                } else {

                    if (AppController.cartArrayList.size() > 0) {

                        int count = 0;
                        for (int i = 0; i < AppController.cartArrayList.size(); i++) {
                            SQLiteAdapter mAdapter = new SQLiteAdapter(
                                    mActivity, DBNAME);
                            mAdapter.openToRead();
                            count = mAdapter.getCountDuplicate(
                                    AppController.cartArrayList.get(i)
                                            .getProdName(),
                                    AppController.cartArrayList.get(i)
                                            .getProdSize(),
                                    AppController.cartArrayList.get(i)
                                            .getProdColor());

                        }
                        System.out.println("Duplicate Count"
                                + String.valueOf(count));
                    }

                }

            }

            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (isShowMessage) {
                // VKCUtils.showtoast(mActivity, 46);
                showExceedDialog();
            }
            setCartQuantity();
            AppController.p_id = "";

        }

    }

    private void LikeApi(final View v) {
        final VKCInternetManager manager = new VKCInternetManager(
                LIKE_PRODUCT_URL);
        String name[] = {"product_id", "user_id"};
        String value[] = {productModel.getId(),
                AppPrefenceManager.getUserId(this)};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {


                try {
                    JSONObject jobj = new JSONObject(successResponse);
                    JSONObject response = jobj.optJSONObject("response");
                    String status = response.optString("status");
                    if (status.equals("Success")) {

                        v.setBackgroundResource(R.drawable.likepress);
                        likeCount = likeCount + 1;
                        txtlikeCount.setText(String.valueOf(likeCount));
                    } else {
                        // v.setBackgroundResource(R.drawable.like);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub


            }
        });

    }

    private void LikeCountApi() {
        final VKCInternetManager manager = new VKCInternetManager(
                LIKE_COUNT_URL);

        String name[] = {"product_id", " user_id"};
        String value[] = {productModel.getId(),
                AppPrefenceManager.getUserId(this)};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                try {
                    JSONObject jobj = new JSONObject(successResponse);
                    JSONObject response = jobj.optJSONObject("response");
                    String status = response.optString("status");
                    int likCount = Integer.valueOf(response.optString("count"));
                    String isLiked = response.optString("isliked");

                    if (status.equals("Success")) {

                        likeCount = likCount;
                        txtlikeCount.setText(String.valueOf(likeCount));
                        if (isLiked.equals("1")) {
                            buttonLike
                                    .setBackgroundResource(R.drawable.likepress);
                        } else {
                            buttonLike.setBackgroundResource(R.drawable.like);
                        }

                        increasePopularCount();

                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                // mIsError = true;

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    public void setCartQuantity() {
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE, "pid"};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");

        // List<CartModel> cartList = new ArrayList<>();
        //cartList = RealmController.with(ProductDetailActivity.this).getBooks();
        int mCount = 0;
        int cartount = 0;
        if (cursor.moveToFirst()) {
            do {
                String count = cursor.getString(cursor
                        .getColumnIndex("productqty"));
                cartount = Integer.parseInt(count);
                mCount = mCount + cartount;
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        cursor.close();
        /*if (cartList.size() > 0) {
            for (int i = 0; i < cartList.size(); i++) {
                String count = cartList.get(i).getProdQuantity();
                cartount = Integer.parseInt(count);
                mCount = mCount + cartount;
            }
        }*/

        //System.out.println("CartList Size :" + cartList.size());
        txtCartCount.setText(String.valueOf(mCount));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // setCartQuantity();
    }

    private int getCartCount() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                // AppController.cartArrayList.add(setCartModel(cursor));
                tableCount = tableCount + cursor.getCount();

                cursor.moveToNext();
            }

        }
        return cursor.getCount();
    }

    private CartModel setCartModel(Cursor cursor) {
        CartModel cartModel = new CartModel();
        cartModel.setProdId(cursor.getString(0));
        cartModel.setProdName(cursor.getString(1));
        cartModel.setProdSizeId(cursor.getString(2));
        cartModel.setProdSize(cursor.getString(3));
        cartModel.setProdColorId(cursor.getString(4));
        cartModel.setProdColor(cursor.getString(5));
        cartModel.setProdQuantity(cursor.getString(6));
        cartModel.setProdGridValue(cursor.getString(7));
        return cartModel;
    }

    private void parseResponse(String response) {

        if (!response.equals("")) {
            try {
                JSONObject jsonObjectresponse = new JSONObject(response);
                JSONObject obj = jsonObjectresponse.optJSONObject("response");
                String status = obj.getString("status");
                JSONArray jsonArrayresponse = obj.getJSONArray("details");

                for (int j = 0; j < jsonArrayresponse.length(); j++) {

                    JSONObject jsonObjectZero = jsonArrayresponse
                            .getJSONObject(j);
                    ProductModel productModel = new ProductModel();
                    productModel.setProductdescription(jsonObjectZero
                            .optString("productdescription"));
                    productModel.setCategoryName(jsonObjectZero
                            .optString(JSON_CATEGORY_NAME));
                    productModel.setCategoryId(jsonObjectZero
                            .optString("categoryid"));
                    String CatId = jsonObjectZero.optString("categoryid");
                    productModel.setmSapId(jsonObjectZero
                            .optString("productSapId"));
                    String SapId = jsonObjectZero.optString("productSapId");
                    productModel.setmProductPrize(jsonObjectZero
                            .optString(JSON_CATEGORY_COST));
                    productModel.setId(jsonObjectZero
                            .optString(JSON_PRODUCT_ID));
                    productModel.setmProductName(jsonObjectZero
                            .optString(JSON_PRODUCT_NAME));
                    productModel.setProductquantity(jsonObjectZero
                            .optString(JSON_PRODUCT_QTY));

                    productModel.setProductDescription(jsonObjectZero
                            .optString(JSON_PRODUCT_DESCRIPTION));

                    productModel.setProductViews(jsonObjectZero
                            .optString(JSON_PRODUCT_VIEWS));

                    productModel.setTimeStamp(jsonObjectZero
                            .optString(JSON_PRODUCT_TIMESTAMP));

                    productModel.setmProductOff(jsonObjectZero
                            .optString(JSON_PRODUCT_OFFER));
                    int orderCount = 0;
                    try {
                        orderCount = Integer.parseInt(jsonObjectZero
                                .optString(JSON_PRODUCT_ORDER));
                    } catch (Exception e) {
                        orderCount = 0;
                    }
                    productModel.setmProductOrder(orderCount);
                    JSONArray productColorArray = jsonObjectZero
                            .getJSONArray(JSON_PRODUCT_COLOR);
                    JSONArray productImageArray = jsonObjectZero
                            .getJSONArray(JSON_PRODUCT_IMAGE);
                    System.out.println("size product image "
                            + productImageArray.length());
                    JSONArray productSizeArray = jsonObjectZero
                            .getJSONArray(JSON_PRODUCT_SIZE);
                    JSONArray productTypeArray = jsonObjectZero
                            .getJSONArray(JSON_PRODUCT_TYPE);
                    JSONArray productCaseArray = jsonObjectZero
                            .getJSONArray(JSON_PRODUCT_CASE);
                    JSONArray productNewArrivalArray = jsonObjectZero
                            .getJSONArray("new_arrivals");
                    ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
                    for (int i = 0; i < productColorArray.length(); i++) {

                        ColorModel colorModel = new ColorModel();
                        JSONObject jsonObject = productColorArray
                                .getJSONObject(i);
                        colorModel.setId(jsonObject
                                .optString(JSON_SETTINGS_COLORID));
                        colorModel.setColorcode(jsonObject
                                .optString(JSON_SETTINGS_COLORCODE));
                        colorModel.setColorName(jsonObject
                                .optString("color_name"));
                        colorModels.add(colorModel);
                    }
                    productModel.setProductColor(colorModels);
                    ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
                    for (int i = 0; i < productImageArray.length(); i++) {

                        ProductImages images = new ProductImages();
                        JSONObject jsonObject = productImageArray
                                .getJSONObject(i);
                        images.setId(jsonObject.optString("image_id"));
                        images.setImageName(BASE_URL
                                + jsonObject.optString(JSON_COLOR_IMAGE));
                        images.setProductName(jsonObject
                                .optString("product_name"));
                        ColorModel colorModel = new ColorModel();
                        colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                        colorModel.setColorcode(jsonObject
                                .optString(JSON_SETTINGS_COLORCODE));
                        images.setColorModel(colorModel);
                        productImages.add(images);

                    }
                    productModel.setProductImages(productImages);
                    // ///

                    ArrayList<ProductImages> newArrivals = new ArrayList<ProductImages>();
                    for (int i = 0; i < productNewArrivalArray.length(); i++) {

                        ProductImages images = new ProductImages();
                        JSONObject jsonObject = productNewArrivalArray
                                .getJSONObject(i);
                        images.setId(jsonObject
                                .optString(JSON_SETTINGS_COLORID));
                        images.setImageName(BASE_URL
                                + jsonObject.optString(JSON_COLOR_IMAGE));
                        images.setProductName(jsonObject
                                .optString("product_name"));
                        ColorModel colorModel = new ColorModel();
                        colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                        colorModel.setColorcode(jsonObject
                                .optString(JSON_SETTINGS_COLORCODE));
                        images.setColorModel(colorModel);
                        images.setCatId(jsonObject.optString("categoryid"));
                        newArrivals.add(images);

                    }
                    productModel.setmNewArrivals(newArrivals);
                    ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
                    for (int i = 0; i < productSizeArray.length(); i++) {

                        SizeModel sizeModel = new SizeModel();
                        JSONObject jsonObject = productSizeArray
                                .getJSONObject(i);
                        sizeModel.setId(jsonObject
                                .optString(JSON_SETTINGS_SIZEID));
                        sizeModel.setName(jsonObject
                                .optString(JSON_SETTINGS_SIZENAME));

                        sizeModels.add(sizeModel);

                    }
                    productModel.setmProductSize(sizeModels);
                    // /////
                    ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
                    for (int i = 0; i < productTypeArray.length(); i++) {

                        BrandTypeModel typeModel = new BrandTypeModel();
                        JSONObject jsonObject = productTypeArray
                                .getJSONObject(i);
                        typeModel.setId(jsonObject
                                .optString(JSON_SETTINGS_BRANDID));
                        typeModel.setName(jsonObject
                                .optString(JSON_SETTINGS_BRANDNAME));
                        typeModel.setImgUrl(jsonObject
                                .optString(JSON_BRAND_IMAGENAME));

                        brandTypeModels.add(typeModel);

                    }
                    productModel.setProductType(brandTypeModels);

                    ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
                    for (int i = 0; i < productCaseArray.length(); i++) {

                        CaseModel caseModel = new CaseModel();
                        JSONObject jsonObject = productCaseArray
                                .getJSONObject(i);
                        caseModel.setId(jsonObject
                                .optString(JSON_SETTINGS_CASEID));
                        caseModel.setName(jsonObject
                                .optString(JSON_SETTINGS_CASENAME));

                        caseModels.add(caseModel);

                    }
                    productModel.setmProductCases(caseModels);
                    productModels.add(productModel);

                }
                productModel = productModels.get(0);
                int imageListSize = productModel.getProductImages().size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            VKCUtils.showtoast(mActivity, 42);
        }

    }

    private void getProducts(String product_id) {
        productModels.clear();
        colorArrayList.clear();
        caseArrayList.clear();
        newArrivalArrayList.clear();
        String name[] = {"productId", "categoryId"};
        String values[] = {product_id, AppController.category_id};
        final VKCInternetManager manager = new VKCInternetManager(
                URL_GET_PRODUCT_DETAIL);

        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) { //
                // TODO Auto-generated method stub
                parseResponse(successResponse);
                if (!successResponse.equals("")) {
                    txtNameText.setText(""
                            + productModel.getProductType().get(0).getName());

                    txtViewPrice.setText("Rs."
                            + productModel.getmProductPrize());
                    txtDescription.setText(productModel.getProductdescription());
                    txtCatName.setText(productModel.getCategoryName());
                    imageUrls = productModel.getProductImages();
                    colorArrayList = productModel.getProductColor();
                    sizeArrayList = productModel.getmProductSize();
                    caseArrayList = productModel.getmProductCases();
                    newArrivalArrayList = productModel.getmNewArrivals();
                    mImagePager = (ViewPager) findViewById(R.id.imagePager);
                    mViewPagerAdapter = new ViewpagerAdapter(mActivity,
                            imageUrls);
                    mImagePager.setAdapter(mViewPagerAdapter);
                    mListAdapter = new ListImageAdapter(mActivity,
                            newArrivalArrayList);
                    mHorizontalListView.setAdapter(mListAdapter);
                    colorGridAdapter = new ColorGridAdapter(mActivity,
                            colorArrayList, 1);
                    sizeGridAdapter = new SizeGridAdapter(mActivity,
                            caseArrayList);
                    listViewColor.setAdapter(colorGridAdapter);
                    listViewSize.setAdapter(sizeGridAdapter);
                    setColorGridClickListener(listViewColor);
                    setHorizontalListAction(mHorizontalListView);
                    getCartPrice();
                    if (AppController.cartArrayListSelected.size() > 0) {
                        createTableWithData();
                    } else {
                        createTable();
                    }
                    AppController.product_id = "";

                } else {
                    VKCUtils.showtoast(mActivity, 42);
                }

            }

            public void createTableWithData() {
                // TODO Auto-generated method stub
                tableLayout.removeAllViews();

                if (AppController.cartArrayListSelected.size() < caseArrayList
                        .size()) {
                    int diff = caseArrayList.size()
                            - AppController.cartArrayListSelected.size();
                    for (int i = 0; i <= diff; i++) {
                        CartModel model = new CartModel();
                        model.setPid("");
                        model.setProdColor("");
                        model.setProdName("");
                        model.setProdQuantity("");
                        model.setProdSize("");
                        AppController.cartArrayListSelected.add(model);
                    }
                }
                tempSize = AppController.cartArrayListSelected.size();
                String caseName = "", colorName = "";
                int column_width = 140;
                TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();

                tableLayout.setBackgroundColor(getResources().getColor(
                        R.color.vkcred));

                // 2) create tableRow params
                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
                tableRowParams.setMargins(1, 1, 1, 1);
                tableRowParams.weight = 1;
                editList.clear();
                for (int i = 0; i < caseArrayList.size() + 1; i++) {
                    if (i == 0) {
                        caseName = "";
                    } else {
                        caseName = caseArrayList.get(i - 1).getName();
                    }

                    TableRow tableRow = new TableRow(ProductDetailActivity.this);
                    tableRow.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));
                    tableRow.setBackgroundColor(getResources().getColor(
                            R.color.vkcred));
                    tableRow.setPadding(0, 0, 0, 2);

                    for (int j = 0; j < colorArrayList.size() + 1; j++) {

                        if (j == 0) {
                            colorName = "";
                        } else {
                            colorName = colorArrayList.get(j - 1)
                                    .getColorName();
                        }
                        TextView textView = new TextView(
                                ProductDetailActivity.this);
                        textView.setBackgroundColor(Color.WHITE);
                        textView.setWidth(column_width);
                        textView.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                        textView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        String s1 = Integer.toString(i);
                        String s2 = Integer.toString(j);
                        String s3 = s1 + s2;
                        int id = Integer.parseInt(s3);
                        if (i == 0 && j == 0) {
                            textView.setText("Test");
                            textView.setBackgroundColor(Color.WHITE);
                            textView.setVisibility(View.INVISIBLE);

                        } else if (i == 0) {
                            textView.setText(colorArrayList.get(j - 1)
                                    .getColorName());
                        } else if (j == 0) {
                            textView.setText(caseArrayList.get(i - 1).getName());
                        } else {
                            textView.setVisibility(View.GONE);
                            EditText edit = new EditText(
                                    ProductDetailActivity.this);

                            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                            edit.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            edit.setHint("Qty.");
                            edit.setTextSize(15);
                            edit.setWidth(column_width);
                            edit.setBackgroundColor(Color.WHITE);
                            edit.setGravity(Gravity.CENTER
                                    | Gravity.CENTER_HORIZONTAL);
                            edit.setTag(i * 10 + j);
                            edit.setSingleLine(true);
                            edit.setId(i * 10 + j);

                            tableRow.addView(edit, tableRowParams);

                            editList.add(edit);

                        }
                        // 5) add textView to tableRow
                        tableRow.addView(textView, tableRowParams);

                    }

                    // 6) add tableRow to tableLayout
                    tableLayout.addView(tableRow, tableLayoutParams);

                }
                setTableData();
            }

            public void setTableData() {
                String caseName = "", listCaseName;
                String colorName = "", listColorName;
                String Quantity = "";
                int selectedRow = 0;
                int selectedColumn = 0;
                int setPosition;

                for (int i = 0; i < AppController.cartArrayListSelected.size(); i++) {
                    colorName = AppController.cartArrayListSelected.get(i)
                            .getProdColor();
                    caseName = AppController.cartArrayListSelected.get(i)
                            .getProdSize();
                    Quantity = AppController.cartArrayListSelected.get(i)
                            .getProdQuantity();
                    AppController.size = caseName;
                    AppController.color = colorName;
                    for (int j = 0; j < caseArrayList.size(); j++) {
                        listCaseName = caseArrayList.get(j).getName();
                        if (listCaseName.equals(caseName)) {
                            selectedRow = j;
                        }

                    }
                    for (int k = 0; k < colorArrayList.size(); k++) {
                        listColorName = colorArrayList.get(k).getColorName();
                        if (listColorName.equals(colorName)) {
                            selectedColumn = k;
                        }
                    }
                    if (caseArrayList.get(selectedRow).getName()
                            .equals(caseName)
                            && colorArrayList.get(selectedColumn)
                            .getColorName().equals(colorName)) {
                        setPosition = selectedRow * colorArrayList.size()
                                + selectedColumn;
                        editList.get(setPosition).setText(Quantity);
                    }

                }

            }

            @Override
            public void responseFailure(String failureResponse) { //
                // TODO Auto-generated method stub
            }
        });

    }

    public void createTable() {
        tableLayout.removeAllViews();
        @SuppressWarnings("deprecation")
        int column_width = 140;
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();

        tableLayout.setBackgroundColor(getResources().getColor(R.color.vkcred));

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        editList.clear();
        if (caseArrayList.size() > 0 && colorArrayList.size() > 0) {

            for (int i = 0; i < caseArrayList.size() + 1; i++) {
                // 3) create tableRow
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tableRow.setBackgroundColor(getResources().getColor(
                        R.color.vkcred));
                tableRow.setPadding(0, 0, 0, 2);

                for (int j = 0; j < colorArrayList.size() + 1; j++) {
                    // 4) create textView
                    TextView textView = new TextView(this);
                    // textView.setText(String.valueOf(j));
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setWidth(column_width);
                    textView.setGravity(Gravity.CENTER
                            | Gravity.CENTER_HORIZONTAL);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    String s1 = Integer.toString(i);
                    String s2 = Integer.toString(j);
                    String s3 = s1 + s2;
                    int id = Integer.parseInt(s3);
                    if (i == 0 && j == 0) {
                        textView.setText("Test");
                        textView.setBackgroundColor(Color.WHITE);
                        textView.setVisibility(View.INVISIBLE);

                    } else if (i == 0) {
                        textView.setText(colorArrayList.get(j - 1)
                                .getColorName());
                    } else if (j == 0) {
                        textView.setText(caseArrayList.get(i - 1).getName());
                    } else {
                        textView.setVisibility(View.GONE);
                        EditText edit = new EditText(ProductDetailActivity.this);
                        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        edit.setHint("Qty.");
                        edit.setTextSize(15);
                        edit.setWidth(column_width);
                        edit.setBackgroundColor(Color.WHITE);
                        edit.setGravity(Gravity.CENTER
                                | Gravity.CENTER_HORIZONTAL);
                        edit.setTag(i * 10 + j);
                        edit.setSingleLine(true);
                        edit.setId(i * 10 + j);
                        tableRow.addView(edit, tableRowParams);
                        editList.add(edit);

                    }
                    // 5) add textView to tableRow
                    tableRow.addView(textView, tableRowParams);

                }

                // 6) add tableRow to tableLayout
                tableLayout.addView(tableRow, tableLayoutParams);

            }
        } else {
            // Toast.makeText(mActivity, "", duration)
        }
        if (mPendingList != null) {
            if (mPendingList.size() > 0) {

                setTableDataFromApi();
            }
        }
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        // setCartQuantity();

    }

    public void setTableDataFromApi() {
        String caseName = "", listCaseName;
        String colorName = "", listColorName;
        String Quantity = "";
        int selectedRow = 0;
        int selectedColumn = 0;
        int setPosition;

        for (int i = 0; i < mPendingList.size(); i++) {
            colorName = mPendingList.get(i).getColor();
            caseName = mPendingList.get(i).getSize();
            Quantity = mPendingList.get(i).getPendingQty();

            for (int j = 0; j < caseArrayList.size(); j++) {
                listCaseName = caseArrayList.get(j).getName();
                if (listCaseName.equals(caseName)) {
                    selectedRow = j;

                }

            }
            for (int k = 0; k < colorArrayList.size(); k++) {
                listColorName = colorArrayList.get(k).getColorName();
                if (listColorName.equals(colorName)) {
                    selectedColumn = k;

                }

            }
            if (caseArrayList.get(selectedRow).getName().equals(caseName)
                    && colorArrayList.get(selectedColumn).getColorName()
                    .equals(colorName)) {
                setPosition = selectedRow * colorArrayList.size()
                        + selectedColumn;
                editList.get(setPosition).setText(Quantity);
            }

        }

    }

    private void getCartDataForTable() {

        AppController.cartArrayListSelected.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE, "pid"};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                String pid = cursor.getString(1);
                if (pid.equals(AppController.product_id)) {
                    CartModel model = new CartModel();
                    model.setPid(cursor.getString(8));
                    model.setProdColor(cursor.getString(5));
                    model.setProdName(cursor.getString(1));
                    model.setProdQuantity(cursor.getString(6));
                    model.setProdSize(cursor.getString(3));
                    AppController.cartArrayListSelected.add(model);
                }
                cursor.moveToNext();
            }
        }
    }

    private void getCartData() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE, "pid", "sapid", "catid", "status"};
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                AppController.cartArrayList.add(setCartModel(cursor));

                cursor.moveToNext();
            }

        } else {

        }

    }

    private void getCartPrice() {
        VKCInternetManager manager = null;
        String name[] = {"productid", "userid", "categoryid"};
        String value[] = {AppController.product_id,
                AppPrefenceManager.getUserId(mActivity),
                AppController.category_id,};// AppPrefenceManager.getUserId(mActivity)
        manager = new VKCInternetManager(GET_CART_VALUE_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                try {
                    JSONObject responseObj = new JSONObject(successResponse);
                    JSONObject response = responseObj.getJSONObject("response");
                    String status = response.getString("status");
                    if (status.equals("Success")) {
                        try {
                            cartPrice = Float.parseFloat(response
                                    .getString("cartvalue"));
                        } catch (NumberFormatException e) {
                        }

                        getCreditValue();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) { // TODO
                // Auto-generated method stub

            }
        });

    }

    public void getCartValue() {
        SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
        mAdapter.openToRead();
        String sumValue = mAdapter.getCartSum();
        if (sumValue == null) {

            cartValue = 0;
        } else {

            cartValue = Integer.parseInt(sumValue);
        }
    }

    private void getCreditValue() {
        VKCInternetManager manager = null;
        String cust_id = "";
        if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
            cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
        } else {
            cust_id = AppPrefenceManager.getCustomerId(mActivity);
        }
        String name[] = {"dealerid"};
        String value[] = {cust_id};
        manager = new VKCInternetManager(GET_QUICK_ORDER_CREDIT_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                try {
                    JSONObject responseObj = new JSONObject(successResponse);
                    JSONObject response = responseObj.getJSONObject("response");
                    String status = response.getString("status");
                    if (status.equals("Success")) {

                        creditPrice = Integer.parseInt(response
                                .getString("creditvalue"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) { // TODO
                // Auto-generated method stub

            }
        });

    }

    public void insertToDb() {
        for (int i = 0; i < editList.size(); i++) {
            String id = String.valueOf(editList.get(i).getId());
            String qty = editList.get(i).getText().toString().trim();
            System.out.println("Edit Qty-" + qty);
            System.out.println("id" + id);
            Long numberofitems = null;
            if (qty.length() > 0) {
                try {
                    // the String to int conversion happens here
                    numberofitems = Long.parseLong(qty);

                    // print out the value after the conversion

                } catch (NumberFormatException nfe) {

                }
            }
            int idForColor;
            int idForSize;
            if (id.length() > 2) {
                idForColor = Integer.parseInt(id.substring(2)) - 1;
                idForSize = Integer.parseInt(id.substring(0, 2)) - 1;
            } else {
                idForColor = Integer.parseInt(id.substring(1)) - 1;
                idForSize = Integer.parseInt(id.substring(0, 1)) - 1;
            }
            if (qty.length() > 0) {

                getCartCount();

                String[][] values = {
                        {PRODUCT_ID, productModel.getId()},
                        {PRODUCT_NAME, productModel.getmProductName()},
                        {PRODUCT_SIZEID, caseArrayList.get(idForSize).getId()},
                        {PRODUCT_SIZE, caseArrayList.get(idForSize).getName()},
                        {PRODUCT_COLORID,
                                colorArrayList.get(idForColor).getId()},
                        {PRODUCT_COLOR,
                                colorArrayList.get(idForColor).getColorName()},
                        {PRODUCT_QUANTITY, qty}, {PRODUCT_GRIDVALUE, ""},
                        //	{ "pid", String.valueOf(tableCount + 1) },
                        {"sapid", productModel.getmSapId()},
                        {"catid", productModel.getCategoryId()},

                };

                SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity, DBNAME);
                mAdapter.openToRead();
                int count = mAdapter.getCount(AppController.p_id, caseArrayList
                                .get(idForSize).getName(),
                        colorArrayList.get(idForColor).getColorName(), qty);
                int countDuplicate = mAdapter.getCountDuplicateEntry(
                        productModel.getmProductName(),
                        caseArrayList.get(idForSize).getName(), colorArrayList
                                .get(idForColor).getColorName());
                if (countDuplicate == 0) {
                    if (AppController.p_id.length() > 0) {
                        mAdapter.deleteUser(AppController.p_id, caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                productModel.getCategoryId());
                        mAdapter.close();
                        addCartApi(productModel.getmProductName(), qty,
                                productModel.getCategoryId(), caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                "6", values);
                        databaseManager
                                .insertIntoDb(TABLE_SHOPPINGCART, values);

                    } else {
                        mAdapter.deleteUser(productModel.getmProductName(),
                                caseArrayList.get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                productModel.getCategoryId());
                        mAdapter.close();

                        addCartApi(productModel.getmProductName(), qty,
                                productModel.getCategoryId(), caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                "6", values);
                        databaseManager
                                .insertIntoDb(TABLE_SHOPPINGCART, values);
                    }
                } else if (countDuplicate > 0) {
                    if (AppController.p_id.length() > 0) {
                        mAdapter.deleteUser(AppController.p_id, caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                productModel.getCategoryId());
                        mAdapter.close();

                        addCartApi(productModel.getmProductName(), qty,
                                productModel.getCategoryId(), caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                "6", values);
                        databaseManager
                                .insertIntoDb(TABLE_SHOPPINGCART, values);
                    } else {
                        mAdapter.deleteUser(productModel.getmProductName(),
                                caseArrayList.get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                productModel.getCategoryId());
                        mAdapter.close();

                        addCartApi(productModel.getmProductName(), qty,
                                productModel.getCategoryId(), caseArrayList
                                        .get(idForSize).getName(),
                                colorArrayList.get(idForColor).getColorName(),
                                "6", values);

                        databaseManager
                                .insertIntoDb(TABLE_SHOPPINGCART, values);
                    }

                }

                listTemp.add(qty);

            }

        }

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                // Here your code that runs on UI Threads

                try {
                    if (listTemp.size() > 0) {
                        VKCUtils.showtoast(mActivity, 5);
                    } else {
                        VKCUtils.showtoast(mActivity, 40);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                for (int i = 0; i < editList.size(); i++) {
                    editList.get(i).setText("");
                }

                //setCartQuantity();
            }
        });
        AppController.p_id = "";
        isInserted = true;
    }

    public class ExceedAlert extends Dialog implements
            android.view.View.OnClickListener, VKCDbConstants {

        public Activity mActivity;
        public Dialog d;
        int position;

        public ExceedAlert(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_exceeds_limit);
            init();

        }

        private void init() {

            Button buttonSet = (Button) findViewById(R.id.buttonOk);
            buttonSet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    insertToDb();
                    dismiss();

                }

            });
            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    private void showExceedDialog() {
        ExceedAlert appDeleteDialog = new ExceedAlert(mActivity);

        appDeleteDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        appDeleteDialog.setCancelable(true);
        appDeleteDialog.show();

    }

    private void addCartApi(String product_id, String qty, String category_id,
                            String size, String color, String role, final String[][] values) {
        String cust_id = "";
        VKCInternetManagerWithoutDialog manager = null;
        String name[] = {"product_id", "quantity", "category_id", "size",
                "color", "role", "cust_id"};

        if (AppPrefenceManager.getUserType(this).equals("4")) {
            cust_id = AppPrefenceManager.getSelectedUserId(mActivity);
        } else {
            cust_id = AppPrefenceManager.getCustomerId(mActivity);
        }
        String value[] = {product_id, qty, category_id, size, color, role,
                cust_id};
        manager = new VKCInternetManagerWithoutDialog(ADD_TO_CART_API);
        manager.getResponsePOST(mActivity, name, value,
                new ResponseListenerWithoutDialog() {

                    @Override
                    public void responseSuccess(String successResponse) {

                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void responseFailure(String failureResponse) { // TODO
                        // Auto-generated method stub

                    }
                });

    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }
}
