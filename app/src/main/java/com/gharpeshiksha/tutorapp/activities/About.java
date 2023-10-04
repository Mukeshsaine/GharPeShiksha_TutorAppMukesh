package com.gharpeshiksha.tutorapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.R;

public class About extends AppCompatActivity {
    ProgressDialog progressDialog;
    WebView webView;
    String url;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        relativeLayout = findViewById(R.id.completedatalayout);
        webView = findViewById(R.id.webview);
        webView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

    }

    public void privacyweb(View view) {
        url = "https://gharpeshiksha.com/privacy-policy.jsp";
        showwebview(url);
    }

    public void helpnsupportweb(View view) {
        url = "https://www.gharpeshiksha.com/help_support.jsp";
        showwebview(url);
    }

    public void termsnuseweb(View view) {
        url = "https://gharpeshiksha.com/refund-cancellation-policy.jsp";
        showwebview(url);
    }

    public void termsnconditionweb(View view) {
        url = "https://gharpeshiksha.com/terms.jsp";
        showwebview(url);
    }

    private void showwebview(String url) {


        webView.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(About.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog.dismiss();
                finish();
                return true;
            }

            return false;
        });
        progressDialog.setCancelable(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                noNetworkDialog();
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

    protected void noNetworkDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_network_dialog);
        Button retry = dialog.findViewById(R.id.retry);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

    }
}
