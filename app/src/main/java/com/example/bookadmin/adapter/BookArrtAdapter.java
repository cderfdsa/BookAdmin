package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.BookAttr;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017-05-05.
 */

public class BookArrtAdapter extends SimpleAdapter<BookAttr> {


    public BookArrtAdapter(Context context, List<BookAttr> datas) {
        super(context, R.layout.item_bookattr, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final BookAttr bookAttr, int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setAspectRatio(Contants.aspectRatio);
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + bookAttr.getBs_photo()));

        viewHolder.getTextView(R.id.bl_books).setText(bookAttr.getBs_name());
//        viewHolder.getTextView(R.id.bs_evaluate).setText(TimeUtils.formarTime(bookAttr.getBs_evaluate()));
        viewHolder.getTextView(R.id.bs_author).setText(bookAttr.getBs_author());
        viewHolder.getTextView(R.id.bs_publish).setText(bookAttr.getPl_name());
        if(!bookAttr.getBs_title().equals("0")) {
            viewHolder.getTextView(R.id.bs_title).setText(bookAttr.getBs_title());
        }else{
            viewHolder.getTextView(R.id.bs_title).setText("暂无主题");
        }

//        if(draweeView != null) {
//            draweeView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.showShortToast(context, bookAttr.getBs_photo());
//
//                }
//            });
//        }

    }
}
