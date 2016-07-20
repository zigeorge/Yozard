package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Menu {
	
	@SerializedName("name")
	String name;
	@SerializedName("price")
	String price;
	@SerializedName("description")
	String description;
	@SerializedName("extras")
	String extras;
	@SerializedName("heading_title")
	String heading_title;
	@SerializedName("subheading_title")
	String subheading_title;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}

	public String getHeading_title() {
		return heading_title;
	}

	public void setHeading_title(String heading_title) {
		this.heading_title = heading_title;
	}

	public String getSubheading_title() {
		return subheading_title;
	}

	public void setSubheading_title(String subheading_title) {
		this.subheading_title = subheading_title;
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
