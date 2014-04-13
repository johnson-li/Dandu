package com.dandu.contentfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Press;
import com.dandu.menu.MenuFragment;
import com.dandu.slidefragment.IntroductionFragment;
import com.dandu.slidefragment.PressFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by johnson on 3/29/14.
 */
public class FindMagazineInfoContentFragment extends ContentFragment{

    int pressID, offset, currentIndex;
    Press press;
    View view;
    ViewPager viewPager;
    ImageView underline;
    ArrayList<Fragment> fragmentArrayList;
    PressFragment pressFragment;
    IntroductionFragment introductionFragment;

    public FindMagazineInfoContentFragment(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_MAGAZINE_INFO);
    }

    public void setPressID(int pressID) {
        this.pressID = pressID;
        press = MainActivity.backend.getPressByID(pressID);
    }

    @Override
    public void backward() {
        isFindInMagazineInfo = false;
        MenuFragment.changeFragment(FIND_MAGAZINE);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.find_magazine_info_fragment, container, false);
        TextView pressTitle = (TextView)view.findViewById(R.id.pressTitle);
        pressTitle.setText(press.name);
        Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        viewPager = (ViewPager)view.findViewById(R.id.viewPaper);
        underline = (ImageView)view.findViewById(R.id.underline);

        initTab();
        initImageView();
        initFragments();
        initViewPager();
        return view;
    }

    void initTab() {
        final TextView magazinesTab = (TextView)view.findViewById(R.id.magazinesTab);
        TextView introductionTab = (TextView)view.findViewById(R.id.introductionTab);
        magazinesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        introductionTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    void initImageView() {
        underline = (ImageView)view.findViewById(R.id.underline);
        offset = MainActivity.screenWidth / 2;
    }

    void initFragments() {
        pressFragment = new PressFragment(pressID);
        introductionFragment = new IntroductionFragment(pressID);
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(pressFragment);
        fragmentArrayList.add(introductionFragment);
    }

    void initViewPager() {
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
