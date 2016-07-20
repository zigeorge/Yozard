package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class SavedCouponResponse {
	@SerializedName("saved_coupons")
	private SavedCoupons saved_coupons;
	@SerializedName("following")
	private Vector<Following> following;
	@SerializedName("message")
	private String message;

	public SavedCoupons getSaved_coupons() {
		return saved_coupons;
	}

	public void setSaved_coupons(SavedCoupons saved_coupons) {
		this.saved_coupons = saved_coupons;
	}

	public Vector<Following> getFollowing() {
		return following;
	}

	public void setFollowing(Vector<Following> following) {
		this.following = following;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
