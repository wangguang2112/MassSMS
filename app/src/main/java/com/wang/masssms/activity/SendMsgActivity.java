package com.wang.masssms.activity;

import android.os.Bundle;
import android.view.View;

import com.wang.masssms.R;
import com.wang.masssms.uiview.IMHeadView;

/**
 * Created by wangguang on 2016/4/18.
 */
public class SendMsgActivity  extends BaseActivity{
    public static int FROM_MY_SEND_TYPE=0;
    public static int FROM_GROUP_TYPE=1;
    public static int FROM_DRAFT=2;
    public static int FROM_HAVA_SEND=3;
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
        //TODO
    }
}
