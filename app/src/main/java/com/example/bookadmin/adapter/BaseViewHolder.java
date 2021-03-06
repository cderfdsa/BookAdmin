package com.example.bookadmin.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-05-05.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private SparseArray<View> views;

    private BaseAdapter.OnItemClickListener mOnItemClickListener;
    private BaseAdapter.OnItemLongClickListener onItemLongClickListener;

    private View view;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener onItemClickListener, BaseAdapter.OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.view = itemView;
        this.mOnItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.views = new SparseArray<View>();
    }

    public View getView(){
        return itemView;
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public CheckBox getCheckBox(int viewId) {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }

    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return onItemLongClickListener != null && onItemLongClickListener.onItemLongClick(v, getLayoutPosition());
    }
}
