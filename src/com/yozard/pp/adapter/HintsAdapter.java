package com.yozard.pp.adapter;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.yozard.pp.R;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Offer;
import com.yozard.pp.utils.HashStatic;

/**
 * Created by AAPBD k on 9/13/2015.
 */
public class HintsAdapter extends ArrayAdapter<Coupon> implements Filterable {

	Context con;
	Vector<Coupon> hintsList;
	private StringBuilder mSb = new StringBuilder();

	public HintsAdapter(Context context) {
		super(context, -1);
		this.con = context;
	}

	private class ViewHolder {
		TextView tvCompanyname, tvAreaname, tvDiscountamount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.hints_row, null);
			holder = new ViewHolder();
			holder.tvCompanyname = (TextView) convertView
					.findViewById(R.id.tvCompanyname);
			holder.tvAreaname = (TextView) convertView
					.findViewById(R.id.tvAreaname);
			holder.tvDiscountamount = (TextView) convertView
					.findViewById(R.id.tvDiscountamount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < HashStatic.hintsList.size()) {
			holder.tvCompanyname.setText(setCompanyName(getItem(position)));
			holder.tvAreaname.setText(setAreaName(getItem(position)));
			holder.tvDiscountamount.setText(setDiscountAmount(getItem(position)));
		}

		return convertView;
	}

	private String setCompanyName(final Coupon hint) {
		mSb.setLength(0);
		mSb.append(hint.getCompany_name());
		return mSb.toString();
	}

	private String setAreaName(final Coupon hint) {
		mSb.setLength(0);
		mSb.append(hint.getArea_name());
		return mSb.toString();
	}

	private String setDiscountAmount(final Coupon hint){
		mSb.setLength(0);
		if(HashStatic.HASH_SEARCH_LIVE){
			Offer anOffer = hint.getCoupon_offer();
			mSb.append(anOffer.getOffer_amount());
		}else{
			Offer anOffer = hint.gethOffer();
			mSb.append(anOffer.getOffer_amount());
		}
		return mSb.toString()+"%";
	}

	@Override
	public Filter getFilter() {
		// hintsList
		Filter searchFilter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				Vector<Coupon> hints = null;
				if (charSequence != null) {
					try {
						hints = HashStatic.hintsList;
					} catch (Exception e) {
					}
				}
				if (hints == null) {
					hints = new Vector<Coupon>();
				}
				final FilterResults filterResults = new FilterResults();
				filterResults.values = hints;
				filterResults.count = hints.size();

				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence,
					FilterResults filterResults) {
				clear();
				for (Coupon hint : (List<Coupon>) filterResults.values) {
					add(hint);
				}
				if (filterResults.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			public CharSequence convertResultToString(Object resultValue) {
				return resultValue == null ? ""
						: setCompanyName((Coupon) resultValue);
			}
		};
		return searchFilter;
	}

	// private Vector<Hint> getSearchHints(String term) {
	// String hintUrl = AllUrl.getUrl(AllUrl.API_NAME_HINT);
	//
	// AsyncHttpClient client = new AsyncHttpClient();
	//
	// RequestParams params = new RequestParams();
	// params.add("filter", "all");
	// params.add("term", term);
	// Log.e("Term >>>", term);
	// client.addHeader("name", "Content-Type");
	// client.addHeader("value", "application/form-data");
	// client.addHeader("token", AppDataHolder.getToken(con));
	// client.get(hintUrl, params, new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, byte[]
	// responseBody) {
	//
	// String resp = new String(responseBody);
	// Gson g = new Gson();
	// HintResponse hintResponse = g.fromJson(resp, HintResponse.class);
	// if (hintResponse.isStatus() && !hintResponse.isEmpty()) {
	// hintsList = hintResponse.getResult().getData();
	// } else {
	// // Toast.makeText(con, hintResponse.getMessage(),
	// Toast.LENGTH_LONG).show();
	// }
	// // notifyDataSetChanged();
	//
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers, byte[]
	// responseBody, Throwable error) {
	// // Toast.makeText(con, "Try again later!", Toast.LENGTH_SHORT).show();
	// error.printStackTrace();
	// }
	// });
	// return hintsList;
	//
	// }
}
