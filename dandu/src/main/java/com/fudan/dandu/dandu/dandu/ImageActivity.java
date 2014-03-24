package com.fudan.dandu.dandu.dandu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import java.io.File;

/**
 * Created by johnson on 3/24/14.
 */
public class ImageActivity extends Activity{

    public static String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        displayImage(imageView);

    }

    void displayImage(ImageView imageView) {
        File file = new File(src);
        if (!file.exists() || !file.isFile()) {
            finish();
        }
//        imageView.setImageBitmap(BitmapFactory.decodeFile(src));
    }
}
