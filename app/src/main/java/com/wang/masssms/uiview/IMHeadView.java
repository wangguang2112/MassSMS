package com.wang.masssms.uiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.masssms.R;


/**
 * Created by 58 on 2016/3/8.
 */
public class IMHeadView extends FrameLayout implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnReturnButtonClickListener mReturnButtonClickListener;

    private LinearLayout mHeadBar1LL;
    private LinearLayout mHeadBar2LL;
    private LinearLayout mHeadBar3LL;
    private TextView mTitleView;
    private ImageButton mReturnIB;
    private ImageButton mRightIB;
    private OnReturnButtonClickListener mOnReturnButtonClickListener;
    private OnRightButtonClickListener mRightOnClickListener;

    public IMHeadView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public IMHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public IMHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.ui_view_headview, this);
        // mLayoutInflater.from(context).inflate();
        mHeadBar1LL = (LinearLayout) this.findViewById(R.id.headbar1);
        mHeadBar2LL = (LinearLayout) this.findViewById(R.id.headbar2);
        mHeadBar3LL = (LinearLayout) this.findViewById(R.id.headbar3);
        mReturnIB = (ImageButton) this.findViewById(R.id.head_bar_left_button);
        if (mHeadBar1LL.getVisibility() == View.VISIBLE) {
            mReturnIB.setOnClickListener(this);
        }
        mReturnIB.setVisibility(View.GONE);
        mTitleView = (TextView) findViewById(R.id.head_bar1_title);
        mRightIB = (ImageButton) findViewById(R.id.head_bar_right_button);
        initAttr(attrs);
    }

    // 初始化属性
    private void initAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.headbar);
        mTitleView.setTextSize(typedArray.getDimension(R.styleable.headbar_titlesize, 18));
        if (typedArray
                .getString(R.styleable.headbar_headtitle) != null)
            mTitleView.setText(typedArray
                    .getString(R.styleable.headbar_headtitle));
        if (typedArray.getBoolean(R.styleable.headbar_rightvisiable, false)) {
            mRightIB.setVisibility(View.VISIBLE);
            mRightIB.setImageResource(typedArray.getResourceId(R.styleable.headbar_rightsrc, -1));
        } else {
            mRightIB.setVisibility(View.GONE);
        }
        if (typedArray.getBoolean(R.styleable.headbar_returnvisiable, false)) {
            mReturnIB.setVisibility(View.VISIBLE);
        } else {
            mReturnIB.setVisibility(View.GONE);
        }


        typedArray.recycle();
    }

    /**
     * 设置返回按钮监听器
     *
     * @param onReturnButtonClickListener
     */
    public void setOnReturnButtonClickListener(OnReturnButtonClickListener onReturnButtonClickListener) {
        this.mReturnButtonClickListener = onReturnButtonClickListener;
        mReturnIB.setVisibility(View.VISIBLE);
    }
    public void setOnRightButtonClickListener(OnRightButtonClickListener onRightOnClickListener) {
        this.mRightOnClickListener = onRightOnClickListener;
        mReturnIB.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_bar_left_button:
                if (mReturnButtonClickListener != null) {
                    mReturnButtonClickListener.onReturnClick(v);
                }
                break; case R.id.head_bar_right_button:
                if (mRightOnClickListener != null) {
                    mRightOnClickListener.onRightClick(v);
                }
                break;
            default:
                break;
        }
    }
    public interface OnRightButtonClickListener {
        public void onRightClick(View view);
    }
    public interface OnReturnButtonClickListener {
        public void onReturnClick(View view);
    }
}
