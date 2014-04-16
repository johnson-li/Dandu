package com.dandu.slidefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Press;
import com.fudan.dandu.dandu.dandu.R;

/**
 * Created by johnson on 4/13/14.
 */
public class IntroductionFragment extends Fragment implements View.OnTouchListener{
    int pressID;
    Press press;
    View view;
    public IntroductionFragment(int pressID) {
        this.pressID = pressID;
        press = MainActivity.backend.getPressByID(pressID);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.introduction_fragment, container, false);
        TextView title = (TextView)view.findViewById(R.id.title);
        title.getPaint().setFakeBoldText(true);
        title.setText(press.name);
        TextView magazineNum = (TextView)view.findViewById(R.id.magazineNum);
//        magazineNum.setText(press.count);
        TextView introduction = (TextView)view.findViewById(R.id.introduction);
        introduction.setText(press.description);

        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
