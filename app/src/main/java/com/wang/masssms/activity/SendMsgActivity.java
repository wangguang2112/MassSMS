package com.wang.masssms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wang.masssms.R;
import com.wang.masssms.adapter.GroupListAdapter;
import com.wang.masssms.adapter.MessageListAdapter;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.ContactToGroup;
import com.wang.masssms.model.orm.Contacts;
import com.wang.masssms.proxy.ContactProxy;
import com.wang.masssms.proxy.GroupListProxy;
import com.wang.masssms.proxy.MessageProxy;
import com.wang.masssms.proxy.ProxyEntity;
import com.wang.masssms.proxy.SendMsgProxy;
import com.wang.masssms.uiview.AddDailog;
import com.wang.masssms.uiview.IMHeadView;
import com.wang.masssms.uiview.multiplechoice.MultipleChoiceActionSheet;
import com.wang.masssms.uiview.multiplechoice.MultipleChoiceSelectorAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.ArrayList;
import java.util.List;

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

    TextView mDescTV;

    FlowLayout mGroupLayout;

    private SendMsgProxy mProxy;

    private MessageProxy mMessageProxy;

    private GroupListProxy mGroupListProxy;

    private ContactProxy mContactProxy;

    private FragmentManager mFragmentManager;

    private List<ContactGroup> mGroupList;

    private List<Contacts> mContactses;

    private List<ContactGroup> mAllContactGroups;

    private List<Contacts> mAllContacts;

    private EmojiconsFragment mEmojiconsFragment;

    private boolean isMoreLayoutShow = false;

    private boolean isFromGroup = true;

    private int type;

    private MultipleChoiceActionSheet mChoiceActionSheet;

    private MultipleChoiceSelectorAdapter<ContactGroup> mChoiceSelectorAdapter;

    private MultipleChoiceSelectorAdapter<Contacts> mContactChoiceSelectorAdapter;

    private String mMsg;
    private Long mid;

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
                if (handleToDraft()) {
                } else {
                    SendMsgActivity.this.finish();
                }
            }
        });
        mGroupLayout = (FlowLayout) findViewById(R.id.sendmsg_flowtag_layout);
        mMsgET = (EmojiconEditText) findViewById(R.id.sendmsg_message_et);
        mMsgET.setOnClickListener(this);
        mMsgTV = (EmojiconTextView) findViewById(R.id.sendmsg_message_tv);
        mMsgTV.setOnClickListener(this);
        mDescTV = (TextView) findViewById(R.id.sendmsg_desc_tv);
        mMoreBox = (RadioGroup) findViewById(R.id.sendmsg_handle_box);
        mMoreBox.setOnCheckedChangeListener(this);
        mMoreLayout = (FrameLayout) findViewById(R.id.sendmsg_handle_more);
        mAddBT = (ImageView) findViewById(R.id.sendmsg_add_bt);
        mAddBT.setOnClickListener(this);
        mMoreBox.clearCheck();
        mMoreCloseBT = (RadioButton) findViewById(R.id.handle_close);
        mMoreCloseBT.setVisibility(View.INVISIBLE);
//        预加载下比较好
        mProxy = new SendMsgProxy(this, getCallbackHandler());
        mContactProxy = new ContactProxy(this, getCallbackHandler());
        mGroupListProxy = new GroupListProxy(this, getCallbackHandler());
        mMessageProxy = new MessageProxy(this, getCallbackHandler());
        type = getIntent().getIntExtra("type", 0);
        mGroupList = new ArrayList<>();
        mContactses = new ArrayList<>();
        mAllContactGroups = new ArrayList<>();
        mAllContacts = new ArrayList<>();
        initData();
    }

    private void initData() {
        switch (type) {
            //发送完成后自动添加组
            case FROM_MY_SEND_TYPE:
                mMsg="";
                mAddBT.setImageResource(R.drawable.add_contact);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgTV.setVisibility(View.GONE);
                mDescTV.setText(getResources().getText(R.string.sendmsg_contact));
                isFromGroup = false;
                break;
            case FROM_GROUP_TYPE:
                mMsg="";
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgTV.setVisibility(View.GONE);
                mGroupList.add(mGroupListProxy.getAContactGroup(getIntent().getLongExtra("gid", 0)));
                mDescTV.setText(getResources().getText(R.string.sendmsg_group));
                isFromGroup = true;
                break;
            case FROM_DRAFT:
                mid=getIntent().getLongExtra("mid",-1);
                mMsg=getIntent().getStringExtra("msg");
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.GONE);
                mMsgTV.setVisibility(View.VISIBLE);
                mMsgTV.setText(mMsg);
                mMsgET.setText(mMsg);
                mDescTV.setText(getResources().getText(R.string.sendmsg_group));
                isFromGroup = true;
                break;
            case FROM_HAVA_SEND:
                mid=getIntent().getLongExtra("mid",-1);
                mMsg=getIntent().getStringExtra("msg");
                mAddBT.setImageResource(R.drawable.add_group);
                mMsgET.setVisibility(View.GONE);
                mMsgTV.setVisibility(View.VISIBLE);
                mMsgTV.setText(mMsg);
                mMsgET.setText(mMsg);
                mMessageProxy.getMessageGroups(mid);
                mDescTV.setText(getResources().getText(R.string.sendmsg_group));
                isFromGroup = true;
                setOnBusy(true);
                break;
            default:
                break;

        }
        addTag();
        createActionSheet();
    }

    private void createActionSheet() {
        mChoiceActionSheet = new MultipleChoiceActionSheet<ContactGroup>(this);
        mChoiceActionSheet.builder();//必须放前边
        mChoiceActionSheet.setOnActionSheetListener(new MultipleChoiceActionSheet.OnActionSheetListener() {
            @Override
            public void onClickComplete(List<Integer> positions) {
                if (isFromGroup) {
                    for (Integer i : positions) {
                        if (!mGroupList.contains(mAllContactGroups.get(i))) {
                            mGroupList.add(mAllContactGroups.get(i));
                        }
                    }
                } else {
                    for (Integer i : positions) {
                        if (!mContactses.contains(mAllContacts.get(i))) {
                            mContactses.add(mAllContacts.get(i));
                        }
                    }
                }
                addTag();
                mChoiceActionSheet.dismiss();
            }

            @Override
            public void onItemClick(int position, boolean isChecked) {

            }
        });
        if (isFromGroup) {
            mChoiceSelectorAdapter = new MultipleChoiceSelectorAdapter<ContactGroup>(this) {
                @Override
                public String displayContent(int position) {
                    return mAllContactGroups.get(position).getName();
                }
            };
            mChoiceSelectorAdapter.setData((ArrayList<ContactGroup>) mAllContactGroups);
            mChoiceActionSheet.setAdapter(mChoiceSelectorAdapter);
            mChoiceActionSheet.setInfoText("选择分组");
        } else {
            mContactChoiceSelectorAdapter = new MultipleChoiceSelectorAdapter<Contacts>(this) {
                @Override
                public String displayContent(int position) {
                    return mAllContacts.get(position).getName();
                }
            };
            mContactChoiceSelectorAdapter.setData((ArrayList<Contacts>) mAllContacts);
            mChoiceActionSheet.setAdapter(mContactChoiceSelectorAdapter);
            mChoiceActionSheet.setInfoText("选择联系人");
        }
        mChoiceActionSheet.setChoiceMaxCount(10);//最大选择人数为10人

//        mChoiceActionSheet.setDisplayField(name) 利用反射做的

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
        switch (v.getId()) {
            case R.id.sendmsg_message_tv:
                mMsgTV.setVisibility(View.GONE);
                mMsgET.setVisibility(View.VISIBLE);
                mMsgET.setText(mMsgTV.getText());
                showSoftKeyboard(mMsgET);
                break;
            case R.id.sendmsg_message_et:
                showSoftKeyboard(mMsgET);
                break;
            case R.id.sendmsg_add_bt:
                if (isFromGroup) {
                    setOnBusy(true);
                    mGroupListProxy.getGroupList();
                } else {
                    setOnBusy(true);
                    mContactProxy.getALLContactList();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(ProxyEntity proxyEntity) {
        super.onResponse(proxyEntity);
        String action = proxyEntity.action;
        if (action.equals(GroupListProxy.GET_GROUP_LIST_SUCCESS)) {
            mAllContactGroups.clear();
            mAllContactGroups.addAll((List<ContactGroup>) proxyEntity.data);
            mChoiceSelectorAdapter.notifyDataSetChanged();
            mChoiceActionSheet.show();
            setOnBusy(false);
        } else if (action.equals(ContactProxy.GET_ALL_CONTACT_LIST_SUCCESS)) {
            mAllContacts.clear();
            mAllContacts.addAll((List<Contacts>) proxyEntity.data);
            mContactChoiceSelectorAdapter.notifyDataSetChanged();
            mChoiceActionSheet.show();
            setOnBusy(false);
        } else if (action.equals(MessageProxy.GET_MESSAGE_GROUP_SUCCESS)) {
            mGroupList.addAll((List<ContactGroup>) proxyEntity.data);
            addTag();
            setOnBusy(false);
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
     */
    private void displayMore(boolean isShow) {
        if (isShow && isShow != isMoreLayoutShow) {
            mMoreLayout.setVisibility(View.VISIBLE);
            isMoreLayoutShow = true;
            mMoreCloseBT.setVisibility(View.VISIBLE);
        } else if (!isShow || isShow != isMoreLayoutShow) {
            mMoreBox.clearCheck();
            mMoreLayout.setVisibility(View.GONE);
            mMoreCloseBT.setVisibility(View.INVISIBLE);
            isMoreLayoutShow = false;
        }
        return;
    }

    //    增加联系人或分组标签
    private void addTag() {
        mGroupLayout.removeAllViews();
        final ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 15;
        params.topMargin = 5;
        params.bottomMargin = 5;
        if (isFromGroup) {
            for (ContactGroup g : mGroupList) {
                final TextView textView = new TextView(this);
                textView.setText(g.getName());
                textView.setBackgroundResource(R.drawable.group_tag);
                textView.setTextColor(Color.WHITE);
                mGroupLayout.addView(textView, params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = mGroupLayout.indexOfChild(textView);
                        mGroupList.remove(position);
                        mGroupLayout.removeViewAt(position);
                    }
                });

            }
        } else {
            for (Contacts c : mContactses) {
                final TextView textView = new TextView(this);
                textView.setText(c.getName());
                textView.setBackgroundResource(R.drawable.contact_tag);
                textView.setTextColor(Color.WHITE);
                mGroupLayout.addView(textView, params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = mGroupLayout.indexOfChild(textView);
                        mContactses.remove(position);
                        mGroupLayout.removeViewAt(position);
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (handleToDraft()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean handleToDraft() {
        final String msg = mMsgET.getText().toString();
        if (msg.equals(mMsg)) {
            return false;
        } else {
            String dc="是否添加草稿";
            if(type==FROM_DRAFT){
                dc="是否保存到草稿";
            }else{
                dc="是否添加草稿";
            }
            AddDailog.showMsgWithCancleListener(this,dc, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(type==FROM_DRAFT){
                        mMessageProxy.updataDraftMsg(mid,msg);
                    }else {
                        mMessageProxy.insertDraftMsg(msg);
                    }
                    hideSoftKeyboard();
                    SendMsgActivity.this.finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hideSoftKeyboard();
                    SendMsgActivity.this.finish();
                }
            });
            return true;
        }
    }

}

