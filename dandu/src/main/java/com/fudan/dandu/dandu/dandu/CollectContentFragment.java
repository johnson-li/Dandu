package com.fudan.dandu.dandu.dandu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/15/14.
 */
public class CollectContentFragment extends ContentFragment{

    public CollectContentFragment(SlidingMenu slidingMenu){
        super(slidingMenu, ContentFragment.COLLECT);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.collect_frame, container, false);
    }
}
