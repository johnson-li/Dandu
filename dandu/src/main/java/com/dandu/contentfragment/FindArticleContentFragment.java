package com.dandu.contentfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/30/14.
 */
public class FindArticleContentFragment extends ContentFragment{

    public static int magazineID;
    public static int postID;
    public FindArticleContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_ARTICLE);
    }

    @Override
    public void backward() {
        isFindInArticle = false;
        MenuFragment.changeFragment(FIND_MAGAZINE);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_article_fragment, container, false);
        Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        Button share = (Button)view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.share_dialog);
            }
        });
        return view;
    }

    public void setArticleID(int magazineID, int postID) {
        //postID == 0 means that it's debugging and there is no need to do anything with backend
        if (postID == 0) {
            return;
        }
        this.postID = postID;
        this.magazineID = magazineID;
        MainActivity.backend.getPostContent(postID);
        Magazine magazine = MainActivity.backend.getMagazineByID(magazineID);
        Post post = magazine.posts.get(postID);

    }
}
