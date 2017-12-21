package com.lorenzo.timedsettingsplus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class TSManagerHelper extends BroadcastReceiver {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR_START = "timeHourStart";
    public static final String TIME_MINUTE_START = "timeMinuteStart";
    public static final String OPTION1 = "option1";
    public static final String OPTION2 = "option2";
    public static final String OPTION3 = "option3";
    public static final String OPTION4 = "option4";
    public static final String OPTION5 = "option5";
    public static final String OPTION6 = "option6";
    public static final String OPTION7 = "option7";
    public static final String OPTION8 = "option8";
    public static final String OPTION9 = "option9";
    public static final String OPTION10 = "option10";
    public static final String LAST_DAY = "lastDay";
    public static final String REPEAT_WEEKLY = "repeatWeekly";


    @Override
    public void onReceive(Context context, Intent intent) {
     setTSs(context);
    }

    public static void setTSs(Context context) {

        TSDBHelper dbHelper = TSDBHelper.getInstance(context);

        List<TSModel> ts =  dbHelper.getTSs();

        if(ts!=null) {
            for (TSModel alarm : ts) {
                if (alarm.isEnabled) {

                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    Calendar calendar = Calendar.getInstance();


                    //Find next time to set
                    final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
                    boolean TSSet = false;


                    //First check if it's later in the week
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
                        if (alarm.getRepeatingDay(dayOfWeek-1) && dayOfWeek >= nowDay &&
                                !(dayOfWeek == nowDay && alarm.timeHourStart < nowHour) &&
                                !(dayOfWeek == nowDay && alarm.timeHourStart == nowHour && alarm.timeMinuteStart <= nowMinute)) {
                            Log.e("LATER","LATER");


                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHourStart);
                            calendar.set(Calendar.MINUTE, alarm.timeMinuteStart);
                            calendar.set(Calendar.SECOND, 00);



                            TSSet = true;
                            break;
                        }
                    }

                    //Else check if it's earlier in the week
                    if (!TSSet) {
                        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
                            if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay) {

                                Log.e("EARLIER","EARLIER");
                                if((nowDay==Calendar.SUNDAY && dayOfWeek==Calendar.SUNDAY) || !(nowDay!=Calendar.SUNDAY && dayOfWeek==Calendar.SUNDAY))
                                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHourStart);
                                calendar.set(Calendar.MINUTE, alarm.timeMinuteStart);
                                calendar.set(Calendar.SECOND, 00);


                                TSSet = true;
                                break;
                            }


                        }
                    }
                    if(calendar.compareTo(Calendar.getInstance()) < 0) {
                        // calendarStart is in the past, use next Sunday
                        calendar.add(Calendar.DAY_OF_YEAR, 7);
                        Log.e("Earlier","risultato nel passato, aggiunta settimana");
                    }

                    Log.e("setTSs", "attivato " +alarm.name +" in data "+calendar.getTime()+" lastDayActived è "+alarm.getLastDayActived());
                    setTS(context, calendar, pIntent);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private static void setTS(Context context, Calendar calendar, PendingIntent pIntent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pIntent);
        else
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pIntent);

    }

    public static void cancelTSs(Context context) {
        TSDBHelper dbHelper = TSDBHelper.getInstance(context);

        List<TSModel> TSs =  dbHelper.getTSs();

        if (TSs != null) {
            for (TSModel alarm : TSs) {

                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                    Log.e("cancelTSs","cancellato "+alarm.name);

            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, TSModel model) {
        Intent intent = new Intent(context, TSServiceStart.class);
        intent.putExtra(ID, model.id);
        intent.putExtra(NAME, model.name);
        intent.putExtra(TIME_HOUR_START, model.timeHourStart);
        intent.putExtra(TIME_MINUTE_START, model.timeMinuteStart);
        intent.putExtra(OPTION1, model.op1);
        intent.putExtra(OPTION2, model.op2);
        intent.putExtra(OPTION3, model.op3);
        intent.putExtra(OPTION4, model.op4);
        intent.putExtra(OPTION5, model.op5);
        intent.putExtra(OPTION6, model.op6);
        intent.putExtra(OPTION7, model.op7);
        intent.putExtra(OPTION8, model.op8);
        intent.putExtra(OPTION9, model.op9);
        intent.putExtra(OPTION10, model.op10);
        intent.putExtra(LAST_DAY, model.getLastDayActived());
        intent.putExtra(REPEAT_WEEKLY,model.repeatWeekly);

        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
