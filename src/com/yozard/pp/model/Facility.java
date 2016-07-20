package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Facility {

	@SerializedName("description")
	String description;
	@SerializedName("facility_name")
	String facility_name;
	@SerializedName("ref_type")
	String ref_type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFacility_name() {
		return facility_name;
	}

	public void setFacility_name(String facility_name) {
		this.facility_name = facility_name;
	}

	public String getRef_type() {
		return ref_type;
	}

	public void setRef_type(String ref_type) {
		this.ref_type = ref_type;
	}

}
