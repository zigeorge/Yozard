package com.yozard.pp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yozard.pp.R;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

public class MoreActivity extends Activity {

	Context context;

	// View Items
	CircularImageView civProfileImage;
	TextView tvProfileName, tvEmail, tvEdit;
	LinearLayout llProfileDetail, llSettings,
			llInviteFriends, llAbout, llSignout, llInstructions;	//,llMyProfile llInstoreCoupons, llEnterCode,
	RelativeLayout rlCouponExReminder;
	ToggleButton tbCouponExpReminder;

	boolean isSettingsOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		context = this;

		initUI();

		setData();
	}

	private void initUI() {
		civProfileImage = (CircularImageView) findViewById(R.id.civProfileImage);
		tvProfileName = (TextView) findViewById(R.id.tvProfileName);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEdit = (TextView) findViewById(R.id.tvEdit);
		tvEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent profileIntent = new Intent(context,
						MyProfileActivity.class);
				profileIntent.putExtra(MyProfileActivity.EDITABLE, true);
				startActivity(profileIntent);
			}
		});
		llProfileDetail = (LinearLayout) findViewById(R.id.llProfileDetail);
		// llMyProfile = (LinearLayout) findViewById(R.id.llMyProfile);
		// llMyProfile.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent profileIntent = new Intent(context,
		// MyProfileActivity.class);
		// profileIntent.putExtra(MyProfileActivity.EDITABLE, false);
		// startActivity(profileIntent);
		// }
		// });
//		llInstoreCoupons = (LinearLayout) findViewById(R.id.llInstoreCoupons);
//		llInstoreCoupons.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// MyToast.makeToast("In Store Coupons", context);
//				startActivity(new Intent(context, DiscountCouponActivity.class));
//			}
//		});
		llInstructions = (LinearLayout) findViewById(R.id.llInstructions);
		llInstructions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserGuideActivity.isFromMore = true;
				startActivity(new Intent(context, UserGuideActivity.class));
			}
		});
		llSettings = (LinearLayout) findViewById(R.id.llSettings);
		rlCouponExReminder = (RelativeLayout) findViewById(R.id.rlCouponExReminder);
		tbCouponExpReminder = (ToggleButton) findViewById(R.id.tbCouponExpReminder);
		tbCouponExpReminder
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							Log.d("Toggle", "IS CHECKED");
						} else {
							Log.d("Toggle", "IS NOT CHECKED");
						}

					}
				});
		llSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSettingsOn) {
					rlCouponExReminder.setVisibility(View.GONE);
					isSettingsOn = false;
				} else {
					rlCouponExReminder.setVisibility(View.VISIBLE);
					isSettingsOn = true;
				}
			}
		});
		/*
		 * llTransactionHistory = (LinearLayout)
		 * findViewById(R.id.llTransactionHistory);
		 * llTransactionHistory.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub MyToast.makeToast("Transaction History", context); } });
		 */
		llInviteFriends = (LinearLayout) findViewById(R.id.llInviteFriends);
		llInviteFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inviteFriends();
			}
		});
		llAbout = (LinearLayout) findViewById(R.id.llAbout);
		llAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, AboutUs_webview.class);
				startActivity(in);
			}
		});
		llSignout = (LinearLayout) findViewById(R.id.llSignout);
		llSignout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				signOut();
			}
		});
	}

	private void inviteFriends() {
		SharedPreferences registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String myText = "Save on everyday shopping online & in-store. Send money or pay bills with your savings - All in one APP. Download the app at";

		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		String extraText = myText + "\n" + base_url + "dl";
		share.putExtra(Intent.EXTRA_TEXT, extraText);
		startActivity(Intent.createChooser(share, "Invite Friends"));
	}

	private void setData() {
		SharedPreferences registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String firstname = registration_preference.getString(
				HashStatic.HASH_firstNAME, "");
		String lastname = registration_preference.getString(
				HashStatic.HASH_LastNAME, "");
		String email = registration_preference.getString(HashStatic.HASH_email,
				"");
		String pro_image = registration_preference.getString(
				HashStatic.HASH_image_url, "");
		String imgUrl = base_url + pro_image;

		tvProfileName.setText(firstname + " " + lastname);
		tvEmail.setText(email);
		if (MyProfileActivity.profile_pic_selected) {
			Log.e("Img Url", pro_image);
			if (TextUtils.isEmpty(pro_image)) {
				civProfileImage.setImageResource(R.drawable.upload);
			} else {
				civProfileImage.setImageURI(Uri.parse(pro_image));
			}
		} else {
			Log.e("Img Url", imgUrl);
			ImageLoader imageLoader = ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.upload)
					.showImageOnFail(R.drawable.upload)
					.showImageOnLoading(R.drawable.upload).build();
			imageLoader.displayImage(imgUrl, civProfileImage, options);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyProfileActivity.profile_pic_selected) {
			SharedPreferences registration_preference = getSharedPreferences(
					HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
			String pro_image = registration_preference.getString(
					HashStatic.HASH_image_url, "");
			Log.e("IMG URL", pro_image);
			if (TextUtils.isEmpty(pro_image)) {
				civProfileImage.setImageResource(R.drawable.upload);
			} else {
				civProfileImage.setImageURI(Uri.parse(pro_image));
			}
			// MyProfileActivity.profile_pic_selected = false;
		}
	}


	public void signOut() {

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.signout_dialog);

		TextView tvYes = (TextView) dialog.findViewById(R.id.tvYes);
		TextView tvNo = (TextView) dialog.findViewById(R.id.tvNo);

		tvYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				new Async_signout_call().execute();
			}
		});

		tvNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	// private void confirmSignout() {
	// FavoutiteDbHelper dbHelper = new FavoutiteDbHelper(context);
	// dbHelper.deleteWholeDatabase(context);
	//
	// SharedPreferences registration_preference = getSharedPreferences(
	// HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
	// String regId =
	// registration_preference.getString(HashStatic.Hash_GCMID,
	// "");
	// // String email_sp = registration_preference.getString(
	// // HashStatic.HASH_email, null);
	// // String userId_sp = registration_preference.getString(
	// // HashStatic.CUSTOMER_ID, null);
	// // ServerUtilities.unregister(context, regId, email_sp, userId_sp);
	// registration_preference.edit().clear().commit();
	//
	// SharedPreferences fpref = getSharedPreferences("PromoRegToken", 0);
	// fpref.edit().clear().commit();
	//
	// TabActivity tab = ((TabActivity) getParent());
	// tab.closeTheActivity2();
	// }

	public class Async_signout_call extends AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		JSONParser jParser = null;
		HashMap<String, String> showmap = new HashMap<String, String>();
		boolean is_signed_out = false;
		SharedPreferences registration_preference;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Logging out...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();

					registration_preference = getSharedPreferences(
							HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
					String email_sp = registration_preference.getString(
							HashStatic.HASH_email, null);
					String base_url = registration_preference.getString(
							HashStatic.HASH_baseUrl, null);
					String userId_sp = registration_preference.getString(
							HashStatic.CUSTOMER_ID, null);
					String regId = registration_preference.getString(
							HashStatic.Hash_GCMID, null);
					String auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);

					String url_select = null;
					try {
						url_select = base_url
								+ BaseURL.API
								+ "notifications.php?authtoken="
								+ URLEncoder
										.encode(auth_token, "utf-8")
								+ "&JSONParam="
								+ URLEncoder.encode("{\"reg_id\":\"" + regId
										+ "\", " + "\"email\":\"" + email_sp
										+ "\"," + "\"user_type\":\""
										+ "customer" + "\"," + "\"user_id\":\""
										+ userId_sp + "\"," + "\"type\":\""
										+ "unregister"

										+ "\"}", "utf-8");
					} catch (UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					System.out.println(url_select);

					jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);
					System.out.println(jobj.toString());

					String message = jobj.getString("message");
					System.out.println(message);
					if (message != null && message.contains("success")) {
						is_signed_out = true;
					}

					System.out.println(jobj.toString());

				} else {
					makeToast("Internet Connection not Available");
				}

			} catch (JSONException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (is_signed_out) {
				registration_preference.edit().clear().commit();
				finish();
				startActivity(new Intent(context, LoginActivity.class));
			} else {
				makeToast("Something went wrong. Please retry");
			}

		}

		public void makeToast(final String str) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					com.yozard.pp.utils.MyToast.makeToast(str, context);
				}
			});
		}

	}
}
