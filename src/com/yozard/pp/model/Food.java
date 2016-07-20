package com.yozard.pp.model;

import java.util.Vector;

public class Food {
	String name;
	String price;
	String description;
	String subHeading;
	String heading;
	Vector<Extras> extras;

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

	public String getSubHeading() {
		return subHeading;
	}

	public void setSubHeading(String subHeading) {
		this.subHeading = subHeading;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public Vector<Extras> getExtras() {
		return extras;
	}

	public void setExtras(Vector<Extras> extras) {
		this.extras = extras;
	}

}
