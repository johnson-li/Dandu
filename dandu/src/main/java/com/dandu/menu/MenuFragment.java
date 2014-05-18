package com.dandu.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandu.activity.ImageActivity;
import com.dandu.activity.LoginActivity;
import com.dandu.constant.Constants;
import com.dandu.contentfragment.ContentFragment;
import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 3/15/14.
 */
public class MenuFragment extends Fragment{
    static OnHeadlineSelectedListener mCallback;
    View view;
    public interface OnHeadlineSelectedListener {
        public void onFragmentChanged(int fragmentNum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_frame, null);
        TextView userName = (TextView)view.findViewById(R.id.userName);
        if (Constants.userName != null && !Constants.userName.equals("")) {
            userName.setText(Constants.userName);
        }
        Button find = (Button)view.findViewById(R.id.find);
        Button collect = (Button)view.findViewById(R.id.collect);
        Button suggest = (Button)view.findViewById(R.id.suggest);
        Button night = (Button)view.findViewById(R.id.night);
        ImageView head = (ImageView)view.findViewById(R.id.head);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContentFragment.isFindInArticle) {
                    changeFragment(ContentFragment.FIND_ARTICLE);
                }
                else if(ContentFragment.isFindInMagazineInfo) {
                    changeFragment(ContentFragment.FIND_MAGAZINE_INFO);
                }
                else if(ContentFragment.isFindInMagazine) {
                    changeFragment(ContentFragment.FIND_MAGAZINE);
                }
                else {
                    changeFragment(ContentFragment.FIND);
                }
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(ContentFragment.COLLECT);
            }
        });
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContentFragment.isSettingClicked) {
                    changeFragment(ContentFragment.SETTING);
                }
                else {
                    changeFragment(ContentFragment.SUGGEST);
                }
            }
        });
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserName() {
        ((TextView)view.findViewById(R.id.userName)).setText(Constants.userName);
    }

    public static void changeFragment(int fragment) {
        mCallback.onFragmentChanged(fragment);
    }
}
