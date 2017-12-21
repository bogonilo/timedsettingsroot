package com.lorenzo.timedsettingsplus;


import java.util.Calendar;

/**
 *
 * Created by lorenzo on 05/10/15.
 */
public class TSModel {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public long id = -1;
    public int timeHourStart;
    public int timeMinuteStart;
    private boolean repeatingDays[];
    public boolean repeatWeekly;
    public String name;
    public boolean lastDay;
    public boolean isEnabled;
    public String op1;   // ringer mode
    public boolean op2;  // wi-fi
    public boolean op3;  // bluetooth
    public int op4;  // brightness
    public boolean op5;  // auto-sync
    public int op6;  // screen-rotation
    public boolean op7;  // auto-brightness
    public boolean op8;  // mobile-data
    public boolean op9;  // power-save
    public boolean op10;  // airplane-mode

    private Calendar calendar=Calendar.getInstance();

    public TSModel(){
        repeatingDays=new boolean[7];
    }


    public void setRepeatingDay(int dayOfWeek, boolean value) {
        repeatingDays[dayOfWeek] = value;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public int getLastDayActived(){

        int nowDay = calendar.get(Calendar.DAY_OF_WEEK);
        int last=0;

        for(int dayOfWeek=nowDay; dayOfWeek<=Calendar.SATURDAY; dayOfWeek++)
            if(getRepeatingDay(dayOfWeek - 1) && dayOfWeek>=nowDay)
                   last=dayOfWeek;
        for(int dayOfWeek=nowDay; dayOfWeek>=Calendar.SUNDAY; dayOfWeek--) {
            if (getRepeatingDay(dayOfWeek - 1) && dayOfWeek < nowDay) {
                last = dayOfWeek;
                break;
            }
        }
        return last;
    }

    public boolean moreThanOneDay(){
        int i;
        int m=0;
        for(i=0; i<7; i++){
            if(getRepeatingDay(i))
                m=m+1;
        }
        if(m>1)
            return true;
        else
            return false;

    }

    public String getOp1(){
        return op1;
    }
    public boolean getOp2(){
        return op2;
    }
    public boolean getOp3(){
        return op3;
    }
    public int getOp4(){
        return op4;
    }
    public boolean getOp5(){
        return op5;
    }
    public int getOp6(){
        return op6;
    }
    public boolean getOp7(){
        return op7;
    }
    public boolean getOp8(){
        return op8;
    }
    public boolean getOp9(){
        return op9;
    }
    public boolean getOp10(){
        return op10;
    }

}
