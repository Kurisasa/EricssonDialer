package com.boha.ericssen.library.util;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

public class CustomToast extends Toast {
    int mDuration;
    boolean mShowing = false;
    public CustomToast(Context context) {
    	super(context);
    }


    /**
     * Set the time to show the toast for (in seconds) 
     * @param seconds Seconds to display the toast
     */

    public void setTimeToShow(int seconds) {
        mDuration = seconds;
        setDuration(LENGTH_LONG);
    }
    @Override
    public void show() {
        super.show();
        final Toast thisToast = this;
        if(mShowing) {
            //System.out.println("-------- Toast still up. mShowing: " + mShowing);
        	return;
        } else {
            Log.i(LOG, "***** mShowing is FALSE...");
        }

        mShowing = true;
        if (countDownTimer == null) {
            Log.e(LOG,"+++++++ creating new countDownTimer");
            countDownTimer = new CountDownTimer(mDuration * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    thisToast.show();
                }
                @Override
                public void onFinish() {
                    mShowing = false;
                    countDownTimer.cancel();
                    Log.d(LOG, "------ countDownTimer cancelled");
                }

            }.start();
        }
    }
    CountDownTimer countDownTimer;
    static final String LOG = CustomToast.class.getSimpleName();
    public void forceShow() {
        mShowing = false;
        show();
    }
    @Override
    public void cancel() {
        mShowing = false;
        countDownTimer.cancel();
        Log.w(LOG,"-------- Toast cancelling from within, mShowing: " + mShowing);
        super.cancel();
    }
}