package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 4/16/14.
 */
public class CollectMagazineFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collect_magazine_fragment, container, false);

        return view;
    }
}
