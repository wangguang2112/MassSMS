package com.wang.masssms.uiview.multiplechoice;


import com.wang.masssms.uiview.util.ReflectionUtils;

import android.content.Context;

/**
 * 帮帮项目组 Bangbang
 *
 * @Author zhaobo
 * @Date 2015/5/23 14:30
 * @Copyright:58.com Inc. All rights reserved.
 */
public class MultipleChoiceReflectionAdapter extends MultipleChoiceSelectorAdapter {

    private String mDisplayField;

    public MultipleChoiceReflectionAdapter(Context ctx) {
        super(ctx);
    }

    public MultipleChoiceReflectionAdapter(Context ctx, String display) {
        this(ctx);
        mDisplayField = display;
    }

    @Override
    public String displayContent(int position) {
        Object item = mData.get(position);
        return getContent(item);
    }

    private String getContent(Object item) {
        Object value = null;
        try {
            value = ReflectionUtils.getFiled(item, mDisplayField);
        } catch (Exception e) {

        }
        if (value != null) {
            return value.toString();
        }
        return null;
    }
}