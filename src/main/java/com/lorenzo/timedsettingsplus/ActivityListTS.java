package com.lorenzo.timedsettingsplus;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.lorenzo.timedsettingsplus.fragments.Friday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Monday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Saturday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Sunday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Thursday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Tuesday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Wednesday_fragment;

import java.util.Calendar;

public class ActivityListTS extends AppCompatActivity {

    private TSDBHelper dbHelper;
    private Context mContext;
    private boolean write_settings;
    private ViewPager mViewPager;
    private PagerTabStrip pagerTabStrip;
    private FragPagerAdapter pagerAdapter;
    private String page_day="";
    private int currDay=0;
    private Calendar today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ts);


        getSupportActionBar().setElevation(0);

        mContext=this;
        dbHelper = TSDBHelper.getInstance(mContext);

        today=Calendar.getInstance();
        switch(today.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY :
                 page_day="Sun"; break;
            case Calendar.MONDAY :
                 page_day="Mon"; currDay=1; break;
            case Calendar.TUESDAY :
                 page_day="Tue"; currDay=2; break;
            case Calendar.WEDNESDAY :
                 page_day="Wed"; currDay=3; break;
            case Calendar.THURSDAY :
                 page_day="Thu"; currDay=4; break;
            case Calendar.FRIDAY :
                 page_day="Fri"; currDay=5; break;
            case Calendar.SATURDAY :
                 page_day="Sat"; currDay=6; break;
        }

        mViewPager = (ViewPager)findViewById(R.id.pager);
        pagerTabStrip=(PagerTabStrip)findViewById(R.id.pager_header);

        pagerAdapter = new FragPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        mViewPager.setAdapter(pagerAdapter);


        pagerTabStrip.setTabIndicatorColor(0xffffff);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        mViewPager.setCurrentItem(currDay);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 : page_day = "Sun" ;
                        break;
                    case 1 : page_day = "Mon" ;
                        break;
                    case 2 : page_day = "Tue" ;
                        break;
                    case 3 : page_day = "Wed" ;
                        break;
                    case 4 : page_day = "Thu" ;
                        break;
                    case 5 : page_day = "Fri" ;
                        break;
                    case 6 : page_day = "Sat" ;
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setRippleColor(553333);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityDetailsTS(page_day);
            }
        });







        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            write_settings = Settings.System.canWrite(getApplicationContext());
            if (!write_settings) {
                startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
                Toast t=Toast.makeText(this,getResources().getText(R.string.enab_sett), Toast.LENGTH_LONG);
                t.show();
            }
        }


    }

    @Override
    protected void onResume(){
        super.onResume();

    }


    public void setTSEnabled(long id, boolean isEnabled) {
        TSManagerHelper.cancelTSs(this);
        TSModel model = dbHelper.getTS(id);
        Log.e("setTSEnalbled","checkbox di:"+model.name);
        model.isEnabled = isEnabled;
        dbHelper.updateTS(model);
        TSManagerHelper.setTSs(this);
        notifyToList(mViewPager.getCurrentItem());
        if(model.moreThanOneDay()) {
            if (mViewPager.getCurrentItem() != 0)
                notifyToList(mViewPager.getCurrentItem() - 1);
            if (mViewPager.getCurrentItem() != 6)
                notifyToList(mViewPager.getCurrentItem() + 1);
        }
    }


    public void startActivityDetailsTS(String day) {
        Intent intent = new Intent(this, ActivityDetailsTS.class);
        intent.putExtra("day", day);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            notifyToList(mViewPager.getCurrentItem());
            if (data.getBooleanExtra("moreDays", true)) {
                if (mViewPager.getCurrentItem() != 0)
                    notifyToList(mViewPager.getCurrentItem() - 1);
                if (mViewPager.getCurrentItem() != 6)
                    notifyToList(mViewPager.getCurrentItem() + 1);
            }
        }

    }


    public void deleteTS(long id) {
        final long TSId = id;
        final TSModel model= dbHelper.getTS(TSId);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(getResources().getString(R.string.del_conf))
                .setTitle(getResources().getString(R.string.del_q))
                .setCancelable(true)
                .setNegativeButton(getResources().getString(R.string.canc), null)
                .setPositiveButton(getResources().getString(R.string.conf), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!model.moreThanOneDay()) {
                            //Cancel Alarms
                            TSManagerHelper.cancelTSs(mContext);
                            //Delete alarm from DB by id
                            dbHelper.deleteTS(TSId);

                            notifyToList(mViewPager.getCurrentItem());

                            pagerAdapter.notifyDataSetChanged();

                            TSManagerHelper.setTSs(mContext);
                        }
                        else {

                            TSManagerHelper.cancelTSs(mContext);

                            model.setRepeatingDay(mViewPager.getCurrentItem(), false);

                            dbHelper.updateTS(model);

                            if(mViewPager.getCurrentItem()!=0)
                             notifyToList(mViewPager.getCurrentItem()-1);
                            notifyToList(mViewPager.getCurrentItem());
                            if(mViewPager.getCurrentItem()!=6)
                             notifyToList(mViewPager.getCurrentItem()+1);

                            pagerAdapter.notifyDataSetChanged();

                            TSManagerHelper.setTSs(mContext);
                        }

                    }
                }).show();
    }


    public void notifyToList(int day){

        switch (day){
            case 0 :
                Sunday_fragment.mAdapter.setTSs(dbHelper.getTSs(0));
                Sunday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 1 :
                Monday_fragment.mAdapter.setTSs(dbHelper.getTSs(1));
                Monday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 2 :
                Tuesday_fragment.mAdapter.setTSs(dbHelper.getTSs(2));
                Tuesday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 3 :
                Wednesday_fragment.mAdapter.setTSs(dbHelper.getTSs(3));
                Wednesday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 4 :
                Thursday_fragment.mAdapter.setTSs(dbHelper.getTSs(4));
                Thursday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 5 :
                Friday_fragment.mAdapter.setTSs(dbHelper.getTSs(5));
                Friday_fragment.mAdapter.notifyDataSetChanged();
                break;
            case 6 :
                Saturday_fragment.mAdapter.setTSs(dbHelper.getTSs(6));
                Saturday_fragment.mAdapter.notifyDataSetChanged();
                break;
        }
    }


}
