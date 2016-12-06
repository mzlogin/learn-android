package org.mazhuang.webviewtest;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static org.mazhuang.webviewtest.R.id.webview;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(webview);
        WebSettings settings = webView.getSettings();

        // 启用 Javascript
        settings.setJavaScriptEnabled(true);

        // 启用 localStorage 和数据库
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }

        // 允许 https 与 http 的 mixed content，
        // 详见 https://developer.android.com/about/versions/android-5.0-changes.html#BehaviorWebView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 不显示滚动条
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        // 禁止拖动网页
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        // 让当前 webView 处理页面跳转
        // https://developer.android.com/guide/webapps/webview.html#HandlingNavigation
        // https://developer.android.com/reference/android/webkit/WebViewClient.html#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://mazhuang.org");
    }
}
