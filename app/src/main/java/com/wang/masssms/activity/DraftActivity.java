package com.wang.masssms.activity;

import com.wang.masssms.R;
import com.wang.masssms.uiview.IMHeadView;

import android.os.Bundle;
import android.view.View;

public class DraftActivity extends BaseActivity {
    IMHeadView mHeadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        mHeadView= (IMHeadView) findViewById(R.id.activity_draft_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                DraftActivity.this.finish();
            }
        });
    }
}
