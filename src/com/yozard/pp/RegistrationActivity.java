package com.yozard.pp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.Normalizer;
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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.Country_manager;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

@SuppressLint("ClickableViewAccessibility")
public class RegistrationActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener {

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
	public static final String HASH_picture = "picture";

	public static final String HASH_GOOGLE_pp = "image_link";
	public static final String HASH_GOOGLE_userLink = "user_link";

	Button btnFbLogin, btngplogin;
	RadioButton female_bt, male_bt;
	SharedPreferences fpref;
	private String selectedImagePath = null;
	CircularImageView upload_IV;
	private boolean mIntentInProgress;

	private static final int SELECT_PICTURE = 007;
	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	private static String APP_ID = "578277912272525"; // "771442562903897"; //
														// "277681635750065";//"648082168606567";

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	String access_token = null;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;
	private static final int RC_SIGN_IN = 0;
	TextView title_tv;
	ImageButton backTitle_bt;

	EditText dateOFbirth;
	EditText lastname_et, firstName_et, emaid_et, date_et, password_et;
	String picture_url;
	Button join_now_bt;

	String lastName_str = null, firstName_str = null, emai_str = null,
			date_str = null, password_str = null, gender = null, auth_token;
	RadioGroup rd;
	SharedPreferences registration_preference;
	Editor editor;
	HashMap<String, String> regMapGoogle;
	ProgressDialog progressDialog;

	LinearLayout name_linear, gender_liner, email_linear, password_linear,
			birthDate_linear;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent in = new Intent(RegistrationActivity.this,
				StarterActivity.class);
		startActivity(in);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.custom_title);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setTintColor(Color.parseColor("#ff8c00"));
			tintManager.setStatusBarTintEnabled(true);
			// Holo light action bar color is #DDDDDD
			int actionBarColor = Color.parseColor("#ff8c00");
			tintManager.setStatusBarTintResource(R.drawable.orange);
			// tintManager.setTintResource(R.drawable.orange);
			// tintManager.setStatusBarTintColor(actionBarColor);
		}

		backTitle_bt = (ImageButton) findViewById(R.id.title_back_bt);
		backTitle_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(RegistrationActivity.this,
						StarterActivity.class);
				startActivity(in);
				finish();
			}
		});
		title_tv = (TextView) findViewById(R.id.Title_tv);
		title_tv.setText("Let's Get Started!");
		getActionBar().setHomeButtonEnabled(true);
		// showHashKey(RegistrationActivity.this);

		female_bt = (RadioButton) findViewById(R.id.reg_female_bt);
		male_bt = (RadioButton) findViewById(R.id.reg_male_bt);

		rd = (RadioGroup) findViewById(R.id.radioGroup_gender);
		rd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case R.id.reg_male_bt:
					gender = "male";
					break;
				case R.id.reg_female_bt:
					gender = "female";
					break;

				default:
					break;
				}
			}
		});

		lastname_et = (EditText) findViewById(R.id.regUser_lastname);
		firstName_et = (EditText) findViewById(R.id.regUser_firstname);
		emaid_et = (EditText) findViewById(R.id.regUser_email_et);
		password_et = (EditText) findViewById(R.id.regUser_passwor_et);
		date_et = (EditText) findViewById(R.id.regUser_bday_et);

		btnFbLogin = (Button) findViewById(R.id.reg_facebook_login_bt);
		btnFbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToFacebook();
			}
		});

		btngplogin = (Button) findViewById(R.id.reg_gPlus_login_bt);
		btngplogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("geee");
				signInWithGplus();
			}
		});

		upload_IV = (CircularImageView) findViewById(R.id.userId_IV);
		upload_IV.setBorderColor(Color.WHITE);
		upload_IV.setBorderWidth(10);
		upload_IV.addShadow();
		upload_IV.setImageResource(R.drawable.upload);

		join_now_bt = (Button) findViewById(R.id.submit_joinNow_bt);
		join_now_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lastName_str = lastname_et.getText().toString();
				firstName_str = firstName_et.getText().toString();
				emai_str = emaid_et.getText().toString();
				password_str = password_et.getText().toString();
				date_str = date_et.getText().toString();
				// /removing special charecter
				lastName_str = Normalizer.normalize(lastName_str,
						Normalizer.Form.NFD).replaceAll("[^a-zA-Z]", "");
				firstName_str = Normalizer.normalize(firstName_str,
						Normalizer.Form.NFD).replaceAll("[^a-zA-Z]", "");

				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String countryIso = tm.getSimCountryIso();

				// countryIso="bd";

				System.out.println("countryIso: " + countryIso);

				String country = Country_manager.getCountry(countryIso
						.toUpperCase());
				System.out.println("country: " + country);
				String baseurl = BaseURL.selectBaseUrl(country);
				System.out.println("baseurl: " + baseurl);

				BitmapDrawable drawable = (BitmapDrawable) upload_IV
						.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				String picture = convertProPicture(bitmap);

				HashMap<String, String> Regmap = new HashMap<String, String>();
				Regmap.put(HASH_email, emai_str);
				Regmap.put(HASH_firstNAME, firstName_str);
				Regmap.put(HASH_lastname, lastName_str);
				Regmap.put(HASH_date, date_str);
				Regmap.put(HASH_gender, gender);
				Regmap.put(HASH_country, country);
				Regmap.put(HASH_baseUrl, baseurl);
				Regmap.put(HASH_password, password_str);
				Regmap.put(HASH_picture, picture);

				refreshTheForm();
				int i = validation(lastName_str, firstName_str, emai_str,
						date_str, password_str, gender);
				if (i == 0) {
					makeToast("Please fill the form correctly");
				} else {
					new SubmitAsync().execute(Regmap);
				}

			}
		});

		upload_IV.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					Intent intentt = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					/*
					 * intentt.setType("image/*");
					 * intentt.setAction(Intent.ACTION_GET_CONTENT);
					 */
					startActivityForResult(
							Intent.createChooser(intentt, "Select Picture"),
							SELECT_PICTURE);
				}
				return true;
			}
		});

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		mAsyncRunner = new AsyncFacebookRunner(facebook);

		name_linear = (LinearLayout) findViewById(R.id.name_linear);
		gender_liner = (LinearLayout) findViewById(R.id.gender_linear);
		email_linear = (LinearLayout) findViewById(R.id.email_linear);
		password_linear = (LinearLayout) findViewById(R.id.password_linear);
		birthDate_linear = (LinearLayout) findViewById(R.id.brthDate_linear);

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
				Toast tt = new Toast(RegistrationActivity.this);
				tt.setView(layout);
				tt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0,
						actionBarHeight + 5);
				tt.show();
			}
		});
	}

	public String convertProPicture(Bitmap pic) {
		String resultStr = "";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		pic = Bitmap.createScaledBitmap(pic, 300, 300, true);

		// byte array output streams
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pic.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		// byte array
		byte[] ba = baos.toByteArray();

		// byte to base64 encoded string
		resultStr = com.yozard.pp.utils.Base64.encodeBytes(ba);

		return resultStr;
	}

	public class SubmitAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		int cust_Id;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(RegistrationActivity.this);
			progressDialog.setMessage("Registering...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo
					.getConnectivityStatus(RegistrationActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String email = parametersList.get(HASH_email);
				String fname = parametersList.get(HASH_firstNAME);
				String lastName = parametersList.get(HASH_lastname);
				String Bdate = parametersList.get(HASH_date);
				String password = parametersList.get(HASH_password);
				String gender = parametersList.get(HASH_gender);
				String baseUrl = parametersList.get(HASH_baseUrl);
				String country = parametersList.get(HASH_country);
				String picture = parametersList.get(HASH_picture);

				System.out.println("My COUNTRY: " + country);

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(
						HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs
							.add(new BasicNameValuePair("image", picture));

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

					System.out.println("My COUNTRY2: " + country);

					System.out.println("pic" + picture);

					String url_select = baseUrl
							+ BaseURL.API
							+ "signup.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"firstname\":\"" + fname
									+ "\"" + ",\"lastname\":\"" + lastName
									+ "\"" + ",\"auth_type\":\"" + "normal"
									+ "\"" + ",\"gender\":\"" + gender + "\""
									+ ",\"mobile\":\""
									+ Bdate
									+ "\"" // BDate is actually phone number
									+ ",\"country\":\"" + country + "\""
									+ ",\"password\":\"" + password + "\""
									+ ",\"email\":\"" + email + "\"" + "}",
									"utf-8");

					System.out.println(url_select);

					JSONParser jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", nameValuePairs);
					JSONObject jCust = jobj.getJSONObject("customer");

					String messString = jobj.getString("message");
					if (messString.contains("already exist")) {
						makeToast("Email address already exists please login instead");
					}

					if (messString.contains("Invalid Email Address")) {
						makeToast("Please enter a valid email address");
					}

					cust_Id = jCust.getInt(HashStatic.CUSTOMER_ID);
					System.out.println("CustomerID" + cust_Id);

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
						editor.putString(HashStatic.HASH_country,
								String.valueOf(country)).commit();

						editor.putString(HashStatic.HASH_country,
								String.valueOf(country)).commit();
						String currency = BaseURL.selectCurrency(country);
						editor.putString(HashStatic.HASH_currency,
								String.valueOf(currency)).commit();

						HashStatic.signUp_byEmail = true;
					}

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
				Intent intent = new Intent(RegistrationActivity.this,
						TabActivity.class);
				startActivity(intent);
				finish();
			}
		}

	}

	public void refreshTheForm() {
		name_linear.setBackgroundResource(R.drawable.bg_white);
		email_linear.setBackgroundResource(R.drawable.bg_white);
		birthDate_linear
				.setBackgroundResource(R.drawable.bg_white);
		password_linear.setBackgroundResource(R.drawable.bg_white);
		gender_liner.setBackgroundResource(R.drawable.bg_white);
	}

	public int validation(String lastName_str, String firstName_str,
			String emai_str, String date_str, String password_str, String gender) {
		int result = 1;

		if (lastName_str == null || lastName_str.equals("")) {
			name_linear.setBackgroundResource(R.drawable.border_red_bg_white);
			result = 0;
		}
		if (firstName_str == null || firstName_str.equals("")) {
			name_linear.setBackgroundResource(R.drawable.border_red_bg_white);
			result = 0;
		}
		if (emai_str == null || emai_str.equals("")) {
			email_linear
					.setBackgroundResource(R.drawable.border_red_bg_white);
			result = 0;
		}
		if (date_str == null || date_str.equals("")) {
			birthDate_linear
					.setBackgroundResource(R.drawable.border_red_bg_white);
			result = 0;
		}
		if (password_str == null || password_str.equals("")
				|| password_str.length() < 6) {

			password_linear
					.setBackgroundResource(R.drawable.border_red_bg_white);
			if (password_str.length() < 6)
				makeToast("The password should contain atleast 6 charecters");
			result = 0;
		}
		if (gender == null || gender.equals("")) {
			gender_liner
					.setBackgroundResource(R.drawable.border_red_bg_white);
			result = 0;
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	public void loginToFacebook() {
		fpref = getSharedPreferences(HashStatic.PROMOI_REG_TOKEN, 0);

		mPrefs = getPreferences(MODE_PRIVATE);
		access_token = fpref.getString(HashStatic.ACCESS_TOKEN_FB, null);
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
							System.out.println("HERE: ");
							editor.commit();
							fpref.edit()
									.putString(HashStatic.ACCESS_TOKEN_FB,
											facebook.getAccessToken()).commit();
							System.out.println("Token: "
									+ facebook.getAccessToken());
							System.out.println("HERE: ");

							progressDialog = new ProgressDialog(
									RegistrationActivity.this);
							progressDialog.setMessage("Signing up...");
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

		if (requestCode == SELECT_PICTURE && responseCode == RESULT_OK) {
			try {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				System.out.println(selectedImagePath);
				upload_IV.setVisibility(View.VISIBLE);
				upload_IV.setImageURI(Uri.parse(selectedImagePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public String getPath(Uri uri) {

		if (uri == null) {
			return null;
		}

		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}

		return uri.getPath();
	}

	// //////////////////////////google log
	// in//////////////////////////////////////////////////

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
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
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
				makeToast("Person information is null");
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
			progressDialog = new ProgressDialog(RegistrationActivity.this);
			progressDialog.setMessage("Registering...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo
					.getConnectivityStatus(RegistrationActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(
						HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
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
									+ ",\"lastname\":\"" + "" + "\""
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
						editor.putString(HashStatic.CUSTOMER_EMAIL, email)
								.commit();
						editor.putString(HashStatic.HASH_email, email).commit();
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
				Intent intent = new Intent(RegistrationActivity.this,
						TabActivity.class);
				startActivity(intent);
				finish();
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
					.getConnectivityStatus(RegistrationActivity.this) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(
						HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
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
						/*
						 * editor.putString(HashStatic.FACEBOOK_PRO_LINK,
						 * imageUrl) .commit();
						 */

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
				Intent intent = new Intent(RegistrationActivity.this,
						TabActivity.class);
				startActivity(intent);
				finish();
				// editor.putString(HashStatic.CUSTOMER_ID,
				// String.valueOf(cust_Id)).commit();
			}
		}

	}

}
