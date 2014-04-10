package com.fudan.dandu.dandu.dandu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by johnson on 3/15/14.
 */
public class MenuFragment extends Fragment{
    static OnHeadlineSelectedListener mCallback;

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
        View view = inflater.inflate(R.layout.menu_frame, null);
        Button find = (Button)view.findViewById(R.id.find);
        Button collect = (Button)view.findViewById(R.id.collect);
        Button suggest = (Button)view.findViewById(R.id.suggest);
        Button night = (Button)view.findViewById(R.id.night);
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

    public static void changeFragment(int fragment) {
        mCallback.onFragmentChanged(fragment);
    }
}
