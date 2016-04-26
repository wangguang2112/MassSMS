package com.wang.masssms.activity;

import com.wang.masssms.R;
import com.wang.masssms.uiview.IMHeadView;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wangguang on 2016/4/26.
 */
public class CollectionActivity extends BaseActivity{
    IMHeadView mHeadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_layout);
        mHeadView= (IMHeadView) findViewById(R.id.activity_collection_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                CollectionActivity.this.finish();
            }
        });
    }
}
