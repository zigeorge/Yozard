package com.yozard.pp.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yozard.pp.R;
import com.yozard.pp.adapter.HappyCouponAdapter;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Following;
import com.yozard.pp.model.HappyHourResponse;
import com.yozard.pp.model.SavedCouponResponse;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

public class HappyHourFragment extends Fragment {

	Context context;
	SharedPreferences registration_preference;
	String hash_key = "", auth_token = "";

	SwipeRefreshLayout swipeRefreshLayout;
	ListView lvAllHhCoupons;
	HappyCouponAdapter happyCouponAdapter;

	Vector<Coupon> allHCoupon = new Vector<Coupon>();
	Vector<Following> followings = new Vector<Following>();

	int noOfCoupons = 0;

	boolean canLoadMore = true;

	public HappyHourFragment(String hashHappyhour) {
		this.hash_key = hashHappyhour;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_happy_hour, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		context = getActivity();
		prepareDataset();
		initUI(view);

	}

	private void prepareDataset() {
		registration_preference = getActivity().getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		String couponsRaw = registration_preference.getString(hash_key, null);
		// Log.e("Live Coupons >>> ", couponsRaw);
		Gson g = new Gson();
		if (hash_key.equalsIgnoreCase(HashStatic.HASH_SAVEDCOUPONS)) {
			SavedCouponResponse savedCouponResponse = g.fromJson(couponsRaw,
					SavedCouponResponse.class);
			if (savedCouponResponse.getSaved_coupons().getHappy_hour().size() > 0) {
				allHCoupon = savedCouponResponse.getSaved_coupons()
						.getHappy_hour();
				for (Coupon hCoupon : allHCoupon) {
					hCoupon.setUsage_status("use");
				}
				followings = savedCouponResponse.getFollowing();
				noOfCoupons = allHCoupon.size();
				setCouponFollowing();
			}
		} else {
			HappyHourResponse happyCouponResponse = g.fromJson(couponsRaw,
					HappyHourResponse.class);
			if (happyCouponResponse.getHh_campaign().getAllHCoupon().size() > 0) {
				allHCoupon = happyCouponResponse.getHh_campaign()
						.getAllHCoupon();
				followings = happyCouponResponse.getFollowing();
				noOfCoupons = allHCoupon.size();
				setCouponFollowing();
			}
		}
	}

	private void initUI(View v) {
		lvAllHhCoupons = (ListView) v.findViewById(R.id.lvAllHhCoupons);
		lvAllHhCoupons.setOnScrollListener(new OnScrollListener() {

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
						&& !swipeRefreshLayout.isRefreshing()
						&& !view.canScrollVertically(1) && canLoadMore
						&& allHCoupon.size() >= 10) {
					Log.e("STATUS >> ", "Refresh Time Started");
					swipeRefreshLayout.setRefreshing(true);
					startLoadMore();
				}
			}
		});

		swipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing(false);
			}
		});
		if (allHCoupon.size() > 0) {
			happyCouponAdapter = new HappyCouponAdapter(getActivity(),
					R.layout.happy_coupon_row, allHCoupon);
			lvAllHhCoupons.setAdapter(happyCouponAdapter);
		}
	}

	private void setCouponFollowing() {
		for (Coupon hCoupon : allHCoupon) {
			for (Following following : followings) {
				if (hCoupon.getCompany_id().equalsIgnoreCase(
						following.getCompany_id())) {
					hCoupon.setFollowing(true);
					Log.e("Following", hCoupon.getCompany_id());
				}
			}
		}
	}

	private void startLoadMore() {
		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		Log.e("ID >> >> ", userId_sp);

		new GetAllHHCouponAsync().execute(profileMap);
	}

	public class GetAllHHCouponAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		Editor editor;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		JSONObject jobj;
		String message = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading Coupons...");
			progressDialog.show();
			progressDialog.setCancelable(false);
			canLoadMore = false;
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
				auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {

					String url_select3 = "";

					if (hash_key.equalsIgnoreCase(HashStatic.HASH_SAVEDCOUPONS)) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "saved-coupons.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"offset_happy\":\""
										+ allHCoupon.size() + "\"}", "utf-8");
					} else {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "happy-hours.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"offset_all\":\""
										+ allHCoupon.size() + "\"}", "utf-8");
					}
					// Log.e("JSON String >> ", "{\"customer_id\":\"" + userId
					// + "\"}");

					jobj = jparser.makeHttpRequest(url_select3, "GET",
							paramiters);
					message = jobj.getString("message");
					System.out.println(url_select3);

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
			progressDialog.dismiss();
			if (message.contains("success")) {
				Gson g = new Gson();
				if (hash_key.equalsIgnoreCase(HashStatic.HASH_SAVEDCOUPONS)) {
					SavedCouponResponse savedCouponResponse = g.fromJson(
							jobj.toString(), SavedCouponResponse.class);
					if (savedCouponResponse.getSaved_coupons().getHappy_hour()
							.size() > 0) {
						allHCoupon.addAll(savedCouponResponse
								.getSaved_coupons().getHappy_hour());
					}
				} else {
					HappyHourResponse happyHourResponse = g.fromJson(
							jobj.toString(), HappyHourResponse.class);
					if (happyHourResponse.getHh_campaign().getAllHCoupon()
							.size() > 0) {
						allHCoupon.addAll(happyHourResponse.getHh_campaign()
								.getAllHCoupon());
					}
				}
				setCouponFollowing();
				happyCouponAdapter.notifyDataSetChanged();
				swipeRefreshLayout.setRefreshing(false);
				if (allHCoupon.size() == noOfCoupons) {
					canLoadMore = false;
				} else {
					canLoadMore = true;
					noOfCoupons = allHCoupon.size();
				}
				Log.e("Data", "Loaded Successfully");
			}
		}

	}
}
