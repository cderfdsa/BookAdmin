package com.example.bookadmin.widget.expant;

import android.content.Context;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.bookadmin.R;
import com.example.bookadmin.adapter.SortTypeAdapter;
import com.example.bookadmin.adapter.TextAdapter;
import com.example.bookadmin.bean.OrderType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SortRelativeLayout extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;

	private OnSelectListener mOnSelectListener;
	private SortTypeAdapter adapter;
	private Context mContext;

	private String showText;

	private  List<OrderType>orderTypes = new ArrayList<>();

	public String getShowText() {
		return showText;
	}

	public SortRelativeLayout(Context context) {
		super(context);
		init(context);
	}

	public SortRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SortRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_sort_distance, this, true);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
		mListView = (ListView) findViewById(R.id.listView);

	}

	public void setAdapter(){
		adapter = new SortTypeAdapter(mContext, orderTypes, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
		adapter.setTextSize(14);
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new SortTypeAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					OrderType orderType = orderTypes.get(position);
					showText = orderType.getOt_zhus();
					if(showText.equals("价格")){
						orderType.setOt_sn(adapter.isPriceImg() ? 5 : 10);
						adapter.setPriceImg(!adapter.isPriceImg());
					}
					mOnSelectListener.getValue(orderType, orderType.getOt_zhus(), position);
				}
			}
		});
	}

	public void notifyData(List<OrderType>orderTypes){
		this.orderTypes = orderTypes;
		adapter.notifyData(orderTypes);
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(OrderType orderType, String showText, int pos);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}

}
