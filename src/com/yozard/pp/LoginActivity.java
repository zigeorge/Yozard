package com.yozard.pp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yozard.pp.R;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.Country_manager;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.TypeFace_MY;

public class LoginActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static final int PROFILE_PIC_SIZE = 400;

	public static final String HASH_firstNAME = "firstname";
	public static final String HASH_email = "email";
	public static final String HASH_date = "birthday";
	public static final String HASH_gender = "gender";
	public static final String HASH_lastname = "lastname";
	public static final String HASH_password = "password";
	public static final String HASH_country = "country";
	public static final String HASH_baseUrl = "baseUrl";
	public static final String HASH_auth = "auth_type";

	public static final String HASH_GOOGLE_pp = "image_link";
	public static final String HASH_GOOGLE_userLink = "user_link";

	Button fb_button, btngplogin;
	SharedPreferences fpref;
	// Instance of Facebook Class
	private boolean mIntentInProgress;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	private static String APP_ID = "578277912272525";

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	String access_token = null;

	private boolean mSignInClicked;
	TextView title_tv;
	private ConnectionResult mConnectionResult;
	private static final int RC_SIGN_IN = 0;
	// ImageButton backTitle_bt;
	Button login_bt;
	TextView join_bt;
	SharedPreferences registration_preference;
	Editor editor;
	String auth_token;
	EditText email_et, passWord_et;
	HashMap<String, String> regMapGoogle;
	HashMap<String, String> regMap;
	TextView forgotMypassword;
	Typeface tf_roboto_condensed;

	ProgressDialog progressDialog;

	/** 12/15/2015 **/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
		Log.e("LOGIN", "Content not set yet");
		setContentView(R.layout.activity_login);
		Log.e("LOGIN", "Activity Login Content Set");
		tf_roboto_condensed = TypeFace_MY.getRoboto_condensed(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setTintColor(Color.parseColor("#ff8c00"));
			tintManager.setStatusBarTintEnabled(true);
			// Holo light action bar color is #DDDDDD
			int actionBarColor = Color.parseColor("#ff8c00");
			tintManager.setStatusBarTintResource(R.drawable.orange);
		}
		forgotMypassword = (TextView) findViewById(R.id.forgotMypassword);
		forgotMypassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = email_et.getText().toString();
				showALertResetPassword(email);
			}
		});
		email_et = (EditText) findViewById(R.id.email_login_et);
		passWord_et = (EditText) findViewById(R.id.password_login_et);

		login_bt = (Button) findViewById(R.id.login_login_bt);
		login_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String emai_str = email_et.getText().toString();
				String password = passWord_et.getText().toString();
				if (validateText(emai_str)) {
					int i = validation(password);

					if (i == 0) {

						TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						String countryIso = tm.getSimCountryIso();
						System.out.println("countryIso: " + countryIso);
						String country = Country_manager.getCountry(countryIso
								.toUpperCase());
						String baseurl = BaseURL.selectBaseUrl(country);
						System.out.println("Email/Phone: " + emai_str);
						System.out.println("Country: " + country);
						System.out.println("baseurl: " + baseurl);
						System.out.println("Password: " + password);

						HashMap<String, String> Regmap = new HashMap<String, String>();
						Regmap.put(HASH_email, emai_str);
						Regmap.put(HASH_country, country);
						Regmap.put(HASH_baseUrl, baseurl);
						Regmap.put(HASH_password, password);

						new SubmitAsync().execute(Regmap);
					} else {
						makeToast("Password wasn't entered");
					}
				} else {
					makeToast("Re-enter your email");
				}
				/*
				 * Intent in = new Intent(LoginActivity.this,
				 * TabActivity.class); startActivity(in);
				 */
			}
		});

		join_bt = (TextView) findViewById(R.id.JOinIN_account_bt);
		join_bt.setTypeface(tf_roboto_condensed);
		join_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(LoginActivity.this,
						RegistrationActivity.class);
				startActivity(in);
				finish();
			}
		});

		fb_button = (Button) findViewById(R.id.login_facebook_login_bt);
		fb_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToFacebook();
			}
		});
		btngplogin = (Button) findViewById(R.id.login_gplus_login_bt);
		btngplogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signInWithGplus();
			}
		});

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		mAsyncRunner = new AsyncFacebookRunner(facebook);
	}

	@SuppressWarnings("deprecation")
	public void showALertResetPassword(String emails) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				LoginActivity.this).create();
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.alert_dialog, null);
		alertDialog.setView(dialogView);
		alertDialog.setTitle("Reset Password");

		alertDialog.setButton("submit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				EditText email_et = (EditText) alertDialog
						.findViewById(R.id.alertEmail);
				String email = email_et.getText().toString();
				if (email != null && !email.equals("")) {
					TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String countryIso = tm.getSimCountryIso();
					System.out.println("countryIso: " + countryIso);
					String country = Country_manager.getCountry(countryIso
							.toUpperCase());
					String baseurl = BaseURL.selectBaseUrl(country);

					new Async_forgotPassword_call().execute(baseurl, email);
				} else {
					makeToast("Please fill the email address field");
				}
				// new Async_.execute();
			}
		});

		alertDialog.setButton2("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				alertDialog.dismiss();
			}
		});
		// alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
		EditText email_et = (EditText) alertDialog
				.findViewById(R.id.alertEmail);
		email_et.setText(emails);
	}

	private boolean validateText(String text) {
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.WHITE);
		SpannableStringBuilder ssbuilder;

		if (!checkEmail(text) && !checkPhone(text)) {
			String estring = "Invalid User..";
			ssbuilder = new SpannableStringBuilder(estring);
			ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
			email_et.requestFocus();
			email_et.setError(ssbuilder);
			return false;
		} else {
			return true;
		}
	}

	private boolean checkEmail(String emailText) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
	}

	private boolean checkPhone(String phoneText) {
		return android.util.Patterns.PHONE.matcher(phoneText).matches();
	}

	public void makeToast(String str) {
		final String strr = str;

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.toast_layout, null);

				ImageView image = (ImageView) layout
						.findViewById(R.id.Toastimage);
				image.setImageResource(R.drawable.favorite_1_sel);
				image.setVisibility(View.GONE);
				TextView text = (TextView) layout.findViewById(R.id.Toasttext);
				text.setText(strr);

				int actionBarHeight = 0;
				// /////////set the toasts below
				// actionbar///////////////////////
				TypedValue tv = new TypedValue();
				if (getTheme().resolveAttribute(android.R.attr.actionBarSize,
						tv, true)) {
					actionBarHeight = TypedValue.complexToDimensionPixelSize(
							tv.data, getResources().getDisplayMetrics());
				}
				Toast tt = new Toast(LoginActivity.this);
				tt.setView(layout);
				tt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0,
						actionBarHeight + 5);
				tt.show();
			}
		});
	}

	public class Async_forgotPassword_call extends
			AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		JSONParser jParser = null;
		HashMap<String, String> showmap = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Submitting forgot password request...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(String... arg) {
			// TODO Auto-generated method stub
			String base_url = arg[0];
			String emailtext = arg[1];
			String country;
			try {
				if (ConnectionManagerPromo
						.getConnectivityStatus(LoginActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();

					if (base_url == null || base_url.equals("")) {

						String url = "http://ip-api.com/json";
						JSONParser jp = new JSONParser();
						JSONObject jo = jp.makeHttpRequest(url, "GET", params);

						try {
							country = jo.getString(HashStatic.hash_jsonCountry);
						} catch (Exception e) {
							e.printStackTrace();
							country = "Bangladesh";
						}
						base_url = BaseURL.selectBaseUrl(country);
					}

					@SuppressWarnings("deprecation")
					String url_select = base_url + BaseURL.API
							+ "password-reset.php?authtoken="
							+ URLEncoder.encode(BaseURL.Auth_Token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"email\":\"" + emailtext

							+ "\"}", "utf-8");
					System.out.println(url_select);

					jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);

					String message = jobj.getString("message");
					if (message != null && message.contains("success")) {
						makeToast("An email with reset password instructions is sent to your email address.");
					}

					System.out.println(jobj.toString());

				} else {
					makeToast("Internet Connection not Available");
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

		}

	}

	public int validation(String password) {
		int result = 0;

		if (password == null || password.equals("")) {
			result = 1;
		}

		return result;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);

		Intent in = new Intent(LoginActivity.this, StarterActivity.class);
		startActivity(in);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		facebook.authorizeCallback(requestCode, responseCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void getProfileInformationFB() {
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				System.out.println("Token: "
						+ mPrefs.getString("access_token", null));
				Log.d("Token", mPrefs.getString("access_token", "asd"));

				String json = response;
				try {
					final HashMap<String, String> regMap = new HashMap<String, String>();
					JSONObject profile = new JSONObject(json);
					System.out.println("JSON:: " + profile.toString());
					// getting name of the user
					final String accesstokenLink = "https://graph.facebook.com/me?access_token="
							+ mPrefs.getString("access_token", "")
							+ "&format=json";
					final String name = profile.getString("name");
					// getting email of the user
					final String email = profile.getString("email");
					final String first_name = profile.getString("first_name");
					final String last_name = profile.getString("last_name");
					final String id = profile.getString("id");
					final String imageurl = "http://graph.facebook.com/" + id
							+ "/picture?type=large";
					// final String birthday = profile.getString("birthday");
					final String gender = profile.getString("gender");

					TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String countryIso = tm.getSimCountryIso();
					System.out.println("countryIso: " + countryIso);
					String country = Country_manager.getCountry(countryIso
							.toUpperCase());
					System.out.println("country: " + country);
					String baseurl = BaseURL.selectBaseUrl(country);
					System.out.println("baseurl: " + baseurl);
					System.out.println("accesstoken: " + accesstokenLink);
					Log.e("Email >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", email);

					regMap.put(HashStatic.ID, id);
					regMap.put(HashStatic.FACEBOOK_auth_LINK, accesstokenLink);
					regMap.put(HashStatic.FACEBOOK_PRO_LINK, imageurl);

					regMap.put(HASH_email, email);
					regMap.put(HASH_firstNAME, first_name);
					regMap.put(HASH_lastname, last_name);
					regMap.put(HASH_country, country);
					regMap.put(HASH_baseUrl, baseurl);
					regMap.put(HASH_gender, gender);
					regMap.put(HASH_auth, "facebook");

					// progressDialog.setMessage("Logged In Fb....");

					// Looper.prepare();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							new SubmitSocialAsync().execute(regMap);

						}

					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void loginToFacebook() {
		fpref = getSharedPreferences("PromoRegToken", 0);

		mPrefs = getPreferences(MODE_PRIVATE);
		access_token = fpref.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "email" }, // "email",
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@SuppressWarnings("deprecation")
						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							fpref.edit()
									.putString("token",
											facebook.getAccessToken()).commit();
							System.out.println("Token: "
									+ facebook.getAccessToken());
							progressDialog = new ProgressDialog(
									LoginActivity.this);
							progressDialog.setMessage("Logging in...");
							progressDialog.show();
							progressDialog.setCancelable(false);
							getProfileInformationFB();

						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors
						}

					});
		}
	}

	// //////////////////////////google log in//////////////

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult != null) {
			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult
							.startResolutionForResult(this, RC_SIGN_IN);
				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}
	}

	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}

	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();

				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				int gender = currentPerson.getGender();
				String gender_str = "";
				if (gender == 0)
					gender_str = "male";
				else if (gender == 1)
					gender_str = "female";

				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				regMapGoogle = new HashMap<String, String>();

				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String countryIso = tm.getSimCountryIso();
				System.out.println("countryIso: " + countryIso);
				String country = Country_manager.getCountry(countryIso
						.toUpperCase());
				System.out.println("country: " + country);
				String baseurl = BaseURL.selectBaseUrl(country);
				System.out.println("baseurl: " + baseurl);

				regMapGoogle.put(HASH_email, email);
				regMapGoogle.put(HASH_firstNAME, personName);
				regMapGoogle.put(HASH_gender, gender_str);
				// regMap.put(HASH_lastname, last_name);
				regMapGoogle.put(HASH_country, country);
				regMapGoogle.put(HASH_baseUrl, baseurl);
				regMapGoogle.put(HASH_auth, "google");
				regMapGoogle.put(HASH_GOOGLE_pp, personPhotoUrl);
				regMapGoogle.put(HASH_GOOGLE_userLink, personGooglePlusProfile);

				this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new SubmitGoogleAsync().execute(regMapGoogle);
					}
				});


			} else {
				makeToast("Could not access the google account. Please try again");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		// updateUI(false);
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		getProfileInformation();
	}

	public class SubmitGoogleAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		int cust_Id;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Logging in...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo
					.getConnectivityStatus(LoginActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				String email = parametersList.get(HASH_email);
				String fname = parametersList.get(HASH_firstNAME);
				String gender = parametersList.get(HASH_gender);
				// String lastName = parametersList.get(HASH_lastname);
				String baseUrl = parametersList.get(HASH_baseUrl);
				String auth_type = parametersList.get(HASH_auth);
				String country = parametersList.get(HASH_country);
				// String id = parametersList.get(HashStatic.ID);
				String authUrl = parametersList.get(HASH_GOOGLE_userLink);
				String imageUrl = parametersList.get(HASH_GOOGLE_pp);

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();

					if (country == null || country.equals("")) {
						String url = "http://ip-api.com/json";
						JSONParser jp = new JSONParser();
						JSONObject jo = jp.makeHttpRequest(url, "GET", params);

						try {
							country = jo.getString(HashStatic.hash_jsonCountry);
						} catch (Exception e) {
							e.printStackTrace();
							country = "Bangladesh";
						}
						baseUrl = BaseURL.selectBaseUrl(country);

					}

					String url_select = baseUrl
							+ BaseURL.API
							+ "signup.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"image_url\":\"" + imageUrl
									+ "\"" + ",\"auth_type\":\"" + auth_type
									+ "\"" + ",\"firstname\":\"" + fname + "\""
									+ ",\"lastname\":\"" + "\""
									+ ",\"country\":\"" + country + "\""
									+ ",\"gender\":\"" + gender + "\""
									// + ",\"link\":\"" + authUrl + "\""
									+ ",\"email\":\"" + email + "\"" + "}",
									"utf-8");

					System.out.println(url_select);

					JSONParser jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);
					JSONObject jCust = jobj.getJSONObject("customer");

					cust_Id = jCust.getInt(HashStatic.CUSTOMER_ID);

					if (cust_Id > 0) {
						editor.putString(HashStatic.CUSTOMER_ID,
								String.valueOf(cust_Id)).commit();
						editor.putString(HashStatic.HASH_email, email).commit();
						editor.putString(HashStatic.CUSTOMER_EMAIL, email)
								.commit();
						editor.putString(HashStatic.HASH_firstNAME,
								String.valueOf(fname)).commit();
						// editor.putString(HashStatic.HASH_lastname,
						// String.valueOf(lastName)).commit();
						editor.putString(HashStatic.HASH_baseUrl,
								String.valueOf(baseUrl)).commit();
						editor.putString(HashStatic.HASH_auth,
								String.valueOf(auth_type)).commit();

						editor.putString(HashStatic.HASH_country,
								String.valueOf(country)).commit();
						String currency = BaseURL.selectCurrency(country);
						editor.putString(HashStatic.HASH_currency,
								String.valueOf(currency)).commit();
					}

					System.out.println("CustomerID" + cust_Id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				makeToast("Network connection not available.");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (cust_Id > 0) {
				signOutFromGplus();
				makeToast("Welcome to Yozard!");
				HashStatic.firstTimeLogin = true;
				Intent intent = new Intent(LoginActivity.this,
						TabActivity.class);
				startActivity(intent);
				finish();
				// editor.putString(HashStatic.CUSTOMER_ID,
				// String.valueOf(cust_Id)).commit();
			}
		}

	}

	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();

		}
	}

	public class SubmitSocialAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {

		int cust_Id;

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo
					.getConnectivityStatus(LoginActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				String email = parametersList.get(HASH_email);
				String fname = parametersList.get(HASH_firstNAME);
				String lastName = parametersList.get(HASH_lastname);
				String baseUrl = parametersList.get(HASH_baseUrl);
				String auth_type = parametersList.get(HASH_auth);
				String gender = parametersList.get(HASH_gender);

				String country = parametersList.get(HASH_country);
				String id = parametersList.get(HashStatic.ID);
				String authUrl = parametersList
						.get(HashStatic.FACEBOOK_auth_LINK);
				String imageUrl = parametersList
						.get(HashStatic.FACEBOOK_PRO_LINK);

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("link", authUrl));

					// params.add(new UrlEncodedFormEntity(nameValuePairs));
					if (country == null || country.equals("")) {
						String url = "http://ip-api.com/json";
						JSONParser jp = new JSONParser();
						JSONObject jo = jp.makeHttpRequest(url, "GET", params);

						try {
							country = jo.getString(HashStatic.hash_jsonCountry);
						} catch (Exception e) {
							e.printStackTrace();
							country = "Bangladesh";
						}
						baseUrl = BaseURL.selectBaseUrl(country);

					}

					String url_select = baseUrl
							+ BaseURL.API
							+ "signup.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"firstname\":\"" + fname
									+ "\"" + ",\"lastname\":\"" + lastName
									+ "\"" + ",\"auth_type\":\"" + auth_type
									+ "\"" + ",\"country\":\"" + country + "\""
									+ ",\"id\":\"" + id + "\""
									+ ",\"gender\":\"" + gender + "\""
									// + ",\"link\":\"" + authUrl + "\""
									+ ",\"image_url\":\"" + imageUrl + "\""
									+ ",\"email\":\"" + email + "\"" + "}",
									"utf-8");

					DefaultHttpClient httpClient = JSONParser
							.createHttpClient();
					HttpPost httpPost = new HttpPost(url_select);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "iso-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						json = sb.toString();
						Log.d("String jason", sb.toString());
					} catch (Exception e) {
						Log.e("Buffer Error",
								"Error converting result " + e.toString());
					}

					// try parse the string to a JSON object
					try {
						jObj = new JSONObject(json);
					} catch (JSONException e) {
						Log.e("JSON Parser",
								"Error parsing data " + e.toString());
					}

					JSONObject jCust = jObj.getJSONObject("customer");
					cust_Id = jCust.getInt(HashStatic.CUSTOMER_ID);

					if (cust_Id > 0) {
						editor.putString(HashStatic.CUSTOMER_ID,
								String.valueOf(cust_Id)).commit();
						editor.putString(HashStatic.HASH_email, email).commit();
						editor.putString(HashStatic.CUSTOMER_EMAIL, email)
								.commit();
						editor.putString(HashStatic.HASH_firstNAME,
								String.valueOf(fname)).commit();
						editor.putString(HashStatic.HASH_lastname,
								String.valueOf(lastName)).commit();
						editor.putString(HashStatic.HASH_baseUrl,
								String.valueOf(baseUrl)).commit();
						editor.putString(HashStatic.HASH_auth,
								String.valueOf(auth_type)).commit();

						editor.putString(HashStatic.HASH_country,
								String.valueOf(country)).commit();
						String currency = BaseURL.selectCurrency(country);
						editor.putString(HashStatic.HASH_currency,
								String.valueOf(currency)).commit();
					}

					System.out.println("CustomerID" + cust_Id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				makeToast("Network connection not available");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (cust_Id > 0) {
				makeToast("Welcome to Yozard!");
				HashStatic.firstTimeLogin = true;
				Intent intent = new Intent(LoginActivity.this,
						TabActivity.class);
				startActivity(intent);
				finish();
				// editor.putString(HashStatic.CUSTOMER_ID,
				// String.valueOf(cust_Id)).commit();
			}
		}

	}

	public class SubmitAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		int cust_Id, cust_status;
		String customer_email;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Logging in...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo
					.getConnectivityStatus(LoginActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String email = parametersList.get(HASH_email);
				String password = parametersList.get(HASH_password);
				String baseUrl = parametersList.get(HASH_baseUrl);
				String country = parametersList.get(HASH_country);

				byte[] data;
				try {

					data = password.getBytes("UTF-8");

					password = Base64.encodeToString(data, Base64.DEFAULT);
					password = password.trim();

					// Log.i("Base 64 ", base64);
					System.out.println(password);
					System.out.println(URLEncoder.encode(password, "utf-8"));

				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();

				}

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();

					if (country == null || country.equals("")) {
						String url = "http://ip-api.com/json";
						JSONParser jp = new JSONParser();
						JSONObject jo = jp.makeHttpRequest(url, "GET", params);

						try {
							country = jo.getString(HashStatic.hash_jsonCountry);
						} catch (Exception e) {
							e.printStackTrace();
							country = "Bangladesh";
						}
						baseUrl = BaseURL.selectBaseUrl(country);

					}

					String url_select = baseUrl
							+ BaseURL.API
							+ "authenticate.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"email\":\"" + email + "\""
									+ ",\"auth_type\":\"" + "normal" + "\""

									+ ",\"password\":\"" + password + "\""
									+ "}", "utf-8");

					System.out.println(url_select);

					JSONParser jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);
					System.out.println(jobj.toString());

					JSONObject jCust = jobj.getJSONObject("customer");
					cust_Id = jCust.getInt(HashStatic.CUSTOMER_ID);
					cust_status = jCust.getInt(HashStatic.CUSTOMER_STATUS);
					customer_email = jCust.getString(HashStatic.CUSTOMER_EMAIL);
					System.out.println("CustomerID" + cust_Id);
					System.out.println("Customer Status" + cust_status);
					System.out.println("Customer Email" + customer_email);
					System.out.println("mir: " + jobj.toString());
					if (cust_Id > 0) {
						editor.putString(HashStatic.CUSTOMER_ID,
								String.valueOf(cust_Id)).commit();
						editor.putString(HashStatic.HASH_email, email).commit();
						editor.putString(HashStatic.CUSTOMER_EMAIL,
								customer_email).commit();
						editor.putString(HashStatic.HASH_baseUrl,
								String.valueOf(baseUrl)).commit();

						editor.putString(HashStatic.HASH_country,
								String.valueOf(country)).commit();
						String currency = BaseURL.selectCurrency(country);
						editor.putString(HashStatic.HASH_currency,
								String.valueOf(currency)).commit();
					}

				} catch (Exception e) {
					e.printStackTrace();
					makeToast("User name or password is wrong.");
				}
			} else {
				makeToast("Network connection not available.");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (cust_Id > 0) {
				Log.e("Checking >> ", "We are here");
				makeToast("Welcome to Yozard!");
				HashStatic.firstTimeLogin = true;
				if (cust_status == 2 && customer_email.equalsIgnoreCase("")) {
					Intent intent = new Intent(LoginActivity.this,
							VerifyEmailActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(LoginActivity.this,
							TabActivity.class);
					startActivity(intent);
					finish();
				}
			} else {
				makeToast("User name or password is wrong");
			}
		}

	}

}
