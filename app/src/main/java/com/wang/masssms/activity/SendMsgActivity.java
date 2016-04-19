package com.wang.masssms.activity;

import android.os.Bundle;
import android.view.View;

import com.wang.masssms.R;
import com.wang.masssms.uiview.IMHeadView;

/**
 * Created by wangguang on 2016/4/18.
 */
public class SendMsgActivity  extends BaseActivity{
    IMHeadView mHeadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmsg_layout);
        mHeadView= (IMHeadView) findViewById(R.id.activity_sendmsg_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                SendMsgActivity.this.finish();
            }
        });
    }
}
