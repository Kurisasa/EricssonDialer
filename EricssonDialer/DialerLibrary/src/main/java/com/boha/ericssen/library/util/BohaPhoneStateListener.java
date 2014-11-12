package com.boha.ericssen.library.util;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by aubreyM on 2014/11/12.
 */
public class BohaPhoneStateListener extends PhoneStateListener {
    SmallListener smallListener;
    int offHookCount, idleCount;
    String displayNumber;
    int type;
    Context ctx;

    public BohaPhoneStateListener(Context ctx, String displayNumber, int type, SmallListener smallListener) {
        this.smallListener = smallListener;
        this.type = type;
        this.displayNumber = displayNumber;
        this.ctx = ctx;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if (TelephonyManager.CALL_STATE_RINGING == state) {
            Log.i(LOG, "$$$$$$$ CALL_STATE_RINGING  - doin nutin: " + incomingNumber);

        }
        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
            offHookCount++;
            Log.d(LOG, "***** CALL_STATE_OFFHOOK,  offHookCount: " + offHookCount);
            smallListener.onOffTheHook(offHookCount);
        }
        if (TelephonyManager.CALL_STATE_IDLE == state) {
            idleCount++;
            Log.d(LOG, "###### CALL_STATE_IDLE, idleCount: " + idleCount);

            if (idleCount > 1) {
                smallListener.onIdle(idleCount);

            } else {
                Log.d(LOG, "--> toast is NOT open on CALL_STATE_IDLE, must be just coming in");
            }


        }

    }


    static final String LOG = BohaPhoneStateListener.class.getSimpleName();
}
