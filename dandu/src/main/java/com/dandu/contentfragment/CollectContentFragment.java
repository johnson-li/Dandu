package com.dandu.contentfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.constant.Constants;
import com.dandu.slidefragment.CollectArticleFragment;
import com.dandu.slidefragment.CollectMagazineFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by johnson on 3/15/14.
 */
public class CollectContentFragment extends ContentFragment{

    ViewPager viewPager;
    int offset, screenWidth, currentIndex;
    CollectArticleFragment collectArticleFragment;
    CollectMagazineFragment collectMagazineFragment;
    ArrayList<Fragment> fragmentArrayList;
    ImageView underline;

    public CollectContentFragment(SlidingMenu slidingMenu){
        super(slidingMenu, ContentFragment.COLLECT);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.collect_frame, container, false);
        initTextView(view);
        intiImageView(view);
        initFragments();
        initViewPaper(view);
        return view;
    }

    public void initTextView(View view) {
        TextView collectMagazineTab = (TextView)view.findViewById(R.id.collectMagazineTab);
        TextView collectArticleTab = (TextView)view.findViewById(R.id.collectArticleTab);
        collectMagazineTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        collectArticleTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    public void intiImageView(View view) {
        underline = (ImageView)view.findViewById(R.id.underline);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.underline_black);
        screenWidth = MainActivity.screenWidth;
        offset = screenWidth / 2;
        if (bitmap == null) {
            Log.d("johnson", "bitmap null");
        }
        underline.setImageBitmap(Constants.cutBitmap(bitmap, offset, bitmap.getHeight()));
    }

    public void initFragments() {
        collectMagazineFragment = new CollectMagazineFragment();
        collectArticleFragment = new CollectArticleFragment();
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(collectMagazineFragment);
        fragmentArrayList.add(collectArticleFragment);
    }

    public void initViewPaper(View view) {
        viewPager = (ViewPager)view.findViewById(R.id.collectViewPaper);
        viewPager.setAdapter(new TabFragmentPaperAdapter(getFragmentManager(),fragmentArrayList ));
        currentIndex = 0;
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Animation animation = null;
                if (position == 0 && currentIndex == 1) {
                    animation = new TranslateAnimation(offset, 0, 0, 0);
                }
                else if (position == 1 && currentIndex == 0) {
                    animation = new TranslateAnimation(0, offset, 0, 0);
                }
                currentIndex = position;
                animation.setFillAfter(true);
                animation.setDuration(300);
                underline.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    class TabFragmentPaperAdapter extends android.support.v13.app.FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList;

        public TabFragmentPaperAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentArrayList) {
            super(fragmentManager);
            this.fragmentArrayList = fragmentArrayList;
        }

        @Override
        public android.app.Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

    }
}
