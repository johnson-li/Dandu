package com.dandu.contentfragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.dandu.activity.MainActivity;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by johnson on 4/24/14.
 */
public class FindArticleContentFragmentWeb extends ContentFragment{
    public static int magazineID;
    public static int postID;
    WebView webView;

    public FindArticleContentFragmentWeb(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_ARTICLE);
    }

    @Override
    public void backward() {
        isFindInArticle = false;
        MenuFragment.changeFragment(FIND_MAGAZINE);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_article_fragment_web, container, false);
        Button back = (Button)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        Button share = (Button)view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.share_dialog);
            }
        });
        webView = (WebView)view.findViewById(R.id.findWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MainActivity.backend.getPostByID(postID).postLink);
        Log.d("johnson", MainActivity.backend.getPostByID(postID).postLink);
        return view;
    }

    public void setArticleID(int magazineID, int postID) {
        if (magazineID == 0) {
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {

                    return null;
                }

                @Override
                protected void onPostExecute(String s) {

                }
            }.execute();
            return;
        }

        //postID == 0 means that it's debugging and there is no need to do anything with backend
        if (postID == 0) {
            return;
        }
        this.postID = postID;
        this.magazineID = magazineID;
        if (webView == null) {
            return;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MainActivity.backend.getPostByID(postID).postLink);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                Log.d("johnson", "progress: " + progress);
            }
        });
//        MainActivity.backend.getPostContent(postID);
//        Magazine magazine = MainActivity.backend.getMagazineByID(magazineID);
//        Post post = magazine.posts.get(postID);

    }
}
