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

public class ClassificationAdapter extends SimpleAdapter<BookAttr> {


    public ClassificationAdapter(Context context, List<BookAttr> datas) {
        super(context, R.layout.item_classifition, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final BookAttr bookAttr, int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);

        draweeView.setAspectRatio(Contants.aspectRatio);
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + bookAttr.getBs_photo()));

        viewHolder.getTextView(R.id.bl_books).setText(bookAttr.getBs_name());
//        viewHolder.getTextView(R.id.bs_evaluate).setText(bookAttr.getBs_author());

//        if(draweeView != null) {
//            draweeView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.showShortToast(context, bookAttr.getBs_photo());
//                }
//            });
//        }

    }
}
