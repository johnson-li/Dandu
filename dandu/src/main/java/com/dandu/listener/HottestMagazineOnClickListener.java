package com.dandu.listener;

import android.view.View;

import com.dandu.activity.MainActivity;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.menu.MenuFragment;

/**
 * Created by johnson on 4/11/14.
 */
public class HottestMagazineOnClickListener implements View.OnClickListener{

    int magazineID;

    public HottestMagazineOnClickListener(int magazineID) {
        this.magazineID = magazineID;
    }

    @Override
    public void onClick(View v) {
        ContentFragment.isFindInMagazine = true;
        MainActivity.setMagazineID(magazineID);
        MenuFragment.changeFragment(ContentFragment.FIND_MAGAZINE);
    }
}
