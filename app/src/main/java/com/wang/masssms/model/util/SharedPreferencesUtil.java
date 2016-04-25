package com.wang.masssms.model.util;

import com.wang.masssms.App;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

/**
 * Created by wangguang on 2016/4/25.
 */
public class SharedPreferencesUtil {
    private static final String sharedPreferencesInfo = "masssms.shareInfo";
    private static final String SHAREDPREFERENCES_HEADER = "masssms.header";

    public static final String AUTO_IMPORT_FROM_PHONE_FLAG="auto_import_from_phone_flag";
    private static Context myContext;
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedInstance = new SharedPreferencesUtil();

    private SharedPreferencesUtil() {

    }

    /**
     * 单例模式获得对象实例
     *
     * @param context
     * @return
     */
    public static SharedPreferencesUtil getInstance(Context context) {
        myContext = App.getApp();
        if (mPreferences == null && myContext != null) {
            mPreferences = myContext.getSharedPreferences(
                    sharedPreferencesInfo, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        }
        return mSharedInstance;
    }

    public static SharedPreferencesUtil getInstance() {
        return getInstance(null);
    }

    /**
     * 是否有键
     *
     * @param key
     * @return
     */
    public boolean isContainKey(String key) {
        return mPreferences.contains(key);
    }

    /**
     * 删除指定键的值item
     *
     * @param key
     * @return
     */
    public boolean clearItem(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }

    /**
     * 获得所有保存对象
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, ?> getAll() {
        if(mPreferences.getAll() instanceof  HashMap) {
            return (HashMap<String, ?>) mPreferences.getAll();
        }
        return null;
    }

    /**
     * 给指定键设置String值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setString(String key, String value) {
        if (mPreferences.contains(key)) {
            mEditor.remove(key);
        }
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    /**
     * 获得指定键的String类型值
     *
     * @param key
     *            键
     * @return
     */
    public String getString(String key) {
        return mPreferences.getString(key, "");
    }

    /**
     * 获得指定键的String类型值，带有默认值的
     *
     * @param key
     *            键
     * @param defValue
     *            默认值
     * @return
     */
    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    /**
     * 给指定键设置int值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setInt(String key, int value) {
        if (mPreferences.contains(key)) {
            mEditor.remove(key);
        }
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    /**
     * 获得int类型数据
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    /**
     * 获得int类型数据，带有默认值的
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    /**
     * 设置float类型数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setFloat(String key, float value) {
        if (mPreferences.contains(key)) {
            mEditor.remove(key);
        }
        mEditor.putFloat(key, value);
        return mEditor.commit();
    }

    /**
     * 获得float类型数据
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        return mPreferences.getFloat(key, 0);
    }

    /**
     * 获得float类型数据，带有默认值的
     *
     * @param key
     * @param defValue
     * @return
     */
    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    /**
     * 设置long类型数据，带有默认值的
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setBoolean(String key, boolean value) {
        if (mPreferences.contains(key)) {
            mEditor.remove(key);
        }
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }

    /**
     * 获得boolean类型数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    /**
     * 设置long类型数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setLong(String key, long value) {
        if (mPreferences.contains(key)) {
            mEditor.remove(key);
        }
        mEditor.putLong(key, value);
        return mEditor.commit();
    }

    /**
     * 获得long类型数据
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        return mPreferences.getLong(key, 0);
    }

    /**
     * 获得long类型数据，带有默认值的
     *
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    /**
     * 存储对象类型到share preferences
     * @param key
     * @param object  待存储对象
     */

    private static final String FILE_NAME = "peipei_file_name";

    public static void setObject(String key, Object object) {
        SharedPreferences sp = App.getApp().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (Exception e) {
            Log.e("SharePrefence", e.toString());
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.e("SharePrefence", e.toString());
            }
        }
    }
    /**
     * 获取share preferences中的对象
     * @param key
     */
    public static <T> T getObject(String key) {
        SharedPreferences sp = App.getApp().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (Exception e) {

                }
            }
        }
        return null;
    }


    /**
     * 程序第一次运行之后设置
     *
     * @param context
     *            上下文
     * @param uid
     *            uid
     * @author 黄宏宇
     */
    public static void setAppIsFirstInit(Context context, long uid) {
        SharedPreferences preferences = context.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("FIRST_INIT" + uid, false);
        editor.commit();
    }

    /**
     * 程序是否第一次运行
     *
     * @param context
     *            上下文
     * @param uid
     *            uid
     * @return boolean
     * @author 黄宏宇
     */
    public static boolean appIsFirstInit(Context context, long uid) {
        SharedPreferences preferences = context.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("FIRST_INIT" + uid, true);
    }

    /**
     * 单例模式获得记录加载头像的对象实例
     *
     * @param context
     * @return
     */
    public static SharedPreferencesUtil getHeaderInstance(Context context) {
        myContext = App.getApp();
        if (mPreferences == null && myContext != null) {
            mPreferences = myContext.getSharedPreferences(
                    SHAREDPREFERENCES_HEADER, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        }
        return mSharedInstance;
    }


    /**
     * 清除xml中的全部数据
     */
    public void clear() {
        mEditor.clear();
    }
}

