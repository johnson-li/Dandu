package com.fudan.dandu.dandu.dandu;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by johnson on 3/22/14.
 */
public class MagazineView extends ViewGroup{

    String src, magazine_name, title;
    ImageView magazineCover;
    TextView titleTextView, magazineInfoTextView;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    int version;
    public MagazineView(Context context) {
        super(context);
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setMagazine_name(String magazine_name) {
        this.magazine_name = magazine_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMagazineVersion(int version) {
        this.version = version;
    }

    void initImageView() {
        magazineCover = new ImageView(getContext());
        magazineCover.setImageBitmap(BitmapFactory.decodeFile(src));
        addView(magazineCover);
    }

    void initTitle() {
        titleTextView = new TextView(getContext());
        titleTextView.setText(title);
        addView(titleTextView);
    }

    void initMagazineInfo() {
        magazineInfoTextView = new TextView(getContext());
        String str = magazine_name + " - " + "第" + version + "期";
        magazineInfoTextView.setText(str);
        addView(magazineInfoTextView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        initImageView();
        initTitle();
        initMagazineInfo();
        titleTextView.layout(l, t, r, b);
    }

    @Override
    public void addView(View child) {
        addView(child, -1);
    }

    @Override
    public View getChildAt(int index) {
        switch (index) {
            case 0:
                return magazineCover;
            case 1:
                return titleTextView;
            case 2:
                return magazineInfoTextView;
            default:
                return null;
        }
    }
}
