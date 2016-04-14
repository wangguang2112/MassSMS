package com.wang.masssms.proxy;

import java.io.Serializable;

/**
 * Created by 58 on 2016/2/29.
 *
 * @author wangguang
 *         代理回调实体
 */
public class ProxyEntity implements Serializable {
    //实体参数
    public String action = "";
    public int errorCode;
    public Object data;

    /**
     * 默认构造
     */

    public ProxyEntity() {
    }

    public ProxyEntity(String mAction) {
        this.action = mAction;
    }

    public ProxyEntity(String mAction, Object mData) {
        this.action = mAction;
        this.data = mData;
    }

    public ProxyEntity(String mAction, int mErrorCode) {
        this.action = mAction;
        this.errorCode = mErrorCode;
    }

    /**
     * 带参构造安徽省农户
     *
     * @param mAction    响应的action
     * @param mErrorCode 错误码
     * @param mData      response返回的数据
     */

    public ProxyEntity(String mAction, int mErrorCode, Object mData) {
        this.data = mData;
        this.errorCode = mErrorCode;
        this.action = mAction;
    }


}
