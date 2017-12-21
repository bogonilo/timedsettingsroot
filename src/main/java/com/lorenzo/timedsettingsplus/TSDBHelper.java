package com.lorenzo.timedsettingsplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lorenzo.timedsettingsplus.TSContract.TS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorenzo on 06/10/15.
 */
public class TSDBHelper extends SQLiteOpenHelper {

    private static TSDBHelper mInstance ;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "timedsettings.db";

    private static final String SQL_CREATE_TS = "CREATE TABLE " + TS.TABLE_NAME + " (" +
            TS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TS.COLUMN_NAME_TS_NAME + " TEXT," +
            TS.COLUMN_NAME_TS_TIME_HOUR_START + " INTEGER," +
            TS.COLUMN_NAME_TS_TIME_MINUTE_START + " INTEGER," +
            TS.COLUMN_NAME_TS_SUNDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_MONDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_TUESDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_WEDNESDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_THURSDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_FRIDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_SATURDAY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_REPEAT_WEEKLY + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP1 + " TEXT," +
            TS.COLUMN_NAME_TS_OP2 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP3 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP4 + " INTEGER," +
            TS.COLUMN_NAME_TS_OP5 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP6 + " INTEGER," +
            TS.COLUMN_NAME_TS_OP7 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP8 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP9 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_OP10 + " BOOLEAN," +
            TS.COLUMN_NAME_TS_LAST_DAY + " BOOLEAN, " +
            TS.COLUMN_NAME_TS_ENABLED + " BOOLEAN" +
            " )";

    private static final String SQL_DELETE_TS =
            "DROP TABLE IF EXISTS " + TS.TABLE_NAME;

    private TSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TS);
        onCreate(db);
    }

    private TSModel populateModel(Cursor c) {
        TSModel model = new TSModel();
        model.id = c.getLong(c.getColumnIndex(TS._ID));
        model.name = c.getString(c.getColumnIndex(TS.COLUMN_NAME_TS_NAME));
        model.timeHourStart = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_TIME_HOUR_START));
        model.timeMinuteStart = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_TIME_MINUTE_START));
        model.repeatWeekly = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_REPEAT_WEEKLY)) == 0 ? false : true;
        model.op1 = c.getString(c.getColumnIndex(TS.COLUMN_NAME_TS_OP1));
        model.op2 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP2)) == 0 ? false : true;
        model.op3 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP3)) == 0 ? false : true;
        model.op4= c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP4));
        model.op5 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP5)) == 0 ? false : true;
        model.op6 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP6));
        model.op7 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP7)) == 0 ? false : true;
        model.op8 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP8)) == 0 ? false : true;
        model.op9 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP9)) == 0 ? false : true;
        model.op10 = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_OP10)) == 0 ? false : true;
        model.lastDay = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_LAST_DAY)) == 0 ? false : true;
        model.isEnabled = c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_ENABLED)) == 0 ? false : true;

        model.setRepeatingDay(0, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_SUNDAY)) == 0 ? false : true);
        model.setRepeatingDay(1, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_MONDAY)) == 0 ? false : true);
        model.setRepeatingDay(2, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_TUESDAY)) == 0 ? false : true);
        model.setRepeatingDay(3, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_WEDNESDAY)) == 0 ? false : true);
        model.setRepeatingDay(4, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_THURSDAY)) == 0 ? false : true);
        model.setRepeatingDay(5, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_FRIDAY)) == 0 ? false : true);
        model.setRepeatingDay(6, c.getInt(c.getColumnIndex(TS.COLUMN_NAME_TS_SATURDAY)) == 0 ? false : true);

        return model;
    }

    private ContentValues populateContent(TSModel model) {
        ContentValues values = new ContentValues();
        values.put(TS.COLUMN_NAME_TS_NAME, model.name);
        values.put(TS.COLUMN_NAME_TS_TIME_HOUR_START, model.timeHourStart);
        values.put(TS.COLUMN_NAME_TS_TIME_MINUTE_START, model.timeMinuteStart);
        values.put(TS.COLUMN_NAME_TS_REPEAT_WEEKLY, model.repeatWeekly);
        values.put(TS.COLUMN_NAME_TS_SUNDAY, model.getRepeatingDay(0));
        values.put(TS.COLUMN_NAME_TS_MONDAY, model.getRepeatingDay(1));
        values.put(TS.COLUMN_NAME_TS_TUESDAY, model.getRepeatingDay(2));
        values.put(TS.COLUMN_NAME_TS_WEDNESDAY, model.getRepeatingDay(3));
        values.put(TS.COLUMN_NAME_TS_THURSDAY, model.getRepeatingDay(4));
        values.put(TS.COLUMN_NAME_TS_FRIDAY, model.getRepeatingDay(5));
        values.put(TS.COLUMN_NAME_TS_SATURDAY, model.getRepeatingDay(6));
        values.put(TS.COLUMN_NAME_TS_OP1, model.op1);
        values.put(TS.COLUMN_NAME_TS_OP2, model.op2);
        values.put(TS.COLUMN_NAME_TS_OP3, model.op3);
        values.put(TS.COLUMN_NAME_TS_OP4, model.op4);
        values.put(TS.COLUMN_NAME_TS_OP5, model.op5);
        values.put(TS.COLUMN_NAME_TS_OP6, model.op6);
        values.put(TS.COLUMN_NAME_TS_OP7, model.op7);
        values.put(TS.COLUMN_NAME_TS_OP8, model.op8);
        values.put(TS.COLUMN_NAME_TS_OP9, model.op9);
        values.put(TS.COLUMN_NAME_TS_OP10, model.op10);
        values.put(TS.COLUMN_NAME_TS_LAST_DAY, model.lastDay);
        values.put(TS.COLUMN_NAME_TS_ENABLED, model.isEnabled);


        return values;
    }

    public long createTS(TSModel model) {
        Log.e("createTS", "chiamato createTS da :"+ model.name);
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(TS.TABLE_NAME, null, values);
    }

    public long updateTS(TSModel model) {
        Log.e("updateTS", "chiamato updateTS da :"+ model.name);
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(TS.TABLE_NAME, values, TS._ID + " = ?", new String[]{String.valueOf(model.id)});
    }

    public TSModel getTS(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS._ID + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return populateModel(c);
        }

        return null;
    }

    public List<TSModel> getTSs() {
        Cursor c=null;
        List<TSModel> alarmList;
        SQLiteDatabase db = getWritableDatabase() ;

            try {

                String select = "SELECT * FROM " + TS.TABLE_NAME;

                c = db.rawQuery(select, null);

                alarmList = new ArrayList<TSModel>();
                while (c.moveToNext()) {
                    alarmList.add(populateModel(c));
                }
                if (!alarmList.isEmpty()) {
                    db.close();
                    c.close();
                    return alarmList;
                }
            }
            finally {
                db.close();
                if(c!=null)
                 c.close();
            }
        db.close();
        c.close();
        return null;


    }

    public List<TSModel> getTSs(int day) {
        List<TSModel> TSList = new ArrayList<TSModel>();
        Cursor c = null;
        SQLiteDatabase db = getWritableDatabase() ;

        try {
            String select="";
            switch(day){
                case 0:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_SUNDAY + " = 1";
                    break;
                case 1:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_MONDAY + " = 1";
                    break;
                case 2:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_TUESDAY + " = 1";
                    break;
                case 3:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_WEDNESDAY + " = 1";
                    break;
                case 4:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_THURSDAY + " = 1";
                    break;
                case 5:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_FRIDAY + " = 1";
                    break;
                case 6:
                    select = "SELECT * FROM " + TS.TABLE_NAME + " WHERE " + TS.COLUMN_NAME_TS_SATURDAY + " = 1";
                    break;
            }

            c = db.rawQuery(select, null);

            while (c.moveToNext()) {
                Log.e("getTSs(day)","cicla nel nuovo getTSs(day) con day:" +day);
                TSList.add(populateModel(c));
            }
            if (!TSList.isEmpty()) {
                db.close();
                c.close();
                return TSList;
            }
        }
        catch(SQLiteException e){
            Log.e("SQLiteException","Eccezione nel database???");
        }
        finally {
            db.close();
            if(c!=null)
                c.close();
        }
        db.close();

        return null;

    }

    public int deleteTS(long id) {
        return getWritableDatabase().delete(TS.TABLE_NAME, TS._ID + " = ?", new String[] { String.valueOf(id) });
    }


    public static TSDBHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TSDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
}
