package com.dandu.slidefragment;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.constant.Constants;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.fdureader.Backend;
import com.dandu.fdureader.Magazine;
import com.dandu.listener.HottestMagazineOnClickListener;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

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
public class NewestFragment extends android.app.Fragment {
    List<String> stringList;
    List<LinearLayout> magazineLayoutList;
    static DisplayMetrics displayMetrics;
    int imageWidth, imageHeight;
    LinearLayout scrollViewLayout;
    PullToRefreshScrollView mPullRefreshScrollView;
    ViewPager viewPager;
    View.OnClickListener onClickListener;
    ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        magazineLayoutList = new ArrayList<LinearLayout>();
        stringList = initiateImageScr();
        displayMetrics = initiateDisplayMetrics();
        View view = inflater.inflate(R.layout.newest_fragment, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.newestSlide);
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
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int postID = MainActivity.backend.slider.get(viewPager.getCurrentItem()).postid;
                ContentFragment.isFindInArticle = true;
                MainActivity.findArticleContentFragment.postID = postID;
                MainActivity.findArticleContentFragment.magazineID = 0;
                MenuFragment.changeFragment(ContentFragment.FIND_ARTICLE);
//                if (postID != MainActivity.findArticleContentFragment.oldPostID) {
//                    MainActivity.findArticleContentFragment.clearPage();
//                }
//                MainActivity.findArticleContentFragment.setArticleID(0, postID);
            }
        };
        int height = imageHeight * displayMetrics.widthPixels / imageWidth;
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, height + 1);
        viewPager.setLayoutParams(layoutParams);
        scrollViewLayout = (LinearLayout)view.findViewById(R.id.scrollViewLayout);
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.newestScrollView);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                while (MainActivity.backend.magazineIDsOrderByTime.isEmpty()) {
                    try {
                        Thread.sleep(500);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                initMagazines();
            }
        }.execute();
        return view;
    }

    public void addMagazine(final Magazine magazine1, final Magazine magazine2) {
        if (magazine1 == null || magazine2 == null) {
            return;
        }
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
        i1.setImageResource(R.drawable.magazine_loading);
        final String src1 = Constants.MAGAZINE_COVER_PATH + File.separator + magazine1.coverImage.substring(magazine1.coverImage.lastIndexOf("/") + 1);
        final File image1 = new File(src1);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                if (image1.exists()) {
                    Log.d("johnson", "magazine cover exists " + src1);
                    return null;
                }
                Log.d(Constants.TAG, magazine1.coverImage );
                Log.d(Constants.TAG, "GetMagazineCoverRunnable.run()" );
                try {
                    URL Url;
                    Url = new URL(magazine1.coverImage);
                    URLConnection conn = Url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int fileSize = conn.getContentLength();
                    if ( fileSize <= 0 ) {
                        throw new RuntimeException("无法获知文件大小 ");
                    }
                    if (is == null) {
                        throw new RuntimeException("无法获取文件");
                    }
                    @SuppressWarnings("resource")
                    FileOutputStream FOS = new FileOutputStream(src1);
                    byte buf[] = new byte[1024];
                    @SuppressWarnings("unused")
                    int downLoadFilePosition = 0;
                    int numRead;
                    while ((numRead = is.read(buf)) != -1) {
                        FOS.write(buf, 0, numRead);
                        downLoadFilePosition += numRead;
                    }
                    is.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (magazine1.coverImage != null && !magazine1.coverImage.equals("")) {
                    Log.d("johnson", src1);
                    i1.setImageBitmap(BitmapFactory.decodeFile(src1));
                    i1.setLayoutParams(new LinearLayout.LayoutParams(Constants.getMagazineCoverWidth(), Constants.getMagazineCoverHeight(src1)));
                }
            }
        }.execute();

        TextView t2 = (TextView)view.findViewById(R.id.articleTitle2);
        TextView m2 = (TextView)view.findViewById(R.id.magazineInfo2);
        t2.setText(magazine2.name);
        t2.getPaint().setFakeBoldText(true);
        m2.setText(magazine2.description);
        final ImageView i2 = (ImageView)view.findViewById(R.id.magazineCover2);
        i2.setImageResource(R.drawable.magazine_loading);
        final String src2 = Constants.MAGAZINE_COVER_PATH + File.separator + magazine2.coverImage.substring(magazine2.coverImage.lastIndexOf("/") + 1);
        final File image2 = new File(src2);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                if (image2.exists()) {
                    Log.d("johnson", "magazine cover exists " + src1);
                    return null;
                }
                Log.d(Constants.TAG, magazine2.coverImage );
                Log.d(Constants.TAG, "GetMagazineCoverRunnable.run()" );
                try {
                    URL Url;
                    Url = new URL(magazine2.coverImage);
                    URLConnection conn = Url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int fileSize = conn.getContentLength();
                    if ( fileSize <= 0 ) {
                        throw new RuntimeException("无法获知文件大小 ");
                    }
                    if (is == null) {
                        throw new RuntimeException("无法获取文件");
                    }
                    @SuppressWarnings("resource")
                    FileOutputStream FOS = new FileOutputStream(src2);
                    byte buf[] = new byte[1024];
                    @SuppressWarnings("unused")
                    int downLoadFilePosition = 0;
                    int numRead;
                    while ((numRead = is.read(buf)) != -1) {
                        FOS.write(buf, 0, numRead);
                        downLoadFilePosition += numRead;
                    }
                    is.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (magazine2.coverImage != null && !magazine2.coverImage.equals("")) {
                    i2.setImageBitmap(BitmapFactory.decodeFile(src2));
                    i2.setLayoutParams(new LinearLayout.LayoutParams(Constants.getMagazineCoverWidth(), Constants.getMagazineCoverHeight(src2)));
                }
            }
        }.execute();
        LinearLayout linearLayout1 = (LinearLayout)view.findViewById(R.id.magazineLinearLayout1);
        LinearLayout linearLayout2 = (LinearLayout)view.findViewById(R.id.magazineLinearLayout2);
        linearLayout1.setOnClickListener(new HottestMagazineOnClickListener(magazine1.term_id));
        linearLayout2.setOnClickListener(new HottestMagazineOnClickListener(magazine2.term_id));
        magazineLayoutList.add(linearLayout1);
        magazineLayoutList.add(linearLayout2);

        scrollViewLayout.addView(view);
    }

    public void addMagazine(final Magazine magazine) {
        if (magazine == null) {
            return;
        }
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
        t1.getPaint().setFakeBoldText(true);
        t1.setText(magazine.name);
        m1.setText(magazine.description);
        final ImageView i1 = (ImageView)view.findViewById(R.id.magazineCover1);
        final String src1 = Constants.MAGAZINE_COVER_PATH + File.separator + magazine.coverImage.substring(magazine.coverImage.lastIndexOf("/") + 1);
        final File image1 = new File(src1);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                if (image1.exists()) {
                    return null;
                }
                try {
                    URL Url;
                    Url = new URL(magazine.coverImage);
                    URLConnection conn = Url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int fileSize = conn.getContentLength();
                    if ( fileSize <= 0 ) {
                        throw new RuntimeException("无法获知文件大小 ");
                    }
                    if (is == null) {
                        throw new RuntimeException("无法获取文件");
                    }
                    @SuppressWarnings("resource")
                    FileOutputStream FOS = new FileOutputStream(src1);
                    byte buf[] = new byte[1024];
                    @SuppressWarnings("unused")
                    int downLoadFilePosition = 0;
                    int numRead;
                    while ((numRead = is.read(buf)) != -1) {
                        FOS.write(buf, 0, numRead);
                        downLoadFilePosition += numRead;
                    }
                    is.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (magazine.coverImage != null && !magazine.coverImage.equals("")) {
                    i1.setImageBitmap(BitmapFactory.decodeFile(src1));
                    i1.setLayoutParams(new LinearLayout.LayoutParams(Constants.getMagazineCoverWidth(), Constants.getMagazineCoverHeight(src1)));
                }
            }
        }.execute();

        TextView t2 = (TextView)view.findViewById(R.id.articleTitle2);
        TextView m2 = (TextView)view.findViewById(R.id.magazineInfo2);
        ImageView i2 = (ImageView)view.findViewById(R.id.magazineCover2);
        t2.setVisibility(View.INVISIBLE);
        m2.setVisibility(View.INVISIBLE);
        i2.setVisibility(View.INVISIBLE);

        i1.setImageResource(R.drawable.magazine_loading);


        LinearLayout linearLayout1 = (LinearLayout)view.findViewById(R.id.magazineLinearLayout1);
        linearLayout1.setOnClickListener(new HottestMagazineOnClickListener(magazine.term_id));
        magazineLayoutList.add(linearLayout1);

        scrollViewLayout.addView(view);
    }

    List<String> initiateImageScr() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("/sdcard/DCIM/Camera/a.png");
        return stringList;
    }

    public void initSlide() {
        int size = MainActivity.backend.slider.size();
        String[] urls = new String[size];
        Log.d("johnson", "slider number: " + urls.length);
        for (int i = 0; i < size; i++) {
            urls[i] = MainActivity.backend.slider.get(i).url;
        }
        Log.d("johnson", "urls length: " + urls.length);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        List<ImageView> imageViewList = new ArrayList<ImageView>();
        for (final String url: urls) {
            final ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setLayoutParams(layoutParams);
            imageHeight = BitmapFactory.decodeResource(getResources(), R.drawable.find_sliding_loading).getHeight();
            imageWidth = BitmapFactory.decodeResource(getResources(), R.drawable.find_sliding_loading).getWidth();
            imageView.setImageResource(R.drawable.find_sliding_loading);
            imageView.setOnClickListener(onClickListener);
            imageViewList.add(imageView);
            final String src = Constants.SLIDER_PATH + url.substring(url.lastIndexOf("/") + 1);
            File sliderImage = new File(src);
            Log.d("johnson", "slider: " + src);
            if (sliderImage.exists()) {
                Log.d("johnson", "slider exists: " + src);
                imageView.setImageBitmap(BitmapFactory.decodeFile(sliderImage.getAbsolutePath()));
            }
            else {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        Log.d("johnson", "fetch " + src);
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
                            @SuppressWarnings("resource")
                            FileOutputStream FOS = new FileOutputStream(src);
                            byte buf[] = new byte[1024];
                            @SuppressWarnings("unused")
                            int numread;
                            while ((numread = is.read(buf)) != -1) {
                                FOS.write(buf, 0, numread);
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
//                        imageView.setImageBitmap(BitmapFactory.decodeFile(src));
                        imageView.setImageBitmap(BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/b.png"));
                        Log.d("johnson", "set slid " + src);
                    }
                }.execute();
            }
        }
        viewPager.setAdapter(new ImagePagerAdapter(imageViewList));
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

    void initMagazines() {
        clearMagazines();
        addMagazines();
    }

    void clearMagazines() {
        while (scrollViewLayout.getChildCount() > 1) {
            scrollViewLayout.removeViewAt(scrollViewLayout.getChildCount() - 1);
        }
    }

    void addMagazines() {
        ArrayList<Integer> IDs = Constants.getNewestMagazines();
        for (int i = 0;i < IDs.size(); i += 2) {
            if (i + 1 < IDs.size()) {
                addMagazine(MainActivity.backend.getMagazineByID(IDs.get(i)),
                        MainActivity.backend.getMagazineByID(IDs.get(i + 1)));
            }
            else {
                addMagazine(MainActivity.backend.getMagazineByID(IDs.get(i)));
            }
        }
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
