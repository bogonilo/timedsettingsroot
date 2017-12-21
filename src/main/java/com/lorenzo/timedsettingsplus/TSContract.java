package com.lorenzo.timedsettingsplus;

import android.provider.BaseColumns;

/**
 * Created by lorenzo on 06/10/15.
 */
public class TSContract {

    public TSContract() {}

    public static abstract class TS implements BaseColumns {
        public static final String TABLE_NAME = "TS";
        public static final String COLUMN_NAME_TS_NAME = "name";
        public static final String COLUMN_NAME_TS_TIME_HOUR_START = "hour_start";
        public static final String COLUMN_NAME_TS_TIME_MINUTE_START = "minute_start";
        public static final String COLUMN_NAME_TS_SUNDAY = "sunday";
        public static final String COLUMN_NAME_TS_MONDAY = "monday";
        public static final String COLUMN_NAME_TS_TUESDAY = "tuesday";
        public static final String COLUMN_NAME_TS_WEDNESDAY = "wednesday";
        public static final String COLUMN_NAME_TS_THURSDAY = "thursday";
        public static final String COLUMN_NAME_TS_FRIDAY = "friday";
        public static final String COLUMN_NAME_TS_SATURDAY = "saturday";
        public static final String COLUMN_NAME_TS_REPEAT_WEEKLY = "weekly";
        public static final String COLUMN_NAME_TS_OP1 = "op1";
        public static final String COLUMN_NAME_TS_OP2 = "op2";
        public static final String COLUMN_NAME_TS_OP3 = "op3";
        public static final String COLUMN_NAME_TS_OP4 = "op4";
        public static final String COLUMN_NAME_TS_OP5 = "op5";
        public static final String COLUMN_NAME_TS_OP6 = "op6";
        public static final String COLUMN_NAME_TS_OP7 = "op7";
        public static final String COLUMN_NAME_TS_OP8 = "op8";
        public static final String COLUMN_NAME_TS_OP9 = "op9";
        public static final String COLUMN_NAME_TS_OP10 = "op10";
        public static final String COLUMN_NAME_TS_LAST_DAY = "last_day";
        public static final String COLUMN_NAME_TS_ENABLED = "enabled";
    }

}
