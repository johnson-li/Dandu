package com.fudan.dandu.dandu.dandu;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
public class MagazineView extends View{

    String src, magazine_name, title;
    int version;
    public MagazineView(Context context) {
        super(context);
        initImageView();
        initTitle();
        initMagazineInfo();
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
//        magazineCover = new ImageView(getContext());
//        magazineCover.setImageBitmap(BitmapFactory.decodeFile(src));
    }

    void initTitle() {
    }

    void initMagazineInfo() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
    }
}
