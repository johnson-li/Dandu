package com.dandu.contentfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandu.constant.Constants;
import com.dandu.fdureader.Magazine;
import com.dandu.slidefragment.HottestFragment;
import com.dandu.activity.MainActivity;
import com.dandu.slidefragment.NewestFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;

/**
 * Created by johnson on 3/15/14.
 */
public class FindContentFragment extends ContentFragment implements View.OnTouchListener{

    ViewPager viewPager;
    int offset, screenWidth, currentIndex;
    NewestFragment newestFragment;
    HottestFragment hottestFragment;
    ArrayList<Fragment> fragmentArrayList;
    ImageView underline;
    public FindContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND);
    }

    public FindContentFragment() {
        super(MainActivity.slidingMenu, FIND);
    }

    public void initNewestSlide() {
        newestFragment.initSlide();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_frame, container, false);
        initTextView(view);
        intiImageView(view);
        initFragments();
        initViewPaper(view);
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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.underline_black);
        screenWidth = MainActivity.screenWidth;
        offset = screenWidth / 2;
        if (bitmap == null) {
            Log.d("johnson", "bitmap null");
        }
        underline.setImageBitmap(Constants.cutBitmap(bitmap, offset, bitmap.getHeight()));
    }

    public void initFragments() {
        newestFragment = new NewestFragment();
        hottestFragment = new HottestFragment();
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(newestFragment);
        fragmentArrayList.add(hottestFragment);
    }

    public void initViewPaper(View view) {
        viewPager = (ViewPager)view.findViewById(R.id.findViewPaper);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("johnson", "touch in find");
        return false;
    }

    public void addHottestMagazine(Magazine magazine1, Magazine magazine2) {
        hottestFragment.addMagazine(magazine1, magazine2);
    }

    public void addHottestMagazine(Magazine magazine) {
        hottestFragment.addMagazine(magazine);
    }

    public void addNewestMagazine(Magazine magazine1, Magazine magazine2) {
        newestFragment.addMagazine(magazine1, magazine2);
    }

    public void addNewestMagazine(Magazine magazine) {
        newestFragment.addMagazine(magazine);
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
