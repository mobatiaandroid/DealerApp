/**
 * 
 */
package com.mobatia.vkcsalesapp.customview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcsalesapp.R;

/**
 * @author mobatia-user
 * 
 */
public class CustomToast {

	Activity mActivity;
	TextView mTextView;
	Toast mToast;

	public CustomToast(Activity mActivity) {
		this.mActivity = mActivity;
		init();

	}

	public void init() {
		LayoutInflater inflater = mActivity.getLayoutInflater();
		View layouttoast = inflater.inflate(R.layout.custom_toast, null);
		mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

		mToast = new Toast(mActivity);
		mToast.setView(layouttoast);
	}

	public void show(int errorCode) {
		if (errorCode == 0) {
			mTextView.setText(mActivity.getResources().getString(
					R.string.common_error_message));
		}
		if (errorCode == 1) {
			mTextView.setText("Successfully logged in.");
		}
		if (errorCode == 2) {
			mTextView.setText("Login failed.Please try again later");
		}
		if (errorCode == 3) {
			mTextView.setText("Please select size and color");
		}
		if (errorCode == 4) {
			mTextView.setText("Please select size,color and quantity");
		}
		if (errorCode == 5) {
			mTextView.setText("Succesfully added to cart");
		}
		if (errorCode == 6) {
			mTextView.setText("Successfully submitted login request");
		}
		if (errorCode == 7) {
			mTextView.setText("Registration failed.Try again later");
		}
		if (errorCode == 8) {
			mTextView.setText("Email Already Exists");
		}
		if (errorCode == 9) {
			mTextView.setText("No items in the cart");
		}
		if (errorCode == 10) {
			mTextView.setText("Please enter valid username and password");
		}
		if (errorCode == 11) {
			mTextView.setText("All fields are mandatory");
		}
		if (errorCode == 12) {
			mTextView.setText("Complaint send successfully");
		}
		if (errorCode == 13) {
			mTextView.setText("Failed.Try again later");
		}
		if (errorCode == 14) {
			mTextView.setText("Feedback send successfully");
		}
		if (errorCode == 15) {
			mTextView.setText("Salesorder submitted successfully");
		}
		if (errorCode == 16) {
			mTextView.setText("No enough values for placing sales order");
		}
		if (errorCode == 17) {
			mTextView.setText("Sorry, no result found");
		}
		if (errorCode == 18) {
			mTextView.setText("Please select any Category");
		}
		if (errorCode == 19) {
			mTextView.setText("Please select any Sub Category in filter");
		}
		if (errorCode == 20) {
			mTextView.setText("Please select state");
		}
		if (errorCode == 21) {
			mTextView.setText("Please select district");
		}
		if (errorCode == 22) {
			mTextView.setText("Please select place");
		}
		if (errorCode == 23) {
			mTextView
					.setText("Login failed.Invalid login credentials or account not approved");
		}
		if (errorCode == 24) {
			mTextView.setText("Sorry, cannot add this item to cart");
		}
		if (errorCode == 25) {
			mTextView.setText("Cannot enter value greater than existing quantity");
		}
		if (errorCode == 26) {
			mTextView.setText("Order updated successfully");
		}
		if(errorCode==27)
		{
			mTextView.setText("Cannot enter a quantity greater than order quantity");
		}
		if (errorCode == 28) {
			mTextView.setText("Updated successfully !");
		}
		if (errorCode == 29) {
			mTextView.setText("Quantity cannot be empty !");
		}
		if (errorCode == 30) {
			mTextView.setText("Please fill all the quantity fields correctly");
		}
		if (errorCode == 31) {
			mTextView.setText("Dealers added successfully !");
		}
		if (errorCode == 32) {
			mTextView.setText("There are no pending orders");
		}
		if (errorCode == 33) {
			mTextView.setText("State value missing");
		}
		if (errorCode == 34) {
			mTextView.setText("District value missing");
		}
		
		if (errorCode == 35) {
			mTextView.setText("Product already exist in cart adding quantity");
		}
		if (errorCode == 36) {
			mTextView.setText("Please enter keyword to search");
		}
		if (errorCode == 37) {
			mTextView.setText("No results found");
		}
		if (errorCode == 38) {
			mTextView.setText("Please enter a keyword to search");
		}
		if (errorCode == 39) {
			mTextView.setText("Please select a customer category");
		}
		if(errorCode==40)
		{
			mTextView.setText("Please fill the quantity value before you proceed");
		}
		if(errorCode==41)
		{
			mTextView.setText("Please choose customer category and customer name from cart before you proceed");
		}
		if(errorCode==42)
		{
			mTextView.setText("No details found");
		}
		if(errorCode==43)
		{
			mTextView.setText("No results found");
		}
		if(errorCode==44)
		{
			mTextView.setText("No records to display");
		}
		if(errorCode==45)
		{
			mTextView.setText("Please enter the article number");
		}
		
		if(errorCode==46)
		{
			mTextView.setText("Unable to add to cart as your cart value exceeds credit limit !");
		}
		if(errorCode==47)
		{
			mTextView.setText("Please check the size value !");
		}
		if(errorCode==48)
		{
			mTextView.setText("Issue point should not be greater than your points!");
		}
		if(errorCode==49)
		{
			mTextView.setText("Please select user type !");
		}
		if(errorCode==50)
		{
			mTextView.setText("Successfully issued points");
		}
		if(errorCode==51)
		{
			mTextView.setText("Please select a user");
		}
		if(errorCode==52)
		{
			mTextView.setText("Please enter point value");
		}
		if(errorCode==53)
		{
			mTextView.setText("Please enter email Id");
		}
		if(errorCode==54)
		{
			mTextView.setText("Invalid email Id");
		}
		if(errorCode==55)
		{
			mTextView.setText("Cart has been cleared successfully");
		}
		if(errorCode==56)
		{
			mTextView.setText("Please select dealer to load the cart data");
		}
		
		if(errorCode==57)
		{
			mTextView.setText("Unable to load transaction history, since there is no scheme running in your state");
		}
		
		if(errorCode==58)
		{
			mTextView.setText("Unable to load report, since there is no scheme running in your state");
		}
		if(errorCode==59)
		{
			mTextView.setText("Please grant permission to continue");
		}

		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}
	/*
	 * CustomToast toast = new CustomToast(mActivity); toast.show(18);
	 */
}
