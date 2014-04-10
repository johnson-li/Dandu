package com.dandu.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.dandu.contentfragment.CollectContentFragment;
import com.dandu.contentfragment.ContentFragment;
import com.dandu.contentfragment.FindArticleContentFragment;
import com.dandu.contentfragment.FindContentFragment;
import com.dandu.contentfragment.FindMagazineContentFragment;
import com.dandu.contentfragment.FindMagazineInfoContentFragment;
import com.dandu.contentfragment.SettingContentFragment;
import com.dandu.contentfragment.SuggestContentFragmentOnboard;
import com.dandu.mainfragment.MenuFragment;
import com.dandu.constant.Constants;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity implements MenuFragment.OnHeadlineSelectedListener{

    public static int fragmentNum;
    public static int screenWidth;
    public ContentFragment findContentFragment, collectContentFragment,
            suggestContentFragment, settingContentFragment,
            findMagazineContentFragment,
            findMagazineInfoContentFragment,findArticleContentFragment,
            collectMagazineContentFragment, collectArticleContentFragment;
    SlidingMenu slidingMenu;

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
}
