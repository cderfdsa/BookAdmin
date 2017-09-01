package com.example.bookadmin.widget.expant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookadmin.R;
import com.example.bookadmin.adapter.GvPublishAdapter;
import com.example.bookadmin.adapter.GvScreenAdapter;
import com.example.bookadmin.bean.ScreenAll;
import com.example.bookadmin.bean.ScreenPublish;
import com.example.bookadmin.tools.utils.GsonUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ScreenRelativeLayout extends RelativeLayout implements ViewBaseAction, View.OnClickListener{

	private Context mContext;
	private LayoutInflater inflate;

	private View goodsNoView;
	private TextView filterReset;
	private TextView filterSure;

	private GridView gvPublish;
	private GridView gvAuthor;
	private GridView gvTitle;

	private EditText etLow;
	private EditText etHigh;

	private GvPublishAdapter gvPublishAdapter;
	private GvScreenAdapter gvAuthorAdapter;
	private GvScreenAdapter gvTitleAdapter;

	private List<ScreenPublish> publishes = new ArrayList<>();
	private List<ScreenAll> authors = new ArrayList<>();
	private List<ScreenAll> titles = new ArrayList<>();

	private OnSelectListener mOnSelectListener;
	private OnCancelListener mOnCancelListener;
	private OnFilterSureListener mOnFilterSureListener;


	public ScreenRelativeLayout(Context context) {
		super(context);
		init(context);
	}

	public ScreenRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		mContext = context;
		inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.popup_goods_details, this, true);
		gvPublish = (GridView) findViewById(R.id.gv_publish);
		gvAuthor = (GridView) findViewById(R.id.gv_author);
		gvTitle = (GridView) findViewById(R.id.gv_title);

		etLow = (EditText) findViewById(R.id.etLow);
		etHigh = (EditText) findViewById(R.id.etHigh);

		goodsNoView = findViewById(R.id.popup_goods_noview);
		goodsNoView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnCancelListener != null){
					mOnCancelListener.OnCancel(v);
				}
			}
		});

		filterReset = (TextView) findViewById(R.id.filter_reset);
		filterSure = (TextView) findViewById(R.id.filter_sure);
		filterReset.setOnClickListener(this);
		filterSure.setOnClickListener(this);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
	}

	public void setAdapter(){
		gvPublishAdapter = new GvPublishAdapter(mContext, publishes);
		gvPublish.setAdapter(gvPublishAdapter);
		gvPublish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//设置当前选中的位置的状态为非。
				publishes.get(position).setChecked(!publishes.get(position).isChecked());
				gvPublishAdapter.notifyData(publishes);
			}
		});

		gvAuthorAdapter = new GvScreenAdapter(mContext, authors);
		gvAuthor.setAdapter(gvAuthorAdapter);
		gvAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				authors.get(position).setChecked(!authors.get(position).isChecked());
				gvAuthorAdapter.notifyData(authors);
			}
		});

		gvTitleAdapter = new GvScreenAdapter(mContext, titles);
		gvTitle.setAdapter(gvTitleAdapter);
		gvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//设置当前选中的位置的状态为非。
				titles.get(position).setChecked(!titles.get(position).isChecked());
				gvTitleAdapter.notifyData(titles);
			}
		});


	}

	public void notifyData(List<ScreenPublish> publishes, List<ScreenAll> authors, List<ScreenAll> titles){
		this.publishes = publishes;
		gvPublishAdapter.notifyData(publishes);
		this.authors = authors;
		gvAuthorAdapter.notifyData(authors);
		this.titles = titles;
		gvTitleAdapter.notifyData(titles);
	}


	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public void setOnCancelListener (OnCancelListener onCancelListener){
		mOnCancelListener = onCancelListener;
	}

	public void setOnFilterSureListener (OnFilterSureListener onFilterSureListener){
		mOnFilterSureListener = onFilterSureListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.filter_reset:
				etLow.setText("");
				etHigh.setText("");
				for(ScreenPublish screenPublish : publishes){
					screenPublish.setChecked(false);
				}
				gvPublishAdapter.notifyDataSetChanged();
				for(ScreenAll screenAll : authors){
					screenAll.setChecked(false);
				}
				gvAuthorAdapter.notifyDataSetChanged();
				for(ScreenAll screenAll : titles){
					screenAll.setChecked(false);
				}
				gvTitleAdapter.notifyDataSetChanged();
				break;
			case R.id.filter_sure:
				int low = 0;
				int high = 0;
				String sLow = etLow.getText().toString();
				String sHigh = etHigh.getText().toString();
				if(sLow.length() > 0){
					low = Integer.parseInt(sLow);
				}
				if(sHigh.length() > 0) {
					high = Integer.parseInt(sHigh);
				}
				if(low != 0 && high != 0){
					if(low > high){
						int c = high;
						high = low;
						low = c;
					}
				}
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(low);
				jsonArray.put(high);
				String price = jsonArray.toString();//[12,22]

				List<String>publicshId = new ArrayList<>();
                String publish = "";
                    for (ScreenPublish screenPublish : publishes) {
                        if (screenPublish.isChecked()) {
                            publicshId.add(screenPublish.getPl_id());
                        }
                    }
                    if(publicshId.size() > 0){
                        publish = GsonUtil.GsonString(publicshId);//["1","2"]
                    }


				List<String>authorName = new ArrayList<>();
                String author = "";
                for(ScreenAll screenAll : authors){
					if(screenAll.isChecked()){
						authorName.add(screenAll.getName());
					}
				}
				if(authorName.size() > 0) {
                    GsonUtil.GsonString(authorName);//["me","十万个冷笑话"]
                }

				List<String>titleName = new ArrayList<>();
                String title = "";
				for(ScreenAll screenAll : titles){
					if(screenAll.isChecked()){
						titleName.add(screenAll.getName());
					}
				}
				if(titleName.size() > 0) {
                    title = GsonUtil.GsonString(titleName);//["拥抱明天","村暖好看"]
                }

                if(low == 0 && high == 0 && publish.equals("") && author.equals("") && title.equals("")) {
					mOnFilterSureListener.OnFilterSureNull();
                }else{
                    if (mOnFilterSureListener != null) {
                        mOnFilterSureListener.OnFilterSure(publish, author, title, price);
                    }
                }
				break;
		}
	}

	public interface OnSelectListener {
		public void getValue(String distance, String showText);
	}

	public interface OnCancelListener{
		public void OnCancel(View v);
	}

	public interface OnFilterSureListener{
		public void OnFilterSure(String publish, String author, String title, String price);
        public void OnFilterSureNull();
	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}


}
