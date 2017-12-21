package com.lorenzo.timedsettingsplus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lorenzo.timedsettingsplus.R;
import com.lorenzo.timedsettingsplus.TSDBHelper;
import com.lorenzo.timedsettingsplus.TSListAdapter;

/**
 * Created by lorenzo on 29/10/15.
 */
public class Tuesday_fragment extends Fragment {

    private Context mContext;
    private TSDBHelper dbHelper;
    public static TSListAdapter mAdapter;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mContext=getContext();
        dbHelper=TSDBHelper.getInstance(mContext);
        mAdapter= new TSListAdapter(mContext,dbHelper.getTSs(2));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.listview_tuesday, container, false);

        list = (ListView)rootView.findViewById(R.id.tuesday_list);

        list.setAdapter(mAdapter);

        return rootView;
    }



    @Override
    public void onResume(){
        super.onResume();
    }


}
