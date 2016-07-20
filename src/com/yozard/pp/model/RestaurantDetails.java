package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class RestaurantDetails {

	@SerializedName("details")
	Details details;
	@SerializedName("menu")
	Vector<Menu> menu;
	@SerializedName("facility")
	Vector<Facility> facility;
	@SerializedName("type")
	Vector<Type> type;

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	public Vector<Menu> getMenu() {
		return menu;
	}

	public void setMenu(Vector<Menu> menu) {
		this.menu = menu;
	}

	public Vector<Facility> getFacility() {
		return facility;
	}

	public void setFacility(Vector<Facility> facility) {
		this.facility = facility;
	}

	public Vector<Type> getType() {
		return type;
	}

	public void setType(Vector<Type> type) {
		this.type = type;
	}

}
