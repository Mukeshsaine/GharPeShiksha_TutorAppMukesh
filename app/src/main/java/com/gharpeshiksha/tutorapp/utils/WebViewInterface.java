package com.gharpeshiksha.tutorapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.gharpeshiksha.tutorapp.UpgradeActivity;

import java.util.List;

public class WebViewInterface {
    Context mContext;
    //replace with your UPI handle
//    String payeeAddress = "XXXXXXXXXX@okhdfcbank";
//    String payeeName = "Dushyant";
//    String transactionNote = "Buy me a coffee!";
//    String amount = "1";
//    String currencyUnit = "INR";

    /** Instantiate the interface and set the context */
    public WebViewInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public String upiHandlers(String payUris){
//        Uri uri = Uri.parse("upi://pay?ver=01&mode=01&pa=rpy.qrgharpeshik47177257@icici&pn=GHARPESHIKSHA&tr=RZPKqLjYGYOVNTjtXqrv2&am=200&tn=PaymenttoGHARPESHIKSHA&cu=INR&mc=8299&qrMedium=04/");
        Uri uri = Uri.parse(payUris);
        Log.d("Apurav UPI deeplink", "onClick: uri: "+uri);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        List<ResolveInfo> activities =  mContext.getPackageManager().queryIntentActivities(intent,0);
        Log.v(tag, mContext.getPackageManager().resolveActivity(intent, 0) + "");
        StringBuilder apps = new StringBuilder();
        boolean first = true;
        for (ResolveInfo act : activities) {
            ActivityInfo ai = act.activityInfo;
            Log.d("###",""+" :: "+ai.applicationInfo.packageName+" | "+ai.name);
            if(first){
                apps.append(ai.applicationInfo.packageName+"|"+ai.name);
                first=!first;
            }else{
                apps.append(",").append(ai.applicationInfo.packageName+"|"+ai.name);

            }
        }
        Log.v("interface.java", apps.toString());
        return  apps.toString();
    }

    String tag = "interface.java";
    @JavascriptInterface
    public void openUPIHandler(String upiPackage,String activity, String payUri){
//        Log.v(tag, "asdfas');");
//        Uri uri = Uri.parse("upi://pay?ver=01&mode=01&pa=rpy.qrgharpeshik47177257@icici&pn=GHARPESHIKSHA&tr=RZPKqLjYGYOVNTjtXqrv2&am=100&tn=PaymenttoGHARPESHIKSHA&cu=INR&mc=8299&qrMedium=04/");
        Uri uri = Uri.parse(payUri);
        Log.d("open UPI handler", "onClick: uri: " + uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClassName(upiPackage,activity);
        if(intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
//            Toast.makeText(mContext, "No upi app found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "No upi app found", Toast.LENGTH_SHORT).show();
        }
        Log.v("interface.java", activity + ", " + upiPackage);
    }
//
//    public void onToast() {
//
//        Log.d("open UPI handler", "onClick: uri: ");
//    }

//Code to add in WebView when upiApps button is clicked by user.
//    window.onload = loadUPIHandlers;
//    function loadUPIHandlers() {
//        var a = ApuSDK.upiHandlers("upi://pay?ver=01&mode=01&pa=rpy.qrgharpeshik47177257@icici&pn=GHARPESHIKSHA&tr=RZPKqLjYGYOVNTjtXqrv2&tn=PaymenttoGHARPESHIKSHA&cu=INR&mc=8299&qrMedium=04/");
//        var upiList = a.length == 0 ? [] : a.split(",");
//        var dom = "";
//        if (upiList.length == 0) {
//            dom = '<div class="box">No UPI Apps found on phone.</div>';
//        } else {
//            for (var i = 0; i < upiList.length; i++) {
//                var info = upiList[i].split("|");
//                dom += '<div onClick="openUPI(\'' + info[0] + '\',\'' + info[1] + '\')"><h4><a href="javascript:void(0)">' + info[0] + '</a></h4></div>';
//            }
//        }
//        document.getElementById("upi").innerHTML = dom;
//    }
//
//    function openUPI(package, activity) {
//        ApuSDK.openUPIHandler(package, activity, "upi://pay?ver=01&mode=01&pa=rpy.qrgharpeshik47177257@icici&pn=GHARPESHIKSHA&tr=RZPKqLjYGYOVNTjtXqrv2&tn=PaymenttoGHARPESHIKSHA&cu=INR&mc=8299&qrMedium=04/")
//    }
}