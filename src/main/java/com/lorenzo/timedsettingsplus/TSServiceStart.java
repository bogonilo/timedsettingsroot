package com.lorenzo.timedsettingsplus;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

public class TSServiceStart extends Service {

    public TSServiceStart() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String op1;
        boolean op2, op3, last, weekly, op5, op7, op8, op9, op10;
        int lastDay, op4, op6;
        long id;
        TSDBHelper tsdbHelper= TSDBHelper.getInstance(this);
        TSModel model;

        AudioManager audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        WifiManager wifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        Calendar calendar=Calendar.getInstance();
        ContentResolver contentResolver=getContentResolver();

        Bundle bundle;



        if(intent!=null) {
            bundle = intent.getExtras();
            op1 = bundle.getString(TSManagerHelper.OPTION1);
            op2 = bundle.getBoolean(TSManagerHelper.OPTION2);
            op3 = bundle.getBoolean(TSManagerHelper.OPTION3);
            lastDay = bundle.getInt(TSManagerHelper.LAST_DAY);
            id = bundle.getLong(TSManagerHelper.ID);
            weekly = bundle.getBoolean(TSManagerHelper.REPEAT_WEEKLY);
            op4= bundle.getInt(TSManagerHelper.OPTION4);
            op5= bundle.getBoolean(TSManagerHelper.OPTION5);
            op6= bundle.getInt(TSManagerHelper.OPTION6);
            op7= bundle.getBoolean(TSManagerHelper.OPTION7);
            op8= bundle.getBoolean(TSManagerHelper.OPTION8);
            op9= bundle.getBoolean(TSManagerHelper.OPTION9);
            op10= bundle.getBoolean(TSManagerHelper.OPTION10);

            Log.e("TSServiceStart","LastDay è "+lastDay+" calendar è "+calendar.get(Calendar.DAY_OF_WEEK));

            //AIRPLANE-MODE
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                try {
                    if(Settings.Global.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON)==1 != op10) {
                        if (op10) {
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put global airplane_mode_on 1");
                            Thread.sleep(500);
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true");
                            Thread.sleep(1500);
                        } else {
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put global airplane_mode_on 0");
                            Thread.sleep(500);
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false");
                            Thread.sleep(1500);
                        }
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    if(Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON)==1 != op10) {
                        if (op10) {
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put system airplane_mode_on 1");
                            Thread.sleep(500);
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true");
                            Thread.sleep(1500);
                        } else {
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put system airplane_mode_on 0");
                            Thread.sleep(500);
                            Shell.executeCommandViaSu(getApplicationContext(), "-c", "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false");
                            Thread.sleep(1500);
                        }

                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if(lastDay==calendar.get(Calendar.DAY_OF_WEEK))
                last=true;
            else
                last=false;

            //RINGER-MODE
            if (op1.equals(getResources().getString(R.string.id0)))
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            else if (op1.equals(getResources().getString(R.string.id1)))
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            else
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            //WI-FI
            if(!op10){
                if(op2)
                    wifiManager.setWifiEnabled(true);
                else
                    wifiManager.setWifiEnabled(false);
            }



            //BLUETOOTH
            if(bluetoothAdapter!=null && !op10){
                    if(op3)
                        bluetoothAdapter.enable();
                    else
                        bluetoothAdapter.disable();
                }



            //BRIGHTNESS
            if(!op7)
            Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS,op4);

            //AUTO-SYNC
            if(op5)
                contentResolver.setMasterSyncAutomatically(true);
            else
                contentResolver.setMasterSyncAutomatically(false);

            //SCREEN-ROTATION
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, op6);

            //AUTO-BRIGHTNESS
                if (op7)
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
                else
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, 0);

            //MOBILE-DATA
            if(!op10)
                if (op8)
                    Shell.executeCommandViaSu(getApplicationContext(),"-c","svc data enable");
                else
                    Shell.executeCommandViaSu(getApplicationContext(),"-c","svc data disable");


            //POWER-SAVER
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                if (op9)
                    Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put global low_power 1");
                else
                    Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put global low_power 0");
            }
            else{
                if (op9)
                    Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put system low_power 1");
                else
                    Shell.executeCommandViaSu(getApplicationContext(), "-c", "settings put system low_power 0");
            }


            //FINAL aggiungere notifydatasetchanged
            model=tsdbHelper.getTS(id);
            if(last && !weekly){
                model.isEnabled=false;
                tsdbHelper.updateTS(model);
                Log.e("lastDay", " lastDay è " + last + " quindi isEnabled di " + model.name + " è " + model.isEnabled);

            }

        }



        TSManagerHelper.cancelTSs(this);
        TSManagerHelper.setTSs(this);




        return super.onStartCommand(intent, flags, startId);



    }

}
