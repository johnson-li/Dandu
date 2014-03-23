package com.fudan.dandu.dandu.dandu;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/15/14.
 */
public abstract class ContentFragment extends Fragment{

    public static boolean settingCliccked = false;
    public static final int FIND = 1;
    public static final int SUGGEST = 2;
    public static final int COLLECT = 3;
    public static final int SETTING = 4;
    public int layout;
    SlidingMenu slidingMenu;
    public ContentFragment(SlidingMenu slidingMenu, int layout) {
        this.slidingMenu = slidingMenu;
        this.layout = layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getView(inflater, container);
        Button button = (Button)view.findViewById(R.id.drawer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!slidingMenu.isMenuShowing()) {
                    slidingMenu.showMenu();
                }
            }
        });
        return view;
    }

    abstract public View getView(LayoutInflater inflater, ViewGroup container);
}


