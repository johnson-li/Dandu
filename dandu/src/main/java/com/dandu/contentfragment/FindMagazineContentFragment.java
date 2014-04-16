package com.dandu.contentfragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.constant.Constants;
import com.dandu.fdureader.Backend;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.listener.ArticleOnClickListener;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/22/14.
 */
public class FindMagazineContentFragment extends ContentFragment{

    View view;
    LayoutInflater inflater;
    ViewGroup container;
    Magazine magazine;
    LinearLayout magazineLayout;
    public static int magazineID;
    public static Handler articleListHandler;
    List<View> viewList;
    public FindMagazineContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_MAGAZINE);
    }

    public void setMagazineID(int magazineID) {
        this.magazineID =magazineID;
        if (viewList == null) {
            viewList = new ArrayList<View>();
        }
        viewList.clear();
        magazine = MainActivity.backend.getMagazineByID(magazineID);
        MainActivity.backend.getPostsByMagazineID(magazineID, 10);
        List<Post> postList = magazine.posts;
        Log.d("johnson", "postList.size() = " + postList.size());
        for (int i = 0; i < postList.size(); i++) {
            addArticleList(postList.get(i));
        }
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        this.inflater = inflater;
        this.container = container;
        view = inflater.inflate(R.layout.magazine_frame, container, false);
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
        Bitmap bitmap = BitmapFactory.decodeFile("sdcard/DCIM/Camera/a.png");
        magazineBackground.setImageBitmap(bitmap);
        magazineBackground.setLayoutParams(new LinearLayout.LayoutParams(Constants.screenWidth, bitmap.getHeight() * Constants.screenWidth / bitmap.getWidth() + 1));

        addArticleList("title", "articleAbstractStr");

        articleListHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Backend.BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED :
                        for (Post post: magazine.posts) {
                            addArticleList(post);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        return view;
    }

    void addArticleList(Post post) {
        String titleStr = post.postTitle;
        String articleAbstractStr = "papapa";
        magazineLayout = (LinearLayout)view.findViewById(R.id.magazineFragmentLayout);
        View listView = inflater.inflate(R.layout.article_in_list, container, false);
        final ImageView strike = (ImageView)listView.findViewById(R.id.strike);
        final TextView title = (TextView)listView.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(titleStr);
        title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap = Constants.resizeImage(getResources(), R.drawable.strike, title.getHeight(), Constants.dip2px(3));
                strike.setImageBitmap(bitmap);
            }
        });
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
        final ImageView strike = (ImageView)listView.findViewById(R.id.strike);
        final TextView title = (TextView)listView.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(titleStr);
        title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap = Constants.resizeImage(getResources(), R.drawable.strike, title.getHeight(), Constants.dip2px(3));
                strike.setImageBitmap(bitmap);
            }
        });
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
