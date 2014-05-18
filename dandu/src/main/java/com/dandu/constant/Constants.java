package com.dandu.constant;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 3/27/14.
 */
public class Constants {
    public static int screenWidth;
    public static float density;
    public static String userName = "";
    public static String password = "";
    public static final String TAG = "BackendDebugging";
    public static String APP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/FDUReader/" ;
    public static String CACHE = APP_PATH + "/cache/";
    public static String MAGAZINE_COVER_PATH = APP_PATH + "/magazines/";
    public static final String SLIDER_PATH = APP_PATH + "/slider/";
    public static final String HOTTEST_SRC = CACHE + "/hottest.dat";
    public static final String NEWEST_SRC = CACHE + "/newest.dat";
    public static final String COLLECT_SRC = CACHE + "/collect.dat";
    public static final URI uri = URI.create( "http://stu.fudan.edu.cn/dandu/xmlrpc.php" );
    public static final String loginUrl = "http://stu.fudan.edu.cn/teleport/gateway/request?returnurl=http://stu.fudan.edu.cn/dandu/auth.php&appid=197741d5cf8ac47a598a1a82d2a0e63824199b1a71e3984d463d5845646ad54e";
    public static final String signHead = "http://stu.fudan.edu.cn/dandu/auth.php|197741d5cf8ac47a598a1a82d2a0e63824199b1a71e3984d463d5845646ad54e";
    public static final String signTail = "f3f613a3b3efe41b66a701b96425f6ce14cbdca7082d8719c996143967c16c8e";
    private static ArrayList<Integer> hottestMagazines = new ArrayList<Integer>();
    private static ArrayList<Integer> newestMagazines = new ArrayList<Integer>();
    private static ArrayList<Integer> bookMarks = new ArrayList<Integer>();

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
        Log.d("johnson", width + " " + originBitmap.getWidth());
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

    public static Bitmap resizeImageByWidth(String src, int width) {
        Bitmap bitmap = BitmapFactory.decodeFile(src);
        float scale = width / (float)bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.reset();
        Log.d("johnson", bitmap.getWidth() + "width");
        Log.d("johnson", width + "width");
        Log.d("johnson", bitmap.getHeight() * scale + "height");
        return Bitmap.createBitmap(bitmap, 0, 0, width, (int)(bitmap.getHeight() * scale), matrix, true);
    }

    public static int getMagazineCoverWidth() {
        return (screenWidth - 2 * dip2px(16 + 7)) / 2;
    }

    public static int getMagazineCoverHeight(String str) {
        Bitmap bitmap = BitmapFactory.decodeFile(str);
        return bitmap.getHeight() * getMagazineCoverWidth() / bitmap.getWidth();
    }

    public static void setHottestMagazines(ArrayList<Integer> list) {
        File file = new File(HOTTEST_SRC);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        hottestMagazines = new ArrayList<Integer>(list);
    }

    public static void setNewestMagazines(ArrayList<Integer> list) {
        File file = new File(NEWEST_SRC);
        if (file.exists()) {
            file.delete();
        }
        try {
            Log.d("johnson", "write to file: newest");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        newestMagazines = new ArrayList<Integer>(list);
    }

    public static ArrayList<Integer> getHottestMagazines() {
        File file = new File(HOTTEST_SRC);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            hottestMagazines = (ArrayList<Integer>)objectInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hottestMagazines;
    }

    public static ArrayList<Integer> getNewestMagazines() {
        File file = new File(NEWEST_SRC);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            newestMagazines = (ArrayList<Integer>)objectInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newestMagazines;
    }

    public static boolean isLogin() {
        if (userName == null || userName.equals("")) {
            return false;
        }
        return true;
    }

    public static boolean hasBookMark(int n) {
        return bookMarks.contains(n);
    }

    public static void addBookMark(int n) {
        if (!hasBookMark(n)) {
            bookMarks.add(n);
            cacheBookMark();
        }
    }

    public static void removeBookMark(Integer n) {
        if (hasBookMark(n)) {
            bookMarks.remove(n);
            cacheBookMark();
        }
    }

    public static void updateBookMark() {
        if (!isLogin()) return;
        new Thread() {
            @Override
            public void run() {
                ArrayList<Integer> list = new ArrayList<Integer>();
                XMLRPCClient client = new XMLRPCClient(uri);
                Object[] params = { 1, userName, password };
                try {
                    Object bookmarksObject = client.callEx( "mark.getUserMark", params );
                    Object[] bookmarksObjects = (Object[])bookmarksObject;
                    for( Object curBookmark : bookmarksObjects ) {
                        list.add( Integer.valueOf((String)(curBookmark.toString())) );
                    }
                } catch (XMLRPCException e) {
                    e.printStackTrace();
                }
                bookMarks = list;
                cacheBookMark();
            }
        }.start();
    }

    public static void cacheBookMark() {
        File file = new File(COLLECT_SRC);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bookMarks);
            fileOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> getCollectMagazine() {
        ArrayList<Integer> magazines = new ArrayList<Integer>();
        for (Integer i: bookMarks) {
            if (i < 0) {
                magazines.add(i);
            }
        }
        return magazines;
    }

    public static ArrayList<Integer> getCollectArticle() {
        ArrayList<Integer> articles = new ArrayList<Integer>();
        for (Integer i: bookMarks) {
            if (i > 0) {
                articles.add(i);
            }
        }
        return articles;
    }
}

