package com.example.bookadmin.widget.expant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.bookadmin.R;
import com.example.bookadmin.adapter.BookTypeAdapter;
import com.example.bookadmin.adapter.TextAdapter;
import com.example.bookadmin.bean.BookTypeBean;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017-05-09.
 */

public class ClaLinerLayout extends LinearLayout implements ViewBaseAction {

    private Context mContext;
    private LayoutInflater inflater;
    //listview
    private ListView earaListView;
    private ListView plateListView;
    //adapter
    private BookTypeAdapter earaListViewAdapter;
    private BookTypeAdapter plateListViewAdapter;
    //position
    private int tEaraPosition = 0;
    private int tBlockPosition = 0;
    //data
    private ArrayList<BookTypeBean> groups = new ArrayList<BookTypeBean>();
    private SparseArray<LinkedList<BookTypeBean>> children = new SparseArray<LinkedList<BookTypeBean>>();

    private OnSelectListener mOnSelectListener;

    private LinkedList<BookTypeBean> childrenItem = new LinkedList<BookTypeBean>();

    private BookTypeBean bookTypeBean;

    public ClaLinerLayout(Context context) {
        super(context);
        init(context);
    }

    public ClaLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_cla_region, this, true);
        earaListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
    }

    public void setAdapter(){
        earaListViewAdapter = new BookTypeAdapter(mContext, groups, R.drawable.choose_item_selected, R.drawable.choose_eara_item_selector);
        earaListViewAdapter.setTextSize(14);
        earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
        earaListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter.setOnItemClickListener(new BookTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < children.size()) {
                    childrenItem.clear();
                    childrenItem.addAll(children.get(position));
                    plateListViewAdapter.setSelectedPosition(0);
                }
            }
        });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));

        //
        plateListViewAdapter = new BookTypeAdapter(mContext, childrenItem, R.drawable.choose_item_right, R.drawable.choose_plate_item_selector);
        plateListViewAdapter.setTextSize(12);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter.setOnItemClickListener(new BookTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bookTypeBean = childrenItem.get(position);
                if (mOnSelectListener != null) {

                    mOnSelectListener.getValue(bookTypeBean);
                }
            }
        });
    }


    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }


    public void notifyData(ArrayList<BookTypeBean> groups, SparseArray<LinkedList<BookTypeBean>> children){
        this.groups = groups;
        this.children = children;
        earaListViewAdapter.notifyData(groups);
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter.notifyData(childrenItem);
    }


    public interface OnSelectListener {
        public void getValue(BookTypeBean bookTypeBean);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

}
