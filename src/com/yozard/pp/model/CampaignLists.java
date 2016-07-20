package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class CampaignLists {
	@SerializedName("all")
	private Vector<Coupon> allCoupons;
	@SerializedName("claimed")
	private Vector<Coupon> claimedCoupons;
	@SerializedName("archived")
	private Vector<Coupon> archivedCoupons;

	public Vector<Coupon> getAllCoupons() {
		return allCoupons;
	}

	public void setAllCoupons(Vector<Coupon> allCoupons) {
		this.allCoupons = allCoupons;
	}

	public Vector<Coupon> getClaimedCoupons() {
		return claimedCoupons;
	}

	public void setClaimedCoupons(Vector<Coupon> claimedCoupons) {
		this.claimedCoupons = claimedCoupons;
	}

	public Vector<Coupon> getArchivedCoupons() {
		return archivedCoupons;
	}

	public void setArchivedCoupons(Vector<Coupon> archivedCoupons) {
		this.archivedCoupons = archivedCoupons;
	}

}
