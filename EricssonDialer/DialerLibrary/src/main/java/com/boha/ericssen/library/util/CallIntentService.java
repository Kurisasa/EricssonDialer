package com.boha.ericssen.library.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class CallIntentService extends IntentService {

    public static final String EXTRA_NUMBER = "util.extra.NUMBER",
            EXTRA_TYPE = "util.extra.EXTRA",
            EXTRA_DISPLAY_NUMBER = "util.extra.DISPLAY_NUMBER",
            LOG = CallIntentService.class.getSimpleName(), ACTION_IDLE = "boha.com.IDLE";
    public static final int
            PAY4ME_CALL = 2, NORMAL_CALL = 1;
    BohaPhoneStateListener bohaPhoneStateListener;

    public CallIntentService() {
        super("CallIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(LOG, "******** ############ onHandleIntent for Service");

        final TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        final CustomToast toast = new CustomToast(this);

        String number = intent.getStringExtra(EXTRA_NUMBER);
        final int type = intent.getIntExtra(EXTRA_TYPE, NORMAL_CALL);
        final String displayNumber = intent.getStringExtra(EXTRA_DISPLAY_NUMBER);

        bohaPhoneStateListener = new BohaPhoneStateListener(getApplicationContext(), displayNumber, type, new SmallListener() {
            @Override
            public void onOffTheHook(int count) {
            }

            @Override
            public void onIdle(int count) {
                if (count > 1) {
                    telephonyManager.listen(bohaPhoneStateListener, PhoneStateListener.LISTEN_NONE);
                    Log.i(LOG, "## onIdle - CLEANED UP! PhoneStateListener.LISTEN_NONE, count: " + count);
                    Intent intent = new Intent(ACTION_IDLE);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
            }

            @Override
            public void onRinging(int count) {

            }
        });
        telephonyManager.listen(bohaPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        String uri = "tel:" + number;
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log.w(LOG, "###### starting phone activity with callIntent: " + uri);
        startActivity(callIntent);
        Log.w(LOG, "====================> Leaving the building, this thread be gone!....");
    }

}
