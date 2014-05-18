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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dandu.activity.MainActivity;
import com.dandu.constant.Constants;
import com.dandu.fdureader.Magazine;
import com.dandu.fdureader.Post;
import com.dandu.menu.MenuFragment;
import com.fudan.dandu.dandu.dandu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.net.URI;
import java.util.Date;
import java.util.Map;

/**
 * Created by johnson on 4/24/14.
 */
public class FindArticleContentFragmentWeb extends ContentFragment{
    public static int magazineID = 0;
    public static int postID;
    public int oldPostID = 0;
    WebView webView;
    TextView progressTextView;
    RelativeLayout loading;

    public FindArticleContentFragmentWeb(SlidingMenu slidingMenu) {
        super(slidingMenu, FIND_ARTICLE);
    }

    @Override
    public void backward() {
        isFindInArticle = false;
        if (isFindInMagazine) {
            MenuFragment.changeFragment(FIND_MAGAZINE);
        }
        else {
            MenuFragment.changeFragment(FIND);
        }
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_article_fragment_web, container, false);
        progressTextView = (TextView)view.findViewById(R.id.progress);
        loading = (RelativeLayout)view.findViewById(R.id.loading);
        view.findViewById(R.id.collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isLogin()) {
                    return;
                }
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... param) {
                        int bookmarkID = postID;
                        XMLRPCClient client = new XMLRPCClient(Constants.uri);
                        Object[] params = { MainActivity.backend.BLOG_ID, MainActivity.backend.username, MainActivity.backend.password, bookmarkID, 1};
                        try {
                            Object bookmarksObject = client.callEx( "mark.getMark", params );
                            Object[] bookmarksObjects = (Object[])bookmarksObject;
                            Integer status = (Integer)(bookmarksObjects[0]);
                            if ( status == 1 ) {
                                Constants.addBookMark(bookmarkID);
                            }
                            else if( status == 0) {
                                Constants.removeBookMark(bookmarkID);
                            }
                        }
                        catch (XMLRPCException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
//                        what should i do
                    }
                }.execute();
            }
        });
        webView = (WebView)view.findViewById(R.id.findWebView);
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
        webView.getSettings().setJavaScriptEnabled(true);
        setArticleID(magazineID, postID);
        return view;
    }

    public void setArticleID(final int magazineID, final int postID) {
        setPostID(postID);
        setMagazineID(magazineID);
        Log.d("johnson", "set article id magazineID: " + magazineID + " postID: " + postID);
        oldPostID = postID;
        if (webView == null) {
            Log.d("johnson", "webview null");
            return;
        }
        if (magazineID == 0) {
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... param) {
                    XMLRPCClient client = new XMLRPCClient(URI.create("http://stu.fudan.edu.cn/dandu/xmlrpc.php"));
                    Object[] fields = { "post_id", "post_title",
                            "post_modified", "post_excerpt", "post_author", "link", "terms" };
                    Object[] params = { MainActivity.backend.BLOG_ID, MainActivity.backend.BLOG_USERNAME, MainActivity.backend.BLOG_PASSWORD
                            , postID, fields };
                    try {
                        Object postObject = client.callEx("wp.getPost", params);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> postMap = (Map<String, Object>) postObject;
                        Post curPost = new Post();
                        curPost.postID = Integer.valueOf((String) postMap.get("post_id"));
                        curPost.postTitle = (String) postMap.get("post_title");
                        curPost.postExcerpt = (String) postMap.get("post_excerpt");
                        curPost.postModifiedDate = (Date) postMap.get("post_modified");
                        curPost.postAuthor = (String) postMap.get("post_author");
                        curPost.postLink = (String) postMap.get("link");
                        Object[] termsObjects = (Object[]) postMap.get("terms");
                        for (Object curTermObject : termsObjects) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> curTermMap = (Map<String, Object>) curTermObject;
                            Integer magazine_id = Integer.valueOf((String) curTermMap.get("term_id"));
                            Integer press_id = Integer.valueOf((String) curTermMap.get("parent"));
                            if (press_id != 0) {
                                Magazine m = MainActivity.backend.getMagazineByID(magazine_id);
                                m.addPost(curPost);
                                setMagazineID(magazineID);
                                setPostID(curPost.postID);
                                break;
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    Log.d("johnson", "add article succeed");
                    webView.pageUp(true);
                    if (MainActivity.backend.getPostByID(postID) == null) {
                        Log.d("johnson", "post id == null");
                        return;
                    }
                    Log.d("johnson", "loading url " + MainActivity.backend.getPostByID(postID).postLink);
                    webView.loadUrl(MainActivity.backend.getPostByID(postID).postLink);
                    webView.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            Log.d("johnson", "progress: " + progress);
                        }
                    });
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
        webView.pageUp(true);
        if (MainActivity.backend.getPostByID(postID) == null){
            Log.d("johnson", "getPostByID return null");
        }
        if (MainActivity.backend.getPostByID(postID).postLink == null) {
            Log.d("johnson", "postLink == null");
            return;
        }
        Log.d("johnson", "webView url: " + MainActivity.backend.getPostByID(postID).postLink);
        webView.loadUrl(MainActivity.backend.getPostByID(postID).postLink);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                loading.setVisibility(View.VISIBLE);
//                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.loading);
//                Log.d("johnson", getActivity().getApplicationContext() + "");
//                Log.d("johnson", animation + "");
//                if (animation != null) {
//                    view.findViewById(R.id.loadingImage).startAnimation(animation);
//                }
                Log.d("johnson", "progress: " + progress);
                progressTextView.setText(progress + " %");
                if (progress >= 99) {
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });
//        MainActivity.backend.getPostContent(postID);
//        Magazine magazine = MainActivity.backend.getMagazineByID(magazineID);
//        Post post = magazine.posts.get(postID);

    }

    public void clearPage() {
//        if (webView != null) {
//            webView.loadUrl("about:blank");
//        }
    }

    void setMagazineID(int id) {
        magazineID = id;
    }

    void setPostID(int id) {
        postID = id;
    }
}
