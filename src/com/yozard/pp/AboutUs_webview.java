package com.yozard.pp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yozard.pp.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.TypeFace_MY;

public class AboutUs_webview extends ActionBarActivity {

	WebView wb;
//	TextView title_tv;
	ProgressBar Pbar;
//	ImageButton backTitle_bt;
	
	String currency = null, user_id = null;
	String Pendingbalance = null;
	SharedPreferences registration_preference;
	String base_url;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("About");
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.orange1));

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
		registration_preference=getSharedPreferences(HashStatic.PREF_NAME_REG,Context.MODE_PRIVATE);
		String userId_sp=registration_preference.getString(HashStatic.CUSTOMER_ID, null);
		String currency_sp=registration_preference.getString(HashStatic.HASH_currency, null);
		base_url=registration_preference.getString(HashStatic.HASH_baseUrl, null);
		
		user_id = userId_sp;
		currency = currency_sp;

		
		Pbar = (ProgressBar) findViewById(R.id.progressBar_webview);

		wb = (WebView) findViewById(R.id.Mywebview_wb);
		WebSettings webSettings = wb.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wb.loadUrl(base_url+"about.php");

		wb.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
					Pbar.setVisibility(ProgressBar.VISIBLE);
				}
				// Pbar.setProgress(progress);
				if (progress == 100) {
					Pbar.setVisibility(ProgressBar.GONE);
					view.setVisibility(View.VISIBLE);
				}
			}
		});

	}

}
