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
        MainActivity.findMagazineContentFragment.clear();
        MainActivity.backend.getPostsByMagazineID(magazineID, 10);
        ContentFragment.isFindInMagazine = true;
        MenuFragment.changeFragment(ContentFragment.FIND_MAGAZINE);
        MainActivity.setMagazineID(magazineID);
    }
}
