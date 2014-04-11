package com.dandu.listener;

import android.view.View;

import com.dandu.activity.MainActivity;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.menu.MenuFragment;

/**
 * Created by johnson on 4/11/14.
 */
public class ArticleOnClickListener implements View.OnClickListener{

    int magazineID;
    int postID;

    public ArticleOnClickListener(int magazineID, int postID) {
        this.magazineID = magazineID;
        this.postID = postID;
    }

    @Override
    public void onClick(View v) {
        ContentFragment.isFindInArticle = true;
        MenuFragment.changeFragment(ContentFragment.FIND_ARTICLE);
        MainActivity.setArticleID(magazineID, postID);
    }
}
