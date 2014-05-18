package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandu.constant.Constants;
import com.fudan.dandu.dandu.dandu.R;

import java.util.ArrayList;

/**
 * Created by johnson on 4/16/14.
 */
public class CollectMagazineFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collect_magazine_fragment, container, false);
        ArrayList<Integer> magazines = Constants.getCollectMagazine();
        if (magazines.size() == 0) {
            return view;
        }
        view.findViewById(R.id.empty).setVisibility(View.GONE);
        view.findViewById(R.id.noEmpty).setVisibility(View.VISIBLE);
        return view;
    }
}
