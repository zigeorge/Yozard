package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class StoreResponse {
	@SerializedName("stores")
	private Vector<Stores> stores;
	@SerializedName("message")
	private String message;

	public Vector<Stores> getStores() {
		return stores;
	}

	public void setStores(Vector<Stores> stores) {
		this.stores = stores;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
