package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantDetailsResponse {

	@SerializedName("restaurant")
	RestaurantDetails restaurant;
	@SerializedName("message")
	String message;

	public RestaurantDetails getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantDetails restaurant) {
		this.restaurant = restaurant;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
