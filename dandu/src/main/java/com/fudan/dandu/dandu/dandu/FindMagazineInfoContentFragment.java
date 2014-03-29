package com.fudan.dandu.dandu.dandu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/29/14.
 */
public class FindMagazineInfoContentFragment extends ContentFragment{

    public FindMagazineInfoContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, ContentFragment.FIND_MAGAZINE_INFO);
    }

    @Override
    public void backward() {
        isFindInMagazineInfo = false;
        MenuFragment.changeFragment(FIND_MAGAZINE);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_magazine_info_fragment, container, false);
        Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        return view;
    }
}
