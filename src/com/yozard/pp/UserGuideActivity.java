package com.yozard.pp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.viewpagerindicator.CirclePageIndicator;
import com.yozard.pp.fragments.FragmentUserGuide1;
import com.yozard.pp.fragments.FragmentUserGuide2;
import com.yozard.pp.fragments.FragmentUserGuide3;
import com.yozard.pp.fragments.FragmentUserGuide4;
import com.yozard.pp.utils.HackyViewPager;

public class UserGuideActivity extends ActionBarActivity {

	TextView tvSecret;
	ShimmerFrameLayout container;

	HackyViewPager pager;
	CirclePageIndicator indicator;

	public static boolean isFromMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_guide);

		if (isFromMore) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle("Instructions");
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.orange1));
		}
		pager = (HackyViewPager) findViewById(R.id.starter_viewpager);
		indicator = (CirclePageIndicator) findViewById(R.id.starterindicator_transparent);

		pager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				getFragments()));
		indicator.setViewPager(pager);

		tvSecret = (TextView) findViewById(R.id.tvSecret);
		tvSecret.setVisibility(View.VISIBLE);
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
			if (pager.getCurrentItem() == 0) {
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isFromMore = false;
		super.onPause();
	}

}
