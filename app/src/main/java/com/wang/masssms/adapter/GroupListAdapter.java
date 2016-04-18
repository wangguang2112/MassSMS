package com.wang.masssms.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.masssms.R;
import com.wang.masssms.model.orm.ContactGroup;
import com.wang.masssms.model.orm.Contacts;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wangguang on 2016/4/5.
 */
public class GroupListAdapter extends BaseAdapter {
    private ArrayList<ContactGroup> data;
    private ArrayList<String> contacts;
    private Context mContext;
    private LayoutInflater mLayoutInflate;
    private OnAddButtonClickListener mAddListener;
    private OnDeleteButtonClickListener mDeleteListener;

    public GroupListAdapter(Context context, ArrayList<ContactGroup> groups, ArrayList<String> contacts) {
        mContext = context;
        mLayoutInflate = LayoutInflater.from(context);
        data = groups;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            Log.d("Adapter data",data.size()+"");
            Log.d("Adapter contact",contacts.size()+"");
            convertView = mLayoutInflate.inflate(R.layout.group_item_layout, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.group_item_name);
            holder.tv.setText(data.get(position).getName());
            holder.mtv = (TextView) convertView.findViewById(R.id.group_contact_names);
            holder.mtv.setText(contacts.get(position)==null?"":contacts.get(position));
            convertView.setTag(holder);
            final ImageView openView = (ImageView) convertView.findViewById(R.id.more_item_open_bt);
            final LinearLayout moreItemLayout = (LinearLayout) convertView.findViewById(R.id.more_item_layout);
            moreItemLayout.setVisibility(View.GONE);
            openView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (moreItemLayout.getVisibility() == View.GONE) {
                        moreItemLayout.setVisibility(View.VISIBLE);
                        createAndShowRotationAnimator(v, true);
                    } else {
                        moreItemLayout.setVisibility(View.GONE);
                        createAndShowRotationAnimator(v, false);
                    }
                    //显示旋转动画
                }
            });
            holder.mAddtv = (TextView) convertView.findViewById(R.id.add_contact_group);
            holder.mDeletetv = (TextView) convertView.findViewById(R.id.delete_contact_group);

            holder.mAddtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddListener != null) {
                        mAddListener.onItemClick(position, data.get(position));
                    }
                }
            });


            holder.mDeletetv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteListener != null) {
                        mDeleteListener.onItemClick(position, data.get(position));
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.tv.setText(data.get(position).getName());
            holder.mtv.setText("联系人:"+contacts.get(position));
            holder.mAddtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddListener != null) {
                        mAddListener.onItemClick(position, data.get(position));
                    }
                }
            });


            holder.mDeletetv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteListener != null) {
                        mDeleteListener.onItemClick(position, data.get(position));
                    }
                }
            });

        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        TextView mtv;
        TextView mAddtv;
        TextView mDeletetv;
    }

    private void createAndShowRotationAnimator(final View v,boolean isVisable) {
        ValueAnimator rotaBT =isVisable?ValueAnimator.ofFloat(0, 180):ValueAnimator.ofFloat(180,360);
        rotaBT.setDuration(360);
        rotaBT.setTarget(v);
        rotaBT.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float i = (Float) animation.getAnimatedValue();
                v.setRotation(i);
            }
        });
        rotaBT.start();
    }

    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.mAddListener = listener;
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.mDeleteListener = listener;
    }

    public interface OnAddButtonClickListener {
        public void onItemClick(int position, ContactGroup item);
    }

    public interface OnDeleteButtonClickListener {
        public void onItemClick(int position, ContactGroup item);
    }
}
