package com.yozard.pp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.yozard.pp.model.WhatsNew;
import com.yozard.pp.utils.AnimatedGifImageView;
import com.yozard.pp.utils.AnimatedGifImageView.TYPE;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.HashStatic;

public class SplashActivity extends Activity {

	public boolean isRegistered = false;

//	Animation anim;
	// ImageView mImageView;
	SharedPreferences registration_preference;
	AnimatedGifImageView playGifView;

	boolean isAppUpdate, isFirstTimeLogin;
	String custId, custEmail, info;
	WhatsNew whatsNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		HashStatic.showHashKey(this);
		playGifView = (AnimatedGifImageView) findViewById(R.id.viewGif);
		playGifView.setAnimatedGif(R.drawable.yozard_splash,
				TYPE.AS_IS);

		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);

		custId = registration_preference
				.getString(HashStatic.CUSTOMER_ID, null);
		custEmail = registration_preference.getString(
				HashStatic.CUSTOMER_EMAIL, "");
		isAppUpdate = registration_preference.getBoolean(
				HashStatic.Hash_AppUpdate, false);
		info = registration_preference.getString(HashStatic.Hash_UpdateInfo,
				null);
		isFirstTimeLogin = registration_preference.getBoolean(HashStatic.Hash_FirstTimeLogin, true);
		if(isFirstTimeLogin){
			registration_preference.edit().putBoolean(HashStatic.Hash_FirstTimeLogin, false).commit();
			registration_preference.edit().putString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token).commit();
		}

		checkAppVersion(info);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(playGifView.getWindowToken(), 0);

		Handler handler = new Handler();

		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (isAppUpdate) {
					Intent intent = new Intent(SplashActivity.this,
							UpdateAppActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					finish();
				} else {
					if (custId == null) {
						Intent intent = new Intent(SplashActivity.this,
								StarterActivity.class);
						startActivity(intent);
						finish();
						// System.out.println("HIII");
					} else {
						Intent intent;
						if (!TextUtils.isEmpty(custEmail)) {
							intent = new Intent(SplashActivity.this,
									TabActivity.class);
						} else {
							intent = new Intent(SplashActivity.this,
									VerifyEmailActivity.class);
						}
						startActivity(intent);
						finish();
						// System.out.println("HIII2");
					}
				}

			}

		}, 1500);
	}

	private void checkAppVersion(String info) {
		try {
			String version = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			Log.e("App Version", version);
			Gson g = new Gson();
			if (info != null && isAppUpdate) {
				whatsNew = g.fromJson(info, WhatsNew.class);
				Log.e("App Version 2", whatsNew.getApp_version());
				if (version.equalsIgnoreCase(whatsNew.getApp_version())) {
					Editor editor = registration_preference.edit();
					editor.putBoolean(HashStatic.Hash_AppUpdate, false);
					isAppUpdate = false;
				}
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
