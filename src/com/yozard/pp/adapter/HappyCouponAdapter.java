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

public class HappyCouponAdapter extends ArrayAdapter<Coupon> {

	Context con;
	Vector<Coupon> hCoupons;
	int viewId;
	SharedPreferences registration_preference;
	String base_url, userId_sp, auth_token;

	// Offer offer;

	public HappyCouponAdapter(Context context, int resource,
			Vector<Coupon> hCoupons) {
		super(context, resource, hCoupons);
		this.con = context;
		this.viewId = resource;
		this.hCoupons = hCoupons;
		registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		auth_token = registration_preference.getString(HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
	}

	private class ViewHolder {
		TextView tvRemainingTime, tvCompanyName, tvCompanyArea, tvCouponStatus,
				tvOfferTitle, tvDiscountAmount;
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
			convertView = inflater.inflate(R.layout.happy_coupon_row, null);
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
			holder.tvOfferTitle = (TextView) convertView
					.findViewById(R.id.tvOfferTitle);
			holder.tvRemainingTime = (TextView) convertView
					.findViewById(R.id.tvRemainingTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvCouponStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (hCoupons.get(position).getUsage_status()
						.equalsIgnoreCase("book")) {
					// Book Coupon
					bookCoupon(position, hCoupons.get(position).getHhc_id());
				} else {
					// Use Coupon
					UseCouponActivity.usableHCoupon = hCoupons.get(position);
					con.startActivity(new Intent(con, UseCouponActivity.class)
							.putExtra(HashStatic.HASH_LIVE, false));
					// useCoupon(position, coupons.get(position).getLcc_id());
				}
			}
		});

		holder.ivFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Coupon coupon = hCoupons.get(position);
				if (coupon.isFollowing()) {
					followCompany(coupon.getCompany_id(), coupon.getHhc_id(),
							"DEL", position);
				} else {
					followCompany(coupon.getCompany_id(), coupon.getHhc_id(),
							"POST", position);
				}
			}
		});

		holder.rlContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (hCoupons.get(position).getUsage_status()
						.equalsIgnoreCase("book")) {
					// Book Coupon
					CouponDetailsActivity.hCoupon = hCoupons.get(position);
					con.startActivity(new Intent(con,
							CouponDetailsActivity.class).putExtra(
							HashStatic.HASH_LIVE, false));
					// bookCoupon(position, coupons.get(position).getLcc_id());
				} else {
					// Use Coupon
					UseCouponActivity.usableHCoupon = hCoupons.get(position);
					con.startActivity(new Intent(con, UseCouponActivity.class)
							.putExtra(HashStatic.HASH_LIVE, false));
					// useCoupon(position, coupons.get(position).getLcc_id());
				}

			}
		});

		if (position < hCoupons.size()) {
			setDataInRow(position, holder);
		}

		return convertView;
	}

	private void setDataInRow(int position, ViewHolder holder) {
		Coupon aCoupon = hCoupons.get(position);
		String remaintingTime = getRemainingTime(aCoupon);
		setImageFromUrl(holder.ivCompanyImage, aCoupon.getCompany_image());
		holder.tvCompanyName.setText(aCoupon.getCompany_name());
		holder.tvCompanyArea.setText(aCoupon.getArea_name());
		holder.tvRemainingTime.setText(remaintingTime);
		setCouponUsageStatus(aCoupon.getUsage_status(), holder);
		// holder.tvCouponStatus.setText(aCoupon.getUsage_status());
		if (aCoupon.isFollowing()) {
			holder.ivFollow.setImageResource(R.drawable.ic_label_follow);
		} else {
			holder.ivFollow.setImageResource(R.drawable.ic_label_unfollow);
		}
		// Gson g = new Gson();
		Offer anOffer = aCoupon.gethOffer();
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

	private void setCouponUsageStatus(String usage, ViewHolder holder) {
		if (usage.equalsIgnoreCase("book")) {
			holder.tvCouponStatus.setText("SAVE");
		} else if (usage.equalsIgnoreCase("use")) {
			holder.tvCouponStatus.setText("USE");
		} else {
			holder.tvCouponStatus.setText("PENDING");
		}
	}

	// img/companies/

	private void setImageFromUrl(ImageView imageView, String imgPath) {
		SharedPreferences registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String base_url = registration_preference.getString(
				HashStatic.HASH_baseUrl, null);
		String imgUrl = base_url + modifyImagePath(imgPath);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true).showImageForEmptyUri(null)
				.showImageOnFail(null).showImageOnLoading(null).build();
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

	private String getRemainingTime(Coupon hCoupon) {
		// String endTime = hCoupon.getEnd_date() + " 23:59";
		String rlTime = "";
		Calendar cal = Calendar.getInstance();
		String currentDate = cal.get(Calendar.YEAR) + "-"
				+ cal.get(Calendar.MONTH) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH);
		String currentTime = currentDate + " " + cal.get(Calendar.HOUR_OF_DAY)
				+ ":" + cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND);
		Calendar cal2 = Calendar.getInstance();
		String startTime = currentDate + " " + hCoupon.getAllowed_start_time();
		String endTime = currentDate + " " + hCoupon.getAllowed_end_time();
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startT = formatDate(startTime, dtFormat);
		Date endT = formatDate(endTime, dtFormat);
		Date currentT = formatDate(currentTime, dtFormat);
		if (currentT.before(startT)||currentT.after(endT)) {
			cal.setTime(startT);
			cal2.setTime(endT);
			rlTime = getHour(cal.get(Calendar.HOUR))
					+ setAmPm(cal.get(Calendar.AM_PM)) + "-"
					+ getHour(cal2.get(Calendar.HOUR))
					+ setAmPm(cal2.get(Calendar.AM_PM));
		} else {
			cal.setTime(currentT);
			cal2.setTime(endT);
			long remainingTime = cal2.getTimeInMillis() - cal.getTimeInMillis();
			int day = (int) ((((remainingTime / 1000) / 60) / 60) / 24);
			remainingTime = remainingTime - (long) day * 24 * 60 * 60 * 1000;
			int hour = (int) (((remainingTime / 1000) / 60) / 60);
			remainingTime = remainingTime - (long) hour * 60 * 60 * 1000;
			int minute = (int) ((remainingTime / 1000) / 60);
			remainingTime = remainingTime - (long) minute * 60 * 1000;
			int seconds = (int) remainingTime / 1000;
			if (hour == 0 && minute > 0) {
				rlTime = minute + "m:" + seconds + "s";
			} else if (hour == 0 && minute == 0) {
				rlTime = seconds + "s";
			} else {
				rlTime = hour + "h:" + minute + "m:" + seconds + "s";
			}
			// tvTime.setText("");
		}
		return rlTime;
	}

	private String setAmPm(int ampm) {
		if (ampm == 1) {
			return "pm";
		} else {
			return "am";
		}
	}

	private String getHour(int hour) {
		if (hour == 0) {
			return 12 + "";
		} else {
			return hour + "";
		}
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
							+ "happy-hours.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\"" + userId
									+ "\",\"campaign_id\":\"" + campaign_id
									+ "\"}", "utf-8");

					JSONObject jobj = jparser.makeHttpRequest(url_select3,
							"POST", paramiters);

					message = jobj.getString("message");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// Toast.makeText(con, "Network connection not available.",
				// Toast.LENGTH_SHORT).show();
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
				hCoupons.get(position).setUsage_status("use");
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
							+ URLEncoder.encode(auth_token, "utf-8")
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

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// Toast.makeText(con, "Network connection not available.",
				// Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			MyToast.makeToast(message, con);
			if (message.contains("success")) {
				if (hCoupons.get(position).isFollowing()) {
					hCoupons.get(position).setFollowing(false);
				} else {
					hCoupons.get(position).setFollowing(true);
				}
				notifyDataSetChanged();
			}
		}

	}

}
