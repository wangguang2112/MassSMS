package com.wang.masssms.uiview.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * 
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * 
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    private Context mContext;

	public HackyViewPager(Context context) {
		super(context);
        mContext = context;
	}
	
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			//不理会
			e.printStackTrace();
			return false;
		}catch (ArrayIndexOutOfBoundsException e) {
			//不理会
			e.printStackTrace();
            return false;
        }
	}

   // private Scroller before;

    public void startChange(int pos,int duration){
        //获取之前scroller
/*        if(before == null) {
            before = getScroller();
        }*/
        //设置修改速度的scroller
        FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, new AccelerateInterpolator());
        scroller.setmDuration(duration);
        setScroller(scroller);
        this.setCurrentItem(pos);
    }



    private Scroller getScroller(){
        Object result = null;
        try {
            Field mField  = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            result = mField.get(this);
        } catch (Exception e) {
            Log.e("zhaobo", "exception", e);
        }
        if(result == null){
            return null;
        }
        return (Scroller)result;
    }

    private void setScroller(Scroller scroller){
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mField.set(this, scroller);
        } catch (Exception e) {
            Log.e("zhaobo", "exception", e);
        }
    }



    private static class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;
        public FixedSpeedScroller(Context context) {
            super(context);
        }
        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        public void setmDuration(int time){
            mDuration = time;
        }
        public int getmDuration(){
            return mDuration;
        }

    }

}
