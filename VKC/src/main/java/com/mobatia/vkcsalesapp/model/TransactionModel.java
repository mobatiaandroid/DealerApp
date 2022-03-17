package com.mobatia.vkcsalesapp.model;

import java.util.ArrayList;

public class TransactionModel {
	
	private ArrayList<HistoryModel> listHistory;
	private String userName;
	private String totalPoints;
	public ArrayList<HistoryModel> getListHistory() {
		return listHistory;
	}
	public void setListHistory(ArrayList<HistoryModel> listHistory) {
		this.listHistory = listHistory;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(String totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	

}
