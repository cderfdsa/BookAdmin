package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.MyReadBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017-05-05.
 */

public class MyReadAdapter extends SimpleAdapter<MyReadBean> {


    public MyReadAdapter(Context context, List<MyReadBean> datas) {
        super(context, R.layout.item_my_read, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final MyReadBean myReadBean, int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setAspectRatio(Contants.aspectRatio);
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + myReadBean.getBs_photo()));

        viewHolder.getTextView(R.id.bs_name).setText(myReadBean.getBs_name());
//        viewHolder.getTextView(R.id.bs_author).setText(TimeUtils.formarTime(myReadBean.getBs_evaluate()));
        viewHolder.getTextView(R.id.bs_author).setText(myReadBean.getBs_author());
//        viewHolder.getTextView(R.id.ub_time).setText(myReadBean.getBs_author());
//        viewHolder.getTextView(R.id.ub_evaluate).setText(myReadBean.getBs_price());
        viewHolder.getTextView(R.id.bs_title).setText(myReadBean.getBs_title());


    }
}
