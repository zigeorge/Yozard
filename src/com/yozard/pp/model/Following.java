package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Following {
	@SerializedName("company_id")
	String company_id;

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

}
