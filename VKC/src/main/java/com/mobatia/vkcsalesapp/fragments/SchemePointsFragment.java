package com.mobatia.vkcsalesapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.constants.VKCJsonTagConstants;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.customview.CustomToast;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;
import com.mobatia.vkcsalesapp.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SchemePointsFragment extends Fragment implements OnClickListener,
        VKCJsonTagConstants, VKCUrlConstants {

    private View mRootView;
    int mDisplayWidth = 0;
    int mDisplayHeight = 0;
    private TextView mTxtPoint;
    List<String> categories = new ArrayList<String>();
    private ArrayList<String> listArticleNumbers;
    Spinner spinnerUserType;
    Button btnSubmit, btnReset;
    EditText mEditPoint;
    int myPoint = 0;
    String userType;
    String selectedId;
    private AutoCompleteTextView edtSearch;
    ArrayList<UserModel> listUsers;
    TextView textId, textName, textAddress, textPhone, textType, textRetailer, textSubdealer;
    LinearLayout llData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.scheme_poits_fragment, container,
                false);
        setDisplayParams();
        init(mRootView, savedInstanceState);

        final ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        View viewActionBar = getActivity().getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Scheme Points");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        return mRootView;
    }

    private void setDisplayParams() {
        DisplayManagerScale displayManagerScale = new DisplayManagerScale(
                getActivity());
        mDisplayHeight = displayManagerScale.getDeviceHeight();
        mDisplayWidth = displayManagerScale.getDeviceWidth();

    }

    private void init(final View v, Bundle savedInstanceState) {
        listUsers = new ArrayList<>();
        spinnerUserType = (Spinner) v.findViewById(R.id.spinnerUserType);
        mTxtPoint = (TextView) v.findViewById(R.id.textPoints);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        btnReset = (Button) v.findViewById(R.id.btnReset);
        mEditPoint = (EditText) v.findViewById(R.id.editPoints);
        llData = (LinearLayout) v.findViewById(R.id.llData);
        llData.setVisibility(View.GONE);
        textId = (TextView) v.findViewById(R.id.textViewId);
        textName = (TextView) v.findViewById(R.id.textViewName);
        textAddress = (TextView) v.findViewById(R.id.textViewAddress);
        textPhone = (TextView) v.findViewById(R.id.textViewPhone);
        textType = (TextView) v.findViewById(R.id.textViewType);
        edtSearch = (AutoCompleteTextView) v.findViewById(R.id.autoSearch);
        textRetailer = (TextView) v.findViewById(R.id.textRetailer);
        textSubdealer = (TextView) v.findViewById(R.id.textSubdealer);

        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        categories.clear();
        categories.add("Select User Type");
        categories.add("Retailer");
        categories.add("Sub Dealer");
        edtSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                edtSearch.showDropDown();
            }
        });
        edtSearch.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String selectedData = edtSearch.getText().toString();
                for (int i = 0; i < listUsers.size(); i++) {
                    if (listUsers.get(i).getUserName().equals(selectedData)) {
                        selectedId = listUsers.get(i).getUserId();
                        System.out.println("Selected Id : " + selectedId);
                        getUserData();
                        break;
                    } else {
                        selectedId = "";
                    }
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                } else {
                    selectedId = "";
                    llData.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerUserType.setAdapter(dataAdapter);
        spinnerUserType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                // TODO Auto-generated method stub
                if (pos > 0) {
                    if (pos == 1) {
                        userType = "5";

                        selectedId = "";
                        edtSearch.setText("");
                        // mEditPoint.setText("");
                        getUsers(userType);
                    } else {
                        userType = "7";
                        selectedId = "";
                        edtSearch.setText("");
                        getUsers(userType);
                    }
                } else {
                    userType = "";
                }
                System.out.println("User Type : " + userType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        getMyPoints();
    }

    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     */
    public void getMyPoints() {
        String name[] = {"cust_id", "role"};
        String values[] = {AppPrefenceManager.getCustomerId(getActivity()),
                AppPrefenceManager.getUserType(getActivity())};
        final VKCInternetManager manager = new VKCInternetManager(
                GET_DEALER_POINTS);

        manager.getResponsePOST(getActivity(), name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        //   Log.v("LOG", "18022015 success" + successResponse);
                        parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                });
    }

    /**
     * Method Name:parseResponse Return Type:void parameters:response Date:Feb
     * 18, 2015 Author:Archana.S
     */
    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject objResponse = jsonObject.optJSONObject("response");
            String status = objResponse.optString("status");

            if (status.equals("Success")) {

                // VKCUtils.showtoast(getActivity(), 14);

                /*
                 * clearEditText(); edtSearch.setText("");
                 */
                String points = objResponse.optString("loyality_point");
                myPoint = Integer.parseInt(points);
                mTxtPoint.setText(points);
                textRetailer.setText("To Retailer: " + objResponse.optString("to_retailer"));
                textSubdealer.setText("To Subdealer: " + objResponse.optString("to_subdealer"));

            } else {

                VKCUtils.showtoast(getActivity(), 13);
            }

        } catch (Exception e) {

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSubmit) {
            if (userType.equals("")) {
                CustomToast toast = new CustomToast(getActivity());
                toast.show(49);
            } else if (edtSearch.getText().toString().trim().equals("")) {
                CustomToast toast = new CustomToast(getActivity());
                toast.show(51);
            } else if (mEditPoint.getText().toString().trim().equals("")) {
                // VKCUtils.textWatcherForEditText(mEditPoint,
                // "Mandatory field");
                CustomToast toast = new CustomToast(getActivity());
                toast.show(52);

            } else if (Integer.parseInt(mEditPoint.getText().toString().trim()) > myPoint) {
                // FeedbackSubmitApi();
                CustomToast toast = new CustomToast(getActivity());
                toast.show(48);
            } else {

                ConfirmIssuePointDIalog dialog = new ConfirmIssuePointDIalog(
                        getActivity(), "");
                dialog.show();

            }

        } else if (v == btnReset) {

            spinnerUserType.setSelection(0);
            userType = "";
            selectedId = "";
            edtSearch.setText("");
            mEditPoint.setText("");
            llData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        // edtSearch.setText(AppController.articleNumber);
        super.onResume();
    }

    private void getUsers(String type) {
        listUsers.clear();
        VKCInternetManager manager = null;
        String name[] = {"cust_id", "user_type"};
        String value[] = {AppPrefenceManager.getCustomerId(getActivity()),
                type};
        manager = new VKCInternetManager(GET_USERS);
        manager.getResponsePOST(getActivity(), name, value,
                new ResponseListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void responseSuccess(String successResponse) {

                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {
                                JSONArray dataArray = response
                                        .optJSONArray("data");
                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        // listArticle[i]=articleArray.getString(i);
                                        JSONObject obj = dataArray
                                                .getJSONObject(i);
                                        UserModel model = new UserModel();
                                        model.setUserId(obj.getString("id"));
                                        model.setUserName(obj.getString("name"));
                                        // model.setCity(obj.getString("city"));
                                        listUsers.add(model);
                                    }
                                    ArrayList<String> listUser = new ArrayList<>();
                                    for (int i = 0; i < listUsers.size(); i++) {
                                        listUser.add(listUsers.get(i)
                                                .getUserName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            getActivity(),
                                            android.R.layout.simple_list_item_1,
                                            listUser);
                                    edtSearch.setThreshold(1);
                                    edtSearch.setAdapter(adapter);

                                } else {
                                    CustomToast toast = new CustomToast(
                                            getActivity());
                                    toast.show(17);
                                }
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

    public void submitPoints() {
        String name[] = {"userid", "to_user_id", "to_role", "points", "role"};
        String values[] = {AppPrefenceManager.getUserId(getActivity()),
                selectedId, userType, mEditPoint.getText().toString(),
                AppPrefenceManager.getUserType(getActivity())};
        final VKCInternetManager manager = new VKCInternetManager(ISSUE_POINTS);

        manager.getResponsePOST(getActivity(), name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        //Log.v("LOG", "18022015 success" + successResponse);
                        try {
                            JSONObject objResponse = new JSONObject(
                                    successResponse);
                            String status = objResponse.optString("response");
                            if (status.equals("1")) {

                                CustomToast toast = new CustomToast(
                                        getActivity());
                                toast.show(50);
                                edtSearch.setText("");
                                mEditPoint.setText("");

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                        getActivity(),
                                        android.R.layout.simple_spinner_item,
                                        categories);
                                dataAdapter
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerUserType.setAdapter(dataAdapter);
                                getMyPoints();
                            } else {
                                CustomToast toast = new CustomToast(
                                        getActivity());
                                toast.show(13);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                });
    }

    public class ConfirmIssuePointDIalog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity mActivity;
        public Dialog d;
        CheckBox mCheckBoxDis;
        ImageView mImageView;
        // public Button yes, no;

        Button bUploadImage;
        String TEXTTYPE;

        ProgressBar mProgressBar;

        public ConfirmIssuePointDIalog(Activity a, String TEXTTYPE) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;

            this.TEXTTYPE = TEXTTYPE;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.confirm_point_issue);
            init();

        }

        private void init() {
            DisplayManagerScale disp = new DisplayManagerScale(mActivity);
            int displayH = disp.getDeviceHeight();
            int displayW = disp.getDeviceWidth();

            RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBaseConfirm);

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);

            Button buttonSet = (Button) findViewById(R.id.buttonYes);
            buttonSet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    submitPoints();

                }
                // alrtDbldr.cancel();

            });
            Button buttonCancel = (Button) findViewById(R.id.buttonNo);
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

    private void getUserData() {
        //listUsers.clear();
        VKCInternetManager manager = null;
        String name[] = {"cust_id", "role"};
        String value[] = {selectedId, userType};
        manager = new VKCInternetManager(GET_DATA);
        manager.getResponsePOST(getActivity(), name, value,
                new ResponseListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void responseSuccess(String successResponse) {

                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {
                                JSONObject objData = response
                                        .optJSONObject("data");
                                String cust_id = objData
                                        .optString("customer_id");
                                String address = objData.optString("address");
                                String name = objData.optString("name");
                                String phone = objData.optString("phone");

                                if (userType.equals("5")) {
                                    textType.setText(": " + "Retailer");
                                } else if (userType.equals("7")) {
                                    textType.setText(": " + "Sub Dealer");
                                }
                                textId.setText(": " + cust_id);
                                textName.setText(": " + name);
                                textAddress.setText(": " + address);
                                textPhone.setText(": " + phone);
                                llData.setVisibility(View.VISIBLE);

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
}
