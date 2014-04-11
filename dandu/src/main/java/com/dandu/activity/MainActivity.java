package com.dandu.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import com.dandu.contentfragment.CollectContentFragment;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.contentfragment.FindArticleContentFragment;
import com.dandu.contentfragment.FindContentFragment;
import com.dandu.contentfragment.FindMagazineContentFragment;
import com.dandu.contentfragment.FindMagazineInfoContentFragment;
import com.dandu.contentfragment.SettingContentFragment;
import com.dandu.contentfragment.SuggestContentFragmentOnboard;
import com.dandu.fdureader.Backend;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.menu.MenuFragment;
import com.dandu.constant.Constants;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;

public class MainActivity extends SlidingFragmentActivity implements MenuFragment.OnHeadlineSelectedListener{

    public static int fragmentNum;
    public static int screenWidth;
    public static ContentFragment collectContentFragment,
            suggestContentFragment, settingContentFragment,
            collectMagazineContentFragment, collectArticleContentFragment;
    public static FindMagazineContentFragment findMagazineContentFragment;
    public static FindContentFragment findContentFragment;
    public static FindMagazineInfoContentFragment findMagazineInfoContentFragment;
    public static FindArticleContentFragment findArticleContentFragment;
    SlidingMenu slidingMenu;
    public static Backend backend;
    Handler backendHandler;
    private Magazine latestMagazine;
    private Post demoPost;


    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_left);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        screenWidth = getScreenWidth();
        Constants.screenWidth = screenWidth;
        Constants.density = getResources().getDisplayMetrics().density;
        setBehindContentView(R.layout.blank_right);
        slidingMenu = this.getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setBehindWidth(dip2px(getApplicationContext(), 200));
        findContentFragment = new FindContentFragment(slidingMenu);
        collectContentFragment = new CollectContentFragment(slidingMenu);
        suggestContentFragment = new SuggestContentFragmentOnboard(slidingMenu);
        settingContentFragment = new SettingContentFragment(slidingMenu);
        findMagazineContentFragment = new FindMagazineContentFragment(slidingMenu);
        findMagazineInfoContentFragment = new FindMagazineInfoContentFragment(slidingMenu);
        findArticleContentFragment = new FindArticleContentFragment(slidingMenu);
//        addAllFragment();
//        clearFragment();
        changeLeftFragment(findContentFragment);
        MenuFragment rightMenuFragment = new MenuFragment();
        changeRightFragment(rightMenuFragment);

        initBackend();

    }

    public void addAllFragment() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.blank_left, findContentFragment, "find")
                .add(R.id.blank_left, collectContentFragment, "collect")
                .add(R.id.blank_left, suggestContentFragment, "suggest")
                .commit();
    }

    private void changeLeftFragment(ContentFragment fragment) {
        if (fragment.isAdded()) {
            if (getUsingFragment() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .hide(getUsingFragment())
                        .show(fragment)
                        .commit();
            }
            else {
                getFragmentManager()
                        .beginTransaction()
                        .show(fragment)
                        .commit();
            }
        }
        else {
            if (getUsingFragment() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .hide(getUsingFragment())
                        .add(R.id.blank_left, fragment)
                        .commit();
            }
            else {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.blank_left, fragment)
                        .commit();
            }
        }
        fragmentNum = fragment.layout;
    }

    private void changeRightFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.blank_right, fragment);
        fragmentTransaction.commit();
    }

    private Fragment getUsingFragment() {
        switch (fragmentNum) {
            case ContentFragment.FIND:
                return findContentFragment;
            case ContentFragment.COLLECT:
                return collectContentFragment;
            case ContentFragment.SUGGEST:
                return suggestContentFragment;
            case ContentFragment.SETTING:
                return settingContentFragment;
            case ContentFragment.FIND_MAGAZINE:
                return findMagazineContentFragment;
            case ContentFragment.FIND_MAGAZINE_INFO:
                return findMagazineInfoContentFragment;
            case ContentFragment.FIND_ARTICLE:
                return findArticleContentFragment;
            default:
                return null;
        }
    }

    @Override
    public void onFragmentChanged(int fragmentNum) {
        switch (fragmentNum) {
            case ContentFragment.FIND:
                changeLeftFragment(findContentFragment);
                break;
            case ContentFragment.COLLECT:
                changeLeftFragment(collectContentFragment);
                break;
            case ContentFragment.SUGGEST:
                changeLeftFragment(suggestContentFragment);
                break;
            case ContentFragment.SETTING:
                changeLeftFragment(settingContentFragment);
                break;
            case ContentFragment.FIND_MAGAZINE:
                changeLeftFragment(findMagazineContentFragment);
                break;
            case ContentFragment.FIND_MAGAZINE_INFO:
                changeLeftFragment(findMagazineInfoContentFragment);
                break;
            case ContentFragment.FIND_ARTICLE:
                changeLeftFragment(findArticleContentFragment);
            default:
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                if (!slidingMenu.isMenuShowing()) {
                    slidingMenu.showMenu();
                }
                else {
                    slidingMenu.showContent();
                }
                return false;
            case KeyEvent.KEYCODE_BACK:
                if (!slidingMenu.isMenuShowing()) {
                    if (ContentFragment.isSettingClicked && fragmentNum == ContentFragment.SETTING) {
                        settingContentFragment.backward();
                        return false;
                    }
                    else if (ContentFragment.isFindInMagazine && fragmentNum == ContentFragment.FIND_MAGAZINE) {
                        findMagazineContentFragment.backward();
                        return false;
                    }
                    else if (ContentFragment.isFindInArticle && fragmentNum == ContentFragment.FIND_ARTICLE) {
                        findArticleContentFragment.backward();
                        return false;
                    }
                    else if (ContentFragment.isFindInMagazineInfo && fragmentNum == ContentFragment.FIND_MAGAZINE_INFO) {
                        findMagazineInfoContentFragment.backward();
                        return false;
                    }
                    else {
                        finish();
                    }
                }
                else {
                    slidingMenu.showContent();
                    return false;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearFragment() {
        while(getUsingFragment() != null) {
             getFragmentManager().beginTransaction().remove(getUsingFragment()).commit();
        }
    }

    public static void setMagazineID(int magazineID) {
        if (findMagazineContentFragment.magazineID != magazineID) {
            findMagazineContentFragment.setMagazineID(magazineID);
        }
    }

    public static void setArticleID(int magazineID, int postID) {
        if (findArticleContentFragment.magazineID != magazineID || findArticleContentFragment.postID != postID) {
            findArticleContentFragment.setArticleID(magazineID, postID);
        }
    }

    void initBackend() {
        backendHandler = new Handler()
        {
            @Override
            public void handleMessage( Message msg )
            {
                Log.d( Constants.TAG, "msg" );
				/*if( msg.what == Backend.BACKEND_MSG_GET_POST_LIST_COMPLETED )
				{
					Log.d( Constants.TAG, "BACKEND_MSG_GET_POST_LIST_COMPLETED" );
					Log.d( Constants.TAG, backend.posts.toString() );
					return;
				}*/
                if ( msg.what == Backend.BACKEND_MSG_GETPOSTCONTENT_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_POST_CONTENT_COMPLETED" );
                    Log.d( Constants.TAG, backend.getPostByID(msg.arg1).postContent );
//                    tv1.setText( "demoPost正文：\n"
//                                    +demoPost.postContent
//                    );
                    return;
                }
                if ( msg.what == Backend.BACKEND_MSG_GETPOSTMEDIA_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_POST_MEDIA_COMPLETED" );
                    return;
                }
                if ( msg.what == Backend.BACKEND_MSG_GETTERMS_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_TERMS_COMPLETED" );
                    Log.d( Constants.TAG, backend.presses.toString() );
                    Log.d( Constants.TAG, backend.magazineIDsOrderByTime.toString() );
                    addMagazines();

                    //现在取回最新的magazine的文章列表
                    //这步可以拿出来写到一个Button上，但必须保证按这个Button前上面的函数完成了，，为了方便我就不写到Button里了
                    //这个函数现在好像出问题了，以前用的好好的，不知道为什么刚刚我测试的时候它提示服务器那边的API没了，我正在问DS
//                    backend.getPostsByMagazineID( latestMagazine.term_id, 10 );
                    return;
                }
                if ( msg.what == Backend.BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_POSTS_BY_MAGAZINE_ID_COMPLETED" );
                    Magazine m = backend.getMagazineByID( 6 );
                    Log.d( Constants.TAG, m.posts.toString() );
//                    tv1.setText("latestMagazine.posts现在可用" );
                    if ( latestMagazine.count == 0 )
                    {
//                        tv2.setText( "最新的杂志里面目前没有文章……");
                        //这种情况其实不应该出现，但目前测试的网站里面最新的杂志里确实没有文章。。
                    }
                    else {
                        //最新的杂志里面的文章
//                        tv2.setText( latestMagazine.posts.toString() );

                        //取回最新的杂志的第一篇文章的信息
                        demoPost = latestMagazine.posts.get(0);
//                        tv1.setText( demoPost.postTitle );

                        //取回demoPost的正文
                        //这步也可以拿到一个Button里……
                        backend.getPostContent( demoPost.postID );
                    }
                    return;
                }
                if ( msg.what == Backend.BACKEND_MSG_GETMAGAZINECOVER_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_MAGAZINE_COVER_COMPLETED" );
                    Log.d( Constants.TAG, String.valueOf(msg.arg1) );
                }
                if ( msg.what == Backend.BACKEND_MSG_RETRIVEMARKEDPOST_COMPLETED )
                {
                    Log.d( Constants.TAG, "BACKEND_MSG_GET_MAGAZINE_COVER_COMPLETED" );
                    Log.d( Constants.TAG, String.valueOf(msg.arg1) );
                    Log.d(Constants.TAG, backend.getPostByID(msg.arg1).toString());
                    //Log.d( Constants.TAG, backend.getMagazineByID(6).posts.toString() );
                }
            }

        };
        backend = new Backend(backendHandler);
        backend.getTerms();
    }

    void addMagazines() {
        ArrayList<Integer> IDs = backend.magazineIDsOrderByTime;
        for (int i = 0;i < IDs.size(); i += 2) {
            if (i + 1 < IDs.size()) {
                findContentFragment.addHottestMagazine(backend.getMagazineByID(IDs.get(i)),
                        backend.getMagazineByID(IDs.get(i + 1)));
            }
            else {
                findContentFragment.addHottestMagazine(backend.getMagazineByID(IDs.get(i)));
            }
        }
    }
}
