package com.fudan.dandu.dandu.dandu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/15/14.
 */
public class SuggestContentFragmentOnboard extends ContentFragment{

    public SuggestContentFragmentOnboard(SlidingMenu slidingMenu) {
        super(slidingMenu, ContentFragment.SUGGEST);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.suggest_frame, container, false);
        Button setting = (Button)view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentFragment.settingCliccked = true;
                MenuFragment.changeFragment(ContentFragment.SETTING);
            }
        });
        return view;
    }
}
