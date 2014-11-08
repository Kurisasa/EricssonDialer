package com.boha.ericssen.library.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aubreyM on 2014/11/08.
 */
public class BohaUtil {

    public static List<BohaCallLog> getCallDetails(Context ctx) {

        List<BohaCallLog> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

        ContentResolver cr = ctx.getContentResolver();
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor managedCursor = cr.query(callUri, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int cachedName = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int newCall = managedCursor.getColumnIndex(CallLog.Calls.NEW);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            BohaCallLog log = new BohaCallLog();
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String name = managedCursor.getString(cachedName);
            String stringNew = managedCursor.getString(newCall);

            log.setDate(callDayTime);
            log.setDuration(Long.parseLong(callDuration));
            log.setName(name);
            log.setNumber(phNumber);
            log.setType(Integer.parseInt(callType));
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    log.setStringType(dir);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    log.setStringType(dir);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    log.setStringType(dir);
                    break;
            }
            list.add(log);
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
            Log.i("BohaUtil",sb.toString());
        }
        managedCursor.close();
        return list;

    }
}
