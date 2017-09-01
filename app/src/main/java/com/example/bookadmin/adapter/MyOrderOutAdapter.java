package com.example.bookadmin.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.BaseAdapter;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.Date_find;
import com.example.bookadmin.bean.OrderOut;
import com.example.bookadmin.tools.utils.DensityUtil;
import com.example.bookadmin.tools.utils.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-25.
 */

public class MyOrderOutAdapter extends android.widget.BaseAdapter{

    private Context mContext;
    private ArrayList<OrderOut> orderOuts;
    private LayoutInflater inflater;

    private OurClickListener ourClickListener;

    private int downX = 0;

    public MyOrderOutAdapter(Context mContext, ArrayList<OrderOut> orderOuts) {
        this.mContext = mContext;
        this.orderOuts = orderOuts;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return orderOuts.size();
    }

    @Override
    public OrderOut getItem(int position) {
        return orderOuts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_myorder, null);
            holder = new ViewHolder();
            holder.tvRpname = (TextView) convertView.findViewById(R.id.tv_Rp_name);
//            holder.tvAdname = (TextView) convertView.findViewById(R.id.tv_ad_name);
            holder.tvGsname = (TextView) convertView.findViewById(R.id.tv_gs_name);
            holder.tvRpintime = (TextView) convertView.findViewById(R.id.tv_rp_intime);
            holder.tvBcname = (TextView) convertView.findViewById(R.id.tv_bc_name);
            holder.textTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.linearOrder = (LinearLayout) convertView.findViewById(R.id.linear_order);
            holder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.drawee_view);
            holder.scrollOrder = (HorizontalScrollView) convertView.findViewById(R.id.Scroll_order);
            holder.gridView = (GridView) convertView.findViewById(R.id.ordergrid);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.scrollOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourClickListener.OurClick(position);
            }
        });
        holder.scrollOrder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getRawX();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    int upX = (int) event.getRawX();
                    int distance = downX - upX;
                    if(Math.abs(distance) < 10) {
                        ourClickListener.OurClick(position);
                    }
                }
                return false;
            }
        });
        OrderOut orderOut = orderOuts.get(position);
        String rp_bcod = orderOut.getRp_bcode();
        String rp_name = orderOut.getRp_name();
        String ad_name = orderOut.getAd_name();
        String gs_name = orderOut.getGs_name();
        String rp_intime =  orderOut.getRp_intime();
        String bc_name = orderOut.getBc_name();
        if(rp_name != null) {
            holder.tvRpname.setText(rp_name);
        }else{
            holder.tvRpname.setText("");
        }
//        if(ad_name != null) {
//            holder.tvAdname.setText(ad_name);
//        }
        if(!rp_bcod.equals("0")) {
            if (gs_name != null) {
                holder.tvGsname.setText(gs_name);
            } else {
                holder.tvGsname.setText("");
            }
        }else{
            holder.tvGsname.setText("重新预约书柜");
        }
        if(rp_intime != null) {
            holder.tvRpintime.setText(TimeUtils.formarTime(rp_intime));
        }else{
            holder.tvRpintime.setText("");
        }
        if(!rp_bcod.equals("0")) {
            if (bc_name != null) {
                holder.tvBcname.setText(bc_name);
                if(bc_name.equals("取书") || bc_name.equals("拆包")){
                    holder.tvBcname.setText("完成");
                }
            } else {
                holder.tvBcname.setText("");
            }
        }else{
            holder.tvBcname.setText("");
        }
        String[] photos = orderOut.getPhoto();
        if(photos != null && photos.length > 0){
            showPhoto(holder);
            int size = photos.length;
            int length = DensityUtil.dip2px(mContext, 65);
            int gridviewWidth = (length + 50) * size;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.gridView.setLayoutParams(params); // 重点
            holder.gridView.setColumnWidth(length); // 重点
            holder.gridView.setHorizontalSpacing(50); // 间距
            holder.gridView.setStretchMode(GridView.NO_STRETCH);
            holder.gridView.setNumColumns(size); // 重点

            holder.gridView.setClickable(false);
            holder.gridView.setPressed(false);
            holder.gridView.setEnabled(false);

            holder.gridView.clearDisappearingChildren(); //删除屏幕上残存动画
            PictureAdapter pictureAdapter = new PictureAdapter(mContext, photos);
            holder.gridView.setAdapter(pictureAdapter);

        }else{
            showTitle(holder);
            Date_find date_find = orderOut.getDate_find();
            if(date_find != null){
                holder.textTitle.setText(date_find.getBs_name() + "\n" + date_find.getBs_author() + "\n");
                holder.draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + date_find.getBs_photo()));
            }
        }
        return convertView;
    }

    public interface OurClickListener{
        void OurClick(int position);
    }

    public void setOurClickListener(OurClickListener ourClickListener) {
        this.ourClickListener = ourClickListener;
    }

    public void showPhoto(ViewHolder holder){
        holder.scrollOrder.setVisibility(View.VISIBLE);
        holder.linearOrder.setVisibility(View.GONE);
    }

    public void showTitle(ViewHolder holder){
        holder.scrollOrder.setVisibility(View.GONE);
        holder.linearOrder.setVisibility(View.VISIBLE);
    }

    class ViewHolder{
        private TextView tvRpname;
        private TextView tvRpintime;
        private TextView tvBcname;
        private TextView tvGsname;
//        private TextView tvAdname;

        private LinearLayout linearOrder;//显示标题
        private SimpleDraweeView draweeView;
        private TextView textTitle;

        private HorizontalScrollView scrollOrder;//显示图片
        private GridView gridView;

    }

    class PictureAdapter extends BaseAdapter{

        private Context mContext;
        private String[] photos;

        public PictureAdapter(Context mContext, String[] photos) {
            this.mContext = mContext;
            this.photos = photos;
        }

        @Override
        public int getCount() {
            return photos.length;
        }

        @Override
        public String getItem(int position) {
            return photos[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_myorder_one, null);
            SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.drawee_view);
            draweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + photos[position]));
            return convertView;
        }
    }

}
