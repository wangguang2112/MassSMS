package com.wang.masssms.model.util;

import com.wang.masssms.model.orm.Contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangguang on 2016/4/25.
 */
public class PhoneContactUtil {
    public static String TAG="PhoneContactUtil";
    public static ArrayList<Contacts>  readContacts(Context context){
        ArrayList<Contacts> contacts=new ArrayList<>();
        ContentResolver cr = context.getContentResolver();

        // select * from contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while(cursor.moveToNext()){
           Contacts map=new Contacts();
            String id = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int iHasPhoneNum = Integer.parseInt(cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            map.setName(name);
            if(iHasPhoneNum > 0){
                Cursor numCursor = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null, null);
                while(numCursor.moveToNext()){
                    String number = numCursor.getString(
                            numCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    map.setPhonenumber(number);
                }
                numCursor.close();
            }
           contacts.add(map);
        }
        cursor.close();
        Log.d(TAG,"contact : "+contacts);
        return contacts;
    }
}
