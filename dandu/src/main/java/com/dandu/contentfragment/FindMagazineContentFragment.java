package com.dandu.contentfragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Backend;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.listener.ArticleOnClickListener;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by johnson on 3/22/14.
 */
public class FindMagazineContentFragment extends ContentFragment{

    View view;
    LayoutInflater inflater;
    ViewGroup container;
    static Magazine magazine;
    LinearLayout magazineLayout;
    public static int magazineID;
    public static int oldMagazineID = 0;
    public static Handler articleListHandler;
    public FindMagazineContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_MAGAZINE);
        articleListHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("johnson", "articleListHandler");
                if (msg.what == MainActivity.backend.BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED) {
                    Log.d("johnson", "size" + magazine.posts.size());
                    for (Post post: magazine.posts) {
//                        addArticleList(post);
                    }
                }
            }
        };
    }

    public void setMagazineID(int magazineID) {
        this.magazineID =magazineID;
        if (magazineID == oldMagazineID) {
            return;
        }
        magazine = MainActivity.backend.getMagazineByID(magazineID);
        Log.d("johnson", magazineID + "&&" + oldMagazineID);
        while (magazineLayout != null && magazineLayout.getChildCount() > 1) {
            magazineLayout.removeViewAt(magazineLayout.getChildCount() - 1);
        }
        oldMagazineID = magazineID;
        MainActivity.backend.getPostsByMagazineID(magazineID, 10);
//        List<Post> postList = magazine.posts;
//        for (Post post: postList) {
//            addArticleList(post);
//        }
    }

    @Override
    public View getView(final LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.magazine_frame, container, false);
        magazineLayout = (LinearLayout)view.findViewById(R.id.magazineFragmentLayout);
        articleListHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Backend.BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED :
                        Log.d("johnson", "childCount" + magazineLayout.getChildCount());
                        for (Post post: magazine.posts) {
                            addArticleList(post);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        this.inflater = inflater;
        this.container = container;
        final Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFindInMagazine = false;
                MenuFragment.changeFragment(FIND);
            }
        });
        Button info =  (Button)view.findViewById(R.id.magazineInfoButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFindInMagazineInfo = true;
                MenuFragment.changeFragment(FIND_MAGAZINE_INFO);
                MainActivity.findMagazineInfoContentFragment.setPressID(magazine.parent);
            }
        });

        ImageView magazineBackground = (ImageView)view.findViewById(R.id.magazineBackground);
//        Bitmap bitmap = BitmapFactory.decodeFile("sdcard/DCIM/Camera/a.png");
//        magazineBackground.setImageBitmap(bitmap);
//        magazineBackground.setLayoutParams(new LinearLayout.LayoutParams(Constants.screenWidth, bitmap.getHeight() * Constants.screenWidth / bitmap.getWidth() + 1));
        magazineBackground.setImageResource(R.drawable.magazine_list_loading1);
        return view;
    }

    void addArticleList(Post post) {
        String titleStr = post.postTitle;
        String articleAbstractStr = post.postExcerpt;
        View listView = inflater.inflate(R.layout.article_in_list, container, false);
        final TextView title = (TextView)listView.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(titleStr);
        TextView articleAbstract = (TextView)listView.findViewById(R.id.articleAbstract);
        articleAbstract.setText(articleAbstractStr);
        magazineLayout.addView(listView);
        LinearLayout articleListLayout = (LinearLayout)listView.findViewById(R.id.articleListLayout);
        articleListLayout.setOnClickListener(new ArticleOnClickListener(magazineID, post.postID));
    }

    //This function is only designed for debug
    void addArticleList(String titleStr, String articleAbstractStr) {
        magazineLayout = (LinearLayout)view.findViewById(R.id.magazineFragmentLayout);
        View listView = inflater.inflate(R.layout.article_in_list, container, false);
        final TextView title = (TextView)listView.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(titleStr);
        TextView articleAbstract = (TextView)listView.findViewById(R.id.articleAbstract);
        articleAbstract.setText(articleAbstractStr);
        magazineLayout.addView(listView);
        LinearLayout articleListLayout = (LinearLayout)listView.findViewById(R.id.articleListLayout);

        //post.id = 0 is a debug flag
        articleListLayout.setOnClickListener(new ArticleOnClickListener(magazineID, 0));

    }

    @Override
    public void backward() {
        isFindInMagazine = false;
        MenuFragment.changeFragment(FIND);
    }
}
