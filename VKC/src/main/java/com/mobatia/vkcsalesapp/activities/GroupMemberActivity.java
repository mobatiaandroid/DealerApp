package com.mobatia.vkcsalesapp.activities;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.MemberAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.model.MembersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupMemberActivity extends AppCompatActivity implements OnClickListener,
        VKCUrlConstants {

    Activity mContext;
    ListView listViewMembers;
    TextView textTotalCoupon, textIssuedCoupon, textOrderLimit, textTotalBalance, textTotalDue;
    ArrayList<MembersModel> listMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        mContext = this;
        initialiseUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initialiseUI() {
        listMembers = new ArrayList<MembersModel>();
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Group Member");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setActionBar();
        getSupportActionBar().setLogo(R.drawable.back);
        textTotalCoupon = (TextView) findViewById(R.id.textTotalCoupon);
        textIssuedCoupon = (TextView) findViewById(R.id.textIssued);
        textOrderLimit = (TextView) findViewById(R.id.textTotalOrderLimit);
        textTotalBalance = (TextView) findViewById(R.id.textTotalBalance);
        textTotalDue = (TextView) findViewById(R.id.textTotalDue);
        listViewMembers = (ListView) findViewById(R.id.listViewMembers);
        listViewMembers.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

            }
        });
        getMembers();

    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void getMembers() {
        VKCInternetManager manager = null;
        String name[] = {"cust_id"};
        String value[] = {AppPrefenceManager.getCustomerId(mContext)};
        manager = new VKCInternetManager(GET_MEMBERS_LIST);
        manager.getResponsePOST(mContext, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                try {
                    JSONObject objResponse = new JSONObject(successResponse);
                    JSONArray arrayMembers = objResponse
                            .getJSONArray("response");

                    textTotalCoupon.setText(objResponse.getString("loyalty_points"));
                    textIssuedCoupon.setText(objResponse.getString("tot_issued_points"));
                    textOrderLimit.setText(objResponse.getString("order_limit"));
                    textTotalBalance.setText(objResponse.getString("total_balance"));
                    textTotalDue.setText(objResponse.getString("total_due"));
                    listViewMembers = (ListView) findViewById(R.id.listViewMembers);
                    if (arrayMembers.length() > 0) {
                        for (int i = 0; i < arrayMembers.length(); i++) {
                            JSONObject obj = arrayMembers.optJSONObject(i);
                            MembersModel model = new MembersModel();
                            model.setAddress(obj.optString("address"));
                            model.setCity_name(obj.optString("city_name"));
                            model.setCredit_view(obj.optString("credit_view"));
                            model.setCust_id(obj.optString("cust_id"));
                            model.setCustomer_name(obj
                                    .optString("customer_name"));
                            model.setDealer_count(obj.optString("dealer_count"));
                            model.setDist_name(obj.optString("dist_name"));
                            model.setLogin(obj.optString("login"));
                            model.setRole_id(obj.optString("role_id"));
                            model.setRole_name(obj.optString("role_name"));
                            model.setShopName(obj.optString("shopName"));
                            model.setState_code(obj.optString("state_code"));
                            model.setState_name(obj.optString("state_name"));
                            model.setUser_email(obj.optString("user_email"));
                            model.setUser_id(obj.optString("user_id"));
                            model.setUser_name(obj.optString("user_name"));
                            model.setUser_phone(obj.optString("user_phone"));
                            listMembers.add(model);
                        }
                        MemberAdapter adapter = new MemberAdapter(mContext, listMembers);
                        listViewMembers.setAdapter(adapter);
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

    public void parseJSON(String response) {

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub


    }
}
