package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class HappyHourResponse {
	@SerializedName("message")
	private String message;
	@SerializedName("hh_campaign")
	private HHCampaign hh_campaign;
	@SerializedName("following")
	private Vector<Following> following;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HHCampaign getHh_campaign() {
		return hh_campaign;
	}
	public void setHh_campaign(HHCampaign hh_campaign) {
		this.hh_campaign = hh_campaign;
	}
	public Vector<Following> getFollowing() {
		return following;
	}
	public void setFollowing(Vector<Following> following) {
		this.following = following;
	}
	
	

}
