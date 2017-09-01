package com.example.bookadmin.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.BookTypeBean;
import com.example.bookadmin.bean.OrderType;

import java.util.List;
import java.util.Map;

public class SortTypeAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<OrderType> orderTypes;
    private float textSize;
    private LayoutInflater inflater;
    private int normalDrawbleId;
    private Drawable selectedDrawble;

    private int imagePos = -1;

    private View.OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;

    private int selectedPos = 0;

    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    public SortTypeAdapter(Context context, List<OrderType> orderTypest, int sId, int nId) {
        this.context = context;
        this.orderTypes = orderTypest;
        selectedDrawble = context.getResources().getDrawable(sId);
        normalDrawbleId = nId;
        inflater = LayoutInflater.from(context);

        init();
    }

    private void init() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos);
                }
            }
        };
    }

    public void notifyData(List<OrderType> orderTypes) {
        this.orderTypes = orderTypes;
        notifyDataSetChanged();
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (orderTypes != null && pos < orderTypes.size()) {
            selectedPos = pos;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
    }


    @Override
    public int getCount() {
        return orderTypes.size();
    }

    @Override
    public OrderType getItem(int position) {
        return orderTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = inflater.inflate(R.layout.item_choose_booktype, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_choose);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.im_up_dowm);

        textView.setTag(position);
        String mString = "";
        if (orderTypes != null) {
            if (position < orderTypes.size()) {
                mString = orderTypes.get(position).getOt_zhus();
            }
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setText(mString);

        if (position == selectedPos) {
            textView.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
        } else {
            textView.setBackgroundDrawable(context.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
        }

        textView.setOnClickListener(onClickListener);

        if(mString.equals("价格")){
            imagePos = position;
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
            if(priceImg){//小到大
                imageView.setBackgroundResource(R.drawable.icon_up);
            }else{
                imageView.setBackgroundResource(R.drawable.icon_up_down);

            }
        }


        return convertView;
    }

    private boolean priceImg;

    public void setPriceImg(boolean priceImg) {
        this.priceImg = priceImg;
        notifyDataSetChanged();
    }

    public boolean isPriceImg() {
        return priceImg;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
