package com.fudan.dandu.dandu.dandu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/15/14.
 */
public class SettingContentFragment extends ContentFragment{

    public SettingContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, ContentFragment.SETTING);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.setting_frame, container, false);
        final Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        return view;
    }

    @Override
    public void backward() {
        isSettingClicked = false;
        MenuFragment.changeFragment(SUGGEST);
    }
}
