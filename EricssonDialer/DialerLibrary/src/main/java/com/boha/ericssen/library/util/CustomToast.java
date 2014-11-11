package com.boha.ericssen.library.util;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

public class CustomToast extends Toast {
    int mDuration;
    boolean mShowing = false;
    public CustomToast(Context context) {
    	super(context);
    }
    public CustomToast(Context context, int time) {
        super(context);
        mDuration = time;
    }

    /**
     * Set the time to show the toast for (in seconds) 
     * @param seconds Seconds to display the toast
     */
    @Override
    public void setDuration(int seconds) {
        if(seconds < 2) mDuration = LENGTH_LONG; //Minimum
        mDuration = seconds;
        super.setDuration(mDuration);
    }

    /**
     * Show the toast for the given time 
     */
    @Override
    public void show() {
        super.show();
        if(mShowing) {
        	return;
        }

        mShowing = true;
        final Toast thisToast = this;
        new CountDownTimer((mDuration-2)*1000, 1000)
        {
            public void onTick(long millisUntilFinished) {thisToast.show();}
            public void onFinish() {thisToast.show(); mShowing = false;}

        }.start();  
    }
}