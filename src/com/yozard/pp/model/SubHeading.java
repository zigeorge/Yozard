package com.yozard.pp.model;

import java.util.Vector;

public class SubHeading {
	String subHeadingTitle;
	String headingTitle;
	Vector<Food> foods;

	public String getSubHeadingTitle() {
		return subHeadingTitle;
	}

	public void setSubHeadingTitle(String subHeadingTitle) {
		this.subHeadingTitle = subHeadingTitle;
	}

	public Vector<Food> getFoods() {
		return foods;
	}

	public void setFoods(Vector<Food> foods) {
		this.foods = foods;
	}

	public String getHeadingTitle() {
		return headingTitle;
	}

	public void setHeadingTitle(String headingTitle) {
		this.headingTitle = headingTitle;
	}

}
