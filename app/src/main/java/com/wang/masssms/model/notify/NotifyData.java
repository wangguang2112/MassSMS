package com.wang.masssms.model.notify;

/**
 * Created by wangguang.
 * Date:2016/5/24
 * Description:
 */
public class NotifyData {

    String Action;

    Object data;

    public NotifyData(String action, Object data) {
        Action = action;
        this.data = data;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
