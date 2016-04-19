package com.wang.masssms.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.wang.masssms.R;
import com.wang.masssms.uiview.IMHeadView;

/**
 * Created by wangguang on 2016/4/18.
 */
public class HandleContactActivity extends BaseActivity {
    public static String ADD_TO_GROUP = "add_to_group";
    public static int ADD_RESQUEST_CODE = 1;
    public static String DELETE_FROM_GROUP = "delete_from_group";
    public static int DELETE_REQUEST_CODE = 2;

    IMHeadView mHeadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_contact_layout);
        mHeadView= (IMHeadView) findViewById(R.id.activity_handle_contact_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                HandleContactActivity.this.finish();
            }
        });
    }
}
