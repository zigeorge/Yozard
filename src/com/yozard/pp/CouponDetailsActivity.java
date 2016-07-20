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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yozard.pp.R;
import com.yozard.pp.adapter.StoresAdapter;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Offer;
import com.yozard.pp.model.StoreResponse;
import com.yozard.pp.model.Stores;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;

public class CouponDetailsActivity extends ActionBarActivity {

	// View Items
	TextView tvDetailsTitle, tvInstructionDetails;
	Button btnSave, btnBack;
	ListView lvStores;

	SharedPreferences registration_preference;
	String base_url, userId_sp, auth_token;
	Vector<Stores> stores;
	StoresAdapter storesAdapter;

	public static Coupon coupon = null;
	public static Coupon hCoupon = null;
	public static boolean isSaved = false;

	boolean isLive = true;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_details);
		context = this;

		getSupportActionBar().setTitle("Save First");
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.orange1));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		isLive = getIntent().getBooleanExtra(HashStatic.HASH_LIVE, true);

		initUI();

		getStoreInformation();

	}

	private void initUI() {
		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);

		tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
		tvInstructionDetails = (TextView) findViewById(R.id.tvInstructionDetails);
		lvStores = (ListView) findViewById(R.id.lvStores);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveCoupon();
			}
		});

		setInformations();
	}

	private void setInformations() {
		Offer anOffer = null;
		String campaignTitle = "";
		if (isLive) {
			anOffer = coupon.getCoupon_offer();
			campaignTitle = coupon.getCampaign_title();
			tvInstructionDetails.setText(coupon.getLcc_instruction());
		} else {
			anOffer = hCoupon.gethOffer();
			campaignTitle = hCoupon.getCampaign_title();
			tvInstructionDetails.setText(hCoupon.getInstruction());
		}
		if (anOffer.getOffer_type().equalsIgnoreCase("discounts")) {
			tvDetailsTitle.setText(campaignTitle);
		} else {
			tvDetailsTitle.setText(campaignTitle);
		}
	}

	private void getStoreInformation() {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		if (isLive) {
			profileMap.put(HashStatic.CAMPAIGN_ID, coupon.getLcc_id());
		} else {
			profileMap.put(HashStatic.CAMPAIGN_ID, hCoupon.getHhc_id());
		}
		new GetStoreAsync().execute(profileMap);
	}

	private void saveCoupon() {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		if (isLive) {
			profileMap.put(HashStatic.CAMPAIGN_ID, coupon.getLcc_id());
		} else {
			profileMap.put(HashStatic.CAMPAIGN_ID, hCoupon.getHhc_id());
		}
		Log.e("ID >> >> ", userId_sp);

		new BookCouponAsync().execute(profileMap);
	}

	public class GetStoreAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		String message = "";
		StoreResponse storeResponse;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String campaign_id = parametersList.get(HashStatic.CAMPAIGN_ID);

				Log.e("Follow Request >>", "{\"campaign_id\":\"" + campaign_id
						+ "\",\"campaign_type\":\"live_coupon\"}");

				try {
					String url_select3 = "";
					if (isLive) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "stores.php?authtoken="
								+ URLEncoder
										.encode(auth_token,
												"utf-8")
								+ "&JSONParam="
								+ URLEncoder
										.encode("{\"campaign_id\":\""
												+ campaign_id
												+ "\",\"campaign_type\":\"live_coupon\"}",
												"utf-8");
					} else {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "stores.php?authtoken="
								+ URLEncoder
										.encode(auth_token,
												"utf-8")
								+ "&JSONParam="
								+ URLEncoder
										.encode("{\"campaign_id\":\""
												+ campaign_id
												+ "\",\"campaign_type\":\"happy_hour\"}",
												"utf-8");
					}

					JSONObject jobj;

					jobj = jparser.makeHttpRequest(url_select3, "POST",
							paramiters);

					Gson g = new Gson();
					storeResponse = g.fromJson(jobj.toString(),
							StoreResponse.class);

					message = jobj.getString("message");
					// Log.e("Message", message);
					System.out.println(url_select3);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
//				Toast.makeText(context, "Network connection not available.",
//						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			// MyToast.makeToast(message, context);
			if (message.contains("success")) {
				// tvStoreDetails.setText(text);
				stores = storeResponse.getStores();
				storesAdapter = new StoresAdapter(context, R.layout.stores_row,
						stores);
				lvStores.setAdapter(storesAdapter);
			}
		}

	}

	public class BookCouponAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		String message = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Claiming Coupons...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String userId = parametersList.get(HashStatic.CUSTOMER_ID);
				String campaign_id = parametersList.get(HashStatic.CAMPAIGN_ID);

				try {

					String url_select3 = "";
					if (isLive) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "live-coupons.php?authtoken="
								+ URLEncoder
										.encode(auth_token,
												"utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"campaign_id\":\""
										+ campaign_id + "\"}", "utf-8");
					} else {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "happy-hours.php?authtoken="
								+ URLEncoder
										.encode(auth_token,
												"utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"campaign_id\":\""
										+ campaign_id + "\"}", "utf-8");
					}
					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"POST", paramiters);

					message = jobj.getString("message");

					System.out.println(url_select3);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
//				Toast.makeText(context, "Network connection not available.",
//						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			Log.e("Data", "Loaded Successfully");
			MyToast.makeToast(message, context);
			if (message.contains("success")) {
				if(isLive){
					LiveCouponActivity.isHappyHour = false;
				}else{
					LiveCouponActivity.isHappyHour = true;
				}
				isSaved = true;
				finish();
			}
		}

	}
}
