package com.wang.masssms.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by wangguang on 2016/4/18.
 */
public class HandleContactActivity extends BaseActivity {
    public static String ADD_TO_GROUP = "add_to_group";
    public static int ADD_RESQUEST_CODE = 1;
    public static String DELETE_FROM_GROUP = "delete_from_group";
    public static int DELETE_REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
