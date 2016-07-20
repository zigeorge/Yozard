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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yozard.pp.R;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;
import com.yozard.pp.utils.TypeFace_MY;

public class VerifyEmailActivity extends Activity {

	TextView tvCompleteProfile;
	Button btnDone;
	EditText etFullName, etMail;

	// Actionbar items
	// ImageButton backTitle_bt, search_bt;
	// TextView title_tv, title_footer_tv;
	// ActionBar aBar;

	Typeface tf_roboto_condensed, tf_rancho;

	String firstName = "";
	String lastName = "";

	SharedPreferences registration_preference;
	String base_url, auth_token;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_email);

		context = this;

		tf_roboto_condensed = TypeFace_MY.getRoboto_condensed(context);
		tf_rancho = TypeFace_MY.getRancho(context);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setTintColor(Color.parseColor("#ff8c00"));
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.drawable.orange);
		}

		initUI();
	}

	private void initUI() {
		tvCompleteProfile = (TextView) findViewById(R.id.tvCompleteprofile);
		tvCompleteProfile.setTypeface(tf_roboto_condensed);
		etFullName = (EditText) findViewById(R.id.etFullName);
		etFullName.setTypeface(tf_rancho);
		etMail = (EditText) findViewById(R.id.etMail);
		etMail.setTypeface(tf_rancho);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setTypeface(tf_rancho);
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fullName = etFullName.getText().toString();
				String mail = etMail.getText().toString();
				if (verifyName(fullName)) {
					if (TextUtils.isEmpty(mail)) {
						etMail.setError("Your Email Please");
					} else {
						if (checkMail(mail)) {
							verifyProfile(mail, firstName, lastName);
						} else {
							etMail.setError("Invalid Email");
						}
					}
				}
			}
		});

	}

	private boolean verifyName(String fullname) {
		if (!TextUtils.isEmpty(fullname)) {
			Log.e("Full Name", fullname);
			splitWords(fullname);
			if (firstName.length() <= 2) {
				splitWords(lastName);
			}
			return true;
		} else {
			etFullName.setError("Your full name please");
			return false;
		}
	}

	private void splitWords(String s) {

		int wordCount = 0;
		Log.e("FullName", s);
		boolean word = false;
		int endOfLine = s.length() - 1;

		for (int i = 0; i < s.length(); i++) {
			// if the char is a letter, word = true.
			if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
				word = true;
			} else if (!Character.isLetter(s.charAt(i)) && word) {
				wordCount++;
				if (wordCount == 1) {
					firstName = s.substring(0, i);
					lastName = s.substring(i + 1);
				}
				word = false;
			} else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
				wordCount++;
				if (wordCount == 1) {
					firstName = s;
					lastName = "";
				}
			}
		}
	}

	private boolean checkMail(String emailText) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
	}

	private void verifyProfile(String mail, String fName, String lName) {
		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		String userId_sp = registration_preference.getString(
				HashStatic.CUSTOMER_ID, null);
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.CUSTOMER_EMAIL, mail);
		profileMap.put(HashStatic.HASH_firstNAME, fName);
		profileMap.put(HashStatic.HASH_LastNAME, lName);
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		Log.e("ID >> >> ", userId_sp);
		new DoneAsync().execute(profileMap);
	}

	public class DoneAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		Editor editor;
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
			progressDialog.setMessage("Verifying...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String email = parametersList.get(HashStatic.CUSTOMER_EMAIL);
				String fName = parametersList.get(HashStatic.HASH_firstNAME);
				String lName = parametersList.get(HashStatic.HASH_LastNAME);
				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String userId = parametersList.get(HashStatic.CUSTOMER_ID);

				registration_preference = getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {

					String url_select3 = baseUrl
							+ BaseURL.API
							+ "update-customer.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + userId
									+ "\",\"firstname\":\"" + fName
									+ "\",\"lastname\":\"" + lName
									+ "\",\"email\":\"" + email + "\"}",
									"utf-8");
					Log.e("JSON String >> ", "{\"customer_id\":\"" + userId
							+ "\",\"firstname\":\"" + fName
							+ "\",\"lastname\":\"" + lName + "\",\"email\":\""
							+ email + "\"}");

					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"POST", paramiters);

					System.out.println(url_select3);

					message = jobj.getString("message");
					JSONObject jCust = jobj.getJSONObject("customer");
					cust_Id = jCust.getInt(HashStatic.CUSTOMER_ID);
					cust_status = jCust.getInt(HashStatic.CUSTOMER_STATUS);
					customer_email = jCust.getString(HashStatic.CUSTOMER_EMAIL);
					
					editor.putString(HashStatic.CUSTOMER_ID,
							String.valueOf(cust_Id)).commit();
					editor.putString(HashStatic.HASH_email, customer_email).commit();
					editor.putString(HashStatic.HASH_firstNAME,
							String.valueOf(fName)).commit();
					editor.putString(HashStatic.HASH_LastNAME,
							String.valueOf(lName)).commit();
					editor.putString(HashStatic.CUSTOMER_STATUS,
							String.valueOf(cust_status)).commit();

					Log.e("Message", message);
					Log.e("ID", cust_Id + "");
					Log.e("Status", cust_status + "");
					Log.e("Email", customer_email);

//					MyToast.makeToast(message, context);

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
			if (message.contains("success")) {
				MyToast.makeToast(message, context);
				Intent intent = new Intent(context, TabActivity.class);
				startActivity(intent);
				finish();

			} else {
				MyToast.makeToast(message, context);
			}
		}

	}

}
