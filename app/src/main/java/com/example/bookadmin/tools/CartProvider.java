package com.example.bookadmin.tools;

import android.content.Context;
import android.util.SparseArray;

import com.example.bookadmin.bean.DetailBook;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-11.
 */

public class CartProvider {

    public static final String CART_JSON = "cart_json";

    private SparseArray<ShoppingCart> datas = null;

    private Context mContext;

    public CartProvider(Context context) {

        mContext = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }

    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(Integer.valueOf(cart.getBs_id()));
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(Integer.valueOf(cart.getBs_id()), temp);
        commit();
    }

    public void put(DetailBook book) {
        ShoppingCart cart = convertData(book);
        put(cart);
    }

    public void update(ShoppingCart cart) {
        datas.put(Integer.valueOf(cart.getBs_id()), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete(Integer.valueOf(cart.getBs_id()));
        commit();
    }


    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }


    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, GsonUtil.GsonString(carts));
    }


    private List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }


    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();

        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(Integer.valueOf(cart.getBs_id()), cart);
            }
        }
    }

    private List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = GsonUtil.GsonToArrayList(json, ShoppingCart.class);

        }
        return carts;
    }

    public ShoppingCart convertData(DetailBook item) {

        ShoppingCart cart = new ShoppingCart();

        cart.setBl_library(item.getBl_library());//	库房id
        cart.setBl_books(item.getBl_books());//	书名id
        cart.setBl_nice(item.getBl_nice());//是否推荐
        cart.setBs_id(item.getBs_id());//图书编号
        cart.setBs_name(item.getBs_name());//图书名字
        cart.setBs_price(item.getBs_price());//价格
        cart.setBs_author(item.getBs_author());//	作者名称
        cart.setBs_typeid(item.getBs_typeid());//类型ID
        cart.setBs_publish(item.getBs_publish());//	出版社id
        cart.setBs_remake(item.getBs_remake());//	图书介绍
        cart.setBs_photo(item.getBs_photo());//封面图片
        cart.setBs_isnot(item.getBs_isnot());//是否生效（生效1）
        cart.setBs_isdel(item.getBs_isdel());//是否删除
        cart.setBs_intime(item.getBs_intime());//录入时间
        cart.setBs_uptime(item.getBs_uptime());//更新时间
        cart.setBs_admin(item.getBs_admin());//操作者
        cart.setBs_title(item.getBs_title());//主题
        cart.setBs_order(item.getBs_order());//顺序
        cart.setPl_id(item.getPl_id());//出版社id
        cart.setPl_name(item.getPl_name());//出版社名称
        cart.setPl_remake(item.getPl_remake());//出版社备注
        cart.setPl_isdel(item.getPl_isdel());//是否删除
        cart.setPl_intime(item.getPl_intime());//录入时间
        cart.setPl_uptime(item.getPl_uptime());//更新时间
        cart.setPl_admin(item.getPl_admin());//管理id
        cart.setBs_evaluate(item.getBs_evaluate());//评分

        return cart;
    }

}
