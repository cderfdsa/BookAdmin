package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.DetailOrderBooks;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class OrderDetailAdapter extends SimpleAdapter<DetailOrderBooks> {

    public OrderDetailAdapter(Context context, List<DetailOrderBooks> datas) {
        super(context, R.layout.item_orderdetail, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final DetailOrderBooks detailOrderBooks, int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + detailOrderBooks.getBs_photo()));
        viewHolder.getTextView(R.id.tv_book).setText(detailOrderBooks.getBs_name());
        viewHolder.getTextView(R.id.tv_other).setText(detailOrderBooks.getBs_author());

    }

}
