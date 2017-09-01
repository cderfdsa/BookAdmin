package com.example.bookadmin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.LtTime;

import java.util.List;

/**
 * Created by Administrator on 2017-06-08.
 */

public class TimeAdapter extends BaseAdapter{

    private Context mContext;
    private List<LtTime> ltTimes;
    private LayoutInflater inflater;

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=-1;

    public TimeAdapter(Context context, List<LtTime> ltTimes) {
        this.mContext = context;
        this.ltTimes = ltTimes;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return ltTimes.size();
    }

    @Override
    public LtTime getItem(int position) {
        return ltTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_lv_time, null);
        TextView tvStart = (TextView) convertView.findViewById(R.id.time_start);
        TextView tvEnd = (TextView) convertView.findViewById(R.id.time_end);
        TextView tvCenter = (TextView) convertView.findViewById(R.id.tv_center);

        LtTime ltTime = ltTimes.get(position);

        tvStart.setText(ltTime.getLt_starttime());
        tvEnd.setText(ltTime.getLt_endtime());

        if (position == selectItem) {
            convertView.setBackgroundResource(R.color.orangered);
            tvCenter.setTextColor(mContext.getResources().getColor(R.color.white));
            tvStart.setTextColor(mContext.getResources().getColor(R.color.white));
            tvEnd.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        else {
            convertView.setBackgroundResource(R.color.white);
            tvCenter.setTextColor(mContext.getResources().getColor(R.color.clock_show));
            tvStart.setTextColor(mContext.getResources().getColor(R.color.clock_show));
            tvEnd.setTextColor(mContext.getResources().getColor(R.color.clock_show));
        }
        return convertView;
    }

    public void setData(List<LtTime> ltTimes){
        this.ltTimes = ltTimes;
        notifyDataSetChanged();
    }

}
