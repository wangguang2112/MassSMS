package com.wang.masssms.model.notify;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wangguang.
 * Date:2016/5/24
 * Description:观察者模式
 */
public class Notify extends Observable {
    public static Notify mNotify;
    public static Notify getInstence(){
        if(mNotify==null){
            mNotify=new Notify();
        }
        return mNotify;
    }
    public void registerNotify(Observer observer){
        addObserver(observer);

        Log.d("Notify",observer+" register");
    }
    public void unRegisterNotify(Observer observer){
        deleteObserver(observer);
        Log.d("Notify", observer + " unregister");
    }
}
