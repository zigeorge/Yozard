package com.yozard.pp.fragments;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yozard.pp.R;
import com.yozard.pp.adapter.LiveCouponAdapter;
import com.yozard.pp.model.Coupon;
import com.yozard.pp.model.Following;
import com.yozard.pp.model.LiveCouponResponse;
import com.yozard.pp.utils.HashStatic;

public class ArchievedCouponFragment extends Fragment {

	ListView lvCoupon;
	Vector<Coupon> archievedCoupons;
	Vector<Following> followings;
	
	LiveCouponAdapter couponAdapter;
	
	Context context;

	SharedPreferences registration_preference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		prepareDataset();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.fragment_archieved_coupon, null);
		return inflater.inflate(R.layout.fragment_archieved_coupon, container, false);
	}

	private void initUI(View v) {
		lvCoupon = (ListView) v.findViewById(R.id.lvArchievedCoupons);
		couponAdapter = new LiveCouponAdapter(getActivity(), R.layout.coupon_row,
				archievedCoupons);
		lvCoupon.setAdapter(couponAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		context = getActivity();
		initUI(view);
	}
	
	private void prepareDataset(){
		registration_preference = getActivity().getSharedPreferences(
				HashStatic.PREF_NAME_REG, Context.MODE_PRIVATE);
		String couponsRaw = registration_preference.getString(
				HashStatic.HASH_LIVECOUPONS, null);
//		Log.e("Live Coupons >>> ", couponsRaw);
		Gson g = new Gson();
		LiveCouponResponse liveCouponResponse = g.fromJson(
				couponsRaw, LiveCouponResponse.class);
		archievedCoupons = liveCouponResponse.getLc_campaign().getArchivedCoupons();
		followings = liveCouponResponse.getFollowing();
		setCouponFollowing();
	}
	
	private void setCouponFollowing(){
		for (Coupon coupon : archievedCoupons) {
			for (Following following : followings) {
				if(coupon.getCompany_id().equalsIgnoreCase(following.getCompany_id())){
					coupon.setFollowing(true);
				}
			}
		}
	}
	

}
