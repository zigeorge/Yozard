package com.yozard.pp.adapter;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yozard.pp.R;
import com.yozard.pp.model.Stores;
import com.yozard.pp.utils.HashStatic;

public class StoresAdapter extends ArrayAdapter<Stores> {

	Context con;
	Vector<Stores> stores;
	int viewId;
	SharedPreferences registration_preference;
	String base_url, userId_sp;

	// Offer offer;

	public StoresAdapter(Context context, int resource, Vector<Stores> stores) {
		super(context, resource, stores);
		this.con = context;
		this.viewId = resource;
		this.stores = stores;
		registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
	}

	private class ViewHolder {
		TextView tvStoreName, tvStoreAddress, tvStoreMobile;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.stores_row, null);
			holder = new ViewHolder();
			Log.e("No. Of Stores >>> ", stores.size() + "");
			holder.tvStoreAddress = (TextView) convertView
					.findViewById(R.id.tvStoreAddress);
			holder.tvStoreName = (TextView) convertView
					.findViewById(R.id.tvStoreName);
			holder.tvStoreMobile = (TextView) convertView
					.findViewById(R.id.tvStoreMobile);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < stores.size()) {
			setDataInRow(position, holder);
		}

		return convertView;
	}

	private void setDataInRow(int position, ViewHolder holder) {
		Stores aStore = stores.get(position);
		if(!TextUtils.isEmpty(aStore.getStore_name())){
		holder.tvStoreName.setText(aStore.getStore_name());
		}else{
			holder.tvStoreName.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(aStore.getStore_address())&&!TextUtils.isEmpty(aStore.getStore_zip_code())) {
			holder.tvStoreAddress.setText(aStore.getStore_address() + ","
					+ aStore.getStore_zip_code());
		}else if(!TextUtils.isEmpty(aStore.getStore_address())&&TextUtils.isEmpty(aStore.getStore_zip_code())){
			holder.tvStoreAddress.setText(aStore.getStore_address());
		}else{
			holder.tvStoreAddress.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(aStore.getStore_mobile())){
			holder.tvStoreMobile.setText(aStore.getStore_mobile());
		}else{
			holder.tvStoreMobile.setVisibility(View.GONE);
		}
	}

}
