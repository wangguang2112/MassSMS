package com.wang.masssms.uiview;


import com.wang.masssms.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by huangjinxin on 15/5/5.
 */

public abstract class BaseActionSheet {

    protected Dialog mDialog;

    protected Context mContext;

    protected Display mDisplay;

    private DialogInterface.OnCancelListener mOnCancelListener;

    private DialogInterface.OnDismissListener mOnDismissListener;

    public BaseActionSheet(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
    }

    public BaseActionSheet(Context context,int dialogStyle){
        this.mContext = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();

        mDialog = new Dialog(mContext, dialogStyle);

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mOnCancelListener != null) {
                    mOnCancelListener.onCancel(dialog);
                }
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss(dialog);
                }
            }
        });
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 构建dialog
     *
     * @return
     */
    public abstract BaseActionSheet builder();

    /**
     * 显示dialog
     */
    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    /**
     * 关闭dialog
     */
    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public BaseActionSheet setCanceledOnTouchOutside(boolean cancel) {
        if(mDialog != null){
            mDialog.setCanceledOnTouchOutside(cancel);
        }
        return this;
    }

    public BaseActionSheet setOnDismissListener(DialogInterface.OnDismissListener listener){
        mOnDismissListener = listener;
        return this;
    }

    public BaseActionSheet setOnCancelListener(DialogInterface.OnCancelListener listener){
        mOnCancelListener = listener;
        return this;
    }

    public BaseActionSheet setDialogHeight(int height){
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = height;
        dialogWindow.setAttributes(lp);
        return this;
    }
}
