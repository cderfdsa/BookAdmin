package com.example.bookadmin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.DetailBook;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.CartProvider;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.CustomDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-11.
 */

public class BookAttrDetailActivity extends IMBaseActivity {

    private static final String TAG = "BookAttrDetailActivity";

    private String bsId;

    private NestedScrollView nsv;
    private Toolbar mToolBar;
    private SimpleDraweeView simpleDraweeView;
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvAuthor;
    private TextView tvPlname;
    private WebView mWebView;
    private TextView btnRight;

    private SpotsDialog mDialog;

    private CartProvider cartProvider;

    private DetailBook book;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Serializable serializable = getIntent().getSerializableExtra(Contants.BS_ID);
        if(serializable ==null)
            this.finish();

        bsId = (String) serializable;

        nsv = (NestedScrollView) findViewById(R.id.nsv);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.drawee_view);
        tvTitle = (TextView) findViewById(R.id.bs_title);
        tvPrice = (TextView) findViewById(R.id.bs_price);
        tvAuthor = (TextView) findViewById(R.id.bs_author);
        tvPlname = (TextView) findViewById(R.id.pl_name);
        mWebView = (WebView) findViewById(R.id.web_bs_remake);
        btnRight = (TextView) findViewById(R.id.btnRight);

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WC());

        mDialog = new SpotsDialog(this,"loading....");
        mDialog.show();

        initToolBar();
        initData();

        cartProvider = new CartProvider(this);
        nsv.setVisibility(View.GONE);
        mToolBar.setVisibility(View.GONE);
    }

    private void initToolBar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//借阅
                if(book != null) {
                    int num = book.getNumber();

                    int currnum = 0;
//                    List<ShoppingCart>carts =  cartProvider.getAll();
//                    if(carts != null && carts.size() > 0) {
//                        for (ShoppingCart cart : carts) {
//                            String cartBs_id = cart.getBs_id();
//                            if (cartBs_id.equals(book.getBs_id())) {
//                                currnum = cart.getCount();
//                            }
//                        }
//                    }
                    if(num - currnum > 0) {
                        cartProvider.put(book);
//                        ToastUtils.showToastInCenter(BookAttrDetailActivity.this, 2, "商品已添加至购物车！", Toast.LENGTH_SHORT);
                        CustomDialog.Builder builder = new CustomDialog.Builder(BookAttrDetailActivity.this);
                        builder.setMessage("商品已添加至购物车！");
                        builder.setPositiveButton("继续选择", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                        builder.setNegativeButton("去借书", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(BookAttrDetailActivity.this, MainActivity.class);
                                intent.putExtra("bookattr", "1");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else{
                        ToastUtils.showToastInCenter(BookAttrDetailActivity.this, 1, "没有图书！", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    private void initData() {
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.BOOK)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("address", Contants.ADDRESS)
                .addParams("books", bsId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");
                        IniterUtils.noIntent(BookAttrDetailActivity.this, nsv, mDialog);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if(code == 200){
                                book = GsonUtil.GsonToBean(data, DetailBook.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mToolBar.setVisibility(View.VISIBLE);
                                        nsv.setVisibility(View.VISIBLE);
                                        updataUI();
                                    }
                                });
                            }else if(code == 520){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nsv.setVisibility(View.VISIBLE);
                                        ToastUtils.showToastInCenter(BookAttrDetailActivity.this, 1, "地区id不存在", Toast.LENGTH_SHORT);
                                    }
                                });
                            }else if(code == 521){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nsv.setVisibility(View.VISIBLE);
                                        ToastUtils.showToastInCenter(BookAttrDetailActivity.this, 1, "不存在数据", Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void updataUI(){
        mToolBar.setTitle(book.getBs_name());
        tvAuthor.setText(book.getBs_author());
        tvPrice.setText("￥ " + String.valueOf(book.getBs_price()) + " 元");
        tvPlname.setText(book.getPl_name());
        if(!book.getBs_title().equals("0")) {
            tvTitle.setText(book.getBs_title());
        }else{
            tvTitle.setText("暂无主题");
        }
        simpleDraweeView.setAspectRatio(Contants.aspectRatio);
        simpleDraweeView.setImageURI(Uri.parse(Contants.API.IP_UTL + book.getBs_photo()));

        String postData = "token=" + BookApplication.getInstance().getKey()
                +"&number=" + String.valueOf(BookApplication.getInstance().getNumber())
                +"&books=" + book.getBs_id();
        mWebView.postUrl(Contants.API.BASE_URL + Contants.API.BOOK_HTML, postData.getBytes());
    }


    class  WC extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mDialog !=null && mDialog.isShowing())
                mDialog.dismiss();

        }
    }

}
