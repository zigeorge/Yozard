package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class HHCampaign {
	@SerializedName("all")
	private Vector<Coupon> allHCoupon;

	public Vector<Coupon> getAllHCoupon() {
		return allHCoupon;
	}

	public void setAllHCoupon(Vector<Coupon> allHCoupon) {
		this.allHCoupon = allHCoupon;
	}

}
