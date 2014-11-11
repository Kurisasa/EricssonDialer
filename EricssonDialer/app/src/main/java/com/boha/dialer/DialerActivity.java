package com.boha.dialer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.boha.dialer.fragments.DialerFragment;
import com.boha.dialer.fragments.PageFragment;
import com.boha.ericssen.library.util.BohaUtil;
import com.boha.ericssen.library.util.CallIntentService;
import com.boha.ericssen.library.util.CustomToast;

import java.util.ArrayList;
import java.util.List;


public class DialerActivity extends ActionBarActivity implements DialerFragment.DialerFragmentListener {

    PagerAdapter adapter;
    ViewPager mPager;
    PagerTitleStrip titleStrip;
    Context ctx;
    int currentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.activity_dialer);
        mPager = (ViewPager) findViewById(R.id.pager);
        titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setVisibility(View.GONE);

        buildPages();
    }

    private void buildPages() {

        pageFragmentList = new ArrayList<PageFragment>();
        dialerFragment = new DialerFragment();


        pageFragmentList.add(dialerFragment);
        initializeAdapter();

    }


    private void initializeAdapter() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageIndex = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialer, menu);
        BohaUtil.getCallDetails(getApplicationContext());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    DialerFragment dialerFragment;
    CustomToast toast;
    boolean isPay4MeCall;
    public static final String MTN_PREFIX = "127";
    EndCallListener endCallListener;
    TelephonyManager telephonyManager;

    @Override
    public void onResume() {
        Log.e(LOG, "############### onResume ");
        if (toast != null) {
            toast.getView().setVisibility(View.GONE);
            toast.cancel();
            Log.e(LOG, "$$$$$$ toast view setVisibility = GONE");
        }
        super.onResume();
    }

    TextView tv;
    static final int TOAST_DURATION = 60;
    public void openCustomToast(String number) {
        Log.w(LOG, "################# creating new toast and showing it.....");
        if (toast != null) {
            toast.cancel();
        }
        toast = new CustomToast(ctx);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(TOAST_DURATION);

        LayoutInflater inf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(com.boha.ericssen.library.R.layout.toast_calling, null);
        tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtNumber);
        tv.setText(number);
        toast.setView(v);
        if (type == DialerFragment.PAY4ME_CALL) {
            tv.setTextColor(ctx.getResources().getColor(R.color.orange));
        } else {
            tv.setTextColor(ctx.getResources().getColor(R.color.green));
        }
        toast.getView().setVisibility(View.VISIBLE);
        toast.show();
    }

    int type;
    String number;

    private void startCall(int type, String number) {
        endCallListener = new EndCallListener();

        if (telephonyManager == null) {
            telephonyManager =
                    (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(endCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        switch (type) {
            case DialerFragment.NORMAL_CALL:
                Log.w(LOG, "###### this is a NORMAL_CALL");
                break;
            case DialerFragment.PAY4ME_CALL:
                Log.w(LOG, "###### this is a PAY4ME_CALL");
                number = MTN_PREFIX + number;
                break;
        }

        Intent i = new Intent(ctx, CallIntentService.class);
        i.putExtra(CallIntentService.EXTRA_NUMBER, number);
        ctx.startService(i);
    }

    @Override
    public void onCallRequested(String number, int type) {
        Log.e(LOG, "############ onCallRequested");
        isCallActive = true;
        this.type = type;
        this.number = number;
        if (type == DialerFragment.PAY4ME_CALL) {
            isPay4MeCall = true;
        } else {
            isPay4MeCall = false;
        }
        startCall(type, number);

    }

    boolean isCallActive;

    private class EndCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i(LOG, "$$$$$$$ CALL_STATE_RINGING  - doin nutin: " + incomingNumber);

            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                Log.i(LOG, "************** CALL_STATE_OFFHOOK, call type: " + type);
                openCustomToast(number);

            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Log.i(LOG, "@@@@@@@@@@@@ CALL_STATE_IDLE - doin nutin");

            }

        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";


            return title;
        }
    }

    private List<PageFragment> pageFragmentList;
    static final String LOG = DialerActivity.class.getSimpleName();

}
