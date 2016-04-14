package com.wang.masssms.uiview;

/**
 * Loading弹出框
 *
 * @project: 58bangbang
 * @file: IMLoadingDialog.java
 * @copyright: 2014  58.com Inc.  All rights reserved.
 * @date: 2014年9月20日 下午4:00:07
 * @author 段屈直
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.wang.masssms.R;


public class IMLoadingDialog extends ProgressDialog {

    private String mText = "正在加载";
    private TextView textView;
    private boolean mIsBusyDialog;

    /**
     * Creates a new instance of IMLoadingDialog.
     *
     * @param context
     *            context
     */
    public IMLoadingDialog(Context context) {
        super(context);
    }

    /**
     * Creates a new instance of IMLoadingDialog.
     *
     * @param context
     *            context
     * @param theme
     *            theme
     */
    public IMLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * @see ProgressDialog#onCreate(Bundle)
     * @param savedInstanceState
     *            savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置底端为透明色
        getWindow().getDecorView().setBackgroundColor(getContext().getResources().getColor(R.color.transparency));
        if(mIsBusyDialog){
            setContentView(R.layout.im_busy_progress_dialog);
        }else{
            setContentView(R.layout.im_progress_dialog);
        }

        textView = (TextView) findViewById(R.id.im_progress_text_view);
        changeText();
    }

    // 改变提示文字
    private void changeText() {
        if (textView != null) {
            textView.setText(mText);
        }
    }

    /**
     * 设置提示文字，会在界面创建完成后生效。
     *
     * @author 段屈直
     * @param text
     *            提示文字内容
     */
    public void setText(String text) {
        mText = text;
        changeText();
    }

    public void setBusyDialog(boolean busydialog) {
        mIsBusyDialog = busydialog;
    }

    /**
     * IMLoadingDialog 创建器
     *
     * @author 段屈直
     * @date: 2014年9月20日 下午4:03:51
     */
    public static class Builder {
        private Context mContext;
        private String mText;
        private boolean mCancelable;
        /**加载时间*/
        private int mDuration;
        private Handler mHandler;
        private IMLoadingDialog myProgress;
        private final int DISMISS_DIALOG = 0X1;
        private boolean isBusyDialog;

        /**
         * Creates a new instance of Builder.
         * @param context context
         */
        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置提示文字
         * @author 段屈直
         * @param textID 文字资源ID
         * @return Builder
         */
        public Builder setText(int textID) {
            mText = (String) mContext.getText(textID);
            return this;
        }

        /**
         * 设置提示文字
         * @author 段屈直
         * @param text  文字内容
         * @return Builder
         */
        public Builder setText(String text) {
            mText = text;
            return this;
        }

        /**
         * 设置是否可取消
         * @author 段屈直
         * @param cancelable  是否可取消
         * @return Builder
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setOnBusyDialog(boolean onbusy){
            isBusyDialog = true;
            return this;
        }

        /**
         * 创建LoadingDialog
         * @author 段屈直
         * @return LoadingDialog示例
         */
        public IMLoadingDialog create() {
            myProgress = new IMLoadingDialog(mContext);
            myProgress.setCancelable(mCancelable);
            if (mText != null) {
                myProgress.setText(mText);
            }
            myProgress.setBusyDialog(isBusyDialog);
            return myProgress;
        }

        /**
         * 设置显示的时间，必须在show()之前调用
         * @param duration 时间值单位ms
         */
        public void setDuration(int duration){

            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                    if(msg.what == DISMISS_DIALOG){

                        if(myProgress != null && myProgress.isShowing()){

                            myProgress.dismiss();
                        }
                    }
                }
            };
            mHandler.sendEmptyMessageAtTime(DISMISS_DIALOG, duration);
        }
    }


}