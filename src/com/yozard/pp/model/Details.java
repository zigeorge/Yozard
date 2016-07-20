package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Details {

	@SerializedName("name")
	String name;
	@SerializedName("description")
	String description;
	@SerializedName("image")
	String image;
	@SerializedName("logo")
	String logo;
	@SerializedName("known_for")
	String known_for;
	@SerializedName("location")
	String location;
	@SerializedName("rate_sum")
	String rate_sum;
	@SerializedName("unique_ratings")
	String unique_ratings;
	@SerializedName("menu_name")
	String menu_name;
	@SerializedName("vat")
	String vat;
	@SerializedName("service_charge")
	String service_charge;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getKnown_for() {
		return known_for;
	}

	public void setKnown_for(String known_for) {
		this.known_for = known_for;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getService_charge() {
		return service_charge;
	}

	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
	}

}
