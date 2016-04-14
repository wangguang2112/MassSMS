package com.wang.masssms.uiview;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by wangguang on 2016/4/13.
 */
public class AddDailog{
    Context mContext;
    String mTitle;
    String mMsg;
    String mPsitive;
    String mNature;
    String mNegative;
    AlertDialog.Builder builder;
    public AddDailog(Context context) {
        mContext=context;
    }
    public AddDailog(Context context,String title,String msg,String psbt,String ngbt,String ntbt) {
        mContext=context;
        mTitle=title;
        mMsg=msg;
        mPsitive=psbt;
        mNature=ngbt;
        mNegative=ngbt;
    }
    public void create(){
        builder=new AlertDialog.Builder(mContext);
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public void setmPsitive(String mPsitive) {
        this.mPsitive = mPsitive;
    }

    public void setmNature(String mNature) {
        this.mNature = mNature;
    }

    public void setmNegative(String mNegative) {
        this.mNegative = mNegative;
    }
}
