package com.gharpeshiksha.tutorapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;

public class OnNotificationWebView extends AppCompatActivity {
    ProgressDialog progressDialog;
    WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_notification_web_view);

        webView = findViewById(R.id.webviewNoti);
        progressDialog = new ProgressDialog(OnNotificationWebView.this);
        progressDialog.setMessage("Loading...");

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
            startActivity(new Intent(OnNotificationWebView.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else {
            Log.e("Url@NotificationWebView", url);

            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                        progressDialog.dismiss();
                        finish();
                        return true;
                    }

                    return false;
                }
            });

            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressDialog.dismiss();
                }

            });
        }
    }
}
