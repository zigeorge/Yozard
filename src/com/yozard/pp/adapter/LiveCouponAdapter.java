package com.yozard.pp.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yozard.pp.CouponDetailsActivity;
import com.yozard.pp.R;
import com.yozard.pp.UseCouponActivity;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Offer;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;

public class LiveCouponAdapter extends ArrayAdapter<Coupon> {

	Context con;
	Vector<Coupon> coupons;
	int viewId;
	SharedPreferences registration_preference;
	String base_url, userId_sp, auth_token;

	// Offer offer;

	public LiveCouponAdapter(Context context, int resource, Vector<Coupon> coupons) {
		super(context, resource, coupons);
		this.con = context;
		this.viewId = resource;
		this.coupons = coupons;
		registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
	}

	private class ViewHolder {
		TextView tvRemainingTime, tvCompanyName, tvCompanyArea,
				tvBooked, tvLeft, tvCouponStatus, tvOfferTitle,
				tvDiscountAmount;    //tvUsed, 
		ImageView ivOfferImage, ivCompanyImage, ivFollow;
		RelativeLayout rldiscount, rlContainer;
		LinearLayout llOtherOffer;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.coupon_row, null);
			holder = new ViewHolder();
			holder.ivCompanyImage = (ImageView) convertView
					.findViewById(R.id.ivCompanyImage);
			holder.ivOfferImage = (ImageView) convertView
					.findViewById(R.id.ivOfferImage);
			holder.llOtherOffer = (LinearLayout) convertView
					.findViewById(R.id.llOtherOffer);
			holder.rldiscount = (RelativeLayout) convertView
					.findViewById(R.id.rldiscount);
			holder.rlContainer = (RelativeLayout) convertView
					.findViewById(R.id.rlContainer);
			holder.tvBooked = (TextView) convertView
					.findViewById(R.id.tvBooked);
			holder.tvCompanyArea = (TextView) convertView
					.findViewById(R.id.tvCompanyArea);
			holder.tvCompanyName = (TextView) convertView
					.findViewById(R.id.tvCompanyName);
			holder.tvCouponStatus = (TextView) convertView
					.findViewById(R.id.tvCouponStatus);
			holder.tvDiscountAmount = (TextView) convertView
					.findViewById(R.id.tvDiscountAmount);
			holder.ivFollow = (ImageView) convertView
					.findViewById(R.id.ivFollow);
			holder.tvLeft = (TextView) convertView.findViewById(R.id.tvLeft);
			holder.tvOfferTitle = (TextView) convertView
					.findViewById(R.id.tvOfferTitle);
			holder.tvRemainingTime = (TextView) convertView
					.findViewById(R.id.tvRemainingTime);
//			holder.tvUsed = (TextView) convertView.findViewById(R.id.tvUsed);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvCouponStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (coupons.get(position).getCoupon_status()
						.equalsIgnoreCase("SAVE")) {
					// Book Coupon
					bookCoupon(position, coupons.get(position).getLcc_id());
				} else {
					// Use Coupon
					UseCouponActivity.usableCoupon = coupons.get(position);
					con.startActivity(new Intent(con, UseCouponActivity.class).putExtra(HashStatic.HASH_LIVE, true));
//					useCoupon(position, coupons.get(position).getLcc_id());
				}
			}
		});

		holder.ivFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Coupon coupon = coupons.get(position);
				if (coupon.isFollowing()) {
					followCompany(coupon.getCompany_id(), coupon.getLcc_id(),
							"DEL", position);
				} else {
					followCompany(coupon.getCompany_id(), coupon.getLcc_id(),
							"POST", position);
				}
			}
		});

		holder.rlContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (coupons.get(position).getCoupon_status()
						.equalsIgnoreCase("SAVE")) {
					// Book Coupon
					CouponDetailsActivity.coupon = coupons.get(position);
					con.startActivity(new Intent(con,
							CouponDetailsActivity.class).putExtra(HashStatic.HASH_LIVE, true));
					// bookCoupon(position, coupons.get(position).getLcc_id());
				} else {
					// Use Coupon
					UseCouponActivity.usableCoupon = coupons.get(position);
					con.startActivity(new Intent(con, UseCouponActivity.class).putExtra(HashStatic.HASH_LIVE, true));
//					 useCoupon(position, coupons.get(position).getLcc_id());
				}

			}
		});

		if (position < coupons.size()) {
			setDataInRow(position, holder);
		}

		return convertView;
	}

	private void setDataInRow(int position, ViewHolder holder) {
		Coupon aCoupon = coupons.get(position);
		String remaintingTime = getRemainingTime(aCoupon);
		setImageFromUrl(holder.ivCompanyImage, aCoupon.getCompany_image());
		holder.tvCompanyName.setText(aCoupon.getCompany_name());
		holder.tvCompanyArea.setText(aCoupon.getArea_name());
		holder.tvBooked.setText(aCoupon.getTotal_claimed() + " saved");
//		holder.tvUsed.setText(aCoupon.getTotal_used() + " used");
		holder.tvLeft.setText(getCouponLeft(aCoupon) + " Left");
		holder.tvRemainingTime.setText(remaintingTime);
		holder.tvCouponStatus.setText(aCoupon.getCoupon_status());
		if (aCoupon.isFollowing()) {
			holder.ivFollow.setImageResource(R.drawable.ic_label_follow);
		} else {
			holder.ivFollow.setImageResource(R.drawable.ic_label_unfollow);
		}
		// Gson g = new Gson();
		Offer anOffer = aCoupon.getCoupon_offer();
		String oType = anOffer.getOffer_type();
		if (oType.equalsIgnoreCase("discounts")) {
			// Discount
			holder.llOtherOffer.setVisibility(View.GONE);
			holder.rldiscount.setVisibility(View.VISIBLE);
			holder.tvDiscountAmount.setText(anOffer.getOffer_amount());
		} else {
			// Other
			holder.llOtherOffer.setVisibility(View.VISIBLE);
			holder.rldiscount.setVisibility(View.GONE);
			holder.tvOfferTitle.setText(anOffer.getOffer_title());
			setImageFromUrl(holder.ivOfferImage, anOffer.getOffer_image());
		}
	}

	private void setImageFromUrl(ImageView imageView, String imgPath) {
		SharedPreferences registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String imgUrl = base_url + modifyImagePath(imgPath);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(null)
				.showImageOnFail(null)
				.showImageOnLoading(null).build();
		imageLoader.displayImage(imgUrl, imageView, options);
	}
	
	private String modifyImagePath(String imagePath) {
		int eol = imagePath.length() - 1;
		for (int i = eol; i > 0; i--) {
			if (imagePath.charAt(i) == '/') {
				imagePath = imagePath.substring(0, i) + "/thumb"
						+ imagePath.substring(i, imagePath.length());
				break;
			}
		}
		return imagePath;
	}

	private int getCouponLeft(Coupon coupon) {
		int usageLimit = Integer.parseInt(coupon.getUsage_limit());
		int totalUsed = Integer.parseInt(coupon.getTotal_used());
		return usageLimit - totalUsed;
	}

	private String getRemainingTime(Coupon coupon) {
		String endTime = coupon.getEnd_time() + " 23:59";
		Calendar cal = Calendar.getInstance();
		long currentTimeMs = cal.getTimeInMillis();
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endDate = formatDate(endTime, dtFormat);
		long remainingTime = endDate.getTime() - currentTimeMs;
		int day = (int) ((((remainingTime / 1000) / 60) / 60) / 24);
		remainingTime = remainingTime - (long) day * 24 * 60 * 60 * 1000;
		int hour = (int) (((remainingTime / 1000) / 60) / 60);
		remainingTime = remainingTime - (long) hour * 60 * 60 * 1000;
		int minute = (int) ((remainingTime / 1000) / 60);
		String rTime = "";
		if (day > 0) {
			rTime = day + "D:" + hour + "H:" + minute + "M";
		} else if (day < 1 && hour > 0) {
			rTime = hour + "H:" + minute + "M";
		} else if (day < 1 && hour < 1 && minute > 0) {
			rTime = minute + "M";
		}
		return rTime;
	}

	private Date formatDate(String inputDate, SimpleDateFormat sdFormat) {
		Date dt = null;
		try {
			dt = sdFormat.parse(inputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}

	private void bookCoupon(int position, String campaign_id) {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		profileMap.put(HashStatic.CAMPAIGN_ID, campaign_id);
		profileMap.put("position", position + "");

		new BookCouponAsync().execute(profileMap);
	}

//	private void useCoupon(int position, String campaign_id) {
//		HashMap<String, String> profileMap = new HashMap<String, String>();
//		profileMap.put(HashStatic.HASH_baseUrl, base_url);
//		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
//		profileMap.put(HashStatic.CAMPAIGN_ID, campaign_id);
//		profileMap.put("position", position + "");
//
//		new UseCouponAsync().execute(profileMap);
//	}

	private void followCompany(String company_id, String campaign_id,
			String request, int position) {
		HashMap<String, String> profileMap = new HashMap<String, String>();
		profileMap.put(HashStatic.HASH_baseUrl, base_url);
		profileMap.put(HashStatic.CUSTOMER_ID, userId_sp);
		profileMap.put(HashStatic.CAMPAIGN_ID, campaign_id);
		profileMap.put(HashStatic.COMPANY_ID, company_id);
		profileMap.put("request", request + "");
		profileMap.put("position", position + "");
		new FollowAsync().execute(profileMap);
	}

	public class BookCouponAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		ProgressDialog progressDialog;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		String message = "";
		int position;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(con);
			progressDialog.setMessage("Claiming Coupons...");
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(con) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String userId = parametersList.get(HashStatic.CUSTOMER_ID);
				String campaign_id = parametersList.get(HashStatic.CAMPAIGN_ID);
				position = Integer.parseInt(parametersList.get("position"));

				try {

					String url_select3 = baseUrl
							+ BaseURL.API
							+ "live-coupons.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + userId
									+ "\",\"campaign_id\":\"" + campaign_id
									+ "\"}", "utf-8");

					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"POST", paramiters);

					message = jobj.getString("message");

					System.out.println(url_select3);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
//				Toast.makeText(con, "Network connection not available.",
//						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			MyToast.makeToast(message, con);
			if (message.contains("success")) {
				coupons.get(position).setCustomer_usage("0");
				notifyDataSetChanged();
			}
		}

	}

	public class FollowAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		String message = "";
		int position;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(con) != HashStatic.TYPE_NOT_CONNECTED) {
				HashMap<String, String> parametersList = paramMap[0];

				String baseUrl = parametersList.get(HashStatic.HASH_baseUrl);
				String userId = parametersList.get(HashStatic.CUSTOMER_ID);
				String campaign_id = parametersList.get(HashStatic.CAMPAIGN_ID);
				String company_id = parametersList.get(HashStatic.COMPANY_ID);
				String request = parametersList.get("request");
				position = Integer.parseInt(parametersList.get("position"));


				try {

					String url_select3 = baseUrl
							+ BaseURL.API
							+ "follow.php?authtoken="
							+ URLEncoder.encode(
									auth_token,
									"utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + userId
									+ "\",\"campaign_id\":\"" + campaign_id
									+ "\",\"company_id\":\"" + company_id
									+ "\",\"campaign_type\":\"live_coupon\"}",
									"utf-8");

					JSONObject jobj;

					if (request.equalsIgnoreCase("DEL")) {
						jobj = jparser.makeHttpRequest(url_select3, "DEL",
								paramiters);
					} else {
						jobj = jparser.makeHttpRequest(url_select3, "POST",
								paramiters);
					}

					message = jobj.getString("message");
					System.out.println(url_select3);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
//				Toast.makeText(con, "Network connection not available.",
//						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			MyToast.makeToast(message, con);
			if (message.contains("success")) {
				if (coupons.get(position).isFollowing()) {
					coupons.get(position).setFollowing(false);
				} else {
					coupons.get(position).setFollowing(true);
				}
				notifyDataSetChanged();
			}
		}

	}

}
