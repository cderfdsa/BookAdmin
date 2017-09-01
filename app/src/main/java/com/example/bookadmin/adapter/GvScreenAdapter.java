package com.example.bookadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.ScreenAll;

import java.util.List;

/**
 * Created by Administrator on 2017-05-22.
 */

public class GvScreenAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<ScreenAll> authors;
    private LayoutInflater inflater;

    public GvScreenAdapter(Context context, List<ScreenAll> authors) {
        this.context = context;
        this.authors = authors;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return authors.size();
    }

    @Override
    public ScreenAll getItem(int position) {
        return authors.get(position);
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
        if (authors != null) {
            if (position < authors.size()) {
                attr.setText(authors.get(position).getName());
            }
            /**
             * 根据选中状态来设置item的背景和字体颜色
             */
            if (authors.get(position).isChecked()) {
                attr.setBackgroundResource(R.drawable.goods_attr_selected_shape);
                attr.setTextColor(Color.WHITE);
            } else {
                attr.setBackgroundResource(R.drawable.goods_attr_unselected_shape);
                attr.setTextColor(Color.GRAY);
            }
        }

        return convertView;
    }

    public void notifyData(List<ScreenAll> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

//    public void notifyDataSetChanged(boolean isUnfold, final List<ScreenAll> authors) {
//        if (authors == null || 0 == authors.size()) {
//            return;
//        }
//        this.authors.clear();
//        // 如果是展开的，则加入全部data，反之则只显示3条
//        if (isUnfold) {
//            authors.addAll(authors);
//        } else {
//            authors.add(authors.get(0));
//            authors.add(authors.get(1));
//            authors.add(authors.get(2));
//        }
//        notifyDataSetChanged();
//    }
}
