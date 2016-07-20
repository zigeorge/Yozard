package com.yozard.pp.model;

import java.util.Vector;

public class Heading {
	String headingTitle;
	Vector<SubHeading> subHeadings;

	public String getHeadingTitle() {
		return headingTitle;
	}

	public void setHeadingTitle(String headingTitle) {
		this.headingTitle = headingTitle;
	}

	public Vector<SubHeading> getSubHeadings() {
		return subHeadings;
	}

	public void setSubHeadings(Vector<SubHeading> subHeadings) {
		this.subHeadings = subHeadings;
	}

}
