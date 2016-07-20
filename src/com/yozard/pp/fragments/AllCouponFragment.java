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
import com.yozard.pp.adapter.LiveCouponAdapter;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Following;
import com.yozard.pp.model.LiveCouponResponse;
import com.yozard.pp.model.SavedCouponResponse;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

public class AllCouponFragment extends Fragment {

	ListView lvAllCoupons;
	SwipeRefreshLayout swipeRefreshLayout;
	Vector<Coupon> allCoupons = new Vector<Coupon>();
	Vector<Following> followings = new Vector<Following>();
	LiveCouponAdapter liveCouponAdapter;

	int noOfCoupons = 0;

	boolean canLoadMore = true;

	Context context;

	SharedPreferences registration_preference;
	String hash_key = "", auth_token;

	public AllCouponFragment(String hashLivecoupons) {
		// TODO Auto-generated constructor stub
		this.hash_key = hashLivecoupons;
	}

	public AllCouponFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_all_coupon, container, false);
	}

	private void initUI(View v) {
		lvAllCoupons = (ListView) v.findViewById(R.id.lvAllCoupons);
		lvAllCoupons.setOnScrollListener(new OnScrollListener() {

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
						&& allCoupons.size() >= 10) {
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
		if (allCoupons.size() > 0) {
			Log.e("No. Of Coupons initUI >>> ", allCoupons.size() + "");
			liveCouponAdapter = new LiveCouponAdapter(getActivity(),
					R.layout.coupon_row, allCoupons);
			lvAllCoupons.setAdapter(liveCouponAdapter);
		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		prepareDataset();
		context = getActivity();
		initUI(view);

	}

	private void prepareDataset() {
		registration_preference = getActivity().getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String couponsRaw = registration_preference.getString(hash_key, null);
		// Log.e("Live Coupons >>> ", couponsRaw);
		Gson g = new Gson();
		if (hash_key.equalsIgnoreCase(HashStatic.HASH_SAVEDCOUPONS)) {
			SavedCouponResponse savedCouponResponse = g.fromJson(couponsRaw,
					SavedCouponResponse.class);
			if (savedCouponResponse.getSaved_coupons().getLive_coupon().size() > 0) {
				allCoupons = savedCouponResponse.getSaved_coupons()
						.getLive_coupon();
				followings = savedCouponResponse.getFollowing();
				noOfCoupons = allCoupons.size();
				setCouponFollowing();
			}
		} else {
			LiveCouponResponse liveCouponResponse = g.fromJson(couponsRaw,
					LiveCouponResponse.class);
			if (liveCouponResponse.getLc_campaign().getAllCoupons().size() > 0) {
				allCoupons = liveCouponResponse.getLc_campaign()
						.getAllCoupons();
				followings = liveCouponResponse.getFollowing();
				noOfCoupons = allCoupons.size();
				setCouponFollowing();
			}
		}
	}

	private void setCouponFollowing() {
		for (Coupon coupon : allCoupons) {
			for (Following following : followings) {
				if (coupon.getCompany_id().equalsIgnoreCase(
						following.getCompany_id())) {
					coupon.setFollowing(true);
					Log.e("Following", coupon.getCompany_id());
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

		new GetAllCouponAsync().execute(profileMap);
	}

	public class GetAllCouponAsync extends
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
										+ userId + "\",\"offset_live\":\""
										+ allCoupons.size() + "\"}", "utf-8");
					} else {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "live-coupons.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"offset_all\":\""
										+ allCoupons.size() + "\"}", "utf-8");
					}
					// Log.e("JSON String >> ", "{\"customer_id\":\"" + userId
					// + "\"}");

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
			progressDialog.dismiss();
			if (message.contains("success")) {
				Gson g = new Gson();
				if (hash_key.equalsIgnoreCase(HashStatic.HASH_SAVEDCOUPONS)) {
					SavedCouponResponse savedCouponResponse = g.fromJson(
							jobj.toString(), SavedCouponResponse.class);
					if (savedCouponResponse.getSaved_coupons().getLive_coupon()
							.size() > 0) {
						allCoupons.addAll(savedCouponResponse
								.getSaved_coupons().getLive_coupon());
					}
				} else {
					LiveCouponResponse liveCouponResponse = g.fromJson(
							jobj.toString(), LiveCouponResponse.class);
					if (liveCouponResponse.getLc_campaign().getAllCoupons()
							.size() > 0) {
						allCoupons.addAll(liveCouponResponse.getLc_campaign()
								.getAllCoupons());
					}
				}
				setCouponFollowing();
				liveCouponAdapter.notifyDataSetChanged();
				swipeRefreshLayout.setRefreshing(false);
				if (allCoupons.size() == noOfCoupons) {
					canLoadMore = false;
				} else {
					canLoadMore = true;
					noOfCoupons = allCoupons.size();
				}
				Log.e("Data", "Loaded Successfully");
			}
		}

	}

}
