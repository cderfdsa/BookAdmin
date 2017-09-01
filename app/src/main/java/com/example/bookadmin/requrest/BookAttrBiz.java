package com.example.bookadmin.requrest;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.BookAttr;
import com.example.bookadmin.bean.FirstTypeParam;
import com.example.bookadmin.fragment.ClassificationFragment;
import com.example.bookadmin.interf.OnPageListener;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.expant.ExpandTabView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zt on 2017/5/6.
 */

public class BookAttrBiz<T> {

    public static final String TAG = "BookAttrBiz";

    //    private static Builder builder;
    private Builder builder;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFERSH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private SpotsDialog spotsDialog;

    protected BookAttrBiz(Builder builder) {
        this.builder = builder;
        spotsDialog = new SpotsDialog(builder.mActivity, "拼命加载中...");
        initRefreshLayout();

    }

    private void initRefreshLayout() {
        if(builder.type == 1) {
            builder.btnAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.mRefreshLayout.setVisibility(View.VISIBLE);
                    builder.relativeLayout.setVisibility(View.GONE);
                    builder.expandTabView.setVisibility(View.VISIBLE);
                    request();
                    builder.classFragment.loadExpandtab();
                }
            });
        }
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);

        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.canLoadMore = true;
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.canLoadMore)
                    loadMore();
                else {
//                    ToastUtils.showShortToast(builder.mActivity, "无更多数据");
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 获取builder
     *
     * @return
     */
//    public Builder newBuilder(){
//
//        builder = new Builder();
//        return builder;
//    }
    public void request() {
        requestData();
    }

    private void showDialog() {
        spotsDialog.show();
    }

    private void dismissDialog() {
        spotsDialog.dismiss();
    }

    /**
     * 类builder
     */
    public static class Builder {

        private static Builder mInstance;

        public static Builder getInstance() {
            return mInstance;
        }

        public Builder(Activity activity) {
            mInstance = this;
            this.mActivity = activity;
        }

        private Activity mActivity;
        private ClassificationFragment classFragment;
        private FirstTypeParam firstTypeParam;
        private MaterialRefreshLayout mRefreshLayout;
        private RelativeLayout relativeLayout;
        private Button btnAgain;
        private boolean canLoadMore;
        private OnPageListener onPageListener;
        private ExpandTabView expandTabView;
        private int type = -1;

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setFragment(ClassificationFragment classFragment) {
            this.classFragment = classFragment;
            return this;
        }

        public Builder setExpandTabView(ExpandTabView expandTabView) {
            this.expandTabView = expandTabView;
            return this;
        }

        public Builder setFirstTypeParam(FirstTypeParam firstTypeParam) {
            this.firstTypeParam = firstTypeParam;
            return this;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;
            return this;
        }

        public Builder setRelativeLayout(RelativeLayout relativeLayout) {
            this.relativeLayout = relativeLayout;
            return this;
        }

        public Builder setBtnAgain(Button btnAgain) {
            this.btnAgain = btnAgain;
            return this;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;
            return this;
        }

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return this;
        }


        public BookAttrBiz build() {
            valid();
            return new BookAttrBiz(this);
        }

        private void valid() {
            if (this.mActivity == null)
                throw new RuntimeException("content can't be null");
            if (this.firstTypeParam == null)
                throw new RuntimeException("firstTypeParam can't be  null");
            if (this.mRefreshLayout == null)
                throw new RuntimeException("MaterialRefreshLayout can't be  null");
            if (this.type == -1)
                throw new RuntimeException("MaterialRefreshLayout can't be  null");
            if (type == 1) {
                if (this.relativeLayout == null)
                    throw new RuntimeException("relativeLayout can't be  null");
                if (this.btnAgain == null)
                    throw new RuntimeException("btnAgain can't be  null");
                if (this.expandTabView == null)
                    throw new RuntimeException("expandTabView can't be  null");
                if (this.classFragment == null)
                    throw new RuntimeException("classFragment can't be  null");
            }

        }
    }

    /**
     * 刷新
     */
    private void refresh() {
        state = STATE_REFERSH;
        builder.firstTypeParam.setPage(1);
        requestData();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        state = STATE_MORE;
        int currPage = builder.firstTypeParam.getPage();
        builder.firstTypeParam.setPage(currPage + 1);
        requestData();
    }


    /**
     * 请求数据
     * address：书库地址id
     * booktype：图书分类（nll）s_id
     * ordertype：排序分类（1）
     * page：页数（1）
     * search：搜索（null）书名、作者
     * Activity activity, final ClassificationFragment fragment,
     */
    public void requestData() {
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.RECOMMEND)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("address", builder.firstTypeParam.getAddress())
                .addParams("booktype", builder.firstTypeParam.getBooktype())
                .addParams("ordertype", builder.firstTypeParam.getOrdertype())
                .addParams("page", String.valueOf(builder.firstTypeParam.getPage()))
                .addParams("search", builder.firstTypeParam.getSearch())
                .addParams("publish", builder.firstTypeParam.getPublish())
                .addParams("author", builder.firstTypeParam.getAuthor())
                .addParams("title", builder.firstTypeParam.getTitle())
                .addParams("price", builder.firstTypeParam.getPrices())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        builder.mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error();
                                IniterUtils.noIntent(builder.mActivity, null, null);

                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if (code == 200) {
                                final List<BookAttr> numbookAttrs = GsonUtil.GsonToArrayList(data, BookAttr.class);
                                final List<BookAttr> bookAttrs = new ArrayList<BookAttr>();
                                for (BookAttr bookAttr : numbookAttrs) {
                                    if (bookAttr.getNumber() > 0) {
//                                        LogUtils.i("图书的个数：" + bookAttr.getNumber());
                                        bookAttrs.add(bookAttr);
                                    }
                                }
                                builder.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        if (bookAttrs == null || bookAttrs.size() == 0) {
                                            ToastUtils.showToastInCenter(builder.mActivity, 1, "加载不到数据", Toast.LENGTH_SHORT);
                                            builder.canLoadMore = false;
                                        } else {
                                            success(bookAttrs);
                                        }
                                    }
                                });

                            } else if (code == 521) {
                                builder.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        noData();
                                    }
                                });
                            } else {

                                error();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void noData() {
//        ToastUtils.showShortToast(builder.mActivity, "没有数据");
        if (STATE_NORMAL == state) {
            if (builder.onPageListener != null) {
                builder.onPageListener.load(null, builder.firstTypeParam);
            }
        } else if (STATE_REFERSH == state) {
            builder.mRefreshLayout.finishRefresh();
            if (builder.onPageListener != null) {
                builder.onPageListener.refresh(null, builder.firstTypeParam);
            }
        } else if (STATE_MORE == state) {
            builder.canLoadMore = false;
            builder.mRefreshLayout.finishRefreshLoadMore();
            if (builder.onPageListener != null) {
                builder.onPageListener.loadMore(null, builder.firstTypeParam);
            }
        }
    }

    private void error() {
        dismissDialog();
        if (STATE_REFERSH == state) {
            builder.mRefreshLayout.finishRefresh();
        } else if (STATE_MORE == state) {
            builder.canLoadMore = false;
            builder.mRefreshLayout.finishRefreshLoadMore();
        }

        if (builder.type == 1) {
            builder.mRefreshLayout.setVisibility(View.GONE);
            builder.relativeLayout.setVisibility(View.VISIBLE);
            builder.expandTabView.setVisibility(View.GONE);
        }
    }

    private void success(List<BookAttr> bookAttrs) {
        if (STATE_NORMAL == state) {
            if (builder.onPageListener != null) {
                builder.onPageListener.load(bookAttrs, builder.firstTypeParam);
            }
        } else if (STATE_REFERSH == state) {
            builder.mRefreshLayout.finishRefresh();
            if (builder.onPageListener != null) {
                builder.onPageListener.refresh(bookAttrs, builder.firstTypeParam);
            }
        } else if (STATE_MORE == state) {
            builder.mRefreshLayout.finishRefreshLoadMore();
            if (builder.onPageListener != null) {
                builder.onPageListener.loadMore(bookAttrs, builder.firstTypeParam);
            }
        }
    }


}
