package com.dandu.constant;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 4/18/14.
 */
public class Trash {
    static List<Bitmap> bitmapList = new ArrayList<Bitmap>();

    public static void drop(Bitmap bitmap) {
        bitmapList.add(bitmap);
    }

    public static void recycle() {
        for (Bitmap bitmap : bitmapList) {
            bitmap.recycle();
        }
    }
}
