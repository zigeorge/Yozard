package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Extras {
	@SerializedName("name")
	String name;
	@SerializedName("price")
	String price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
