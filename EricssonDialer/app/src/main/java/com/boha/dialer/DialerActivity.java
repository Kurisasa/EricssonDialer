package com.boha.dialer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    boolean isPay4MeCall, isFirstService;
    public static final String MTN_PREFIX = "127";
    CustomToast toast;

    @Override
    public void onResume() {
        Log.i(LOG, "############### onResume");
        if (toast != null) {
            toast.getView().setVisibility(View.GONE);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            toast.cancel();
            toast = null;
            Log.e(LOG, "############### onResume - toast cancelled");
        }

        super.onResume();
    }

    TextView tv;
    int type;
    String number, displayNumber;
    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCallRequested(String number, int type) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG,"##### onReceive: " + intent.toString());
                if (toast != null) {
                    try {
                        Log.e(LOG, "...zzzzzzzz ##### onReceive:  -  sleep 5 seconds, then kill toast");
                        View v = toast.getView();
                        TextView tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtTile);
                        tv.setText("Closing call ...");
                        toast.show();
                        Thread.sleep(5000);
                        toast.cancel();
                        toast = null;
                        Log.e(LOG,"##### onReceive: Toast cancelled");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        },new IntentFilter(CallIntentService.ACTION_IDLE));

        Intent i = new Intent(ctx, CallIntentService.class);
        i.putExtra(CallIntentService.EXTRA_DISPLAY_NUMBER, number);
        this.type = type;
        this.number = number;
        this.displayNumber = number;
        if (type == CallIntentService.PAY4ME_CALL) {
            isPay4MeCall = true;
        } else {
            isPay4MeCall = false;
        }
        switch (type) {
            case CallIntentService.NORMAL_CALL:
                Log.d(LOG, "###### onCallRequested, this is a NORMAL_CALL");
                break;
            case CallIntentService.PAY4ME_CALL:
                Log.e(LOG, "###### onCallRequested, this is a PAY4ME_CALL");
                number = MTN_PREFIX + number;
                break;
        }


        i.putExtra(CallIntentService.EXTRA_NUMBER, number);
        i.putExtra(CallIntentService.EXTRA_TYPE, type);
        startService(i);
    }

    @Override
    public void onPause() {
        Log.w(LOG, "########################### onPause");
        if (!isBackPressed)
            openCustomToast();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.e(LOG, "########################### onBackPressed");
        isBackPressed = true;
        if (toast != null) {
            toast.getView().setVisibility(View.GONE);
            toast.cancel();
        }
        finish();
    }

    boolean isBackPressed;

    private void openCustomToast() {
        Log.w(LOG, "################# creating new toast and showing it.....");
        toast = new CustomToast(this);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setTimeToShow(300);

        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(com.boha.ericssen.library.R.layout.toast_calling, null);
        TextView tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtNumber);
        tv.setText(displayNumber);
        toast.setView(v);
        if (type == CallIntentService.PAY4ME_CALL) {
            tv.setTextColor(ctx.getResources().getColor(com.boha.ericssen.library.R.color.orange));
        } else {
            tv.setTextColor(ctx.getResources().getColor(com.boha.ericssen.library.R.color.green));
        }
        toast.getView().setVisibility(View.VISIBLE);
        toast.forceShow();
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
