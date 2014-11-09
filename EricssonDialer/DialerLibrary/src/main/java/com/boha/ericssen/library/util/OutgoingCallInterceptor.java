package com.boha.ericssen.library.util;

/**
 * Created by aubreyM on 2014/11/08.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutgoingCallInterceptor extends BroadcastReceiver {                            // 1

    @Override
    public void onReceive(Context context, Intent intent) {                                 // 2
        final String oldNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);          // 3
        this.setResultData(oldNumber);                                                   // 4
        final String newNumber = this.getResultData();
        String msg = "*** Intercepted outgoing call. Old number " + oldNumber + ", new number " + newNumber;
        Log.e("OutgoingCallInterceptor",msg);

        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
