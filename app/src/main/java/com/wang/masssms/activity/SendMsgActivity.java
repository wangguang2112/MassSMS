package com.wang.masssms.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wang.masssms.R;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.proxy.SendMsgProxy;
import com.wang.masssms.uiview.IMHeadView;

/**
 * Created by wangguang on 2016/4/18.
 */
public class SendMsgActivity extends BaseActivity
        implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    public static final int FROM_MY_SEND_TYPE = 0;

    public static final int FROM_GROUP_TYPE = 1;

    public static final int FROM_DRAFT = 2;

    public static final int FROM_HAVA_SEND = 3;

    EmojiconEditText mMsgET;

    EmojiconTextView mMsgTV;

    FrameLayout mMoreLayout;

    RadioGroup mMoreBox;
    //关闭按钮
    RadioButton mMoreCloseBT;
    IMHeadView mHeadView;

    ImageView mAddBT;
    private SendMsgProxy mProxy;
    private FragmentManager mFragmentManager;

    private EmojiconsFragment mEmojiconsFragment;
    private boolean isMoreLayoutShow=false;

    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmsg_layout);
        mFragmentManager = getSupportFragmentManager();
        mHeadView = (IMHeadView) findViewById(R.id.activity_sendmsg_headbar);
        mHeadView.setOnReturnButtonClickListener(new IMHeadView.OnReturnButtonClickListener() {
            @Override
            public void onReturnClick(View view) {
                hideSoftKeyboard();
                SendMsgActivity.this.finish();
            }
        });
        mMsgET = (EmojiconEditText) findViewById(R.id.sendmsg_message_et);
        mMsgET.setOnClickListener(this);
        mMsgTV = (EmojiconTextView) findViewById(R.id.sendmsg_message_tv);
        mMsgET.setOnClickListener(this);
        mMoreBox = (RadioGroup) findViewById(R.id.sendmsg_handle_box);
        mMoreBox.setOnCheckedChangeListener(this);
        mMoreLayout = (FrameLayout) findViewById(R.id.sendmsg_handle_more);
        mAddBT= (ImageView) findViewById(R.id.sendmsg_add_bt);
        mAddBT.setOnClickListener(this);
        mMoreBox.clearCheck();
        mMoreCloseBT= (RadioButton) findViewById(R.id.handle_close);
        mMoreCloseBT.setVisibility(View.INVISIBLE);
//        预加载下比较好
        mEmojiconsFragment = EmojiconsFragment.newInstance(false);
        mProxy=new SendMsgProxy(this,getCallbackHandler());
        type=getIntent().getIntExtra("type",0);
        initData();
    }
    private  void initData(){
        switch (type){
            case FROM_MY_SEND_TYPE:
                mAddBT.setImageResource(R.drawable.add_contact);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgTV.setVisibility(View.GONE);
                break;
            case FROM_GROUP_TYPE:
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgTV.setVisibility(View.GONE);
                break;
            case FROM_DRAFT:
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.GONE);
                mMsgTV.setVisibility(View.VISIBLE);
                mMsgTV.setText(getIntent().getStringExtra("msg"));
                break;
            case FROM_HAVA_SEND:
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.GONE);
                mMsgTV.setVisibility(View.VISIBLE);
                mMsgTV.setText(getIntent().getStringExtra("msg"));
                break;
            default:
                break;

        }
    }
    public void onSendClick(View view) {
        Toast.makeText(SendMsgActivity.this, "123", Toast.LENGTH_SHORT).show();
        //TODO
        mMsgTV.setVisibility(View.VISIBLE);
        mMsgET.setVisibility(View.GONE);
        mMsgTV.setText(mMsgET.getText().toString());
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(mMsgET);
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mMsgET, emojicon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendmsg_desc_tv:
                mMsgTV.setVisibility(View.GONE);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgET.setText(mMsgTV.getText());
                showSoftKeyboard(mMsgET);
            break;
            case R.id.sendmsg_message_et:
                showSoftKeyboard(mMsgET);
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.handle_face:
                hideSoftKeyboard();
                if (mEmojiconsFragment == null) {
                    mEmojiconsFragment = EmojiconsFragment.newInstance(false);
                }

                    mFragmentManager.beginTransaction()
                            .replace(R.id.sendmsg_handle_more, mEmojiconsFragment)
                            .commitAllowingStateLoss();
                displayMore(true);
                break;
            case R.id.handle_close:
                displayMore(false);
                break;
            default:
                break;
        }
    }
    // 隐藏键盘
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mMsgET.getWindowToken(), 0);
    }
    // 显示键盘
    private void showSoftKeyboard(EditText editText) {
        if (editText == null) {
            return;
        }
        displayMore(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 展示下面的按钮
     * @param isShow
     */
    private void displayMore(boolean isShow){
        if(isShow&&isShow!=isMoreLayoutShow){
            mMoreLayout.setVisibility(View.VISIBLE);
            isMoreLayoutShow=true;
            mMoreCloseBT.setVisibility(View.VISIBLE);
        }else if(!isShow||isShow!=isMoreLayoutShow){
            mMoreBox.clearCheck();
            mMoreLayout.setVisibility(View.GONE);
            mMoreCloseBT.setVisibility(View.INVISIBLE);
            isMoreLayoutShow = false;
        }
        return;
    }
}

