package com.gharpeshiksha.tutorapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

public class HelpAndSupport extends AppCompatActivity {
    private WebView webView;
    ProgressDialog progressDialog;
    String url;
    SessionConfig sessionConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);

        sessionConfig = new SessionConfig(HelpAndSupport.this);

        url = "https://www.gharpeshiksha.com/help_support.jsp?phone=" + sessionConfig.getPhone();

        progressDialog = new ProgressDialog(HelpAndSupport.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
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
        progressDialog.setCancelable(false);
        webView = findViewById(R.id.webview);
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

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

    }
}
