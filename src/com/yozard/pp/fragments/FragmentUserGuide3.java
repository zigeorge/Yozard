package com.yozard.pp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yozard.pp.R;
import com.yozard.pp.utils.AnimatedGifImageView;
import com.yozard.pp.utils.AnimatedGifImageView.TYPE;

public class FragmentUserGuide3 extends Fragment{
	
	AnimatedGifImageView viewGif;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_userguide3, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewGif = (AnimatedGifImageView) view.findViewById(R.id.viewGif);
		viewGif.setAnimatedGif(R.drawable.user_guide_save,
				TYPE.FIT_CENTER);
		super.onViewCreated(view, savedInstanceState);
	}
	
}
