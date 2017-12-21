package com.lorenzo.timedsettingsplus;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lorenzo on 07/10/15.
 */
public class TSListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TSModel> mTSs;

    public TSListAdapter(Context context, List<TSModel> TSs) {
        mContext = context;
        mTSs = TSs;
    }

    public void setTSs(List<TSModel> TS) {
        mTSs = TS;
    }

    @Override
    public int getCount() {
        if (mTSs != null) {
            return mTSs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mTSs != null) {
            return mTSs.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mTSs != null) {
            return mTSs.get(position).id;
        }
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ts_list_item, parent, false);
        }

         final TSModel model = (TSModel) getItem(position);
         // final TSDBHelper dbHelper = new TSDBHelper(mContext);

        final TextView txtTimeStart = (TextView) view.findViewById(R.id.timeS);
        txtTimeStart.setText(String.format("%02d:%02d", model.timeHourStart, model.timeMinuteStart));


        final TextView tsName = (TextView) view.findViewById(R.id.TSname);
        tsName.setText(model.name);

        updateTextColor((TextView) view.findViewById(R.id.sunday), model.getRepeatingDay(TSModel.SUNDAY));
        updateTextColor((TextView) view.findViewById(R.id.monday), model.getRepeatingDay(TSModel.MONDAY));
        updateTextColor((TextView) view.findViewById(R.id.tuesday), model.getRepeatingDay(TSModel.TUESDAY));
        updateTextColor((TextView) view.findViewById(R.id.wednesday), model.getRepeatingDay(TSModel.WEDNESDAY));
        updateTextColor((TextView) view.findViewById(R.id.thursday), model.getRepeatingDay(TSModel.THURSDAY));
        updateTextColor((TextView) view.findViewById(R.id.friday), model.getRepeatingDay(TSModel.FRIDAY));
        updateTextColor((TextView) view.findViewById(R.id.saturday), model.getRepeatingDay(TSModel.SATURDAY));
        updateTextColor((TextView) view.findViewById(R.id.repeat), model.repeatWeekly);

        final CheckBox enab = (CheckBox) view.findViewById(R.id.enabledTS);
        enab.setTag(model.id);
        enab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (model.isEnabled != isChecked)
                    ((ActivityListTS) mContext).setTSEnabled((Long) enab.getTag(), isChecked);
            }
        });
        enab.setChecked(model.isEnabled);


        view.setTag(model.id);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActivityListTS) mContext).startActivityDetailsTS(((String) view.getTag().toString()));
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                ((ActivityListTS) mContext).deleteTS(((Long) view.getTag()));
                return true;
            }
        });




        return view;
    }



    private void updateTextColor(TextView view, boolean isOn) {
        if (isOn) {
            view.setTextColor(Color.GREEN);
        } else {
            view.setTextColor(Color.GRAY);
        }
    }




}
