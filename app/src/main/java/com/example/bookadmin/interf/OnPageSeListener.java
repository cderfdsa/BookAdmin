package com.example.bookadmin.interf;

import com.example.bookadmin.bean.FirstTypeParam;

import java.util.List;

/**
 * Created by Administrator on 2017-05-08.
 */

public interface OnPageSeListener<T> {

    void load(List<T> datas);

    void refresh(List<T> datas);

    void loadMore(List<T> datas);
}
