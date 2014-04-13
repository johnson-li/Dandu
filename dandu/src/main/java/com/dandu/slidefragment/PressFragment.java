package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Press;
import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 4/13/14.
 */
public class PressFragment extends Fragment {
    View view;
    int pressID;
    Press press;

    public PressFragment(int pressID) {
        this.pressID = pressID;
        press = MainActivity.backend.getPressByID(pressID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.press_fragment, container, false);
        TextView title = (TextView)view.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(press.name);
        TextView magazineNum = (TextView)view.findViewById(R.id.magazineNum);
        magazineNum.setText(press.count);

        return view;
    }

    void addMagazine(Magazine magazine) {

    }

    void addMagazine(Magazine magazine1, Magazine magazine2) {

    }
}
