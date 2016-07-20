package com.yozard.pp.model;

import android.hardware.Camera.Area;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Coupon {

	@SerializedName("lcc_id")
	private String lcc_id;
	@SerializedName("company_id")
	private String company_id;
	@SerializedName("adv_id")
	private String adv_id;
	@SerializedName("campaign_title")
	private String campaign_title;
	@SerializedName("claim_limit")
	private String claim_limit;
	@SerializedName("usage_limit")
	private String usage_limit;
	@SerializedName("same_coupon_usage_limit")
	private String same_coupon_usage_limit;
	@SerializedName("start_time")
	private String start_time;
	@SerializedName("end_time")
	private String end_time;
	@SerializedName("lcc_offer")
	private String lcc_offer;
	@SerializedName("lcc_generic_code")
	private String lcc_generic_code;
	@SerializedName("lcc_instruction")
	private String lcc_instruction;
	@SerializedName("lcc_featured")
	private String lcc_featured;
	@SerializedName("lcc_subscription_type")
	private String lcc_subscription_type;
	@SerializedName("lcc_approval")
	private String lcc_approval;
	@SerializedName("lcc_created_date")
	private String lcc_created_date;
	@SerializedName("company_name")
	private String company_name;
	@SerializedName("company_area")
	private String company_area;
	@SerializedName("company_image")
	private String company_image;
	@SerializedName("company_address")
	private String company_address;
	@SerializedName("total_used")
	private String total_used;
	@SerializedName("total_claimed")
	private String total_claimed;
	@SerializedName("customer_usage")
	private String customer_usage;
	@SerializedName("order_status")
	private String order_status;
	@SerializedName("area_name")
	private String area_name;
	private Offer coupon_offer;
	private String coupon_status;
	private boolean following = false;
	
	@SerializedName("hhc_id")
	private String hhc_id;
	@SerializedName("booking_limit")
	private String booking_limit;
	@SerializedName("offer")
	private String offer;
	@SerializedName("instruction")
	private String instruction;
	@SerializedName("hhc_featured")
	private String hhc_featured;
	@SerializedName("subscription_type")
	private String subscription_type;
	@SerializedName("start_date")
	private String start_date;
	@SerializedName("end_date")
	private String end_date;
	@SerializedName("allowed_day")
	private String allowed_day;
	@SerializedName("allowed_start_time")
	private String allowed_start_time;
	@SerializedName("allowed_end_time")
	private String allowed_end_time;
	@SerializedName("created_date")
	private String created_date;
	@SerializedName("hhc_approval")
	private String hhc_approval;
	@SerializedName("approval_time")
	private String approval_time;
	@SerializedName("usage_status")
	private String usage_status;
	private Offer hOffer;
	
	public boolean isFollowing() {
		
		return following;
	}

	public void setFollowing(boolean isFollowing) {
		this.following = isFollowing;
	}

	public Offer getCoupon_offer() {
		Gson g = new Gson();
		coupon_offer = g.fromJson(this.lcc_offer, Offer.class);
		return coupon_offer;
	}

	public void setCoupon_offer(Offer coupon_offer) {
		this.coupon_offer = coupon_offer;
	}

	public String getLcc_id() {
		return lcc_id;
	}

	public void setLcc_id(String lcc_id) {
		this.lcc_id = lcc_id;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getAdv_id() {
		return adv_id;
	}

	public void setAdv_id(String adv_id) {
		this.adv_id = adv_id;
	}

	public String getCampaign_title() {
		return campaign_title;
	}

	public void setCampaign_title(String campaign_title) {
		this.campaign_title = campaign_title;
	}

	public String getClaim_limit() {
		return claim_limit;
	}

	public void setClaim_limit(String claim_limit) {
		this.claim_limit = claim_limit;
	}

	public String getUsage_limit() {
		return usage_limit;
	}

	public void setUsage_limit(String usage_limit) {
		this.usage_limit = usage_limit;
	}

	public String getSame_coupon_usage_limit() {
		return same_coupon_usage_limit;
	}

	public int getSame_coupon_usageLimit() {
		return Integer.parseInt(this.same_coupon_usage_limit);
	}

	public void setSame_coupon_usage_limit(String same_coupon_usage_limit) {
		this.same_coupon_usage_limit = same_coupon_usage_limit;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getLcc_offer() {
		return lcc_offer;
	}

	public void setLcc_offer(String lcc_offer) {
		this.lcc_offer = lcc_offer;
		Gson g = new Gson();
		this.coupon_offer = g.fromJson(lcc_offer, Offer.class);
	}

	public String getLcc_generic_code() {
		return lcc_generic_code;
	}

	public void setLcc_generic_code(String lcc_generic_code) {
		this.lcc_generic_code = lcc_generic_code;
	}

	public String getLcc_instruction() {
		return lcc_instruction;
	}

	public void setLcc_instruction(String lcc_instruction) {
		this.lcc_instruction = lcc_instruction;
	}

	public String getLcc_featured() {
		return lcc_featured;
	}

	public void setLcc_featured(String lcc_featured) {
		this.lcc_featured = lcc_featured;
	}

	public String getLcc_subscription_type() {
		return lcc_subscription_type;
	}

	public void setLcc_subscription_type(String lcc_subscription_type) {
		this.lcc_subscription_type = lcc_subscription_type;
	}

	public String getLcc_approval() {
		return lcc_approval;
	}

	public void setLcc_approval(String lcc_approval) {
		this.lcc_approval = lcc_approval;
	}

	public String getLcc_created_date() {
		return lcc_created_date;
	}

	public void setLcc_created_date(String lcc_created_date) {
		this.lcc_created_date = lcc_created_date;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_area() {
		return company_area;
	}

	public void setCompany_area(String company_area) {
		this.company_area = company_area;
	}

	public String getCompany_image() {
		return company_image;
	}

	public void setCompany_image(String company_image) {
		this.company_image = company_image;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	public String getTotal_used() {
		return total_used;
	}

	public void setTotal_used(String total_used) {
		this.total_used = total_used;
	}

	public String getTotal_claimed() {
		return total_claimed;
	}

	public void setTotal_claimed(String total_claimed) {
		this.total_claimed = total_claimed;
	}
	
	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getCustomer_usage() {
		return customer_usage;
	}

	public int getCustomerUsage() {
		return Integer.parseInt(customer_usage);
	}

	public void setCustomer_usage(String customer_usage) {
		this.customer_usage = customer_usage;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getCoupon_status() {
		if (TextUtils.isEmpty(customer_usage)) {
			this.coupon_status = "SAVE";
		} else if (!TextUtils.isEmpty(order_status)
				&& order_status.equalsIgnoreCase("pending")) {
			this.coupon_status = "PENDING";
		} else if (getCustomerUsage() < getSame_coupon_usageLimit()) {
			this.coupon_status = "USE";
		} else {
			this.coupon_status = this.getCustomer_usage() + "/"
					+ this.getSame_coupon_usage_limit() + " " + "Used";
		}
		return coupon_status;
	}

	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	
	public Offer gethOffer() {
		Gson g = new Gson();
		hOffer = g.fromJson(this.offer, Offer.class);
		return hOffer;
	}

	public void sethOffer(Offer hOffer) {
		this.hOffer = hOffer;
	}

	public String getHhc_id() {
		return hhc_id;
	}

	public void setHhc_id(String hhc_id) {
		this.hhc_id = hhc_id;
	}

	public String getBooking_limit() {
		return booking_limit;
	}

	public void setBooking_limit(String booking_limit) {
		this.booking_limit = booking_limit;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getHhc_featured() {
		return hhc_featured;
	}

	public void setHhc_featured(String hhc_featured) {
		this.hhc_featured = hhc_featured;
	}

	public String getSubscription_type() {
		return subscription_type;
	}

	public void setSubscription_type(String subscription_type) {
		this.subscription_type = subscription_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getAllowed_day() {
		return allowed_day;
	}

	public void setAllowed_day(String allowed_day) {
		this.allowed_day = allowed_day;
	}

	public String getAllowed_start_time() {
		return allowed_start_time;
	}

	public void setAllowed_start_time(String allowed_start_time) {
		this.allowed_start_time = allowed_start_time;
	}

	public String getAllowed_end_time() {
		return allowed_end_time;
	}

	public void setAllowed_end_time(String allowed_end_time) {
		this.allowed_end_time = allowed_end_time;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getHhc_approval() {
		return hhc_approval;
	}

	public void setHhc_approval(String hhc_approval) {
		this.hhc_approval = hhc_approval;
	}

	public String getApproval_time() {
		return approval_time;
	}

	public void setApproval_time(String approval_time) {
		this.approval_time = approval_time;
	}

	public String getUsage_status() {
		return usage_status;
	}

	public void setUsage_status(String usage_status) {
		this.usage_status = usage_status;
	}

}
