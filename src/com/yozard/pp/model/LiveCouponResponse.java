package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class LiveCouponResponse {

	@SerializedName("message")
	private String message;
	@SerializedName("lc_campaign")
	private CampaignLists lc_campaign;
	@SerializedName("following")
	private Vector<Following> following;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CampaignLists getLc_campaign() {
		return lc_campaign;
	}

	public void setLc_campaign(CampaignLists lc_campaign) {
		this.lc_campaign = lc_campaign;
	}

	public Vector<Following> getFollowing() {
		return following;
	}

	public void setFollowing(Vector<Following> following) {
		this.following = following;
	}
	
}
