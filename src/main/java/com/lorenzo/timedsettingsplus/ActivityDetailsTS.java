package com.lorenzo.timedsettingsplus;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;


public class ActivityDetailsTS extends AppCompatActivity {

    private TSDBHelper dbHelper;

    private WifiManager wifiManager;
    private AudioManager audioManager;
    private BluetoothAdapter bluetoothAdapter;
    private InterstitialAd interstitialAd;
    double adTurn;

    private TSModel TSDetails;

    private EditText editName;


    private int minuteStart, hourStart, op1_id, op4_value;

    private Calendar cStart=Calendar.getInstance();


    private TimePickerDialog timePickerStart;
    private AlertDialog.Builder ringModeSelector;

    private TextView timeStart;
    private TextView op1;
    private TextView op7_text;
    private TextView op8_text;
    private TextView op9_text;
    private TextView op4_text;
    private TextView op10_text;
    private TextView op2_text;
    private TextView op3_text;

    private CheckBox chkWeekly;

    private ToggleButton chkSunday;
    private ToggleButton chkMonday;
    private ToggleButton chkTuesday;
    private ToggleButton chkWednesday;
    private ToggleButton chkThursday;
    private ToggleButton chkFriday;
    private ToggleButton chkSaturday;

    private Switch op2;
    private Switch op3;
    private Switch op5;
    private Switch op6;
    private Switch op7;
    private Switch op8;
    private Switch op9;
    private Switch op10;

    private SeekBar op4;

    private ContentResolver contentResolver;

    public String id0;
    public String id1;
    public String id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7518908736641187/4711700753");
        requestNewInterstitial();

        dbHelper = TSDBHelper.getInstance(this);

        setContentView(R.layout.activity_details_ts);

        try{
            Runtime.getRuntime().exec("su");
        }
        catch (IOException e){
            Log.e("SU", "catched IOException");
            Toast t=Toast.makeText(this,"No rooted device", Toast.LENGTH_LONG);
            t.show();
        }

        id0= this.getResources().getString(R.string.id0);
        id1= this.getResources().getString(R.string.id1);
        id2= this.getResources().getString(R.string.id2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findView();

        editName.setCursorVisible(false);

        dialogRingerMode();

        wifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        contentResolver=getContentResolver();



        op4_value=0;
        op4.setMax(255);
        try {
            op4_value= Settings.System.getInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        minuteStart=cStart.get(Calendar.MINUTE);
        hourStart=cStart.get(Calendar.HOUR_OF_DAY);
        timeStart.setText(formatTime(hourStart,minuteStart));


        timePickerStart = new TimePickerDialog(ActivityDetailsTS.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourStart=hourOfDay;
                minuteStart=minute;
                timeStart.setText(formatTime(hourStart,minuteStart));
            }
        }, cStart.get(Calendar.HOUR_OF_DAY), cStart.get(Calendar.MINUTE), true);

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerStart.show();
            }
        });

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringModeSelector.show();
            }
        });

        op7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    op4_text.setTextColor(Color.GRAY);
                    op4.setVisibility(View.INVISIBLE);
                } else {
                    op4.setVisibility(View.VISIBLE);
                    op4_text.setTextColor(Color.BLACK);
                }
            }
        });

        op10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                        op2.setVisibility(View.INVISIBLE);
                        op3.setVisibility(View.INVISIBLE);
                        op8.setVisibility(View.INVISIBLE);
                        op2_text.setTextColor(Color.GRAY);
                        op3_text.setTextColor(Color.GRAY);
                        op8_text.setTextColor(Color.GRAY);
                }
                else{
                    op2.setVisibility(View.VISIBLE);
                    op3.setVisibility(View.VISIBLE);
                    op8.setVisibility(View.VISIBLE);
                    op2_text.setTextColor(Color.BLACK);
                    op3_text.setTextColor(Color.BLACK);
                    op8_text.setTextColor(Color.BLACK);
                }
            }
        });

        //NUOVO TS
        String day = getIntent().getExtras().getString("day");
        if (day.equals("Sun") || day.equals("Mon") || day.equals("Tue") || day.equals("Wed") || day.equals("Thu") ||
                day.equals("Fri") || day.equals("Sat")) {

            TSDetails = new TSModel();
            Log.e("nuovo"," crea nuovo e day è "+day);
            //WI-FI
            if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED)
                op2.setChecked(true);
            else
                op2.setChecked(false);

            //RINGER-MODE
            if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
                op1.setText(id0);
            else if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE)
                op1.setText(id1);
            else
                op1.setText(id2);

            //BLUETOOTH
            if(bluetoothAdapter!=null)
            if(bluetoothAdapter.getState()==BluetoothAdapter.STATE_ON)
                op3.setChecked(true);
            else
                op3.setChecked(false);

            //BRIGHTNESS
            op4.setProgress(op4_value);

            //AUTO-SYNC
            op5.setChecked(contentResolver.getMasterSyncAutomatically());


            //SCREEN-ROTATION
            try {
                op6.setChecked(Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION)==1);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            //AUTOMATIC-BRIGHTNESS
            try {
                op7.setChecked(Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)==1);
            } catch (Settings.SettingNotFoundException e) {
                op7_text.setTextColor(Color.GRAY);
                op7.setClickable(false);
                Toast t=Toast.makeText(this,"no luminosità automatica", Toast.LENGTH_LONG);
                t.show();
                e.printStackTrace();
            }
            if(op7.isChecked())
                op4.setVisibility(View.INVISIBLE);

            //MOBILE-DATA
            boolean mobileDataEnabled = false; // Assume disabled
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                Class cmClass = Class.forName(cm.getClass().getName());
                Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
                method.setAccessible(true); // Make the method callable
                // get the setting for "mobile data"
                mobileDataEnabled = (Boolean)method.invoke(cm);
            } catch (Exception e) {
                op8_text.setTextColor(Color.GRAY);
                op8.setClickable(false);
            }
            op8.setChecked(mobileDataEnabled);

            //POWER-SAVE
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                    op9.setChecked(Settings.Global.getInt(contentResolver,"low_power")==1);
                else
                    op9.setChecked(Settings.System.getInt(contentResolver,"low_power")==1);
            } catch (Settings.SettingNotFoundException e) {
                op9_text.setTextColor(Color.GRAY);
                op9.setClickable(false);
            }

            //AIRPLANE-MODE
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                    op10.setChecked(Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON)==1);
                else
                    op10.setChecked(Settings.System.getInt(contentResolver, Settings.System.AIRPLANE_MODE_ON)==1);
            } catch (Settings.SettingNotFoundException e) {
                op10_text.setTextColor(Color.GRAY);
                op10.setClickable(false);
            }


            //ACTUAL-DAY
            switch (day){
                case "Sun" :
                    chkSunday.setChecked(true);
                    break;
                case "Mon" :
                    chkMonday.setChecked(true);
                    break;
                case "Tue" :
                    chkTuesday.setChecked(true);
                    break;
                case "Wed" :
                    chkWednesday.setChecked(true);
                    break;
                case "Thu" :
                    chkThursday.setChecked(true);
                    break;
                case "Fri" :
                    chkFriday.setChecked(true);
                    break;
                case "Sat" :
                    chkSaturday.setChecked(true);
                    break;
            }

        }  //MODIFICA TS

        else{
            TSDetails = dbHelper.getTS(Long.parseLong(day));
            Log.e("modifica","modifica uno già esistente, name :"+ TSDetails.name);
            timeStart.setText(formatTime(TSDetails.timeHourStart, TSDetails.timeMinuteStart));

            editName.setText(TSDetails.name);

            chkWeekly.setChecked(TSDetails.repeatWeekly);
            chkSunday.setChecked(TSDetails.getRepeatingDay(TSModel.SUNDAY));
            chkMonday.setChecked(TSDetails.getRepeatingDay(TSModel.MONDAY));
            chkTuesday.setChecked(TSDetails.getRepeatingDay(TSModel.TUESDAY));
            chkWednesday.setChecked(TSDetails.getRepeatingDay(TSModel.WEDNESDAY));
            chkThursday.setChecked(TSDetails.getRepeatingDay(TSModel.THURSDAY));
            chkFriday.setChecked(TSDetails.getRepeatingDay(TSModel.FRIDAY));
            chkSaturday.setChecked(TSDetails.getRepeatingDay(TSModel.SATURDAY));
            op1.setText(TSDetails.getOp1());
            op2.setChecked(TSDetails.getOp2());
            op3.setChecked(TSDetails.getOp3());
            op4.setProgress(TSDetails.getOp4());
            op5.setChecked(TSDetails.getOp5());
            op6.setChecked(TSDetails.getOp6()==1);
            op7.setChecked(TSDetails.getOp7());
            if(op7.isChecked()) {
                op4.setVisibility(View.INVISIBLE);
                op4_text.setTextColor(Color.GRAY);
            }
            op8.setChecked(TSDetails.getOp8());
            op9.setChecked(TSDetails.getOp9());
            op10.setChecked(TSDetails.getOp10());
            if(op10.isChecked()) {
                op2.setVisibility(View.INVISIBLE);
                op3.setVisibility(View.INVISIBLE);
                op8.setVisibility(View.INVISIBLE);
                op2_text.setTextColor(Color.GRAY);
                op3_text.setTextColor(Color.GRAY);
                op8_text.setTextColor(Color.GRAY);
            }
        }

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ts_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.save_button) {
                updateModelFromLayout();

                TSManagerHelper.cancelTSs(this);

                Intent result= new Intent();

                if (TSDetails.id < 0) {
                    dbHelper.createTS(TSDetails);
                } else {
                    dbHelper.updateTS(TSDetails);
                }

                TSManagerHelper.setTSs(this);

                setResult(RESULT_OK, result);
                finish();
            }
        else
          finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.e("onStop","chiamato onStop da ActivityDetailTs");
        adTurn = Math.random()*10;
        if(adTurn <= 8)
            if(interstitialAd.isLoaded())
                interstitialAd.show();
    }

    private void updateModelFromLayout() {

        String string = timeStart.getText().toString();
        String[] parts = string.split(":");
        String part1 = parts[0];
        String part2 = parts[1];
        TSDetails.timeMinuteStart = Integer.parseInt(part2);
        TSDetails.timeHourStart = Integer.parseInt(part1);
        if(editName.getText().toString().equals(""))
            TSDetails.name="No name";
        else
            TSDetails.name = editName.getText().toString();
        TSDetails.repeatWeekly = chkWeekly.isChecked();
        TSDetails.setRepeatingDay(TSModel.SUNDAY, chkSunday.isChecked());
        TSDetails.setRepeatingDay(TSModel.MONDAY, chkMonday.isChecked());
        TSDetails.setRepeatingDay(TSModel.TUESDAY, chkTuesday.isChecked());
        TSDetails.setRepeatingDay(TSModel.WEDNESDAY, chkWednesday.isChecked());
        TSDetails.setRepeatingDay(TSModel.THURSDAY, chkThursday.isChecked());
        TSDetails.setRepeatingDay(TSModel.FRIDAY, chkFriday.isChecked());
        TSDetails.setRepeatingDay(TSModel.SATURDAY, chkSaturday.isChecked());
        TSDetails.op1=op1.getText().toString();
        TSDetails.op2=op2.isChecked();
        TSDetails.op3=op3.isChecked();
        TSDetails.op4=op4.getProgress();
        TSDetails.op5=op5.isChecked();
        if(op6.isChecked())
            TSDetails.op6=1;
        else
            TSDetails.op6=0;
        TSDetails.op7=op7.isChecked();
        TSDetails.op8=op8.isChecked();
        TSDetails.op9=op9.isChecked();
        TSDetails.op10=op10.isChecked();
        TSDetails.isEnabled = true;
    }

    protected String formatTime(int hour, int min){
        String m="00";
        String h="00";
        if(min<10)
            m="0"+min;
        else
            m=String.valueOf(min);
        if(hour<10)
            h="0"+hour;
        else
            h=String.valueOf(hour);
        StringBuilder d=new StringBuilder()
                .append(h).append(":").append(m);
        return d.toString();
    }


    private void findView(){
        editName = (EditText) findViewById(R.id.TSname);
        timeStart=(TextView) findViewById(R.id.timeStart);
        chkWeekly = (CheckBox) findViewById(R.id.ripWeek);
        chkSunday = (ToggleButton) findViewById(R.id.S);
        chkMonday = (ToggleButton) findViewById(R.id.M);
        chkTuesday = (ToggleButton) findViewById(R.id.T);
        chkWednesday = (ToggleButton) findViewById(R.id.W);
        chkThursday = (ToggleButton) findViewById(R.id.Th);
        chkFriday = (ToggleButton) findViewById(R.id.F);
        chkSaturday = (ToggleButton) findViewById(R.id.Sa);
        op1= (TextView) findViewById(R.id.ringMode);
        op2=(Switch) findViewById(R.id.switchOp2);
        op2_text=(TextView)findViewById(R.id.op2);
        op3=(Switch) findViewById(R.id.switchOp3);
        op3_text=(TextView)findViewById(R.id.op3);
        op4=(SeekBar) findViewById(R.id.seekBarOp4);
        op4_text=(TextView)findViewById(R.id.op4);
        op5=(Switch) findViewById(R.id.switchOp5);
        op6=(Switch) findViewById(R.id.switchOp6);
        op7=(Switch) findViewById(R.id.switchOp7);
        op7_text=(TextView)findViewById(R.id.op7);
        op8=(Switch) findViewById(R.id.switchOp8);
        op8_text=(TextView)findViewById(R.id.op8);
        op9=(Switch) findViewById(R.id.switchOp9);
        op9_text=(TextView)findViewById(R.id.op9);
        op10=(Switch) findViewById(R.id.switchOp10);
        op10_text=(TextView)findViewById(R.id.op10);
    }


    private void dialogRingerMode(){
        ringModeSelector=new AlertDialog.Builder(this);
        ringModeSelector.setTitle(this.getResources().getString(R.string.choose_mode));
        ringModeSelector.setItems(R.array.ringer_mode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                op1_id = i;
                if (op1_id == 0)
                    op1.setText(id0);
                else if(op1_id==1)
                    op1.setText(id1);
                else
                    op1.setText(id2);
            }
        });
        ringModeSelector.create();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E7C829D490857E50EA38ACBB72B40CB0")
                .build();

        interstitialAd.loadAd(adRequest);
    }
}
