package com.wang.masssms.uiview;


import com.wang.masssms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by huangjinxin on 15/5/7.
 */
public class ViewActionSheet extends BaseActionSheet {

    protected LinearLayout linearLayout;

    private TextView cancelView;

    private View mContentView;

    private TextView mTitleText;

    private TextView mCompleteButton;

    private OnClickCompleteListener mOnClickCompleteListener;

    public ViewActionSheet(Context context) {
        super(context);
    }

    public ViewActionSheet(Context context,int dialogStyle) {
        super(context,dialogStyle);
    }

    @Override
    public ViewActionSheet builder() {
        // 获取Dialog布局
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.view_actionsheet, null);
        // 设置Dialog最小宽度为屏幕宽度
        mContentView.setMinimumWidth(mDisplay.getWidth());
        // 获取自定义Dialog布局中的控件
        linearLayout = (LinearLayout) mContentView.findViewById(R.id.view_as_linearlayout);
        cancelView = (TextView) mContentView.findViewById(R.id.view_as_cancel);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        mDialog.setContentView(mContentView);

        mTitleText = (TextView)mContentView.findViewById(R.id.action_sheet_title);

        mCompleteButton = (TextView) mContentView.findViewById(R.id.action_sheet_complete);
        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickCompleteListener != null) {
                    mOnClickCompleteListener.onClickComplete(v);
                }
            }
        });

        return this;
    }

    /**
     * 添加自定义view
     *
     * @param view
     * @return
     */
    public ViewActionSheet addView(View view) {
        if (view != null) {
            linearLayout.addView(view);
        }
        return this;
    }

    public ViewActionSheet showCancelButton(boolean show) {
        if (show) {
            cancelView.setVisibility(View.VISIBLE);
        }else{
            cancelView.setVisibility(View.GONE);
        }
        return this;
    }

    public ViewActionSheet showCompleteButton(boolean show) {
        if (show) {
            mCompleteButton.setVisibility(View.VISIBLE);
        }else{
            mCompleteButton.setVisibility(View.GONE);
        }
        return this;
    }

    public ViewActionSheet setTitle(String title){
        mTitleText.setVisibility(View.VISIBLE);
        mTitleText.setText(title);
        return this;
    }

    public interface OnClickCompleteListener{
        void onClickComplete(View v);
    }

    public void setOnClickCompleteListener(OnClickCompleteListener list){
        mOnClickCompleteListener = list;
    }


}
