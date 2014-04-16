package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 4/16/14.
 */
public class CollectArticleFragment extends Fragment{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collect_article_fragment, container, false);


        return view;
    }
}
