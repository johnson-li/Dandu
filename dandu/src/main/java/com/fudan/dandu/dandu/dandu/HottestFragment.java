package com.fudan.dandu.dandu.dandu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/22/14.
 */
public class HottestFragment extends android.app.Fragment{

    List<String> stringList;
    static DisplayMetrics displayMetrics;
    int imageWidth, imageHeight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stringList = initiateImageScr();
        displayMetrics = initiateDisplayMetrics();
        View view = inflater.inflate(R.layout.hottest_fragment, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.hottestSlide);
        List<ImageView> viewList = initImageView();
        viewPager.setAdapter(new ImagePagerAdapter(viewList));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int height = imageHeight * displayMetrics.widthPixels / imageWidth;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, height + 1);
        viewPager.setLayoutParams(layoutParams);
        return view;
    }

    List<String> initiateImageScr() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("/sdcard/DCIM/Camera/a.png");
        stringList.add("/sdcard/DCIM/Camera/a.png");
        return stringList;
    }

    List<ImageView> initImageView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        List<ImageView> imageViewList = new ArrayList<ImageView>();
        for (String src: stringList) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setLayoutParams(layoutParams);
            Bitmap bitmap = BitmapFactory.decodeFile(src);
            imageWidth = bitmap.getWidth();
            imageHeight = bitmap.getHeight();
            imageView.setImageBitmap(bitmap);
            imageViewList.add(imageView);
        }
        return imageViewList;
    }

    DisplayMetrics initiateDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    class ImagePagerAdapter extends PagerAdapter {
        List<ImageView> imageViewList;

        public ImagePagerAdapter(List<ImageView> imageViewList) {
            this.imageViewList = imageViewList;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
