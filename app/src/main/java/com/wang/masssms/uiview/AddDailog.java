package com.wang.masssms.uiview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.masssms.R;

/**
 * dailog
 * Created by wangguang on 2016/4/13.
 */
public class AddDailog {
    private Context mContext;
    private String mTitle;
    private String mMsg;
    private String mPsitive;
    private DialogInterface.OnClickListener mPsitiveClickListener;
    private String mNature;
    private DialogInterface.OnClickListener mNatureClickListener;
    private String mNegative;
    private DialogInterface.OnClickListener mNegativeClickListener;
    //添加分组时用的et
    private EditText mNameET;
    //在添加联系人时使用的
    private EditText mContactNameET;
    private EditText mContactNumberET;

    private AlertDialog.Builder builder;
    private boolean isShowAdd=false;
    private AlertDialog dialog;
    private boolean isShowing=false;
    private boolean isShowNameAndNumber=false;
    public AddDailog(Context context,boolean isShowAdd) {
        mContext = context;
        this.isShowAdd=isShowAdd;
    }
    public void show(){
        if(dialog!=null){
            dialog.show();
        }
    }
    public void dismiss(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     *
     * @param context
     * @param msg
     */
    public static void showMsg(Context context,String msg){
       showMsg(context, msg, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
       });
    }

    /**
     *
     * @param context
     * @param msg
     * @param onClickListener
     */
    public static void showMsg(Context context,String msg,DialogInterface.OnClickListener onClickListener){
        final AddDailog addDialog=new AddDailog(context,false);
        addDialog.setmMsg(msg);
        addDialog.setmTitle("提示");
        addDialog.setmPsitive("确定", onClickListener);
        addDialog.creatAndShow();
    }

    /**
     * 带取消按钮的dialog
     * @param context
     * @param msg
     * @param onClickListener
     */
    public static void showMsgWithCancle(Context context,String msg,DialogInterface.OnClickListener onClickListener){
       showMsgWithCancleListener(context,msg,onClickListener,null);
    }
    /**
     * 带取消按钮的dialog
     * @param context
     * @param msg
     * @param onClickListener
     */
    public static void showMsgWithCancleListener(Context context,String msg,DialogInterface.OnClickListener onClickListener,DialogInterface.OnClickListener onCanclelistener){
        final AddDailog addDialog=new AddDailog(context,false);
        addDialog.setmMsg(msg);
        addDialog.setmTitle("提示");
        addDialog.setmPsitive("确定", onClickListener);
        addDialog.setmNegative("取消",onCanclelistener);
        addDialog.creatAndShow();
    }
    /**
     *
     */
    public void creatAndShow(){
        create();
        show();
    }
    public void create() {
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle == null ? "" : mTitle);
        if(mMsg!=null&&!mMsg.equals("")) {
            builder.setMessage(mMsg);
        }
        if (mPsitive != null) {
            builder.setPositiveButton(mPsitive, mPsitiveClickListener);
        }
        if (mNegative != null) {
            builder.setNegativeButton(mNegative, mNegativeClickListener);
        }
        if (mNature != null) {
            builder.setNeutralButton(mNature, mNatureClickListener);
        }
        if(isShowAdd) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.ui_view_adddialog,null);
            builder.setView(view);
            mNameET= (EditText) view.findViewById(R.id.adddialog_text);
            builder.setIcon(R.drawable.group_contact_add_big);
        }
        if(isShowNameAndNumber){
            View view= LayoutInflater.from(mContext).inflate(R.layout.ui_view_adddialog_more,null);
            builder.setView(view);
            mContactNameET= (EditText) view.findViewById(R.id.dialog_name);
            mContactNumberET= (EditText) view.findViewById(R.id.dialog_number);
            builder.setIcon(R.drawable.group_contact_add_big);
        }
        dialog = builder.create();
    }
    public String getName(){
        if(mNameET!=null) {
            return mNameET.getText().toString();
        }else return "";
    }
    public String[] getContactNameAndNumber(){
        if(mContactNameET!=null&&mContactNumberET!=null) {
            return new String[]{mContactNameET.getText().toString(),mContactNumberET.getText().toString()};
        }else return null;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public void setmPsitive(String mPsitive,DialogInterface.OnClickListener psitiveClickListener) {
        this.mPsitive = mPsitive;
        mPsitiveClickListener=psitiveClickListener;
    }

    public void setmNature(String mNature,DialogInterface.OnClickListener natureClickListener) {
        this.mNature = mNature;
        mNatureClickListener=natureClickListener;
    }

    public void setmNegative(String mNegative,DialogInterface.OnClickListener negativeClickListener) {
        this.mNegative = mNegative;
        mNegativeClickListener=negativeClickListener;
    }
    public void setIsShowAdd(boolean isShowAdd) {
        this.isShowAdd = isShowAdd;
    }
    public void setIsShowNameAndNumber(boolean isShowNameAndNumber){ this.isShowNameAndNumber=isShowNameAndNumber;}
}
