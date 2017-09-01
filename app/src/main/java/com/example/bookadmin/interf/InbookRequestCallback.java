package com.example.bookadmin.interf;

import android.util.SparseArray;

import com.example.bookadmin.bean.BookTypeBean;
import com.example.bookadmin.bean.InBookBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zt on 2017/5/6.
 */

public interface InbookRequestCallback {
    public void handleInbookQueryResult(List<InBookBean> stayInBookBeens, List<InBookBean> contInBookBeens);
    public void noInbookQueryResult();
}
