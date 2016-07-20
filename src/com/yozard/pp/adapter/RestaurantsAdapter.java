package com.yozard.pp.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yozard.pp.R;
import com.yozard.pp.RestaurantDetailActivity;
import com.yozard.pp.model.Restaurant;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;
import com.yozard.pp.utils.MyToast;

public class RestaurantsAdapter extends ArrayAdapter<Restaurant> {

	Context con;
	Vector<Restaurant> restaurants;
	int resId;
	SharedPreferences registration_preference;
	String base_url, userId_sp, auth_token;

	// Offer offer;

	public RestaurantsAdapter(Context context, int resource,
			Vector<Restaurant> restaurants) {
		super(context, resource, restaurants);
		this.con = context;
		this.resId = resource;
		this.restaurants = restaurants;
		registration_preference = con.getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		auth_token = registration_preference.getString(
				HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
	}

	private class ViewHolder {
		TextView tvCompanyName, tvCompanyArea, tvDetails, tvMenu, tvReviews,
				tvFollow, tv_Details;
		RatingBar rbRestaurant;
		ImageView ivCompanyImage;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(resId, null);
			holder = new ViewHolder();
			holder.ivCompanyImage = (ImageView) convertView
					.findViewById(R.id.ivCompanyImage);
			holder.tvCompanyArea = (TextView) convertView
					.findViewById(R.id.tvCompanyArea);
			holder.tvCompanyName = (TextView) convertView
					.findViewById(R.id.tvCompanyName);
			holder.tvDetails = (TextView) convertView
					.findViewById(R.id.tvDetails);
			holder.rbRestaurant = (RatingBar) convertView
					.findViewById(R.id.rbRestaurant);
			holder.tvMenu = (TextView) convertView.findViewById(R.id.tvMenu);
			holder.tvReviews = (TextView) convertView
					.findViewById(R.id.tvReviews);
			holder.tvFollow = (TextView) convertView
					.findViewById(R.id.tvFollow);
			holder.tv_Details = (TextView) convertView
					.findViewById(R.id.tv_Details);
			// holder.tvUsed = (TextView) convertView.findViewById(R.id.tvUsed);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDetailActivity(position, "Menu");
			}
		});

		holder.tv_Details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDetailActivity(position, "Details");
			}
		});

		holder.tvReviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDetailActivity(position, "Reviews");
			}
		});

		holder.tvFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Restaurant restaurant = restaurants.get(position);
				if (restaurant.isFollowing()) {
					followCompany(restaurant.getRestaurant_id(), "", "DEL",
							position);
				} else {
					followCompany(restaurant.getRestaurant_id(), "", "POST",
							position);
				}
			}
		});

		if (position < restaurants.size()) {
			setDataInRow(position, holder);
		}

		return convertView;
	}

	private void openDetailActivity(int position, String target) {
		Intent intent = new Intent(con, RestaurantDetailActivity.class);
		intent.putExtra(HashStatic.HASH_Restaurant_Name,
				restaurants.get(position).getRestaurant_name());
		intent.putExtra(HashStatic.HASH_Restaurant_Id,
				restaurants.get(position).getRestaurant_id());
		con.startActivity(intent);
	}

	private void setDataInRow(int position, ViewHolder holder) {
		Restaurant restaurant = restaurants.get(position);
		setImageFromUrl(holder.ivCompanyImage, restaurant.getRestaurant_image());
		holder.tvCompanyName.setText(restaurant.getRestaurant_name());
		holder.tvCompanyArea.setText(restaurant.getArea_name());
		holder.tvDetails.setText(restaurant.getDescription());
		float rating = restaurant.getRating();
		holder.rbRestaurant.setRating(rating);
		if (restaurant.isFollowing()) {
			holder.tvFollow.setBackground(con.getResources().getDrawable(
					R.drawable.border_orange_rounded_bg_trans));
			holder.tvFollow.setTextColor(con.getResources().getColor(R.color.orange));
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
									+ "\",\"campaign_type\":\"\"}", "utf-8");

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
				if (restaurants.get(position).isFollowing()) {
					restaurants.get(position).setFollowing(false);
				} else {
					restaurants.get(position).setFollowing(true);
				}
				notifyDataSetChanged();
			}
		}

	}

}
