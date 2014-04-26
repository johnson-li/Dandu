package com.dandu.slidefragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Scroller;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.constant.Constants;
import com.dandu.constant.Trash;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.fdureader.Magazine;
import com.dandu.listener.HottestMagazineOnClickListener;
import com.fudan.dandu.dandu.dandu.R;
import com.dandu.view.ScrollViewWithRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/22/14.
 */
public class HottestFragment extends android.app.Fragment {

    List<String> stringList;
    List<LinearLayout> magazineLayoutList;
    static DisplayMetrics displayMetrics;
    int imageWidth, imageHeight;
    LinearLayout scrollViewLayout;
//    RelativeLayout.LayoutParams scrollViewLayoutParams;
//    static ScrollView scrollView;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        magazineLayoutList = new ArrayList<LinearLayout>();
        stringList = initiateImageScr();
        displayMetrics = initiateDisplayMetrics();
        View view = inflater.inflate(R.layout.hottest_fragment, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.hottestSlide);
//        scrollView = (ScrollView)view.findViewById(R.id.hottestScrollView);
//        scrollViewLayoutParams = (RelativeLayout.LayoutParams)scrollView.getLayoutParams();
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
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, height + 1);
        viewPager.setLayoutParams(layoutParams);
        scrollViewLayout = (LinearLayout)view.findViewById(R.id.scrollViewLayout);
//        scroller = new Scroller(view.getContext());
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.d("johnson", "move Y = " + (int)(event.getY() - lastY) + " paddingTop = " + scrollView.getPaddingTop());
//                        float deltaY = event.getY() - lastY;
//                        lastY = event.getY();
//                        if (deltaY > 150 || deltaY < -150) {
//                            return false;
//                        }
//                        int top = Math.max(scrollViewLayoutParams.topMargin + (int)(deltaY * 0.6), 0);
////                        scrollView.setPadding(scrollView.getPaddingLeft(), top, scrollView.getPaddingRight(), scrollView.getPaddingBottom());
//                        scrollViewLayoutParams.topMargin = top;
//                        scrollView.setLayoutParams(scrollViewLayoutParams);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        fling();
//                        break;
//                }
//                return true;
//            }
//        });
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.hottestScrollView);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
////                for (int i = 0; i < )
//            }
//        };
//        new Thread() {
//            @Override
//            public void run() {
//                int i = 20;
//                while (i-- > 0) {
//                    if (refreshed) {
//                        Message msg = new Message();
//                        msg.what = 1;
//                        handler.handleMessage(msg);
//                        break;
//                    }
//                    try {
//                        sleep(50);
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        return view;
    }

    public void addMagazine(Magazine magazine1, Magazine magazine2) {
        View view;
        try {
            view = getActivity().getLayoutInflater().inflate(R.layout.magazine, null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        TextView t1 = (TextView)view.findViewById(R.id.articleTitle1);
        TextView m1 = (TextView)view.findViewById(R.id.magazineInfo1);
        m1.setText(magazine1.description);
        t1.getPaint().setFakeBoldText(true);
        t1.setText(magazine1.name);
        final ImageView i1 = (ImageView)view.findViewById(R.id.magazineCover1);
//        int w = Constants.screenWidth / 2 - Constants.dip2px(16 + 7);
//        i1.setLayoutParams(new LinearLayout.LayoutParams(w, imageHeight * w / imageWidth));
//        i1.setImageBitmap(BitmapFactory.decodeFile("/sdcard/DCIM/Camera/a.png"));
        i1.setImageResource(R.drawable.magazine_loading);

        TextView t2 = (TextView)view.findViewById(R.id.articleTitle2);
        TextView m2 = (TextView)view.findViewById(R.id.magazineInfo2);
        t2.setText(magazine2.name);
        t2.getPaint().setFakeBoldText(true);
        m2.setText(magazine2.description);
        final ImageView i2 = (ImageView)view.findViewById(R.id.magazineCover2);
//        i2.setLayoutParams(new LinearLayout.LayoutParams(w, imageHeight * w / imageWidth));
//        i2.setImageBitmap(BitmapFactory.decodeFile("/sdcard/DCIM/Camera/a.png"));
        i2.setImageResource(R.drawable.magazine_loading);

        LinearLayout linearLayout1 = (LinearLayout)view.findViewById(R.id.magazineLinearLayout1);
        LinearLayout linearLayout2 = (LinearLayout)view.findViewById(R.id.magazineLinearLayout2);
        linearLayout1.setOnClickListener(new HottestMagazineOnClickListener(magazine1.term_id));
        linearLayout2.setOnClickListener(new HottestMagazineOnClickListener(magazine2.term_id));
        magazineLayoutList.add(linearLayout1);
        magazineLayoutList.add(linearLayout2);

        scrollViewLayout.addView(view);

        final String url1 = magazine1.coverImage;
        if (url1 == null) {
            Log.d("johnson", "magazine id: " + magazine1.term_id + ", no cover");
        }
        else {
            final String src1 = Constants.MAGAZINE_COVER_PATH + url1.substring(url1.lastIndexOf("/") + 1);
            final File cover1 = new File(src1);
            if (!cover1.exists()) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            URL Url;
                            Url = new URL(url1);
                            URLConnection conn = Url.openConnection();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            int fileSize = conn.getContentLength();
                            if (fileSize <= 0) {
                                throw new RuntimeException("无法获知文件大小 ");
                            }
                            if (is == null) {
                                throw new RuntimeException("无法获取文件");
                            }
                            FileOutputStream FOS = new FileOutputStream(cover1);
                            byte buf[] = new byte[1024];
                            int numRead;
                            while ((numRead = is.read(buf)) != -1) {
                                FOS.write(buf, 0, numRead);
                            }
                            is.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        return src1;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        i1.setImageBitmap(BitmapFactory.decodeFile(src1));
                    }
                }.execute();
            }
            else {
                i1.setImageBitmap(BitmapFactory.decodeFile(src1));
            }
        }

        final String url2 = magazine2.coverImage;
        if (url2 == null) {
            Log.d("johnson", "magazine id: " + magazine2.term_id + ", no cover");
        }
        else {
            final String src2 = Constants.MAGAZINE_COVER_PATH + url2.substring(url2.lastIndexOf("/") + 1);
            final File cover2 = new File(src2);
            if (!cover2.exists()) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            URL Url;
                            Url = new URL(url2);
                            URLConnection conn = Url.openConnection();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            int fileSize = conn.getContentLength();
                            if (fileSize <= 0) {
                                throw new RuntimeException("无法获知文件大小 ");
                            }
                            if (is == null) {
                                throw new RuntimeException("无法获取文件");
                            }
                            FileOutputStream FOS = new FileOutputStream(cover2);
                            byte buf[] = new byte[1024];
                            int numRead;
                            while ((numRead = is.read(buf)) != -1) {
                                FOS.write(buf, 0, numRead);
                            }
                            is.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        return src2;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        i2.setImageBitmap(BitmapFactory.decodeFile(src2));
                    }
                }.execute();
            }
            else {
                i2.setImageBitmap(BitmapFactory.decodeFile(src2));
            }
        }
    }

    public void addMagazine(final Magazine magazine) {
        View view;
        try {
            view = getActivity().getLayoutInflater().inflate(R.layout.magazine, null);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        TextView t1 = (TextView) view.findViewById(R.id.articleTitle1);
        TextView m1 = (TextView) view.findViewById(R.id.magazineInfo1);
        t1.getPaint().setFakeBoldText(true);
        t1.setText(magazine.name);
        m1.setText(magazine.description);
        final ImageView i1 = (ImageView) view.findViewById(R.id.magazineCover1);

        TextView t2 = (TextView) view.findViewById(R.id.articleTitle2);
        TextView m2 = (TextView) view.findViewById(R.id.magazineInfo2);
        ImageView i2 = (ImageView) view.findViewById(R.id.magazineCover2);
        t2.setVisibility(View.INVISIBLE);
        m2.setVisibility(View.INVISIBLE);
        i2.setVisibility(View.INVISIBLE);

//        int w = Constants.screenWidth / 2 - Constants.dip2px(16 + 7);
//        i1.setLayoutParams(new LinearLayout.LayoutParams(w, imageHeight * w / imageWidth));
//        i1.setImageBitmap(BitmapFactory.decodeFile("/sdcard/DCIM/Camera/a.png"));
        i1.setImageResource(R.drawable.magazine_loading);

        LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.magazineLinearLayout1);
        linearLayout1.setOnClickListener(new HottestMagazineOnClickListener(magazine.term_id));
        magazineLayoutList.add(linearLayout1);

        scrollViewLayout.addView(view);

        final String url = magazine.coverImage;
        if (url == null) {
            Log.d("johnson", "magazine id: " + magazine.term_id + ", no cover");
        }
        else {
            final String src = Constants.MAGAZINE_COVER_PATH + url.substring(url.lastIndexOf("/") + 1);
            final File cover = new File(src);
            if (!cover.exists()) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            URL Url;
                            Url = new URL(url);
                            URLConnection conn = Url.openConnection();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            int fileSize = conn.getContentLength();
                            if (fileSize <= 0) {
                                throw new RuntimeException("无法获知文件大小 ");
                            }
                            if (is == null) {
                                throw new RuntimeException("无法获取文件");
                            }
                            FileOutputStream FOS = new FileOutputStream(cover);
                            byte buf[] = new byte[1024];
                            int numRead;
                            while ((numRead = is.read(buf)) != -1) {
                                FOS.write(buf, 0, numRead);
                            }
                            is.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        return src;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        i1.setImageBitmap(BitmapFactory.decodeFile(src));
                    }
                }.execute();
            }
            else {
                i1.setImageBitmap(BitmapFactory.decodeFile(src));
            }
        }
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
            imageHeight = BitmapFactory.decodeResource(getResources(), R.drawable.find_sliding_loading).getHeight();
            imageWidth = BitmapFactory.decodeResource(getResources(), R.drawable.find_sliding_loading).getWidth();
//            imageWidth = bitmap.getWidth();
//            imageHeight = bitmap.getHeight();
//            imageView.setImageBitmap(bitmap);
            imageView.setImageResource(R.drawable.find_sliding_loading);
            imageViewList.add(imageView);
//            Trash.drop(bitmap);
        }
        return imageViewList;
    }

    DisplayMetrics initiateDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
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
