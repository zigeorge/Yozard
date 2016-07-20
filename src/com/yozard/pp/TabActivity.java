package com.yozard.pp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yozard.pp.adapter.HintsAdapter;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.HappyHourResponse;
import com.yozard.pp.model.LiveCouponResponse;
import com.yozard.pp.model.SavedCouponResponse;
import com.yozard.pp.pushNotification.AlertDialogManager;
import com.yozard.pp.pushNotification.CommonUtilities;
import com.yozard.pp.pushNotification.ConnectionDetector;
import com.yozard.pp.pushNotification.ServerUtilities;
import com.yozard.pp.pushNotification.WakeLocker;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.Swipable_linearLayout;
import com.yozard.pp.utils.TypeFace_MY;

@SuppressWarnings("deprecation")
public class TabActivity extends android.app.TabActivity {

	TabHost tabHost;
	private final String TAG_COUPON = "Coupons";
	private final String TAG_SAVED = "Saved Coupons";
	private final String TAG_MORE = "Zard";
	private final String TAG_MYPROFILE = "Profile";
	private final String TAG_INSTRUCTION = "Instructions";
	private final String TAG_RESTAURANT = "Restaurants";

	public int WHICH_TAB_SELECTED = 1;
	Typeface tf_rancho;
	// int lastTab = 0;
	public static ImageButton search_bt;
	EditText etSearch;
	ProgressBar progressBar_search;
	AutoCompleteTextView actvSearch;
	TextView tvTitleRight; // title_tv, title_footer_tv,
	ImageView ivSignout, ivCloseSearch, ivActionSearch;
	RelativeLayout rlSearchContainer;

	boolean signoutSelected = false;
	LinearLayout msgLinear;
	public static boolean searchHidden = true;
	public static boolean msgLinearhHidden = true;
	public boolean msgLinearhHidden_reload = true;
	IntentFilter filter_network;
	TextView error_message_tv;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private int count = 0;

	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	ConnectionDetector cd;
	GoogleCloudMessaging gcm;
	String regId;

	SharedPreferences registration_preference;
	String auth_token;
	GetSearchAsync searchAsyncTask;
	public boolean isExecuting = false;
	Context context;

	private BroadcastReceiver NetworkBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(TabActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				hideThatMsg();
			} else {
				showThatMsg("Internet Connection not available");
			}
		}
	};

	/** This static method is for LiveCouponActivity and SavedCouponActivity **/
	public static void show_search() {
		search_bt.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.custom_title);
		context = this;

		tf_rancho = TypeFace_MY.getRancho(context);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setTintColor(Color.parseColor("#ff8c00"));
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.drawable.orange);
		}

		registration_preference = this.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(
				HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		String email_sp = registration_preference.getString(
				HashStatic.HASH_email, null);

		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.upload)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		initUI();

		setUpTab();

		if (HashStatic.signUp_byEmail) {
			HashStatic.signUp_byEmail = false;
			// makeToast();
			Veryfiy_alert();
		}

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				tabChanged(tabId);
			}
		});

		GCM_Setup(this, email_sp, userId_sp);

	}

	private void initUI() {
		error_message_tv = (TextView) findViewById(R.id.errormsg_tv);
		filter_network = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		msgLinear = (LinearLayout) findViewById(R.id.msgLinear);
		msgLinear.setOnTouchListener(new Swipable_linearLayout(this,
				TabActivity.this));
		search_bt = (ImageButton) findViewById(R.id.searchButton_title);
		search_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchbuttonClicked();
			}
		});

		ivActionSearch = (ImageView) findViewById(R.id.ivActionSearch);
		progressBar_search = (ProgressBar) findViewById(R.id.progressBar_search);
		tvTitleRight = (TextView) findViewById(R.id.tvTitleRight);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setTypeface(tf_rancho);
		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.addTextChangedListener(searchTextWatcher);
		actvSearch = (AutoCompleteTextView) findViewById(R.id.actvSearch);
		HintsAdapter hintsAdapter = new HintsAdapter(this);
		actvSearch.setThreshold(0);
		actvSearch.setAdapter(hintsAdapter);
		actvSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				searchResultClicked(position);
			}
		});
		rlSearchContainer = (RelativeLayout) findViewById(R.id.rlSearchContainer);
		rlSearchContainer.setVisibility(View.GONE);
		ivCloseSearch = (ImageView) findViewById(R.id.ivCloseSearch);
		ivCloseSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancelSearch();
			}
		});
		ivSignout = (ImageView) findViewById(R.id.ivSignout);
		ivSignout.setVisibility(View.GONE);
		ivSignout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MoreActivity moreActivity = (MoreActivity) getLocalActivityManager()
						.getActivity(TAG_MORE);
				moreActivity.signOut();
			}
		});
	}

	TextWatcher searchTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			actvSearch.setText("");
			if (!TextUtils.isEmpty(s)) {
				if (!isExecuting) {
					initializeSearch(s.toString());
				} else {
					searchAsyncTask.cancel(true);
					initializeSearch(s.toString());
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}
	};

	private void searchbuttonClicked() {
		if (WHICH_TAB_SELECTED == 1) {
			// LiveCouponActivity liveCouponActivity = (LiveCouponActivity)
			// getLocalActivityManager()
			// .getActivity(TAG_COUPON);
			setSearchView();
		} else if (WHICH_TAB_SELECTED == 2) {
			// SavedCouponsActivity saveCouponActivity = (SavedCouponsActivity)
			// getLocalActivityManager()
			// .getActivity(TAG_SAVED);
			setSearchView();
		}
	}

	private void searchResultClicked(int position) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		Coupon aCoupon = HashStatic.hintsList.get(position);
		if (WHICH_TAB_SELECTED == 1) {
			LiveCouponActivity liveCouponActivity = (LiveCouponActivity) getLocalActivityManager()
					.getActivity(TAG_COUPON);
			goToCouponDetails(liveCouponActivity, aCoupon);

		} else if (WHICH_TAB_SELECTED == 2) {
			SavedCouponsActivity saveCouponActivity = (SavedCouponsActivity) getLocalActivityManager()
					.getActivity(TAG_SAVED);
			goToCouponDetails(saveCouponActivity, aCoupon);
		}
	}

	private void setUpTab() {
		Resources ressources = getResources();
		tabHost = getTabHost();

		Intent intentLiveCoupon = new Intent().setClass(this,
				LiveCouponActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab1_selector),
				TAG_COUPON, intentLiveCoupon);

		Intent intentSavedCoupon = new Intent().setClass(this,
				SavedCouponsActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab2_selector),
				TAG_SAVED, intentSavedCoupon);

		Intent intentMyProfile = new Intent().setClass(this,
				MyProfileTabActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab3_selector),
				TAG_MYPROFILE, intentMyProfile);
		
		Intent intentRestaurants = new Intent().setClass(this,
				RestaurantsActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab5_selector),
				TAG_RESTAURANT, intentRestaurants);

		Intent intentInstructions = new Intent().setClass(this,
				UserGuideActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab4_selector),
				TAG_INSTRUCTION, intentInstructions);

		Intent intentMore = new Intent().setClass(this, MoreActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setAndAddTabSpec(ressources.getDrawable(R.drawable.tab6_selector),
				TAG_MORE, intentMore);

		// set Windows tab as default (zero based)
		if (!HashStatic.firstTimeLogin) {
			tabHost.setCurrentTab(0);
			WHICH_TAB_SELECTED = 1;
		}
		setUpTabWidget();
	}

	private void setAndAddTabSpec(Drawable drawable, String tabTag,
			Intent intent) {
		TabSpec tapSpec = tabHost.newTabSpec(tabTag).setIndicator("", drawable)
				.setContent(intent);
		tabHost.addTab(tapSpec);
	}

	private void setUpTabWidget() {
		TabWidget widget = tabHost.getTabWidget();
		for (int i = 0; i < widget.getChildCount(); i++) {
			View v = widget.getChildAt(i);

			TextView tv = (TextView) v.findViewById(android.R.id.title);
			if (tv == null) {
				continue;
			}
			v.setBackgroundResource(R.drawable.tab_selector);
		}

		/* */
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).setPadding(0, 5, 0, 5);
		}
	}

	private void tabChanged(String tabId) {
		tvTitleRight.setText(tabId);
		tvTitleRight.setTypeface(tf_rancho);
		search_bt.setImageResource(R.drawable.action_search);
		tvTitleRight.setVisibility(View.VISIBLE);
		rlSearchContainer.setVisibility(View.GONE);
		ivSignout.setVisibility(View.GONE);
		search_bt.setVisibility(View.INVISIBLE);
		if (TAG_COUPON.equals(tabId)) {
			search_bt.setVisibility(View.VISIBLE);
			WHICH_TAB_SELECTED = 1;
		} else if (TAG_SAVED.equals(tabId)) {
			search_bt.setVisibility(View.VISIBLE);
			WHICH_TAB_SELECTED = 2;
		} else if (TAG_MYPROFILE.equals(tabId)) {
			WHICH_TAB_SELECTED = 3;
		} else if (TAG_INSTRUCTION.equals(tabId)) {
			UserGuideActivity.isFromMore = false;
			WHICH_TAB_SELECTED = 4;
		} else if (TAG_MORE.equals(tabId)) {
			ivSignout.setVisibility(View.VISIBLE);
			WHICH_TAB_SELECTED = 5;
		}else if (TAG_RESTAURANT.equals(tabId)){
			search_bt.setVisibility(View.VISIBLE);
			WHICH_TAB_SELECTED = 1;
		}
	}

	public void GCM_Setup(Context ctx, final String email, final String user_id) {
		// Check if Internet present

		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(ctx, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);
		System.out.println(regId + "");

		String checkSharepref_regId = registration_preference.getString(
				HashStatic.Hash_GCMID, null);

		if (!regId.equals("")) {
			if (registration_preference != null)
				registration_preference.edit().putString(HashStatic.Hash_GCMID,
						regId);
		}

		System.out.println("RegID: " + regId);
		// Check if regid already presents
		if (regId.equals("")) {
			final Context context = this;
			mRegisterTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						if (gcm == null) {
							gcm = GoogleCloudMessaging
									.getInstance(getApplicationContext());
						}
						regId = gcm.register(CommonUtilities.SENDER_ID);

					} catch (IOException ex) {

					}
					System.out.println("onRegistration 2: " + regId);
					ServerUtilities.register(context, email, regId, user_id);

					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mRegisterTask = null;
				}

			};
			mRegisterTask.execute(null, null, null);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				if (checkSharepref_regId == null) {
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user
							System.out.println("onRegistration 2: " + regId);
							ServerUtilities.register(context, email, regId,
									user_id);

							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					mRegisterTask.execute(null, null, null);
				}

			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						System.out.println("onRegistration 2: " + regId);
						ServerUtilities
								.register(context, email, regId, user_id);

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	public Context getInstance() {
		return context;
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			String title = intent.getExtras().getString("title");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			// Log.e("OnRecieved", "Got new massage");
			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			Bundle bundle = intent.getExtras();
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				Log.d("BR", String.format("%s %s (%s)", key, value.toString(),
						value.getClass().getName()));
			}

			System.out.println("On Broadcast1:" + newMessage);

			if (title != null) {
				doIncreaseCount_notification();
			}
			// Releasing wake lock
			WakeLocker.release();
		}
	};

	public void doIncreaseCount_notification() {
		count++;
		invalidateOptionsMenu();
	}

	private void setSearchView() {
		searchHidden = false;
		etSearch.setText("");
		tvTitleRight.setTypeface(tf_rancho);
		tvTitleRight.setVisibility(View.GONE);
		rlSearchContainer.setVisibility(View.VISIBLE);
		search_bt.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (etSearch.requestFocus()) {
			imm.showSoftInput(etSearch, InputMethodManager.SHOW_FORCED);
		}
	}

	private void cancelSearch() {
		progressBar_search.setVisibility(View.GONE);
		ivActionSearch.setVisibility(View.VISIBLE);
		tvTitleRight.setVisibility(View.VISIBLE);
		rlSearchContainer.setVisibility(View.GONE);
		search_bt.setVisibility(View.VISIBLE);
		etSearch.setText("");
		actvSearch.setText("");
		if (isExecuting) {
			searchAsyncTask.cancel(true);
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	public void Veryfiy_alert() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Verify your email address");
		alertDialog
				.setMessage("An email with verification link has been sent to your registered email address. Verify now to start doing the following,\n* Submit code to Save and Win\n* Redeem Balance");
		alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				alertDialog.dismiss();
			}
		});
		// alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.out.println("Done1");
		if (WHICH_TAB_SELECTED == 1) {
			cancelSearch();
			if (searchHidden) {
				super.onBackPressed();
			}
		} else if (WHICH_TAB_SELECTED == 2) {
			cancelSearch();
			if (searchHidden) {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	// ////////////these two functions
	public void showThatMsg(String msg) {
		if (msgLinearhHidden) {
			Animation anim = new TranslateAnimation(0, 0, -200, 0);
			// anim.setFillAfter(true);
			anim.setDuration(800);
			msgLinear.setAnimation(anim);
			msgLinear.setVisibility(View.VISIBLE);
			msgLinearhHidden = false;
			error_message_tv.setText(msg);
		}
	}

	public void hideThatMsg() {
		if (!msgLinearhHidden) {
			Animation anim = new TranslateAnimation(0, 0, 0, -200);
			// anim.setFillAfter(true);
			anim.setDuration(800);
			msgLinear.setAnimation(anim);
			msgLinear.setVisibility(View.GONE);
			msgLinearhHidden = true;

		}
	}

	public void hideThatMsgLeft() {
		if (!msgLinearhHidden) {
			Animation anim = new TranslateAnimation(0, -200, 0, 0);
			// anim.setFillAfter(true);
			anim.setDuration(50);
			msgLinear.setAnimation(anim);
			msgLinear.setVisibility(View.GONE);
			msgLinearhHidden = true;
			error_message_tv.setText("");
		}
	}

	public void hideThatMsgRight() {
		if (!msgLinearhHidden) {
			Animation anim = new TranslateAnimation(0, 200, 0, 0);
			// anim.setFillAfter(true);
			anim.setDuration(50);
			msgLinear.setAnimation(anim);
			msgLinear.setVisibility(View.GONE);
			msgLinearhHidden = true;
			error_message_tv.setText("");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		if (NetworkBroadcastReceiver != null)
			unregisterReceiver(NetworkBroadcastReceiver);

		if (imageLoader != null)
			imageLoader.clearDiscCache();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(NetworkBroadcastReceiver, filter_network);
	}

	@SuppressWarnings("unchecked")
	private void initializeSearch(String key) {
		progressBar_search.setVisibility(View.VISIBLE);
		ivActionSearch.setVisibility(View.INVISIBLE);
		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		profileMap.put(HashStatic.HASH_KEY, key);
		searchAsyncTask = new GetSearchAsync();
		searchAsyncTask.execute(profileMap);
	}

	private void goToCouponDetails(Context con, Coupon aCoupon) {
		if (HashStatic.HASH_SEARCH_SAVED) {
			if (HashStatic.HASH_SEARCH_LIVE) {
				UseCouponActivity.usableCoupon = aCoupon;
				con.startActivity(new Intent(con, UseCouponActivity.class)
						.putExtra(HashStatic.HASH_LIVE, true));
			} else {
				UseCouponActivity.usableHCoupon = aCoupon;
				con.startActivity(new Intent(con, UseCouponActivity.class)
						.putExtra(HashStatic.HASH_LIVE, false));
			}
		} else {
			if (aCoupon.getCoupon_status().equalsIgnoreCase("SAVE")) {
				if (HashStatic.HASH_SEARCH_LIVE) {
					CouponDetailsActivity.coupon = aCoupon;
					con.startActivity(new Intent(con,
							CouponDetailsActivity.class).putExtra(
							HashStatic.HASH_LIVE, true));
				} else {
					CouponDetailsActivity.hCoupon = aCoupon;
					con.startActivity(new Intent(con,
							CouponDetailsActivity.class).putExtra(
							HashStatic.HASH_LIVE, false));
				}
			} else {
				if (HashStatic.HASH_SEARCH_LIVE) {
					UseCouponActivity.usableCoupon = aCoupon;
					con.startActivity(new Intent(con, UseCouponActivity.class)
							.putExtra(HashStatic.HASH_LIVE, true));
				} else {
					UseCouponActivity.usableHCoupon = aCoupon;
					con.startActivity(new Intent(con, UseCouponActivity.class)
							.putExtra(HashStatic.HASH_LIVE, false));
				}
			}
		}
	}

	public class GetSearchAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		String message = "";
		LiveCouponResponse liveCouponResponse;
		HappyHourResponse happyHourResponse;
		SavedCouponResponse savedCouponResponse;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar_search.setVisibility(View.VISIBLE);
			ivActionSearch.setVisibility(View.INVISIBLE);
			isExecuting = true;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String user_id = parametersList.get(HashStatic.CUSTOMER_ID);
				String key_word = parametersList.get(HashStatic.HASH_KEY);

				try {
					String url_select3 = "";
					if (HashStatic.HASH_SEARCH_SAVED) {
						url_select3 = baseUrl
								+ BaseURL.API
								+ "saved-coupons.php?authtoken="
								+ URLEncoder.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"customer_id\":\""
										+ user_id + "\",\"keyword\":\""
										+ key_word + "\"}", "utf-8");
					} else {
						if (HashStatic.HASH_SEARCH_LIVE) {
							url_select3 = baseUrl
									+ BaseURL.API
									+ "live-coupons.php?authtoken="
									+ URLEncoder.encode(auth_token, "utf-8")
									+ "&JSONParam="
									+ URLEncoder.encode("{\"customer_id\":\""
											+ user_id + "\",\"keyword\":\""
											+ key_word + "\"}", "utf-8");
						} else {
							url_select3 = baseUrl
									+ BaseURL.API
									+ "happy-hours?authtoken="
									+ URLEncoder.encode(auth_token, "utf-8")
									+ "&JSONParam="
									+ URLEncoder.encode("{\"customer_id\":\""
											+ user_id + "\",\"keyword\":\""
											+ key_word + "\"}", "utf-8");
						}
					}

					JSONObject jobj;
					jobj = jparser.makeHttpRequest(url_select3, "GET",
							paramiters);

					Gson g = new Gson();
					if (jobj != null) {
						if (HashStatic.HASH_SEARCH_SAVED) {
							savedCouponResponse = g.fromJson(jobj.toString(),
									SavedCouponResponse.class);
						} else {
							if (HashStatic.HASH_SEARCH_LIVE) {
								liveCouponResponse = g.fromJson(
										jobj.toString(),
										LiveCouponResponse.class);
							} else {
								happyHourResponse = g.fromJson(jobj.toString(),
										HappyHourResponse.class);
							}
						}
					}
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
			isExecuting = false;
			progressBar_search.setVisibility(View.GONE);
			ivActionSearch.setVisibility(View.VISIBLE);
			// MyToast.makeToast(message, context);
			if (message.contains("success")) {
				if (HashStatic.HASH_SEARCH_SAVED) {
					if (HashStatic.HASH_SEARCH_LIVE) {
						HashStatic.hintsList = savedCouponResponse
								.getSaved_coupons().getLive_coupon();
					} else {
						HashStatic.hintsList = savedCouponResponse
								.getSaved_coupons().getHappy_hour();
					}
				} else {
					if (HashStatic.HASH_SEARCH_LIVE) {
						HashStatic.hintsList = liveCouponResponse
								.getLc_campaign().getAllCoupons();
					} else {
						HashStatic.hintsList = happyHourResponse
								.getHh_campaign().getAllHCoupon();
					}
				}
				if (HashStatic.hintsList.size() > 0) {
					actvSearch.setText(etSearch.getText());
					actvSearch.showDropDown();
				} else {
					actvSearch.dismissDropDown();
				}

			}
		}

	}

}
