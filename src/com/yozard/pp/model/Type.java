package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Type {

	@SerializedName("type_name")
	String type_name;
	@SerializedName("type_id")
	String type_id;

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

}
