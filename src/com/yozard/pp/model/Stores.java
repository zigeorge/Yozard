package com.yozard.pp.model;

import com.google.gson.annotations.SerializedName;

public class Stores {

	@SerializedName("store_id")
	private String store_id;
	@SerializedName("company_id")
	private String company_id;
	@SerializedName("store_name")
	private String store_name;
	@SerializedName("store_mobile")
	private String store_mobile;
	@SerializedName("store_address")
	private String store_address;
	@SerializedName("store_zip_code")
	private String store_zip_code;
	@SerializedName("store_open_time")
	private String store_open_time;
	@SerializedName("store_close_time")
	private String store_close_time;
	@SerializedName("store_type")
	private String store_type;
	@SerializedName("area_name")
	private String area_name;
	@SerializedName("town_name")
	private String town_name;
	@SerializedName("postal_code")
	private String postal_code;

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getTown_name() {
		return town_name;
	}

	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getStore_mobile() {
		return store_mobile;
	}

	public void setStore_mobile(String store_mobile) {
		this.store_mobile = store_mobile;
	}

	public String getStore_address() {
		return store_address;
	}

	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}

	public String getStore_zip_code() {
		return store_zip_code;
	}

	public void setStore_zip_code(String store_zip_code) {
		this.store_zip_code = store_zip_code;
	}

	public String getStore_open_time() {
		return store_open_time;
	}

	public void setStore_open_time(String store_open_time) {
		this.store_open_time = store_open_time;
	}

	public String getStore_close_time() {
		return store_close_time;
	}

	public void setStore_close_time(String store_close_time) {
		this.store_close_time = store_close_time;
	}

	public String getStore_type() {
		return store_type;
	}

	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}

}
