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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.miscellaneous.VKCUtils;

public class ForgotPasswordActivity extends AppCompatActivity implements OnClickListener,
        VKCUrlConstants {

    EditText editEmail;
    Button buttonSubmit;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Forgot Password");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setActionBar();
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editEmail = (EditText) findViewById(R.id.editEmail);
        buttonSubmit.setOnClickListener(this);

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


    private void getMyPassword() {
        VKCInternetManager manager = null;
        String name[] = {"email"};
        String value[] = {editEmail.getText().toString()};
        manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
        manager.getResponsePOST(mContext, name, value,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {

                        // parseJSON(successResponse);


                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub

                    }
                });

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

        if (v == buttonSubmit) {

            if (editEmail.getText().toString().trim().length() > 0) {
                getMyPassword();
            } else {
                VKCUtils.showtoast(mContext, 53);
            }

        }


    }


}
