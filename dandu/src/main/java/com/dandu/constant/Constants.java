package com.dandu.constant;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by johnson on 3/27/14.
 */
public class Constants {
    public static int screenWidth;
    public static float density;
    public static final String TAG = "BackendDebugging";
    public static String APP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/FDUReader/" ;
    public static String MAGAZINE_COVER_PATH = APP_PATH + "/magazines/";
    public static final String SLIDER_PATH = APP_PATH + "/slider/";


    public static int dip2px(float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static Bitmap resizeImage(String src, int height, int width) {
        Bitmap originBitmap = BitmapFactory.decodeFile(src);
        float scaleWidth = (float)width / originBitmap.getWidth();
        float scaleHeight = (float)height /originBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originBitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeImage(Bitmap originBitmap, int height, int width) {
        float scaleWidth = (float)width / originBitmap.getWidth();
        float scaleHeight = (float)height /originBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originBitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeImage(Resources res, int id, int height, int width) {
        Bitmap originBitmap = BitmapFactory.decodeResource(res, id);
        float scaleWidth = width / (float)originBitmap.getWidth();
        float scaleHeight = height / (float)originBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.reset();
        return Bitmap.createBitmap(originBitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap cutBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }
}

