package com.gharpeshiksha.tutorapp.Interface;

import android.content.Context;

import com.gharpeshiksha.tutorapp.BasicFeatures;

public interface BasicFeature {

    void AlertDialogFreeViews(Context context, Long time);

    void showFreePaidDialog(Context context);

    void CheckPaymentStatus(Context context, Long phone, BasicFeatures.CheckStatus checkStatus);
}
