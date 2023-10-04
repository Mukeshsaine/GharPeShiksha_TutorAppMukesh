package com.gharpeshiksha.tutorapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gharpeshiksha.tutorapp.utils.WebViewInterface;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import java.util.ArrayList;

public class UpgradeActivity extends AppCompatActivity {

    private static final String TAG = "UpgradeActivity";
    private WebView webView;
    SessionConfig sessionConfig;
    ProgressDialog progressDialog;

    /*
    Toolbar toolbar;
    CardView cardView;
    TextView textView, textView2, textView3, bronze, bronze2, bronze3, gold, gold2, gold3, platinum, platinum2, platinum3,
            textC, textT, textU, textN;
    PrefConfig prefConfig;
    ImageView imageView, imageView2, imageView3, imageView4;
    LinearLayout bronzeLay, goldLay, platLay;
    int alpha = 50;
    int textDisable, textEnable, cardBronze, cardGold, cardPlat;
    String plan,Total_amt;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        sessionConfig = new SessionConfig(UpgradeActivity.this);
        //Below is original one.
        String url = "https://www.gharpeshiksha.com/price.jsp?phone=" + String.valueOf(sessionConfig.getPhone());
//        String url = "https://www.gharpeshiksha.com/TxnTest.jsp";
//        String url = "https://api.gharpeshiksha.com";
        Log.d(TAG, "onCreate: url load url :" + url);

        progressDialog = new ProgressDialog(UpgradeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    progressDialog.dismiss();
                    return true;
                }

                return false;
            }
        });
        progressDialog.setCancelable(false);
        webView = findViewById(R.id.upgradeWebview);
        //Enable javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebViewInterface(UpgradeActivity.this), "ApuSDK");
        Log.e(TAG, "UpgradeWebViewURL " + url);
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

//    upi://pay?ver=01&mode=01&pa=rpy.qrgharpeshik47177257@icici&pn=GHARPESHIKSHA&tr=RZPKqLjYGYOVNTjtXqrv2&am=200&tn=PaymenttoGHARPESHIKSHA&cu=INR&mc=8299&qrMedium=04/

/*
        toolbar = findViewById(R.id.toolbarUp);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        bronze = findViewById(R.id.bronze);
        bronze2 = findViewById(R.id.bronze2);
        bronze3 = findViewById(R.id.bronze3);
        gold = findViewById(R.id.gold);
        gold2 = findViewById(R.id.gold2);
        gold3 = findViewById(R.id.gold3);
        platinum = findViewById(R.id.platinum);
        platinum2 = findViewById(R.id.platinum2);
        platinum3 = findViewById(R.id.platinum3);

        cardView = findViewById(R.id.upCard);

        textC = findViewById(R.id.textC);
        textT = findViewById(R.id.textT);
        textU = findViewById(R.id.textU);
        textN = findViewById(R.id.textN);

        imageView = findViewById(R.id.cont);
        imageView2 = findViewById(R.id.tut);
        imageView3 = findViewById(R.id.unl);
        imageView4 = findViewById(R.id.ean);

        bronzeLay = findViewById(R.id.bronzeLay);
        goldLay = findViewById(R.id.goldLay);
        platLay = findViewById(R.id.platLay);
        toolbar.setTitle("Upgrade");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        prefConfig = new PrefConfig(getApplicationContext());
        textDisable = Color.argb(alpha, 255, 0, 0);

        cardBronze = Color.rgb(213, 224, 232);
        cardGold = Color.rgb(252, 248, 227);
        cardPlat = Color.rgb(223, 240, 216);

        textEnable = Color.BLACK;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String string = "<b><font color='red'>* Coupon code is valid for New Users for Limited Period Only</font><b/>";
        textView3.setText(Html.fromHtml(string));
        layListener();

        setPlatinum();
        toast("platinum","10000");*/
    }

    /*
    private void layListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColorBack(bronzeLay, bronze, bronze2, bronze3);
                changeColorBack(goldLay, gold, gold2, gold3);
                changeColorBack(platLay, platinum, platinum2, platinum3);

                switch (v.getId()) {
                    case R.id.bronzeLay:

                        changeColor(bronzeLay, bronze, bronze2, bronze3);
                        imageChange(R.drawable.lock, R.drawable.lock, R.drawable.lock, R.drawable.lock, textDisable, textDisable,
                                textDisable, textDisable, cardBronze);
                        changeText("<b><font color='red'> GPSNEW40</font><b/> to <b><font color='red'>Get 40% off</font><b/>",
                                "<strike>5000</strike> <b><font color='red'>₹3000</font><b/>");
                        toast("bronze","5000");
//                        prefConfig.writePlan("bronze");

                        break;
                    case R.id.goldLay:

                        changeColor(goldLay, gold, gold2, gold3);
                        imageChange(R.drawable.parents1, R.drawable.suggestion1, R.drawable.clients, R.drawable.lock,
                                textEnable, textEnable, textEnable, textDisable, cardGold);
                        changeText("<b><font color='red'> GPSNEW50</font><b/> to <b><font color='red'>Get 50% off</font><b/>",
                                "<strike>8000</strike> <b><font color='red'>₹4000</font><b/>");
                        toast("gold","8000");
//                        prefConfig.writePlan("gold");

                        break;
                    case R.id.platLay:

                        setPlatinum();
                        toast("platinum","10000");
//                        prefConfig.writePlan("platinum");
                        break;
                }
//                Log.d(TAG, "onClick: " + prefConfig.readPlan());
            }
        };

        bronzeLay.setOnClickListener(listener);
        goldLay.setOnClickListener(listener);
        platLay.setOnClickListener(listener);
    }

    private void changeColor(LinearLayout layout, TextView textView, TextView textView2, TextView textView3) {
        layout.setBackgroundResource(R.drawable.second_border);
        textView.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.WHITE);
        textView3.setTextColor(Color.WHITE);
    }

    private void changeColorBack(LinearLayout layout, TextView textView, TextView textView2, TextView textView3) {
        layout.setBackgroundResource(R.drawable.border);
        textView.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        textView3.setTextColor(Color.BLACK);
    }

    private void imageChange(int drawable, int drawable2, int drawable3, int drawable4, int color,
                             int color2, int color3, int color4, int cardColor) {
        imageView.setBackgroundResource(drawable);
        imageView2.setBackgroundResource(drawable2);
        imageView3.setBackgroundResource(drawable3);
        imageView4.setBackgroundResource(drawable4);

        textC.setTextColor(color);
        textT.setTextColor(color2);
        textU.setTextColor(color3);
        textN.setTextColor(color4);

        cardView.setCardBackgroundColor(cardColor);
    }

    private void changeText(String code, String text) {
        String string = "*Use" + code;
        String string2 = "Pay: ₹" + text;

        textView.setText(Html.fromHtml(string));
        textView2.setText(Html.fromHtml(string2));
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColorBack(bronzeLay, bronze, bronze2, bronze3);
        changeColorBack(goldLay, gold, gold2, gold3);
        setPlatinum();

//        prefConfig.writePlan("platinum");
    }

    private void toast(String string ,String charge) {
        plan=string;
        Total_amt=charge;
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    private void setPlatinum() {
        changeColor(platLay, platinum, platinum2, platinum3);
        imageChange(R.drawable.parents1, R.drawable.suggestion1, R.drawable.clients, R.drawable.notification12,
                textEnable, textEnable, textEnable, textEnable, cardPlat);
        changeText("<b><font color='red'> GPSNEW50</font><b/> to <b><font color='red'>Get 50% off</font><b/>",
                "<strike>10000</strike> <b><font color='red'>₹5000</font><b/>");
    }

    public void upgradeOnClick(View view) {

        Intent intent = new Intent(getApplicationContext(), PaytmGatewayActivity.class);

        intent.putExtra("plan",plan);
        intent.putExtra("total_amt",Total_amt);
        startActivity(intent);
    }*/

    protected void noNetworkDialog() {
        if(!isFinishing()) {
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
    final int UPI_PAYMENT = 0;
    public void startIntent(Intent intent) {
        Log.v("UpgradeActivity.java", "hello");
        startActivityForResult(intent, UPI_PAYMENT);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v("UpgradeActivity.java", "paused");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.v("UpgradeActivity.java", "stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("UpgradeActivity.java", "destroyed");
    }

    //onActivityResult method called by android when transaction is done.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        //Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        //Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(UpgradeActivity.this)) {
            String str = data.get(0);
            //Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(UpgradeActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                // Log.d("UPI", "responseStr: "+approvalRefNo);
                Toast.makeText(this, "YOUR ORDER HAS BEEN PLACED\n THANK YOU AND ORDER AGAIN", Toast.LENGTH_LONG).show();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(UpgradeActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(UpgradeActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(UpgradeActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
