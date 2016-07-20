package com.yozard.pp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.viewpagerindicator.CirclePageIndicator;
import com.yozard.pp.fragments.FragmentUserGuide1;
import com.yozard.pp.fragments.FragmentUserGuide2;
import com.yozard.pp.fragments.FragmentUserGuide3;
import com.yozard.pp.fragments.FragmentUserGuide4;
import com.yozard.pp.utils.HackyViewPager;
import com.yozard.pp.utils.TypeFace_MY;

public class StarterActivity extends FragmentActivity {

	TextView tvSecret;
	ShimmerFrameLayout container;

	HackyViewPager pager;
	CirclePageIndicator indicator;

	Button signup_bt, login_bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starter);

		pager = (HackyViewPager) findViewById(R.id.starter_viewpager);
		indicator = (CirclePageIndicator) findViewById(R.id.starterindicator_transparent);

		pager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				getFragments()));
		indicator.setViewPager(pager);

		tvSecret = (TextView) findViewById(R.id.tvSecret);
		tvSecret.setVisibility(View.VISIBLE);

		signup_bt = (Button) findViewById(R.id.signUpStarter_bt);
		login_bt = (Button) findViewById(R.id.loginStarter_bt);


		signup_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(StarterActivity.this,
						RegistrationActivity.class);
				startActivity(in);
				finish();
			}
		});

		login_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(StarterActivity.this,
						LoginActivity.class);
				startActivity(in);
				finish();
			}
		});

	}

	public List<android.support.v4.app.Fragment> getFragments() {
		List<android.support.v4.app.Fragment> fList = new ArrayList<android.support.v4.app.Fragment>();
		fList.add(new FragmentUserGuide1());
		fList.add(new FragmentUserGuide2());
		fList.add(new FragmentUserGuide3());
		fList.add(new FragmentUserGuide4());
		return fList;
	}

	public class MyPageAdapter extends FragmentStatePagerAdapter {
		private List<android.support.v4.app.Fragment> fragments;

		public MyPageAdapter(FragmentManager fm,
				List<android.support.v4.app.Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
			System.out.println("mypageradapter");
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			System.out.println("getitemPos:: " + position);
			if (pager.getCurrentItem()==0) {
				tvSecret.setVisibility(View.VISIBLE);
			} else {
				tvSecret.setVisibility(View.INVISIBLE);
			}
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

}
