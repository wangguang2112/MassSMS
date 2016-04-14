package com.wang.masssms;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.wang.masssms.model.orm.DaoMaster;
import com.wang.masssms.model.orm.DaoSession;


/**
 * Created by 58 on 2016/3/8.
 */
public class App  extends Application{
    private static App instance;
    public static String DATABASE_NAME="masssms_info";
    public App() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //bugly初始化
        CrashReport.initCrashReport(getApplicationContext(), "900022358", false);
    }

    public static App getApp() {
        return instance;
    }
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context)
    {
        if (daoMaster == null)
        {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context)
    {
        if (daoSession == null)
        {
            if (daoMaster == null)
            {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
