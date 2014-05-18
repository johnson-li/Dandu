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
import com.dandu.fdureader.Backend;
import com.dandu.fdureader.Magazine;
import com.dandu.listener.HottestMagazineOnClickListener;
import com.fudan.dandu.dandu.dandu.R;
import com.dandu.view.ScrollViewWithRefresh;
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
//        scrollView = (ScrollView)view.findViewById(R.id.hottestScrollView);
//        scrollViewLayoutParams = (RelativeLayout.LayoutParams)scrollView.getLayoutParams();
        List<ImageView> viewList = initImageView();
        int height = imageHeight * displayMetrics.widthPixels / imageWidth;
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, height + 1);
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
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        new AsyncTask<String, String, String>() {
            ArrayList<Integer> magazineIDsOrderByHot = new ArrayList<Integer>();
            @Override
            protected String doInBackground(String... params) {
                int offset = 0, num = 10;
                Object[] param = {Backend.BLOG_ID, Backend.BLOG_USERNAME, Backend.BLOG_PASSWORD, offset, num};
                XMLRPCClient client = new XMLRPCClient(Constants.uri);
                try
                {
                    Object idsObject = client.callEx( "dandu.getHot", param);
                    Object[] idsObjects = (Object[])idsObject;
                    for( Object id : idsObjects )
                    {
                        magazineIDsOrderByHot.add( Integer.valueOf((String)id));
                    }
                } catch (XMLRPCException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                Constants.setHottestMagazines(magazineIDsOrderByHot);
                Log.d("johnson", "hottest " + magazineIDsOrderByHot);
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
        ArrayList<Integer> IDs = Constants.getHottestMagazines();
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
