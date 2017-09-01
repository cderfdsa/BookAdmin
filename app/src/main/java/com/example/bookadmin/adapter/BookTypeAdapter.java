package com.example.bookadmin.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.BookTypeBean;

import java.util.List;

public class BookTypeAdapter extends android.widget.BaseAdapter {

	private Context context;
	private List<BookTypeBean> bookTypeBeans;
	private float textSize;
	private LayoutInflater inflater;
	private int normalDrawbleId;
	private Drawable selectedDrawble;

	private View.OnClickListener onClickListener;
	private OnItemClickListener mOnItemClickListener;

	private int selectedPos = 0;

	public BookTypeAdapter (Context context, List<BookTypeBean> bookTypeBeans, int sId, int nId){
		this.context = context;
		this.bookTypeBeans = bookTypeBeans;
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

	/**
	 * 设置选中的position,并通知列表刷新
	 */
	public void setSelectedPosition(int pos) {
		if (bookTypeBeans != null && pos < bookTypeBeans.size()) {
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


	public void notifyData(List<BookTypeBean> groups){
		bookTypeBeans = groups;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return bookTypeBeans.size();
	}

	@Override
	public BookTypeBean getItem(int position) {
		return bookTypeBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_choose_booktype, parent, false);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_choose);
		textView.setTag(position);
		String mString = "";
		if (bookTypeBeans != null) {
			if (position < bookTypeBeans.size()) {
				mString = bookTypeBeans.get(position).getS_name();
			}
		}

		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		textView.setText(mString);

		if(position == selectedPos){
			textView.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
		}else{
			textView.setBackgroundDrawable(context.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
		}

		textView.setOnClickListener(onClickListener);
		return convertView;
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
