/**
 * 
 */
package com.mobatia.vkcsalesapp.SQLiteServices;

import java.util.ArrayList;
import java.util.List;

import com.mobatia.vkcsalesapp.model.CartModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * @author Archana.S
 * 
 */
public class SQLiteAdapter {

	private Context context;
	private String DB_NAME;
	private static final int DB_VERSION = 1;
	private static final String TAG = null;
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	public static int MAX_INSERT_RECORDS;
	public static int CURRENT_RECORD;

	public SQLiteAdapter(Context c, String dbName) {
		this.context = c;
		this.DB_NAME = dbName;
	}

	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public SQLiteAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void executeDump() {

	}

	public void excuteRawQuery(String query) throws SQLException {
		sqLiteDatabase.execSQL(query);

	}

	public void close() {
		sqLiteHelper.close();
	}

	public long getCount(String DB_TABLE, String[][] constraints) {
		String constraint = "";
		SQLiteStatement s;
		for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
			if (j == constraints.length - 1 || constraints.length == 1) {
				constraint += constraints[j][0] + " = " + constraints[j][1];
			} else {
				constraint += constraints[j][0] + " = " + constraints[j][1]
						+ " AND ";
			}
		}
		if (constraint != "") {
			s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
					+ DB_TABLE + " WHERE " + constraint);
		} else {
			s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
					+ DB_TABLE);
		}

		long count = s.simpleQueryForLong();
		return count;
	}

	public void update(String[][] content, String DB_TABLE,
			String[][] constraints) {

		// String[][] cons={{"qid","2"}};
		// String[][]
		// data={{"type","1"},{"question","Term Fee"},{"option1","Op1"},{"option2","Op2"},{"option3","Op3"},{"option4","Op4"},{"option5","Op5"}};
		//
		// mySQLiteAdapter = new SQLiteAdapter(this);
		// mySQLiteAdapter.openToWrite();
		// mySQLiteAdapter.update(data, "questions", cons);
		// mySQLiteAdapter.openToRead();
		// mySQLiteAdapter.close();

		ContentValues contentValues = new ContentValues();
		String constraint = "";
		List<String> columns = getAllColumns(DB_TABLE);
		int i = 0;
		for (String COLUMN : columns) {
			for (i = 0; i < content.length; i++) {
				if (content[i][0].equalsIgnoreCase(COLUMN)
						&& (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
					contentValues.put(COLUMN, content[i][1]);
					Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
				}
			}
		}

		for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
			if (j == constraints.length - 1 || constraints.length == 1) {
				constraint += constraints[j][0] + " = " + constraints[j][1];
			} else {
				constraint += constraints[j][0] + " = " + constraints[j][1]
						+ " && ";
			}
		}
		try {
			sqLiteDatabase.beginTransaction();
			sqLiteDatabase.update(DB_TABLE, contentValues, constraint, null);
			sqLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("Error:", "Error: " + e.toString());
		} finally {
			sqLiteDatabase.endTransaction();
		}
	}

	public Boolean insert(String[][] content, String DB_TABLE) {

		// String[][]data={{"qid","5"},{"surid","3"},{"type","1"},{"_id",""},{"question","What is your name?"},{"option1","Test1"},{"option2","Test2"},{"option3","Test3"},{"option4","Test4"},{"option5","Test5"}};
		// mySQLiteAdapter = new SQLiteAdapter(this);
		// mySQLiteAdapter.openToWrite();
		// mySQLiteAdapter.insert(data, "questions");
		// mySQLiteAdapter.openToRead(); mySQLiteAdapter.close();
		ContentValues contentValues = new ContentValues();
		List<String> columns = getAllColumns(DB_TABLE);
		int i = 0;
		for (String COLUMN : columns) {
			for (i = 0; i < content.length; i++) {
				if (content[i][0].equalsIgnoreCase(COLUMN)
						&& (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
					contentValues.put(COLUMN, content[i][1]);
					Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
				}
			}
		}
		try {
			sqLiteDatabase.beginTransaction();
			if (sqLiteDatabase.insert(DB_TABLE, null, contentValues) >= 0) {
				sqLiteDatabase.setTransactionSuccessful();
				sqLiteDatabase.endTransaction();
				return true;
			} else {
				sqLiteDatabase.setTransactionSuccessful();
				sqLiteDatabase.endTransaction();
				return false;
			}
		} catch (Exception e) {
			Log.v("Error:", "Error: " + e.toString());
		}
		return false;
	}

	public int makeEmpty(String DB_TABLE) {
		return sqLiteDatabase.delete(DB_TABLE, null, null);
	}

	public int makeEmpty(String DB_TABLE, String cons) {
		return sqLiteDatabase.delete(DB_TABLE, cons, null);
	}

	public List<String> getAllColumns(String DB_TABLE) {

		// List<String> columns= mySQLiteAdapter.getAllColumns("questions");
		// for(String column: columns){ Log.v("",column); }

		String[] columns;
		List<String> list = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(DB_TABLE, null, null, null, null,
				null, null);
		columns = cursor.getColumnNames();

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		for (int i = 0; i < columns.length; i++) {
			list.add(columns[i]);
		}
		return list;
	}

	public Cursor queueAll(String DB_TABLE, String[] Columns, String order,
			String condition) {
		Cursor cursor = sqLiteDatabase.query(DB_TABLE, Columns, condition,
				null, null, null, order);

		return cursor;
	}

	public int deleteItem(String DB_TABLE, String condition) {

		return sqLiteDatabase.delete(DB_TABLE, condition, null);
	}

	// select last_insert_rowid();
	public int getLatestRowId() {
		int latestId = 0;
		// String query =
		// "SELECT COLUMN_NAME from DB_TABLE order by COLUMN_NAME DESC limit 1";
		// Cursor c = sqLiteDatabase.rawQuery(query,null);
		Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid()",
				null);

		if (cursor != null && cursor.moveToFirst()) {
			latestId = cursor.getInt(0);// The 0 is the column index, we only
										// have 1
										// column, so the index is 0
			System.out.println("LatestId " + latestId);
			cursor.close();
		}
		return latestId;
	}

	private static class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * public ArrayList<CartModel> getDataFromDb(String product_id) { // String
	 * // sql="SELECT * FROM NotificationList ORDER BY notification DESC";
	 * Log.d("TAG", "getDataFromDb>>>>>" + product_id);
	 * 
	 * String sql = "SELECT * FROM ShoppingCart where pid='" + product_id + "'";
	 * SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
	 * DB_VERSION);
	 * 
	 * SQLiteDatabase db = sqLiteHelper.getReadableDatabase(); Cursor c =
	 * db.rawQuery(sql, null); if(c.getCount()>0){ Log.d("TAG",
	 * "getDataFromDb>>>>>1" + c.getString(c.getColumnIndex("productname"))); }
	 * 
	 * ArrayList<CartModel> array = new ArrayList<CartModel>(); if
	 * (c.moveToNext()) { CartModel cartModel = new CartModel();
	 * cartModel.setProdName(c.getString(c.getColumnIndex("productname")));
	 * cartModel.setProdSize(c.getString(c.getColumnIndex("productsize")));
	 * cartModel.setProdSizeId(c.getString(c.getColumnIndex("sizeid")));
	 * cartModel .setProdColor(c.getString(c.getColumnIndex("productcolor")));
	 * System.out.println("c.getString---" +
	 * c.getString(c.getColumnIndex("productname"))); array.add(cartModel);
	 * 
	 * } return array;
	 * 
	 * }
	 */
	// function to delete existing product
	public void deleteUser(String productname, String quantity, String color,
			String categoryId) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		System.out.println("delete called---" + productname + "--" + quantity
				+ "--" + color);
		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		try {
			// db.delete("ShoppingCart", "productname = ? AND productsize=?",
			// new String[] { productname, });
			db.execSQL("Delete From ShoppingCart where productname='"
					+ productname + "' AND productsize='" + quantity
					+ "' AND productcolor='" + color + "' AND productcolor='"
					+ color + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// get countfrpm db

	public int getCount(String productname, String quantity, String color,
			String enteredValue) {
		String countQuery = "SELECT  * FROM " + "ShoppingCart"
				+ " where productname='" + productname + "' AND productsize='"
				+ quantity + "' AND productcolor='" + color
				+ "' AND productqty=" + enteredValue;
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		System.out.println("Count---" + cnt);
		return cnt;
	}

	public int getCountDuplicateEntry(String productname, String quantity,
			String color) {
		String countQuery = "SELECT  * FROM " + "ShoppingCart"
				+ " where productname='" + productname + "' AND productsize='"
				+ quantity + "' AND productcolor='" + color + "' ";
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();

		return cnt;
	}

	public int getCountDuplicate(String productname, String quantity,
			String color) {
		String countQuery = "SELECT  * FROM " + "ShoppingCart"
				+ " where productname='" + productname + "' AND productsize='"
				+ quantity + "' AND productcolor='" + color + "' ";
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();

		return cnt;
	}

	public String getQuantity(String productname, String color, String size) {
		String countQuery = "SELECT  * FROM " + "ShoppingCart"
				+ " where productname='" + productname + "' AND productsize='"
				+ size + "' AND productcolor='" + color + "' ";
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		String cnt = cursor.getString(6);
		System.out.println("Count---" + cnt);

		cursor.close();

		return cnt;
	}

	public void updateData(String prodId, String prodColor, String prodSize,
			String qty) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);

		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		try {

			// db.delete("ShoppingCart", "productname = ? AND productsize=?",
			// new String[] { productname, });
			db.execSQL("Update ShoppingCart SET productqty='" + qty
					+ "' where productname='" + prodId + "' AND productsize='"
					+ prodSize + "' AND productcolor='" + prodColor + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public void deletePending() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);

		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		try {
			String status = "pending";
			// db.delete("ShoppingCart", "productname = ? AND productsize=?",
			// new String[] { productname, });
			db.execSQL("Delete From ShoppingCart where status='" + status + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public String getQty(String productname, String color, String size,
			String categoryId) {
		String details = null;
		sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		String countQuery = "SELECT  * FROM " + "ShoppingCart"
				+ " where productname='" + productname + "' AND productsize='"
				+ size + "' AND productcolor='" + color + "' AND catid='"
				+ categoryId + "'";
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor c = db.rawQuery(countQuery, null);
		if (c != null && c.moveToFirst()) {

			// details=new VehicleDetails();
			details = c.getString(c.getColumnIndex("productqty"));

			// Log.d("TAG", "getMasterId>>>>>Qty"+details);

		}
		if (c != null && !c.isClosed()) {
			c.close();
		}
		return details;

	}

	public String getCartSum() {
		String countQuery = "SELECT  sum(price) FROM ShoppingCart";
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		String sumValue = null;
		
	//	System.out.println("Price" + sumValue);
		if (cursor != null && cursor.moveToFirst()) {

			// details=new VehicleDetails();
			sumValue = cursor.getString(0);

			// Log.d("TAG", "getMasterId>>>>>Qty"+details);

		}
		else
		{
			sumValue="0";
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return sumValue;
	}
	
	public void updateQuantity(String prodId, String prodColor, String prodSize,
			String qty) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
				DB_VERSION);

		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		try {

			// db.delete("ShoppingCart", "productname = ? AND productsize=?",
			// new String[] { productname, });
			db.execSQL("Update ShoppingCart SET productqty='" + qty
					+ "' where productname='" + prodId + "' AND productsize='"
					+ prodSize + "' AND productcolor='" + prodColor + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
}
