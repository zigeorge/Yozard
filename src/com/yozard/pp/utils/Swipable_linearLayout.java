package com.yozard.pp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yozard.pp.TabActivity;

public class Swipable_linearLayout implements OnTouchListener {

	TabActivity tab_activity;
	
	static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;
	
	public Swipable_linearLayout(Context context, Activity activity) {
		// TODO Auto-generated constructor stub
		tab_activity=(TabActivity) activity;
	}

	
	
	public void onRightToLeftSwipe() {
       Log.i(logTag, "RightToLeftSwipe!");
        //Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        tab_activity.hideThatMsgLeft();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
       // Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        tab_activity.hideThatMsgRight();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
       // Log.i(logTag, "onTopToBottomSwipe!");
     //   Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
       // Log.i(logTag, "onBottomToTopSwipe!");
      //  Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        case MotionEvent.ACTION_UP: {
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;

            // swipe horizontal?
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                // left or right
                if (deltaX < 0) {
                    this.onLeftToRightSwipe();
                    return true;
                }
                if (deltaX > 0) {
                    this.onRightToLeftSwipe();
                    return true;
                }
            } else {
                Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                // return false; // We don't consume the event
            }

            // swipe vertical?
            if (Math.abs(deltaY) > MIN_DISTANCE) {
                // top or down
                if (deltaY < 0) {
                    this.onTopToBottomSwipe();
                    return true;
                }
                if (deltaY > 0) {
                    this.onBottomToTopSwipe();
                    return true;
                }
            } else {
                Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                // return false; // We don't consume the event
            }

            return false; // no swipe horizontally and no swipe vertically
        }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}
