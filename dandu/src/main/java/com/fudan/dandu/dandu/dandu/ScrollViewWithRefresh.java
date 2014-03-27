package com.fudan.dandu.dandu.dandu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by johnson on 3/26/14.
 */
public class ScrollViewWithRefresh extends ScrollView{

    View inner;
    int lastY, originTop;
    Scroller scroller;
    static final int refreshThreshold = 70;
    static final int refreshTargetTop = 0;

    public ScrollViewWithRefresh(Context context) {
        super(context);
        initiate();
    }

    public ScrollViewWithRefresh(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initiate();
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        int y = (int)ev.getY();
//        int moveY = y - lastY;
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
//                int top = marginLayoutParams.topMargin;
//                if (!(top == 0 && moveY < 0) && canScroll()) {
//                    moveScrollView(moveY);
//                    return false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                fling();
//                break;
//        }
//        lastY = y;
        return super.onTouchEvent(ev);
    }

    void initiate() {
        scroller = new Scroller(getContext());
    }

    void moveScrollView(int move) {
        Log.d("johnson", "move = " + move);
        Log.d("johnson", "move scroll view");
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
//        marginLayoutParams.topMargin += (move * 0.5f);
        if (marginLayoutParams.topMargin < 0) {
            marginLayoutParams.topMargin = 0;
        }
        setLayoutParams(marginLayoutParams);
    }

    void fling() {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
        if (marginLayoutParams.topMargin >= dip2px(getContext(), refreshThreshold)) {
            refresh();
        }
        else if(marginLayoutParams.topMargin > 0){
            returnInitiateState();
        }
    }

    void returnInitiateState() {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
        int top = marginLayoutParams.topMargin;
        scroller.startScroll(0, -top, 0, -top, 1000);
        invalidate();
    }

    void refresh() {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
        scroller.startScroll(0, -marginLayoutParams.topMargin, 0, -marginLayoutParams.topMargin + dip2px(getContext(), refreshThreshold));
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        scroller.startScroll(0, -marginLayoutParams.topMargin, 0, -marginLayoutParams.topMargin, 1000);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int i = scroller.getCurrY();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams)getLayoutParams();
            int k = Math.max(i, refreshTargetTop);
            marginLayoutParams.topMargin = k;
            setLayoutParams(marginLayoutParams);
            invalidate();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    boolean canScroll() {
        return computeVerticalScrollOffset() == 0;
    }
}
