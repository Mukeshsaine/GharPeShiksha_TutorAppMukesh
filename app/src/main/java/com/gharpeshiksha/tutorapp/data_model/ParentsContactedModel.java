package com.gharpeshiksha.tutorapp.data_model;

import com.gharpeshiksha.tutorapp.activities.ParentsContacted;

public class ParentsContactedModel {
    private String mDate, mFees, mAddress, mAltContact, mContact, mName, mUserClass, mTeachingMode;
    private int mEnqId;

    public ParentsContactedModel(String mDate, String mFees, String mAddress, String mAltContact, String mContact, String mName, String mUserClass, String mTeachingMode, int mEnqId) {
        this.mDate = mDate;
        this.mFees = mFees;
        this.mAddress = mAddress;
        this.mAltContact = mAltContact;
        this.mContact = mContact;
        this.mName = mName;
        this.mUserClass = mUserClass;
        this.mTeachingMode = mTeachingMode;
        this.mEnqId = mEnqId;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmFees() {
        return mFees;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmAltContact() {
        return mAltContact;
    }

    public String getmContact() {
        return mContact;
    }

    public String getmName() {
        return mName;
    }

    public String getmUserClass() {
        return mUserClass;
    }

    public String getmTeachingMode() {
        return mTeachingMode;
    }

    public int getmEnqId() {
        return mEnqId;
    }
}
