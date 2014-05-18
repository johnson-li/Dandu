package com.dandu.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dandu.constant.Constants;
import com.fudan.dandu.dandu.dandu.R;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by johnson on 4/26/14.
 */
public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        WebView webView = (WebView)findViewById(R.id.loginWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("authsucc")) {
                    String str = url.substring(11);
                    Constants.userName = str.split("\\.")[0];
                    Constants.password = str.split("\\.")[1];
                    MainActivity.rightMenuFragment.updateUserName();
                    MainActivity.backend.isLogined = true;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", Constants.userName);
                    editor.putString("password", Constants.password);
                    editor.commit();
                    Log.d("johnson", Constants.userName + " " + Constants.password);
                    finish();
                }
                view.loadUrl(url);
                return true;
            }
        });
        String random = "";
        String sign = "";
        for (int i = 0; i < 20; i++) {
            int ma=(int) (Math.random()*25+65);
            char ca = (char)ma;
            random += ca + "";
        }
        Log.d("johnson", random);
        try {
            sign = computeHash(Constants.signHead + "|" + random + "|||" + Constants.signTail);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String url = Constants.loginUrl + "&state=" + random + "&sign=" + sign;
        Log.d("johnson", url);

        webView.loadUrl(url);
    }

    public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
