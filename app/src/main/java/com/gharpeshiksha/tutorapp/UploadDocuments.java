package com.gharpeshiksha.tutorapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

public class UploadDocuments extends AppCompatActivity {
    private WebView webView;
    ProgressDialog progressDialog;
    SessionConfig sessionConfig;
    private Long phone;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    String url;

    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);

        sessionConfig = new SessionConfig(UploadDocuments.this);

        phone = sessionConfig.getPhone();

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("free")) {
                url = "https://www.gharpeshiksha.com/free_documents.jsp?phone=" + phone;
            } else if (getIntent().getStringExtra("type").equals("paid")) {
                url = "https://www.gharpeshiksha.com/documents.jsp?phone=" +phone;
            }
        } else {
            url = "https://www.gharpeshiksha.com/documents.jsp?phone=" + phone;
        }

        progressDialog = new ProgressDialog(UploadDocuments.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog.dismiss();
                finish();
                return true;
            }

            return false;
        });

        // intent to browser

       /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gharpeshiksha.com/documents.jsp"));
        startActivity(browserIntent);
        finish();*/

        webView = findViewById(R.id.webviewupload);
        webView.getSettings().setJavaScriptEnabled(true);
        if(phone!=null){
            webView.loadUrl(url);
        }


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

        webView.setWebChromeClient(new WebChromeClient() {
                                       // For 3.0+ Devices (Start)
                                       // onActivityResult attached before constructor

                                       // For Lollipop 5.0+ Devices
                                       public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                                           if (uploadMessage != null) {
                                               uploadMessage.onReceiveValue(null);
                                               uploadMessage = null;
                                           }

                                           uploadMessage = filePathCallback;

                                           Intent intent = null;
                                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                               intent = fileChooserParams.createIntent();
                                           }
                                           try {
                                               startActivityForResult(intent, REQUEST_SELECT_FILE);
                                           } catch (ActivityNotFoundException e) {
                                               uploadMessage = null;
                                               Toast.makeText(UploadDocuments.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                                               return false;
                                           }
                                           return true;
                                       }
                                   }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("d", "onActivityResult: uploaded  documents");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = data == null || resultCode != UploadDocuments.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(UploadDocuments.this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }


    protected void noNetworkDialog() {

        try {
            final Dialog dialog = new Dialog(UploadDocuments.this);

            dialog.setContentView(R.layout.no_network_dialog);
            Button retry = dialog.findViewById(R.id.retry);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            retry.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        } catch(Exception e) {
            Log.v("Upload.java", e + "");
        }


    }
}
