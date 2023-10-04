package com.gharpeshiksha.tutorapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.gharpeshiksha.tutorapp.Interface.BasicFeature;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class BasicFeatures implements BasicFeature {

    private VolleyHelper volleyHelper = new VolleyHelper();

    public void AlertDialogFreeViews(final Context context, Long time) {

        Long totalsec = time;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.free_views_timer_cardview, null);
        final TextView textview = view.findViewById(R.id.timer);
        final ImageView closeTime = view.findViewById(R.id.closeTime);
        Button upgradeAccount = view.findViewById(R.id.upgradeAccount);

        upgradeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, UpgradeActivity.class));
            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        final AlertDialog dialog = alert.show();

        closeTime.setOnClickListener(view1 -> dialog.dismiss());

        final Long hours = totalsec / 3600;
        final Long[] min = {(totalsec % 3600) / 60};
        final Long[] sec = {(totalsec % 3600) % 60};


        new CountDownTimer(totalsec * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (sec[0] == 0) {
                    if (min[0] == 00) {
                        min[0] = Long.valueOf(59);
                    } else {
                        min[0] = min[0] - 1;
                    }
                    sec[0] = Long.valueOf(59);
                }
                if (String.valueOf(min[0]).length() == 1) {
                    if (String.valueOf(sec[0]).length() == 1) {

                        textview.setText("" + hours + "h:0" + min[0] + "m:0" + sec[0] + "s");
                    } else {
                        textview.setText("" + hours + "h:0" + min[0] + "m:" + sec[0] + "s");
                    }

                } else {
                    if (String.valueOf(sec[0]).length() == 1) {
                        textview.setText("" + hours + "h:" + min[0] + "m:0" + sec[0] + "s");
                    } else {
                        textview.setText("" + hours + "h:" + min[0] + "m:" + sec[0] + "s");
                    }

                }

                sec[0] = sec[0] - 1;
                /*alertDialog.setMessage("02:00:"+ (millisUntilFinished/1000));*/
            }

            @Override
            public void onFinish() {
/*
                alertDialog.setMessage(""+hours+":"+ min +":"+"00");
                info.setVisibility(View.GONE);*/
            }
        }.start();

    }

    public void showFreePaidDialog(final Context context) {

        Log.d("Called", "showFreePaidDialog: " + context.toString());
        try {

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(context.getResources().getLayout(R.layout.freevspaid_layout), null);
            AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Light_NoTitleBar);
//            AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Light_NoTitleBar);
            alert.setView(view);
            final AlertDialog dialog = alert.show();
            Button paid = view.findViewById(R.id.paidMemberButton);
            TextView free = view.findViewById(R.id.freeTrialButton);
            ImageView close = view.findViewById(R.id.close);

            TextView five = view.findViewById(R.id.freeFive);
            TextView six = view.findViewById(R.id.freeSix);
            TextView seven = view.findViewById(R.id.freeSeven);
            TextView eight = view.findViewById(R.id.freeEight);
            TextView nine = view.findViewById(R.id.freeNine);
            TextView ten = view.findViewById(R.id.freeTen);


            five.setPaintFlags(five.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            six.setPaintFlags(six.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            seven.setPaintFlags(seven.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            eight.setPaintFlags(eight.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            nine.setPaintFlags(nine.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ten.setPaintFlags(ten.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            paid.setOnClickListener(v -> {
                context.startActivity(new Intent(context, UpgradeActivity.class));
                dialog.dismiss();
            });
            free.setOnClickListener(v -> {
                context.startActivity(new Intent(context, UploadDocuments.class).putExtra("type", "free"));
                Log.d(TAG, "showFreePaidDialog: ");
                dialog.dismiss();
            });
            close.setOnClickListener(v -> dialog.dismiss());
        } catch(Exception e) {
            Log.v("Basic.java", e + "");
        }
    }


    public void CheckPaymentStatus(Context context, Long phone, CheckStatus checkStatus) {

        String url = "https://api.gharpeshiksha.com/PaymentStatus/statusnew";
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", "" + phone);
        volleyHelper.VolleyPostRequest(context, url, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                checkStatus.Result(response);
            }

            @Override
            public void onError(String error) {
                checkStatus.onError();
            }
        });

    }


    void showPlanExpiredSnackbar(ViewPager viewPager, Context context) {
        try {
            final Snackbar snackBar = Snackbar.make(viewPager, "Plan Expired ! Please Upgrade Now.", Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction("UPGRADE", v -> {
                context.startActivity(new Intent(context, UpgradeActivity.class));
                //snackBar.dismiss();
            });
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                    snackBar.getView().getLayoutParams();
            //snackBar.getView().setBackground(context.getResources().getDrawable(R.drawable.snackbar_background_corner));
            params.setMargins(0, 0, 0, 160);
            snackBar.getView().setLayoutParams(params);
            snackBar.setActionTextColor(context.getResources().getColor(R.color.snackbarAction))
                    .show();
        } catch (Exception e) {
            Log.d(TAG, "showPlanExpiredSnackbar: " + e);
        }
    }


    public interface CheckStatus {
        void Result(String status);

        void onError();
    }


}
