package com.dandu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dandu.contentfragment.ContentFragment;
import com.fudan.dandu.dandu.dandu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by johnson on 4/18/14.
 */
public class CoverActivity extends Activity{
    public static List<Integer> coverList = new ArrayList<Integer>();
    long time = System.currentTimeMillis();
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.app_cover);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        changeView();
                        break;
                    default:
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                setPriority(Thread.MIN_PRIORITY);
                while (true) {
                    if (System.currentTimeMillis() - time >= 3000) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    }
                }
            }
        }.start();
    }

    void changeView() {
        setContentView(R.layout.cover_image);
        coverList.add(R.drawable.cover1);
        coverList.add(R.drawable.cover2);
        ImageView coverImage = (ImageView)findViewById(R.id.coverImage);
        coverImage.setImageResource(coverList.get(new Random().nextInt(2)));
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().setAttributes(attrs);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                Intent intent = new Intent(CoverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

class MyScrollView extends ScrollView {
    public MyScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}