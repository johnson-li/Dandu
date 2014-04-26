package com.dandu.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by johnson on 3/26/14.
 */
public class ScrollViewWithRefresh extends ScrollView{

    View inner;
    int lastY;
    Scroller scroller;
    boolean first;
    static final int refreshThreshold = 50;


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

    void initiate() {
        scroller = new Scroller(getContext());
    }

    void moveScrollView(int move) {
        int top = (int)(getPaddingTop() + move * 0.3);
        if (top < 0) {
            top = 0;
        }
        setPadding(getPaddingLeft(), top, getPaddingRight(), getPaddingBottom());
    }

    void fling() {
        if (getPaddingTop() >= dip2px(getContext(), refreshThreshold)) {
            refresh();
        }
        else if(getPaddingTop() > 0){

        }
    }

    void refresh() {
        int i = 0;
        try {
            while (i++ < 5) {
                Thread.sleep(100);
                setPadding(getPaddingLeft(), refreshThreshold, getPaddingRight(), getPaddingBottom());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    boolean canScroll() {
        return computeVerticalScrollOffset() == 0;
    }
}
