package com.yozard.pp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManagerPromo {

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == cm.TYPE_WIFI)
				return HashStatic.TYPE_WIFI;

			if (activeNetwork.getType() == cm.TYPE_MOBILE)
				return HashStatic.TYPE_MOBILE;
		}
		return HashStatic.TYPE_NOT_CONNECTED;
	}
	
}
