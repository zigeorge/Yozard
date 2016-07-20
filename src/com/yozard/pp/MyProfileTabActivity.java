package com.yozard.pp;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yozard.pp.R;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;
import com.yozard.pp.utils.TypeFace_MY;

public class MyProfileTabActivity extends ActionBarActivity {

	public static final String HASH_firstNAME = "firstname ";
	public static final String HASH_email = "email";
	public static final String HASH_date = "birthday";
	public static final String HASH_gender = "gender";
	public static final String HASH_lastname = "lastname";
	public static final String HASH_password = "password";
	public static final String HASH_country = "country";
	public static final String HASH_baseUrl = "baseUrl";
	public static final String HASH_auth = "auth_type";

	public static final String HASH_mobile = "mobile";
	public static final String HASH_city = "city";
	public static final String HASH_address = "address";
	public static final String HASH_picture = "picture";
	public static final String EDITABLE = "editable";

	private static final int SELECT_PICTURE = 001;

	EditText first_name_et, lastname_et, email_et, birthday_et, mobileNo_et,
			city_et, address_et;

	String first_name_str, lastname_str, gender_str, email_str, password_str,
			birthday_str, mobileNo_str, city_str, address_str;

	RadioButton male_bt, female_bt;

	String base_url, pro_image, auth_token;
	String currency = null, user_id = null;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	static CircularImageView imageView;

	SharedPreferences registration_preference;
	Editor editor;

	TextView resetpassword, regemailText_tv, birthdayText_tv, mobileText_tv,
			cityText_tv, addressText_tv;
	Button saveprofile, cancelButton;
	ToggleButton editprofile;
	LinearLayout savedButtons_linear;

	Typeface tf_rancroboto_condensedho;

	Context context;

	boolean isEditable = false;
	public static boolean profile_pic_selected = false;

	OnTouchListener mListener_edittext = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			EditText et = (EditText) v;
			String text = et.getText().toString();
			if (text != null && text.equals("N/A")) {
				et.setText("");
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile_tab);

		context = this;
		tf_rancroboto_condensedho = TypeFace_MY.getRoboto_condensed(context);

		isEditable = getIntent().getBooleanExtra(EDITABLE, false);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initUI();
	}

	private void initUI() {
		regemailText_tv = (TextView) findViewById(R.id.reg_email_tv);
		birthdayText_tv = (TextView) findViewById(R.id.birthDate_tv_edit);
		mobileText_tv = (TextView) findViewById(R.id.mobile_linear);
		cityText_tv = (TextView) findViewById(R.id.city_linear_tv);
		addressText_tv = (TextView) findViewById(R.id.address_linear_tv);

		regemailText_tv.setTypeface(tf_rancroboto_condensedho);
		birthdayText_tv.setTypeface(tf_rancroboto_condensedho);
		mobileText_tv.setTypeface(tf_rancroboto_condensedho);
		cityText_tv.setTypeface(tf_rancroboto_condensedho);
		addressText_tv.setTypeface(tf_rancroboto_condensedho);

		first_name_et = (EditText) findViewById(R.id.regUser_firstname_edit);
		first_name_et.setTypeface(tf_rancroboto_condensedho);
		lastname_et = (EditText) findViewById(R.id.regUser_lastname_edit);
		lastname_et.setTypeface(tf_rancroboto_condensedho);
		email_et = (EditText) findViewById(R.id.regUser_email_et_edit);
		email_et.setTypeface(tf_rancroboto_condensedho);
		email_et.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context,
				// "Email address is not editable",Toast.LENGTH_LONG);
			}
		});
		// password_et = (EditText)
		// v.findViewById(R.id.regUser_passwor_et_edit);
		birthday_et = (EditText) findViewById(R.id.regUser_bday_et_edit);
		birthday_et.setTypeface(tf_rancroboto_condensedho);
		birthday_et.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DatePickerDialog dp = new DatePickerDialog();
				dp.show(getFragmentManager(), "Set Birth Date");

			}
		});

		mobileNo_et = (EditText) findViewById(R.id.mobile_et_edit);
		mobileNo_et.setTypeface(tf_rancroboto_condensedho);
		city_et = (EditText) findViewById(R.id.city_et_edit);
		city_et.setTypeface(tf_rancroboto_condensedho);
		address_et = (EditText) findViewById(R.id.address_et_edit);
		address_et.setTypeface(tf_rancroboto_condensedho);

		male_bt = (RadioButton) findViewById(R.id.reg_male_bt_edit);
		female_bt = (RadioButton) findViewById(R.id.reg_female_bt_edit);

		male_bt.setTypeface(tf_rancroboto_condensedho);
		female_bt.setTypeface(tf_rancroboto_condensedho);

		saveprofile = (Button) findViewById(R.id.saveProfile);
		saveprofile.setTypeface(tf_rancroboto_condensedho);
		saveprofile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveProfile();
			}
		});

		cancelButton = (Button) findViewById(R.id.cancelProfile);
		cancelButton.setTypeface(tf_rancroboto_condensedho);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cancelButtonPressed();
			}
		});

		resetpassword = (TextView) findViewById(R.id.resetPassword_tv);
		resetpassword.setTypeface(tf_rancroboto_condensedho);
		resetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showALertResetPassword();
			}
		});

		savedButtons_linear = (LinearLayout) findViewById(R.id.saveCancel_layout);

		editprofile = (ToggleButton) findViewById(R.id.editProfile_bt_edit);
		editprofile.setTypeface(tf_rancroboto_condensedho);
		editprofile
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						editableLayout(isChecked);
					}
				});

		imageView = (CircularImageView) findViewById(R.id.userId_IV_edit);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getImageFromGallery();
			}
		});

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.upload)
				.showImageOnLoading(R.drawable.upload)
				.showImageOnFail(R.drawable.upload)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		/* imageLoader.clearDiscCache(); */

		registration_preference = context.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		String currency_sp = registration_preference.getString(
				HashStatic.HASH_currency, null);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);

		editor = registration_preference.edit();

		user_id = userId_sp;
		currency = currency_sp;

		new Async_final_call().execute();

		disabelEdittext();
		editableLayout(isEditable);
	}

	public class DatePickerDialog extends DialogFragment {

		public int day, year, month;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			// return super.onCreateDialog(savedInstanceState);

			Calendar cl = Calendar.getInstance();
			day = cl.get(Calendar.DAY_OF_MONTH);
			month = cl.get(Calendar.MONTH);
			year = cl.get(Calendar.YEAR);

			android.app.DatePickerDialog.OnDateSetListener ls = new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker arg0, int y, int m, int d) {
					// TODO Auto-generated method stub
					birthday_et.setText(String.valueOf(y) + "-"
							+ String.valueOf(m + 1) + "-" + String.valueOf(d));

				}
			};
			return new android.app.DatePickerDialog(getActivity(), ls, year,
					month, day);
		}
	}

	private void saveProfile() {
		String lastName_str = lastname_et.getText().toString();
		String firstName_str = first_name_et.getText().toString();
		String emai_str = email_et.getText().toString();
		String date_str = birthday_et.getText().toString();

		String mobile = mobileNo_et.getText().toString();
		String address = address_et.getText().toString();
		String city = city_et.getText().toString();

		// /removing special charecter
		lastName_str = Normalizer.normalize(lastName_str, Normalizer.Form.NFD)
				.replaceAll("[^a-zA-Z]", "");
		firstName_str = Normalizer
				.normalize(firstName_str, Normalizer.Form.NFD).replaceAll(
						"[^a-zA-Z]", "");
		String gender = "";
		if (male_bt.isChecked()) {
			gender = "male";
		} else if (female_bt.isChecked()) {
			gender = "female";
		}
		/**/

		// BitmapDrawable drawable = (BitmapDrawable) upload_IV.getDrawable();
		// Bitmap bitmap = drawable.getBitmap();
		// String picture=convertProPicture(bitmap);

		HashMap<String, String> Regmap = new HashMap<String, String>();
		Regmap.put(HASH_email, emai_str);
		Regmap.put(HASH_firstNAME, firstName_str);
		Regmap.put(HASH_lastname, lastName_str);
		Regmap.put(HASH_date, date_str);
		Regmap.put(HASH_gender, gender);
		Regmap.put(HASH_mobile, mobile);
		Regmap.put(HASH_address, address);
		Regmap.put(HASH_city, city);
		// Regmap.put(HASH_picture, picture);
		if (profile_pic_selected) {
			BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			String picture = convertProPicture(bitmap);
			Regmap.put(HASH_picture, picture);
			System.out.println("bitmap");
		}
		new SubmitAsync().execute(Regmap);
	}

	private void cancelButtonPressed() {
		if (isEditable) {
			finish();
		} else {
			savedButtons_linear.setVisibility(View.INVISIBLE);
			resetpassword.setVisibility(View.GONE);
			disabelEdittext();
			editprofile.setChecked(false);
		}
	}

	public void showALertResetPassword() {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle("Reset Password");
		alertDialog.setMessage("Do you really want to reset your password?");
		alertDialog.setButton("yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions

				new Async_resetPassword_call().execute();
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
	}

	private void getImageFromGallery() {
		Intent intentt = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		this.startActivityForResult(
				Intent.createChooser(intentt, "Select Picture"), SELECT_PICTURE);
	}

	private void editableLayout(boolean isEditable) {
		if (isEditable) {
			// The toggle is enabled
			savedButtons_linear.setVisibility(View.VISIBLE);
			resetpassword.setVisibility(View.VISIBLE);
			enableEdittext();

			editprofile.setVisibility(View.INVISIBLE);
		} else {
			// The toggle is disabled
			editprofile.setVisibility(View.VISIBLE);
			savedButtons_linear.setVisibility(View.INVISIBLE);
			resetpassword.setVisibility(View.GONE);
			disabelEdittext();

		}
	}

	private String convertProPicture(Bitmap pic) {
		String resultStr = "";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		pic = Bitmap.createScaledBitmap(pic, 600, 600, true);

		// byte array output streams
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pic.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		// byte array
		byte[] ba = baos.toByteArray();

		// byte to base64 encoded string
		resultStr = com.yozard.pp.utils.Base64.encodeBytes(ba);

		return resultStr;
	}

	public void disabelEdittext() {
		first_name_et.setEnabled(false);
		lastname_et.setEnabled(false);
		email_et.setEnabled(false);
		birthday_et.setEnabled(false);
		mobileNo_et.setEnabled(false);
		city_et.setEnabled(false);
		address_et.setEnabled(false);

		male_bt.setClickable(false);
		female_bt.setClickable(false);
		male_bt.setTextColor(Color.parseColor("#706D6C"));
		female_bt.setTextColor(Color.parseColor("#706D6C"));
		imageView.setClickable(false);
	}

	public void enableEdittext() {
		first_name_et.setEnabled(true);
		lastname_et.setEnabled(true);
		birthday_et.setEnabled(true);
		mobileNo_et.setEnabled(true);
		city_et.setEnabled(true);
		address_et.setEnabled(true);
		male_bt.setClickable(true);
		female_bt.setClickable(true);
		male_bt.setTextColor(Color.parseColor("#000000"));
		female_bt.setTextColor(Color.parseColor("#000000"));
		imageView.setClickable(true);
	}

	public void showReset_SuccessfulAlert() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog alertDialog = new AlertDialog.Builder(context)
						.create();
				alertDialog.setTitle("Email Sent");
				alertDialog
						.setMessage("An email with password reset instruction has been sent to your registered email address."
								+ "Please check your inbox or spam/junk folder to follow instruction and change your password");
				alertDialog.setButton("OK! Got it",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// here you can add functions

								alertDialog.dismiss();
							}
						});

				// alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
			try {
				Uri selectedImageUri = data.getData();
				pro_image = getPath(selectedImageUri);
				System.out.println(pro_image);
				changeProPic(pro_image);
				// imageLoader.displayImage(pro_image, imageView, options);
				// keeping trace that the picture is selected
				profile_pic_selected = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void changeProPic(String selectedImagePath) {
		try {
			if (imageView != null) {
				System.out.println(selectedImagePath);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageURI(Uri.parse(selectedImagePath));
				// s
				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				editor.putString(HashStatic.HASH_image_url, selectedImagePath)
						.commit();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPath(Uri uri) {

		if (uri == null) {
			return null;
		}

		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}

		return uri.getPath();
	}

	public void makeToast(String str) {
		final String strr = str;

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				if (context.getTheme().resolveAttribute(
						android.R.attr.actionBarSize, tv, true)) {
					actionBarHeight = TypedValue.complexToDimensionPixelSize(
							tv.data, getResources().getDisplayMetrics());
				}
				Toast tt = new Toast(context);
				tt.setView(layout);
				tt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0,
						actionBarHeight + 5);
				tt.show();
			}
		});
	}

	public class Async_final_call extends AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		JSONParser jParser = null;
		HashMap<String, String> showmap = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading Data...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					@SuppressWarnings("deprecation")
					String url_select = base_url
							+ BaseURL.API
							+ "profile.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + user_id
									+ "\"}", "utf-8");
					System.out.println(url_select);

					jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"GET", params);

					System.out.println(jobj.toString());

					String message = jobj.getString("message");
					if (message != null && message.equals("success")) {
						JSONObject jCustomer = jobj
								.getJSONObject(HashStatic.HASH_customer_m6);

						String fname = jCustomer
								.getString(HashStatic.HASH_cust_fname_m6);
						String cust_lname = jCustomer
								.getString(HashStatic.HASH_cust_lname_m6);
						String cust_email = jCustomer
								.getString(HashStatic.HASH_cust_email_m6);
						String cust_city = jCustomer
								.getString(HashStatic.HASH_cust_city_m6);
						String cust_image = jCustomer
								.getString(HashStatic.HASH_cust_image_m6);
						String cust_mobile = jCustomer
								.getString(HashStatic.HASH_cust_mobile_m6);
						String cust_gender = jCustomer
								.getString(HashStatic.HASH_cust_gender_m6);
						String cust_birth_date = jCustomer
								.getString(HashStatic.HASH_cust_birth_date_m6);
						String cust_address = jCustomer
								.getString(HashStatic.HASH_cust_address_m6);

						if (fname != null && !fname.equals("")
								&& !fname.equals("null"))
							showmap.put(HashStatic.HASH_cust_fname_m6, fname);
						else {
							showmap.put(HashStatic.HASH_cust_fname_m6, "N/A");
						}
						if (cust_lname != null && !cust_lname.equals("")
								&& !cust_lname.equals("null"))
							showmap.put(HashStatic.HASH_cust_lname_m6,
									cust_lname);
						else {
							showmap.put(HashStatic.HASH_cust_lname_m6, "N/A");
						}
						if (cust_email != null && !cust_email.equals("")
								&& !cust_email.equals("null"))
							showmap.put(HashStatic.HASH_cust_email_m6,
									cust_email);
						else {
							showmap.put(HashStatic.HASH_cust_email_m6, "N/A");
						}
						if (cust_city != null && !cust_city.equals("")
								&& !cust_city.equals("null"))
							showmap.put(HashStatic.HASH_cust_city_m6, cust_city);
						else {
							showmap.put(HashStatic.HASH_cust_city_m6, "N/A");
						}
						if (cust_image != null && !cust_image.equals("")
								&& !cust_image.equals("null"))
							showmap.put(HashStatic.HASH_cust_image_m6,
									cust_image);
						else {
							showmap.put(HashStatic.HASH_cust_image_m6, "N/A");
						}
						if (cust_mobile != null && !cust_mobile.equals("")
								&& !cust_mobile.equals("null"))
							showmap.put(HashStatic.HASH_cust_mobile_m6,
									cust_mobile);
						else {
							showmap.put(HashStatic.HASH_cust_mobile_m6, "N/A");
						}
						if (cust_gender != null && !cust_gender.equals("")
								&& !cust_gender.equals("null"))
							showmap.put(HashStatic.HASH_cust_gender_m6,
									cust_gender);
						else {
							showmap.put(HashStatic.HASH_cust_gender_m6, "N/A");
						}
						if (cust_birth_date != null
								&& !cust_birth_date.equals("0000-00-00")
								&& !cust_birth_date.equals("null"))
							showmap.put(HashStatic.HASH_cust_birth_date_m6,
									cust_birth_date);
						else {
							showmap.put(HashStatic.HASH_cust_birth_date_m6,
									"N/A");
						}
						if (cust_address != null && !cust_address.equals("")
								&& !cust_address.equals("null"))
							showmap.put(HashStatic.HASH_cust_address_m6,
									cust_address);
						else {
							showmap.put(HashStatic.HASH_cust_address_m6, "N/A");
						}
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
			if (!showmap.isEmpty()) {
				first_name_et.setText(showmap
						.get(HashStatic.HASH_cust_fname_m6));
				lastname_et.setText(showmap.get(HashStatic.HASH_cust_lname_m6));
				email_et.setText(showmap.get(HashStatic.HASH_cust_email_m6));
				// password_et.setText(HashStatic.HASH_cust_fname_m6);
				birthday_et.setText(showmap
						.get(HashStatic.HASH_cust_birth_date_m6));
				mobileNo_et
						.setText(showmap.get(HashStatic.HASH_cust_mobile_m6));
				city_et.setText(showmap.get(HashStatic.HASH_cust_city_m6));
				address_et
						.setText(showmap.get(HashStatic.HASH_cust_address_m6));

				first_name_et.setOnTouchListener(mListener_edittext);
				lastname_et.setOnTouchListener(mListener_edittext);
				email_et.setOnTouchListener(mListener_edittext);
				birthday_et.setOnTouchListener(mListener_edittext);
				mobileNo_et.setOnTouchListener(mListener_edittext);
				city_et.setOnTouchListener(mListener_edittext);
				address_et.setOnTouchListener(mListener_edittext);

				MemoryCacheUtil.removeFromCache(
						base_url + showmap.get(HashStatic.HASH_cust_image_m6),
						imageLoader.getMemoryCache());
				DiscCacheUtil.removeFromCache(
						base_url + showmap.get(HashStatic.HASH_cust_image_m6),
						ImageLoader.getInstance().getDiscCache());
				imageLoader.displayImage(
						base_url + showmap.get(HashStatic.HASH_cust_image_m6),
						imageView, options);

				System.out.println("The URL is: " + base_url
						+ showmap.get(HashStatic.HASH_cust_image_m6));

				// saving only name and email for myaccount drawer use
				editor.putString(
						HashStatic.HASH_firstNAME,
						String.valueOf(showmap
								.get(HashStatic.HASH_cust_fname_m6))).commit();
				editor.putString(HashStatic.HASH_LastNAME,
						showmap.get(HashStatic.HASH_cust_lname_m6)).commit();
				editor.putString(HashStatic.HASH_email,
						showmap.get(HashStatic.HASH_cust_email_m6)).commit();

				editor.putString(
						HashStatic.HASH_image_url,
						String.valueOf(showmap
								.get(HashStatic.HASH_cust_image_m6))).commit();
				profile_pic_selected = false;

				if (showmap.get(HashStatic.HASH_cust_gender_m6) != null) {
					if (showmap.get(HashStatic.HASH_cust_gender_m6).equals(
							"male")) {
						male_bt.setChecked(true);
					} else if (showmap.get(HashStatic.HASH_cust_gender_m6)
							.equals("female")) {
						female_bt.setChecked(true);
					}
				}

			}

			// TabActivity ac=getActivity().getlo

			/*
			 * String catId_str; if(catId!=-1) catId_str
			 * =header.get(catId).get(HASH_CAT_ID);
			 */

		}

	}

	public class SubmitAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		int cust_Id;
		int successful = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Submitting Data...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String email = parametersList.get(HASH_email);
				String fname = parametersList.get(HASH_firstNAME);
				String lastName = parametersList.get(HASH_lastname);
				String Bdate = parametersList.get(HASH_date);
				String gender = parametersList.get(HASH_gender);
				String mobile = parametersList.get(HASH_mobile);
				String city = parametersList.get(HASH_city);
				String address = parametersList.get(HASH_address);
				String picture = parametersList.get(HASH_picture);
				// String baseUrl = parametersList.get(HASH_baseUrl);
				// String country = parametersList.get(HASH_country);

				registration_preference = context.getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				editor = registration_preference.edit();

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					if (picture != null) {
						params.add(new BasicNameValuePair("image", picture));
						System.out.println("imageBitmap :::" + picture);
					}

					String url_select = base_url
							+ BaseURL.API
							+ "profile.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"firstname\":\"" + fname
									+ "\"" + ",\"lastname\":\"" + lastName
									+ "\"" + ",\"auth_type\":\"" + "normal"
									+ "\"" + ",\"gender\":\"" + gender + "\""
									+ ",\"birthday\":\"" + Bdate + "\""
									+ ",\"mobile\":\"" + mobile + "\""
									+ ",\"city\":\"" + city + "\""
									+ ",\"address\":\"" + address + "\""
									+ ",\"customer_id\":\"" + user_id + "\""
									// + ",\"country\":\"" + country + "\""
									+ ",\"email\":\"" + email + "\"" + "}",
									"utf-8");

					System.out.println(url_select);

					JSONParser jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);
					String msg = jobj.getString("message");

					// System.out.println("CustomerID" + cust_Id);
					if (msg != null && msg.contains("successful")) {
						makeToast("Editting successful...");
						successful = 1;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				makeToast("Internet Connection not Available");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (successful != 1) {
				MyToast.makeToast("Something went wrong. Please try again...",
						context);
			}
		}
	}

	public class Async_resetPassword_call extends AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		JSONParser jParser = null;
		HashMap<String, String> showmap = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Submitting reset password request...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();

					registration_preference = context.getSharedPreferences(
							HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
					String email = registration_preference.getString(
							HashStatic.HASH_email, null);
					@SuppressWarnings("deprecation")
					String url_select = base_url
							+ BaseURL.API
							+ "password-reset.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8") + "&JSONParam="
							+ URLEncoder.encode("{\"email\":\"" + email

							+ "\"}", "utf-8");
					System.out.println(url_select);

					jParser = new JSONParser();
					JSONObject jobj = jParser.makeHttpRequest(url_select,
							"POST", params);

					String message = jobj.getString("message");
					if (message != null && message.equals("success")) {
						showReset_SuccessfulAlert();
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

}
