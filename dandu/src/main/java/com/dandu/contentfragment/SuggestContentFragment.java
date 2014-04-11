package com.dandu.contentfragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/15/14.
 */
public class SuggestContentFragment extends ContentFragment{

    public SuggestContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, SUGGEST);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.suggest_frame, container, false);
        Button setting = (Button)view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSettingClicked = true;
                MenuFragment.changeFragment(SETTING);
            }
        });
        return view;
    }
}
