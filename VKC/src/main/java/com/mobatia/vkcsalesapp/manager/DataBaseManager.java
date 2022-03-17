/**
 * 
 */
package com.mobatia.vkcsalesapp.manager;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcsalesapp.constants.VKCDbConstants;

/**
 * @author Archana S
 * 
 */
public class DataBaseManager implements VKCDbConstants {

	private Context context;
	private SQLiteAdapter mySQLiteAdapter;

	public DataBaseManager(Context context) {
		this.context = context;
	}

	public void removeFromDb(String tableName, String fieldName,
			String fieldValue) {
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToWrite();
		mySQLiteAdapter.makeEmpty(tableName, fieldName + "==" + fieldValue);
		mySQLiteAdapter.close();
	}
	
	public void deleteTitle(String name) 
	{
	    //return db.delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) > 0;
		
	}

	public Cursor fetchFromDB(String[] columns, String tableName,
			String constraintFieldName) {
		Cursor cursor = null;
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToRead();
		cursor = mySQLiteAdapter.queueAll(tableName, columns, "", null);
		cursor.moveToPosition(0);
		mySQLiteAdapter.close();
		return cursor;
	}

	public void insertIntoDb(String tableName, String[][] data) {
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToWrite();
		mySQLiteAdapter.insert(data, tableName);
		mySQLiteAdapter.close();
	}

	public void updateTableRow(String tableName, String[][] data,
			String[][] constrain) {
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToWrite();
		mySQLiteAdapter.update(data, tableName, constrain);
		mySQLiteAdapter.close();
	}

	public void removeDb(String tableName) {
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToWrite();
		mySQLiteAdapter.makeEmpty(tableName);
		mySQLiteAdapter.close();
	}

	public void updateCartQty(String tableName, String[][] data) {
		mySQLiteAdapter = new SQLiteAdapter(context, DBNAME);
		mySQLiteAdapter.openToWrite();
		//mySQLiteAdapter.makeEmpty(tableName);
		mySQLiteAdapter.close();
	}
	
	

}
