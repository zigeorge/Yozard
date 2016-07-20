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
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yozard.pp.R;
import com.yozard.pp.fragments.AllCouponFragment;
import com.yozard.pp.fragments.HappyHourFragment;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;
import com.yozard.pp.utils.TypeFace_MY;

public class SavedCouponsActivity extends ActionBarActivity {

	// Typefaces
	Typeface tf_roboto_condensed, tf_roboto, tf_roboto_thin;

	// UI Components
	TextView tvLiveCoupon, tvHappyHour; // , btnArchieved;
	View vUnderLineLiveCoupon, vUnderLineHappyHour;// , vUnderLineArchieved;
	ViewPager vpPager;

	// Datasets
	Vector<Coupon> allCoupons;

	MyPagerAdapter adapterViewPager;

	ProgressDialog progressDialog;
	Editor editor;
	SharedPreferences registration_preference;
	String base_url, userId_sp, auth_token;

	public static boolean isSavedHappyHour = false;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_coupons);

		context = this;
		isSavedHappyHour = false;
		TabActivity.show_search();
		tf_roboto_condensed = TypeFace_MY.getRoboto_condensed(context);
		tf_roboto = TypeFace_MY.getRoboto(context);
		tf_roboto_thin = TypeFace_MY.getRoboto_condensed(context);
		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);

		getAllSavedCoupons();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		HashStatic.HASH_SEARCH_SAVED = true;
		if (UseCouponActivity.isUsed) {
			Log.e("Is Live", "LIVE");
			getAllSavedCoupons();
		}

		if (!isSavedHappyHour) {
			HashStatic.HASH_SEARCH_LIVE = true;
		} else {
			HashStatic.HASH_SEARCH_LIVE = false;
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		HashStatic.HASH_SEARCH_SAVED = false;
		super.onPause();
	}

	private void initUI() {
		tvLiveCoupon = (TextView) findViewById(R.id.tvLiveCoupon);
		tvLiveCoupon.setTextColor(Color.WHITE);
		tvLiveCoupon.setTypeface(tf_roboto_condensed);
		vUnderLineLiveCoupon = (View) findViewById(R.id.vUnderLineLivecoupon);
		tvLiveCoupon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				vpPager.setCurrentItem(0);
			}
		});
		tvHappyHour = (TextView) findViewById(R.id.tvHappyHour);
		tvHappyHour.setTypeface(tf_roboto_condensed);
		vUnderLineHappyHour = (View) findViewById(R.id.vUnderLineHappyHour);
		tvHappyHour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				vpPager.setCurrentItem(1);
			}
		});
		vpPager = (ViewPager) findViewById(R.id.pagerLiveCoupon);
		vpPager.setOffscreenPageLimit(2);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				changeButtonTabBG(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		if (isSavedHappyHour) {
			vpPager.setCurrentItem(1);
		}

	}

	private void changeButtonTabBG(int position) {
		switch (position) {
		case 0:
			HashStatic.HASH_SEARCH_LIVE = true;
			tvLiveCoupon.setSelected(true);
			tvHappyHour.setSelected(false);
			vUnderLineLiveCoupon.setVisibility(View.VISIBLE);
			vUnderLineHappyHour.setVisibility(View.GONE);
			break;
		case 1:
			HashStatic.HASH_SEARCH_LIVE = false;
			tvHappyHour.setSelected(true);
			tvLiveCoupon.setSelected(false);
			vUnderLineHappyHour.setVisibility(View.VISIBLE);
			vUnderLineLiveCoupon.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {
		private int NUM_ITEMS = 2;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				return new AllCouponFragment(HashStatic.HASH_SAVEDCOUPONS);
			case 1: // Fragment # 0 - This will show FirstFragment different
				return new HappyHourFragment(HashStatic.HASH_SAVEDCOUPONS);
			default:
				return null;
			}
		}
	}

	private void getAllSavedCoupons() {
		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		Log.e("ID >> >> ", userId_sp);

		new GetAllSavedCouponsAsync().execute(profileMap);

	}

	public class GetAllSavedCouponsAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		int cust_status, cust_Id;
		String message = "";
		String customer_email = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading Saved Coupons...");
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

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				editor = registration_preference.edit();

				try {

					String url_select3 = baseUrl
							+ BaseURL.API
							+ "saved-coupons.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + userId
									+ "\"}", "utf-8");
					Log.e("JSON String >> ", "{\"customer_id\":\"" + userId
							+ "\"}");

					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"GET", paramiters);
					if (jobj != null) {
						message = jobj.getString("message");
						System.out.println(url_select3);
						editor.putString(HashStatic.HASH_SAVEDCOUPONS,
								jobj.toString()).commit();
					}

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
			if (TextUtils.isEmpty(message)) {

				MyToast.makeToast(
						"Problem occured! Please reload the application",
						context);

			} else if (message.contains("success")) {

				UseCouponActivity.isUsed = false;
				initUI();
				Log.e("Data", "Loaded Successfully");
			} else {
				MyToast.makeToast(message, context);
			}
		}

	}

}
