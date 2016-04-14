package com.wang.masssms.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wang.masssms.R;

/**
 * Created by wangguang on 2016/3/25.
 */
public class OpenClass extends BaseActivity {
    private LinearLayout mPreQuestionFloatLayer;


    private boolean isPreQuestionOpen=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        mPreQuestionFloatLayer= (LinearLayout)findViewById(R.id.common_chat_pre_question_layout);
        ImageView closeIV= (ImageView) findViewById(R.id.common_chat_pre_question_close);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePreQuestionFloatLayerState(false);
            }
        });
        Button openBT= (Button) findViewById(R.id.openbt);
        openBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePreQuestionFloatLayerState(true);
            }
        });
    }
    public void changePreQuestionFloatLayerState(final boolean state){
        if(isPreQuestionOpen==state) {
            return;
        }
        if(state) {
            mPreQuestionFloatLayer.setVisibility(View.VISIBLE);
        }else{
            mPreQuestionFloatLayer.setVisibility(View.GONE);

        }
        Animation animation=state? AnimationUtils.loadAnimation(this, R.anim.float_up_in):
                AnimationUtils.loadAnimation(this,R.anim.float_up_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isPreQuestionOpen = state;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mPreQuestionFloatLayer.startAnimation(animation);

    }

}
