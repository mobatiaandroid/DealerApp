package com.mobatia.vkcsalesapp.activities;


import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.adapter.ArticleAdapter;
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants;
import com.mobatia.vkcsalesapp.controller.AppController;
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager;
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcsalesapp.model.ArticleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements VKCUrlConstants {
	private Activity mActivity;
	private String search_key;
	List<ArticleModel> listArticle;
	ListView listViewArticle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_article);
		mActivity = this;
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			search_key = extras.getString("key");

		}

		initialiseUI();
		listViewArticle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				AppController.articleNumber = listArticle.get(arg2)
						.getArticle_no();
			}
		});
	}

	private void initialiseUI() {
		// TODO Auto-generated method stub
		listArticle = new ArrayList<ArticleModel>();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.show();
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(true);
		listViewArticle = (ListView) findViewById(R.id.listViewArticle);
		getArticleApi();
	}

	private void getArticleApi() {
		VKCInternetManager manager = null;
		listArticle.clear();
		String name[] = { "article_no" };
		String value[] = { AppPrefenceManager.getUserId(this) };
		manager = new VKCInternetManager(URL_ARTICLE_SEARCH_PRODUCT);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				parseMyDealerJSON(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub


			}
		});

	}

	private void parseMyDealerJSON(String successResponse) {
		// TODO Auto-generated method stub

		try {

			JSONObject respObj = new JSONObject(successResponse);
			JSONObject response = respObj.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {
				JSONArray respArray = response.getJSONArray("orderdetails");
				for (int i = 0; i < respArray.length(); i++) {
					ArticleModel model = new ArticleModel();
					JSONObject obj = respArray.getJSONObject(i);
					model.setId(obj.getString("id"));
					model.setArticle_no(obj.getString("article_no"));
					listArticle.add(model);
				}
				ArticleAdapter adapter = new ArticleAdapter(mActivity,
						listArticle);
				listViewArticle.setAdapter(adapter);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}





	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(item));
	}

}