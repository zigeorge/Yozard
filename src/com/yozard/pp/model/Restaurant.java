package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

	@SerializedName("restaurant_id")
	String restaurant_id;
	@SerializedName("restaurant_name")
	String restaurant_name;
	@SerializedName("restaurant_image")
	String restaurant_image;
	@SerializedName("restaurant_logo")
	String restaurant_logo;
	@SerializedName("area_name")
	String area_name;
	@SerializedName("description")
	String description;
	@SerializedName("rate_sum")
	String rate_sum;
	@SerializedName("unique_ratings")
	String unique_ratings;
	
	boolean following;

	public String getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public String getRestaurant_image() {
		return restaurant_image;
	}

	public void setRestaurant_image(String restaurant_image) {
		this.restaurant_image = restaurant_image;
	}

	public String getRestaurant_logo() {
		return restaurant_logo;
	}

	public void setRestaurant_logo(String restaurant_logo) {
		this.restaurant_logo = restaurant_logo;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRate_sum() {
		return rate_sum;
	}

	public void setRate_sum(String rate_sum) {
		this.rate_sum = rate_sum;
	}

	public String getUnique_ratings() {
		return unique_ratings;
	}

	public void setUnique_ratings(String unique_ratings) {
		this.unique_ratings = unique_ratings;
	}
	
	public float getRating(){
		int uniqRating= Integer.parseInt(unique_ratings);
		int rateSum = Integer.parseInt(rate_sum);
		float rating = 0;
		if(uniqRating==0){
			return rating;
		}else{
			rating = rateSum/uniqRating;
			return rating;
		}
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}
	
	

}
