package com.fudan.dandu.dandu.dandu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 3/22/14.
 */
public class FindMagazineContentFragment extends ContentFragment{

    public FindMagazineContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, ContentFragment.FIND_MAGAZINE);
    }


    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.magazine_frame, container, false);
        Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentFragment.isFindInMagazine = false;
                MenuFragment.changeFragment(ContentFragment.FIND);
            }
        });
        Button info =  (Button)view.findViewById(R.id.magazineInfoButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentFragment.isFindInMagazineInfo = true;
                MenuFragment.changeFragment(ContentFragment.FIND_MAGAZINE_INFO);
            }
        });

        ImageView magazineBackground = (ImageView)view.findViewById(R.id.magazineBackground);
        Bitmap bitmap = BitmapFactory.decodeFile("sdcard/DCIM/Camera/a.png");
        magazineBackground.setImageBitmap(bitmap);
        magazineBackground.setLayoutParams(new LinearLayout.LayoutParams(Constants.screenWidth, bitmap.getHeight() * Constants.screenWidth / bitmap.getWidth() + 1));
        addArticleList(inflater, container, view, "title", "article abstract");
        addArticleList(inflater, container, view, "title", "article abstract");
        addArticleList(inflater, container, view, "title", "article abstract");
        addArticleList(inflater, container, view, "title", "article abstract");
        addArticleList(inflater, container, view, "title", "article abstract");
        return view;
    }

    void addArticleList(LayoutInflater inflater,ViewGroup container,  View view, String titleStr, String articleAbstractStr) {
        LinearLayout magazineLayout = (LinearLayout)view.findViewById(R.id.magazineFragmentLayout);
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
        articleListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("johnson", "clicked");
                ContentFragment.isFindInArticle = true;
                MenuFragment.changeFragment(FIND_ARTICLE);
            }
        });

    }

    @Override
    public void backward() {
        isFindInMagazine = false;
        MenuFragment.changeFragment(FIND);
    }
}
