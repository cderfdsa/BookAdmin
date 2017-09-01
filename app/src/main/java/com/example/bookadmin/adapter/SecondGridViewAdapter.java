package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.ShopInBook;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017-05-12.
 */

public class SecondGridViewAdapter extends android.widget.BaseAdapter {

    private Context mContext;
    private List<ShopInBook>shopInBooks;
    private LayoutInflater inflater;

    public SecondGridViewAdapter(Context context, List<ShopInBook>shopInBooks) {
        this.mContext = context;
        this.shopInBooks = shopInBooks;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return shopInBooks.size();
    }

    @Override
    public ShopInBook getItem(int position) {
        return shopInBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_grid1_layout, null);
        TextView tvBook = (TextView) convertView.findViewById(R.id.bl_books);
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.drawee_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) draweeView.getLayoutParams();
        params.height = Contants.displayHeight / 5;//256
        draweeView.setLayoutParams(params);
        draweeView.setAspectRatio(Contants.aspectRatio);
        TextView tvState = (TextView) convertView.findViewById(R.id.tv_state);

        final ShopInBook shopInBook = shopInBooks.get(position);
        tvBook.setText(shopInBook.getInBookBean().getBs_name());
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + shopInBook.getInBookBean().getBs_photo()));

//        if (shopInBook.getState() == ShopInBook.STATE_BORROW) {
//            tvState.setText(R.string.renew);
//            ImageUtil.drawText(mContext, Contants.API.IP_UTL + shopInBook.getInBookBean().getBs_photo(), mContext.getString(R.string.renew), draweeView);
//        } else if(shopInBook.getState() == ShopInBook.STATE_STILL) {
//            tvState.setText(R.string.returnBook );
//            ImageUtil.drawText(mContext, Contants.API.IP_UTL + shopInBook.getInBookBean().getBs_photo(), mContext.getString(R.string.returnBook), draweeView);
//        }


        return convertView;
    }


}
