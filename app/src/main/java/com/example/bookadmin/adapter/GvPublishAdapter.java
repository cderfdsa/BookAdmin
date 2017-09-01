package com.example.bookadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.ScreenPublish;

import java.util.List;

/**
 * Created by Administrator on 2017-05-22.
 */

public class GvPublishAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<ScreenPublish> publishs;
    private LayoutInflater inflater;

    public GvPublishAdapter(Context context, List<ScreenPublish> publishes) {
        this.context = context;
        this.publishs = publishes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return publishs.size();
    }

    @Override
    public ScreenPublish getItem(int position) {
        return publishs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_gv_publish, null);
        TextView attr = (TextView) convertView.findViewById(R.id.attr_name);
        attr.setTag(position);
        if(publishs != null) {
            if (position < publishs.size()) {
                attr.setText(publishs.get(position).getPl_name());
            }
            /**
             * 根据选中状态来设置item的背景和字体颜色
             */
            if (publishs.get(position).isChecked()) {
                attr.setBackgroundResource(R.drawable.goods_attr_selected_shape);
                attr.setTextColor(Color.WHITE);
            } else {
                attr.setBackgroundResource(R.drawable.goods_attr_unselected_shape);
                attr.setTextColor(Color.GRAY);
            }
        }

        return convertView;
    }

    public void notifyData(List<ScreenPublish> publishes) {
        publishs = publishes;
        notifyDataSetChanged();
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

//    public void notifyDataSetChanged(boolean isUnfold, final List<ScreenPublish> screenPublishes) {
//        if (screenPublishes == null || 0 == screenPublishes.size()) {
//            return;
//        }
//        this.publishs.clear();
//        // 如果是展开的，则加入全部data，反之则只显示3条
//        if (isUnfold) {
//            publishs.addAll(screenPublishes);
//        } else {
//            publishs.add(screenPublishes.get(0));
//            publishs.add(screenPublishes.get(1));
//            publishs.add(screenPublishes.get(2));
//        }
//        notifyDataSetChanged();
//    }
}
