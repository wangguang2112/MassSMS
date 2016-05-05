package com.wang.masssms.uiview.multiplechoice;

import com.wang.masssms.R;
import com.wang.masssms.uiview.ViewActionSheet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮帮项目组 Bangbang
 *
 * @Author zhaobo
 * @Date 2015/5/23 13:27
 * @Copyright:58.com Inc. All rights reserved.
 */
public class MultipleChoiceActionSheet<T> extends ViewActionSheet implements ViewActionSheet.OnClickCompleteListener {
    private View mView;
    private ListView mList;
    private MultipleChoiceSelectorAdapter<T> mAdapter;
    private Context mContext;
    private OnActionSheetListener mOnActionSheetListener;
    private String mDisplayField;
    private TextView mInfoTextView;
    private int mChoiceMaxCount = -1;
    private String mChoiceMaxMsg;

    public MultipleChoiceActionSheet(Context context) {
        super(context);
        mContext = context;
    }

    public MultipleChoiceActionSheet(Context context, int dialogStyle) {
        super(context, dialogStyle);
        mContext = context;
    }

    public void setAdapter(MultipleChoiceSelectorAdapter<T> adapter){
        mAdapter=adapter;
        if(mList!=null){
            mList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public ViewActionSheet builder() {
        ViewActionSheet actionSheet = super.builder();
        this.showCompleteButton(true);
        actionSheet.setOnClickCompleteListener(this);
        mView = LayoutInflater.from(mContext).inflate(R.layout.actionsheet_miltiple_choice, null);
        actionSheet.addView(mView);
        initView();
        return actionSheet;
    }
    private void initView() {
        mList = (ListView) mView.findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checked = false;
                if(mAdapter != null){
                    List<Integer> poss = mAdapter.getPositions();
                    if(poss.contains(position)){
                        mAdapter.removeDataChanged(position);
                        if(mOnActionSheetListener != null){
                            mOnActionSheetListener.onItemClick(position,false);
                        }
                    }else{
                        if(mChoiceMaxCount > 0 && mAdapter.getPositions() != null && mAdapter.getPositions().size() >= mChoiceMaxCount){
                            if(mChoiceMaxMsg != null && !mChoiceMaxMsg.equals("")){
                                Toast.makeText( mContext, mChoiceMaxMsg,Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText((Activity) mContext, "最多可选" + mChoiceMaxCount + "项", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            mAdapter.addDataChanged(position);
                            if(mOnActionSheetListener != null){
                                mOnActionSheetListener.onItemClick(position,true);
                            }
                        }
                    }
                }
            }
        });
        mInfoTextView = (TextView)mView.findViewById(R.id.info);
    }

    public ViewActionSheet setInfoText(String info){
        mInfoTextView.setVisibility(View.VISIBLE);
        mInfoTextView.setText(info);
        return this;
    }

    public void loadListData(ArrayList<T> data, List<Integer> poss) {
        if (mAdapter == null) {
            mAdapter = new MultipleChoiceReflectionAdapter(mContext, mDisplayField);
            mList.setAdapter(mAdapter);
        }
        if (data != null) {
            mAdapter.setData(data);

        }
        if(poss != null) {
            mAdapter.notifyDataSetChanged(poss);
        }

    }

    @Override
    public void onClickComplete(View v) {
        //start by zhaobo for fixed bug http://bugly.qq.com/detail?app=900001843&pid=1&ii=7477
        if(mOnActionSheetListener != null && mAdapter != null){
            mOnActionSheetListener.onClickComplete(mAdapter.getPositions());
        }
        //end by zhaobo
    }

    public interface OnActionSheetListener {
        public void onClickComplete(List<Integer> positions);
        public void onItemClick(int position, boolean isChecked);
    }

    public void setOnActionSheetListener(OnActionSheetListener listener) {
        mOnActionSheetListener = listener;
    }

    public ViewActionSheet setDisplayField(String displayField) {
        mDisplayField = displayField;
        return this;
    }

    public ViewActionSheet setChoiceMaxCount(int maxCount){
        mChoiceMaxCount = maxCount;
        return this;
    }

    /**
     * 达到最大上限的提示语
     * @param maxMsg
     * @return
     */
    public ViewActionSheet setMaxMessage(String maxMsg){
        mChoiceMaxMsg = maxMsg;
        return this;
    }

}
