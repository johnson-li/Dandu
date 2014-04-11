package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 3/22/14.
 */
public class NewestFragment extends Fragment implements View.OnTouchListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newest_fragment, container, false);
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("johnson", "newest touch");
        return false;
    }
}
