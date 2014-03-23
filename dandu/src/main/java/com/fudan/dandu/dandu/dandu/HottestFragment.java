package com.fudan.dandu.dandu.dandu;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/22/14.
 */
public class HottestFragment extends android.app.Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hottest_fragment, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.hottestSlide);
        viewPager.setAdapter(new ImagePagerAdapter(initList()));
        return view;
    }

    List<String> initList() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("file:///sdcard/DCIM/Camera/a.png");
        stringList.add("file:///sdcard/DCIM/Camera/old.png");
        return stringList;
    }

    class ImagePagerAdapter extends PagerAdapter {
        List<String> stringList;

        public ImagePagerAdapter(List<String> stringList) {
            this.stringList = stringList;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(stringList.get(position));
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
