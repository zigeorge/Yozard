package com.yozard.pp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yozard.pp.model.Details;
import com.yozard.pp.model.ExtraJson;
import com.yozard.pp.model.Extras;
import com.yozard.pp.model.Facility;
import com.yozard.pp.model.Food;
import com.yozard.pp.model.Heading;
import com.yozard.pp.model.Menu;
import com.yozard.pp.model.RestaurantDetailsResponse;
import com.yozard.pp.model.SubHeading;
import com.yozard.pp.model.Type;
import com.yozard.pp.utils.BaseURL;
import com.yozard.pp.utils.ConnectionManagerPromo;
import com.yozard.pp.utils.HashStatic;
import com.yozard.pp.utils.JSONParser;

public class RestaurantDetailActivity extends ActionBarActivity {

	Context context;
	String restaurantName = "";

	ImageView ivCompany, ivCompanyLogo;
	RatingBar ratingRestaurant;
	TextView tvCompanyName, tvAddress, tvDetailAddress, tvCuisine, tvKnownfor,
			tvFacility, tvMenu, tvReview, tvCoupons;
	ProgressBar pbRestaurantHeader, pbAddress, pbMenu, pbCoupons, pbReviews,
			pbCuisine, pbKnownfor, pbFacility;
	ListView lvMenu, lvCoupons, lvReviews;

	SharedPreferences registration_preference;
	String restaurant_id = "";

	Details details;
	Vector<Facility> facilities;
	Vector<Type> types;
	Vector<Menu> menus;

	Vector<Food> foods;
	Vector<SubHeading> subHeadings;
	Vector<Heading> headings;

	String base_url, userId_sp, auth_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);

		context = this;
		restaurantName = getIntent().getStringExtra(
				HashStatic.HASH_Restaurant_Name);
		restaurant_id = getIntent().getStringExtra(
				HashStatic.HASH_Restaurant_Id);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Restaurants");
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.orange1));

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		initUI();

	}

	private void initUI() {
		ivCompany = (ImageView) findViewById(R.id.ivCompany);
		ivCompanyLogo = (ImageView) findViewById(R.id.ivCompanyLogo);

		ratingRestaurant = (RatingBar) findViewById(R.id.ratingRestaurant);

		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvDetailAddress = (TextView) findViewById(R.id.tvDetailAddress);
		tvCuisine = (TextView) findViewById(R.id.tvCuisine);
		tvKnownfor = (TextView) findViewById(R.id.tvKnownfor);
		tvFacility = (TextView) findViewById(R.id.tvFacility);
		tvMenu = (TextView) findViewById(R.id.tvMenu);
		tvReview = (TextView) findViewById(R.id.tvReview);
		tvCoupons = (TextView) findViewById(R.id.tvCoupons);

		pbRestaurantHeader = (ProgressBar) findViewById(R.id.pbRestaurantHeader);
		pbAddress = (ProgressBar) findViewById(R.id.pbAddress);
		pbMenu = (ProgressBar) findViewById(R.id.pbMenu);
		pbCoupons = (ProgressBar) findViewById(R.id.pbCoupons);
		pbReviews = (ProgressBar) findViewById(R.id.pbReviews);
		pbCuisine = (ProgressBar) findViewById(R.id.pbCuisine);
		pbKnownfor = (ProgressBar) findViewById(R.id.pbKnownfor);
		pbFacility = (ProgressBar) findViewById(R.id.pbFacility);

		lvMenu = (ListView) findViewById(R.id.lvMenu);
		lvCoupons = (ListView) findViewById(R.id.lvCoupons);
		lvReviews = (ListView) findViewById(R.id.lvReviews);

		registration_preference = getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		base_url = registration_preference.getString(HashStatic.HASH_baseUrl,
				null);
		userId_sp = registration_preference.getString(HashStatic.CUSTOMER_ID,
				null);
		auth_token = registration_preference.getString(
				HashStatic.Hash_AuthToken, BaseURL.Auth_Token);

		new GetAllRestaurasntAsync().execute();

	}

	public class GetAllRestaurasntAsync extends
			AsyncTask<HashMap<String, String>, Void, Void> {
		Editor editor;
		JSONParser jparser = new JSONParser();
		List<NameValuePair> paramiters = new ArrayList<NameValuePair>();
		JSONObject jobj;
		String message = "";
		String auth_token = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(HashMap<String, String>... paramMap) {
			// TODO Auto-generated method stub
			if (ConnectionManagerPromo.getConnectivityStatus(context) != HashStatic.TYPE_NOT_CONNECTED) {

				registration_preference = context.getSharedPreferences(
						HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
				auth_token = registration_preference.getString(
						HashStatic.Hash_AuthToken, BaseURL.Auth_Token);
				editor = registration_preference.edit();

				try {

					String url_select3 = base_url
							+ BaseURL.API
							+ "restaurants.php?authtoken="
							+ URLEncoder.encode(auth_token, "utf-8")
							+ "&JSONParam="
							+ URLEncoder.encode("{\"customer_id\":\""
									+ userId_sp + "\",\"restaurant_id\":\""
									+ restaurant_id + "\"}", "utf-8");

					jobj = jparser.makeHttpRequest(url_select3, "GET",
							paramiters);

					System.out.println(url_select3);
					message = jobj.getString("message");
					Log.e("JSON String >> ", jobj.toString());

					// editor.putString(HashStatic.HASH_LIVECOUPONS,
					// jobj.toString()).commit();

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// Toast.makeText(context, "Network connection not available.",
				// Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (message.contains("success")) {
				Gson g = new Gson();
				RestaurantDetailsResponse restaurantsDetailsResponse = g
						.fromJson(jobj.toString(),
								RestaurantDetailsResponse.class);
				details = restaurantsDetailsResponse.getRestaurant()
						.getDetails();
				menus = restaurantsDetailsResponse.getRestaurant().getMenu();
				types = restaurantsDetailsResponse.getRestaurant().getType();
				facilities = restaurantsDetailsResponse.getRestaurant()
						.getFacility();
				if (menus.size() > 0) {
					getFoodsList();
				}

				Log.e("Data", "Loaded Successfully");
			}
		}

	}

	private void organizeMenu() {
		int i = 0;
		for (Heading heading : headings) {
			Log.d("Header", heading.getHeadingTitle());
			Log.d("SubHeader", heading.getSubHeadings().get(i)
					.getSubHeadingTitle());
			Log.d("Food", heading.getSubHeadings().get(i).getFoods().get(0)
					.getName());
		}
	}

	private void getFoodsList() {
		Gson g = new Gson();
		foods = new Vector<Food>();
		for (int i = 0; i < menus.size(); i++) {
			Food aFood = new Food();
			String extras = menus.get(i).getExtras();
			Log.d("Extra", extras);
			List<Extras> extrasList = Arrays.asList(g.fromJson(extras,
					Extras[].class));
			Vector<Extras> extRas = getExtraFromList(extrasList);
			String description = menus.get(i).getDescription();
			aFood.setDescription(description);
			aFood.setExtras(extRas);
			aFood.setHeading(menus.get(i).getHeading_title());
			aFood.setSubHeading(menus.get(i).getSubheading_title());
			aFood.setName(menus.get(i).getName());
			aFood.setPrice(menus.get(i).getPrice());
			foods.add(aFood);
		}
		getSubHeadings();
	}

	private Vector<Extras> getExtraFromList(List<Extras> extrasList) {
		Vector<Extras> exTras = new Vector<Extras>();
		for (Extras extras : extrasList) {
			exTras.add(extras);
		}
		return exTras;
	}

	private void getSubHeadings() {
		subHeadings = new Vector<SubHeading>();
		SubHeading aSubHeading = new SubHeading();
		aSubHeading.setSubHeadingTitle(menus.get(0).getSubheading_title());
		aSubHeading.setHeadingTitle(menus.get(0).getHeading_title());
		aSubHeading
				.setFoods(getAllFoodWithin(aSubHeading.getSubHeadingTitle()));
		subHeadings.add(aSubHeading);
		for (int i = 0; i < menus.size(); i++) {
			String subHeadingTitle = menus.get(i).getSubheading_title();
			String headingTitle = menus.get(i).getHeading_title();
			boolean gotIt = false;
			for (int j = 0; j < subHeadings.size(); j++) {
				if (subHeadings.get(j).getSubHeadingTitle()
						.equalsIgnoreCase(subHeadingTitle)) {
					gotIt = true;
					break;
				}
			}
			if (!gotIt) {
				SubHeading aSubHeading2 = new SubHeading();
				aSubHeading2.setSubHeadingTitle(subHeadingTitle);
				aSubHeading2.setFoods(getAllFoodWithin(subHeadingTitle));
				aSubHeading2.setHeadingTitle(headingTitle);
				subHeadings.add(aSubHeading2);
			}
		}
		getHeadings();
	}

	private Vector<Food> getAllFoodWithin(String subHEadingTitle) {
		Vector<Food> foodsList = new Vector<Food>();
		for (Food food : foods) {
			if (food.getSubHeading().equalsIgnoreCase(subHEadingTitle)) {
				foodsList.add(food);
			}
		}
		return foodsList;
	}

	private void getHeadings() {
		headings = new Vector<Heading>();
		Heading aHeading = new Heading();
		aHeading.setHeadingTitle(menus.get(0).getHeading_title());
		aHeading.setSubHeadings(getAllSubHeadWithin(aHeading.getHeadingTitle()));
		headings.add(aHeading);
		for (int i = 0; i < menus.size(); i++) {
			String subHeadingTitle = menus.get(i).getSubheading_title();
			String headingTitle = menus.get(i).getHeading_title();
			boolean gotIt = false;
			for (int j = 0; j < headings.size(); j++) {
				if (headings.get(j).getHeadingTitle()
						.equalsIgnoreCase(headingTitle)) {
					gotIt = true;
					break;
				}
			}
			if (!gotIt) {
				Heading aHeading2 = new Heading();
				aHeading2.setHeadingTitle(headingTitle);
				aHeading2.setSubHeadings(getAllSubHeadWithin(headingTitle));
				headings.add(aHeading2);
			}
		}
		organizeMenu();
	}

	private Vector<SubHeading> getAllSubHeadWithin(String headingTitle) {
		Vector<SubHeading> subHeadingList = new Vector<SubHeading>();
		for (SubHeading subHeading : subHeadings) {
			if (subHeading.getHeadingTitle().equalsIgnoreCase(headingTitle)) {
				subHeadingList.add(subHeading);
			}
		}
		return subHeadingList;
	}

}
