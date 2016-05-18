package com.wang.masssms.model;

import java.io.Serializable;

/**
 * Created by wangguang.
 * Date:2016/5/18
 * Description:
 */
public class WordData{
    private String text;
    private String description;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
