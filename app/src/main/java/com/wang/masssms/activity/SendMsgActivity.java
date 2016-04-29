package com.wang.masssms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wang.masssms.R;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.uiview.IMHeadView;

/**
 * Created by wangguang on 2016/4/18.
 */
public class SendMsgActivity  extends BaseActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener,EmojiconGridFragment.OnEmojiconClickedListener{
    public static int FROM_MY_SEND_TYPE=0;
    public static int FROM_GROUP_TYPE=1;
    public static int FROM_DRAFT=2;
    public static int FROM_HAVA_SEND=3;
    EmojiconEditText mMsgET;
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
        mMsgET= (EmojiconEditText) findViewById(R.id.sendmsg_message_et);
        setEmojiconFragment(false);
    }
    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sendmsg_handle_more, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }
    public void onSendClick(View view){
        Toast.makeText(SendMsgActivity.this, "123", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(mMsgET);
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mMsgET,emojicon);
    }
}
