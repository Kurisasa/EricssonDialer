package com.boha.dialer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.boha.dialer.fragments.DialerFragment;
import com.boha.dialer.fragments.PageFragment;
import com.boha.ericssen.library.util.BohaUtil;
import com.boha.ericssen.library.util.CustomToast;

import java.util.ArrayList;
import java.util.List;


public class DialerActivity extends ActionBarActivity implements DialerFragment.DialerFragmentListener{

    PagerAdapter adapter;
    ViewPager mPager;
    PagerTitleStrip titleStrip;
    int currentPageIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        mPager = (ViewPager)findViewById(R.id.pager);
        titleStrip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
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
    @Override
    public void onToastRequested(String number) {
        toast = new CustomToast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(60);
        LayoutInflater inf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(com.boha.ericssen.library.R.layout.toast_calling,null);
        TextView tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtNumber);
        if (isPay4MeCall) {
            tv.setTextColor(getApplicationContext().getResources().getColor(R.color.absa_red));
        } else {
            tv.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
        }
        tv.setText(number);
        toast.setView(v);
        toast.show();
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
}
