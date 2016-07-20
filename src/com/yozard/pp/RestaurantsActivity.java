package com.yozard.pp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yozard.pp.adapter.RestaurantsAdapter;
import com.yozard.pp.fragments.AllCouponFragment.GetAllCouponAsync;
import com.yozard.pp.model.Following;
import com.yozard.pp.model.Restaurant;
import com.yozard.pp.model.RestaurantsResponse;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

public class RestaurantsActivity extends ActionBarActivity {

	ListView lvRestaurants;
	SwipeRefreshLayout srlRestaurants;
	ProgressBar pbRestaurants;
	RelativeLayout rlProgressLayout;

	RestaurantsAdapter restaurantsAdapter;
	SharedPreferences registration_preference;
	Context context;

	String cust_id, base_url, auth_token;
	Vector<Restaurant> allRestaurants = new Vector<Restaurant>();
	Vector<Following> followings = new Vector<Following>();

	boolean canLoadMore = true;
	int noOfCoupons = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurants);

		context = this;

		initUI();

		prepareAndSetDataset();

	}

	private void initUI() {
		lvRestaurants = (ListView) findViewById(R.id.lvRestaurants);
		lvRestaurants.setVisibility(View.GONE);
		srlRestaurants = (SwipeRefreshLayout) findViewById(R.id.srlRestaurants);
		srlRestaurants.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		srlRestaurants.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				srlRestaurants.setRefreshing(false);
			}
		});
		pbRestaurants = (ProgressBar) findViewById(R.id.pbRestaurants);
		rlProgressLayout = (RelativeLayout) findViewById(R.id.rlProgressLayout);
		rlProgressLayout.setVisibility(View.VISIBLE);

		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);

		lvRestaurants.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int position = firstVisibleItem + visibleItemCount;
				int limit = totalItemCount;
				if (position >= limit && totalItemCount > 0
						&& !srlRestaurants.isRefreshing()
						&& !view.canScrollVertically(1) && canLoadMore
						&& allRestaurants.size() >= 10) {
					Log.e("STATUS >> ", "Refresh Time Started");
					srlRestaurants.setRefreshing(true);
					startLoadMore();
				}
			}
		});
	}

	private void prepareAndSetDataset() {
		restaurantsAdapter = new RestaurantsAdapter(context,
				R.layout.restaurant_row, allRestaurants);
		lvRestaurants.setAdapter(restaurantsAdapter);
		noOfCoupons = allRestaurants.size();

		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		cust_id = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, cust_id);

		new GetAllRestaurasntAsync().execute(profileMap);
	}

	private void setCouponFollowing() {
		for (Restaurant restaurant : allRestaurants) {
			for (Following following : followings) {
				if (restaurant.getRestaurant_id().equalsIgnoreCase(
						following.getCompany_id())) {
					restaurant.setFollowing(true);
					Log.e("Following", restaurant.getRestaurant_id());
				}
			}
		}
	}

	private void startLoadMore() {
		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		Log.e("ID >> >> ", userId_sp);

		new GetAllRestaurasntAsync().execute(profileMap);
	}

	public class GetAllRestaurasntAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		Editor editor;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		JSONObject jobj;
		String message = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			canLoadMore = false;
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String userId = parametersList.get(HashStatic.CUSTOMER_ID);

				registration_preference = context.getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(
						HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {

					String url_select3 = baseUrl
							+ BaseURL.API
							+ "restaurants.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode(
									"{\"customer_id\":\"" + userId
											+ "\",\"offset\":\""
											+ allRestaurants.size() + "\"}",
									"utf-8");

					jobj = jparser.makeHttpRequest(url_select3, "GET",
							paramiters);

					System.out.println(url_select3);
					message = jobj.getString("message");
					Log.e("JSON String >> ", jobj.toString());

					// editor.putString(HashStatic.HASH_LIVECOUPONS,
					// jobj.toString()).commit();

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// Toast.makeText(context, "Network connection not available.",
				// Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (message.contains("success")) {
				srlRestaurants.setRefreshing(false);
				Gson g = new Gson();
				RestaurantsResponse restaurantsResponse = g.fromJson(
						jobj.toString(), RestaurantsResponse.class);
				if (restaurantsResponse.getRestaurants().size() > 0) {
					allRestaurants.addAll(restaurantsResponse.getRestaurants());
				}
				followings.addAll(restaurantsResponse.getFollows());
				setCouponFollowing();
				if (allRestaurants.size() == noOfCoupons) {
					canLoadMore = false;
				} else {
					canLoadMore = true;
					noOfCoupons = allRestaurants.size();
				}

				restaurantsAdapter.notifyDataSetChanged();
				rlProgressLayout.setVisibility(View.GONE);
				lvRestaurants.setVisibility(View.VISIBLE);

				Log.e("Data", "Loaded Successfully");
			}
		}

	}

}
