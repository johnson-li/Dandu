package com.fudan.dandu.dandu.dandu;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/22/14.
 */
public class HottestFragment extends android.app.Fragment implements View.OnTouchListener{

    int lastY;
    List<String> stringList;
    static DisplayMetrics displayMetrics;
    int imageWidth, imageHeight;
    LinearLayout hottestLinearLayout;
    ScrollViewWithRefresh scrollView;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stringList = initiateImageScr();
        displayMetrics = initiateDisplayMetrics();
        View view = inflater.inflate(R.layout.hottest_fragment, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.hottestSlide);
        scrollView = (ScrollViewWithRefresh)view.findViewById(R.id.hottestScrollView);
        hottestLinearLayout = (LinearLayout)view.findViewById(R.id.hottestLinearLayout);
        hottestLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("johnson", "linear layout");
                return false;
            }
        });
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
        LinearLayout relativeLayout = (LinearLayout)view.findViewById(R.id.scrollViewLayout);


        MagazineView magazineView = new MagazineView(getActivity().getApplicationContext());
        magazineView.setSrc("/sdcard/DCIM/Camera/a.png");
        magazineView.setTitle("校医院");
        magazineView.setMagazine_name("九十九度");
        magazineView.setMagazineVersion(9);
        magazineView.setLayoutParams(params);
        relativeLayout.addView(magazineView);


        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setText("46\n5");
        textView.setLayoutParams(params);
        relativeLayout.addView(textView);
        return view;
    }

    List<String> initiateImageScr() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("/sdcard/DCIM/Camera/a.png");
        stringList.add("/sdcard/DCIM/Camera/a.png");
//        stringList.add("/sdcard/DCIM/Camera/2013_12_08_15_38_07.jpg");
//        stringList.add("/sdcard/DCIM/Camera/2013_12_08_15_38_07.jpg");
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("johnson", "touch");
        int y = (int)event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("johnson", "move");
                int m = y - lastY;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                fling();
                break;
        }
        return true;
    }

    void fling() {

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
