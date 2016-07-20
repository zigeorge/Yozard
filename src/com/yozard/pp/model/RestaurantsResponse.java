package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class RestaurantsResponse {
	@SerializedName("restaurants")
	private Vector<Restaurant> restaurants;
	@SerializedName("message")
	private String message;
	@SerializedName("follow")
	private Vector<Following> follows;

	public Vector<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(Vector<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Vector<Following> getFollows() {
		return follows;
	}

	public void setFollows(Vector<Following> follows) {
		this.follows = follows;
	}

}
