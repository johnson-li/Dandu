package com.fudan.dandu.dandu.dandu;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/15/14.
 */
public class FindContentFragment extends ContentFragment{

    ViewPager viewPager;
    List<View> viewList;
    int imageWidth, offset, screenWidth, currentIndex;
    ImageView underline;
    public FindContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, ContentFragment.FIND);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_frame, container, false);
        initTextView(view);
        intiImageView(view);
        initViewPaper(view, inflater);
        return view;
    }

    public void initTextView(View view) {
        TextView newestTab = (TextView)view.findViewById(R.id.newestTab);
        TextView hottestTab = (TextView)view.findViewById(R.id.hottestTab);
        newestTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        hottestTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    public void intiImageView(View view) {
        underline = (ImageView)view.findViewById(R.id.underline);

//        imageWidth = BitmapFactory.decodeResource(getResources(), R.drawable.underline_shape).getWidth();
        screenWidth = MainActivity.screenWidth;
        offset = screenWidth / 2;
    }

    public void initViewPaper(View view, LayoutInflater layoutInflater) {
        viewPager = (ViewPager)view.findViewById(R.id.findViewPaper);
//        viewPager.setAdapter(new TabFragmentPaperAdapter(view.));
//        viewList = new ArrayList<View>();
//        viewList.add(layoutInflater.inflate(R.layout.newest_fragment, null));
//        viewList.add(layoutInflater.inflate(R.layout.hottest_fragment, null));
//        currentIndex = 0;
//        viewPager.setAdapter(new FindPageAdapter(viewList));
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Animation animation = null;
//                if (position == 0 && currentIndex == 1) {
//                    animation = new TranslateAnimation(offset, 0, 0, 0);
//                }
//                else if (position == 1 && currentIndex == 0) {
//                    animation = new TranslateAnimation(0, offset, 0, 0);
//                }
//                currentIndex = position;
//                animation.setFillAfter(true);
//                animation.setDuration(300);
//                underline.startAnimation(animation);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

    class TabFragmentPaperAdapter extends FragmentPagerAdapter {
        Fragment newestFragment = new NewestFragment();
        Fragment hottestFragment = new HottestFragment();


        public TabFragmentPaperAdapter(android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newestFragment;
                case 1:
                    return hottestFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
