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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UseCouponActivity extends ActionBarActivity {

	EditText etOrderAmount;
	Button btnUseNow, btnCancel;
	TextView tvInfoTitle, tvInstructionDetails;
	ImageView ivInstructionExpander, ivStoreInfoExpander;
	ListView lvStores;

	SharedPreferences registration_preference;
	String base_url, userId_sp;
	Vector<Stores> stores;
	StoresAdapter storesAdapter;
	String currency = "", orderAmount = "", auth_token;

	public static Coupon usableCoupon = null;
	public static Coupon usableHCoupon = null;
	public static boolean isUsed = false;

	boolean isInstructionExpand = false;
	boolean isStoreInfoExpand = false;
	boolean isLive = true;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_coupon);
		context = this;

		getSupportActionBar().setTitle("USE COUPON IN-STORE");
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.orange1));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		isLive = getIntent().getBooleanExtra(HashStatic.HASH_LIVE, true);

		setCurrency();
		initUI();
		getStoreInformation();
		setInformations();
	}

	private void initUI() {
		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		etOrderAmount = (EditText) findViewById(R.id.etOrderAmount);
		etOrderAmount.setHint(currency + "0.00");
//		etOrderAmount.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				// etOrderAmount.setText(etOrderAmount.getText()+currency);
////				orderAmount = s.toString();
////				etOrderAmount.setText(currency + s);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
////				etOrderAmount.setText(currency);
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				etOrderAmount.setText(currency+etOrderAmount.getText());
//			}
//
//		});
//		etOrderAmount.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(TextUtils.isEmpty(orderAmount)){
//					etOrderAmount.setText(currency);
//				}
//			}
//		});
		/*(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					if(TextUtils.isEmpty(orderAmount)){
						etOrderAmount.setText(currency);
					}
				}else{
					orderAmount = etOrderAmount.getText().toString().substring(1);
				}
			}
		});*/

		tvInfoTitle = (TextView) findViewById(R.id.tvInfoTitle);
		tvInstructionDetails = (TextView) findViewById(R.id.tvInstructionDetails);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnUseNow = (Button) findViewById(R.id.btnUseNow);
		ivInstructionExpander = (ImageView) findViewById(R.id.ivInstructionExpander);
		ivStoreInfoExpander = (ImageView) findViewById(R.id.ivStoreInfoExpander);
		lvStores = (ListView) findViewById(R.id.lvStores);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnUseNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(etOrderAmount.getText())) {
					orderAmount = etOrderAmount.getText().toString();
					etOrderAmount.setText(currency+orderAmount);
					useCoupon(orderAmount);
				} else {
					etOrderAmount.setText(currency + "0.00");
					useCoupon("0");
				}
			}
		});

		ivInstructionExpander.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInstructionExpand) {
					isInstructionExpand = false;
					tvInstructionDetails.setVisibility(View.GONE);
					ivInstructionExpander
							.setImageResource(R.drawable.expander_open_holo_light);
				} else {
					isInstructionExpand = true;
					tvInstructionDetails.setVisibility(View.VISIBLE);
					ivInstructionExpander
							.setImageResource(R.drawable.expander_close_holo_light);
				}
			}
		});

		ivStoreInfoExpander.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isStoreInfoExpand) {
					isStoreInfoExpand = false;
					lvStores.setVisibility(View.GONE);
					ivStoreInfoExpander
							.setImageResource(R.drawable.expander_open_holo_light);
				} else {
					isStoreInfoExpand = true;
					lvStores.setVisibility(View.VISIBLE);
					ivStoreInfoExpander
							.setImageResource(R.drawable.expander_close_holo_light);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onResume();
	}

	private void setCurrency() {
		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String country = registration_preference.getString(
				HashStatic.HASH_country, null);
		currency = BaseURL.selectCurrency(country);
	}

	private void getStoreInformation() {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		if (isLive) {
			profileMap.put(HashStatic.CAMPAIGN_ID, usableCoupon.getLcc_id());
		} else {
			profileMap.put(HashStatic.CAMPAIGN_ID, usableHCoupon.getHhc_id());
		}
		new GetStoreAsync().execute(profileMap);
	}

	private void setInformations() {
		Offer anOffer = null;
		String campaignTitle = "";
		if (isLive) {
			anOffer = usableCoupon.getCoupon_offer();
			tvInstructionDetails.setText(usableCoupon.getLcc_instruction());
			campaignTitle = usableCoupon.getCampaign_title();
		} else {
			anOffer = usableHCoupon.gethOffer();
			tvInstructionDetails.setText(usableHCoupon.getInstruction());
			campaignTitle = usableHCoupon.getCampaign_title();
		}
		if (anOffer.getOffer_type().equalsIgnoreCase("discounts")) {
			tvInfoTitle.setText(campaignTitle);
		} else {
			tvInfoTitle.setText(campaignTitle);
		}
	}

	private void useCoupon(String orderAmount) {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		if (isLive) {
			profileMap.put(HashStatic.CAMPAIGN_ID, usableCoupon.getLcc_id());
		} else {
			profileMap.put(HashStatic.CAMPAIGN_ID, usableHCoupon.getHhc_id());
		}
		profileMap.put(HashStatic.ORDER_AMOUNT, orderAmount);
		Log.e("ID >> >> ", userId_sp);

		new UseCouponAsync().execute(profileMap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.use_coupon, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
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

				try {

					String url_select3 = "";
					if (isLive) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "stores.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
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
										.encode(auth_token, "utf-8")
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

	public class UseCouponAsync extends
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
			progressDialog.setMessage("Using Coupons...");
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
				String order_amount = parametersList
						.get(HashStatic.ORDER_AMOUNT);

				try {

					String url_select3 = "";
					if (isLive) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "live-coupons.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"campaign_id\":\""
										+ campaign_id
										+ "\",\"order_amount\":\""
										+ order_amount + "\"}", "utf-8");
					} else {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "happy-hours.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ userId + "\",\"campaign_id\":\""
										+ campaign_id
										+ "\",\"order_amount\":\""
										+ order_amount + "\"}", "utf-8");
					}
					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"PUT", paramiters);

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
			Log.e("Data", "Loaded Successfully");
			MyToast.makeToast(message, context);
			if (message.contains("success")) {
				isUsed = true;
				if (isLive) {
					LiveCouponActivity.isHappyHour = false;
					SavedCouponsActivity.isSavedHappyHour = false;
				} else {
					LiveCouponActivity.isHappyHour = true;
					SavedCouponsActivity.isSavedHappyHour = true;
				}
				finish();
			}
		}

	}
}
