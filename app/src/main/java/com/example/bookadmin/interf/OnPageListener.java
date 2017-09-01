package com.example.bookadmin.interf;

import com.example.bookadmin.bean.FirstTypeParam;

import java.util.List;

/**
 * Created by Administrator on 2017-05-08.
 */

public interface OnPageListener<T> {

    void load(List<T> datas, FirstTypeParam param);

    void refresh(List<T> datas, FirstTypeParam param);

    void loadMore(List<T> datas, FirstTypeParam param);
}
