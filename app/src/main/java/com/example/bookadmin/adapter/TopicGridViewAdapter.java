package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.tools.CartProvider;
import com.example.bookadmin.tools.utils.ImageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017-05-12.
 */

public class TopicGridViewAdapter extends android.widget.BaseAdapter {

    private Context mContext;
    private List<ShoppingCart> carts;
    private LayoutInflater inflater;

    private CartProvider cartProvider;

    private boolean isShowDelete;//判断是否显示删除图标跟抖动，true是显示，false是不显示

    public TopicGridViewAdapter(Context context, List<ShoppingCart> carts, boolean isShowDelete) {
        this.mContext = context;
        this.carts = carts;
        this.isShowDelete = isShowDelete;
        inflater = LayoutInflater.from(mContext);

        cartProvider = new CartProvider(context);
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public ShoppingCart getItem(int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_grid_layout, null);
        TextView tvBook = (TextView) convertView.findViewById(R.id.bl_books);
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.drawee_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) draweeView.getLayoutParams();
        params.height = Contants.displayHeight / 5;//256
        draweeView.setLayoutParams(params);
        draweeView.setAspectRatio(Contants.aspectRatio);
        draweeView.setAspectRatio(Contants.aspectRatio);
        ImageView imDel = (ImageView) convertView.findViewById(R.id.iv_delimg);

        final ShoppingCart cart = carts.get(position);
        tvBook.setText(cart.getBs_name());
        draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + cart.getBs_photo()));

        imDel.setBackgroundResource(R.drawable.cha11);

        if (isShowDelete) {    //判断是否显示删除图标
            imDel.setVisibility(View.VISIBLE);
            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.anim);
            shake.reset();
            shake.setFillAfter(true);
            convertView.startAnimation(shake);

        } else {
            imDel.setVisibility(View.GONE);
            convertView.clearAnimation();
        }

        imDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onImdelListener != null){
                    onImdelListener.onImClick(v, position);
                }
            }
        });

        if (cart.isInvalid()) {
            ImageUtil.drawText(mContext, Contants.API.IP_UTL + cart.getBs_photo(),
                    "失效", draweeView);
        }

        return convertView;
    }

    private OnImdelListener onImdelListener;

    public interface OnImdelListener{
        void onImClick(View v, int position);
    }

    public void setOnImdelListener(OnImdelListener onImdelListener) {
        this.onImdelListener = onImdelListener;
    }

    /**
     * 设置显示删除图标
     *
     * @param isShowDelete
     */
    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        this.notifyDataSetChanged();
    }


}
