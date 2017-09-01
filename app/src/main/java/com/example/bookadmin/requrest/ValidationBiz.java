package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.CartProvider;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-05-24.
 */

public class ValidationBiz {
    public static final String TAG = "ValidationBiz";
    private Activity activity;

    public ValidationBiz(Activity activity){
        this.activity = activity;
    }
    //借书判断
    public void validationCart(final List<ShoppingCart> carts, final ValidationRequestShopping validationRequestShopping) {
            String jsonresult = jointJson(carts);
            LogUtils.i(jsonresult);
            OkHttpUtils
                    .post()
                    .url(Contants.API.BASE_URL + Contants.API.IS_NUMBER_BOOK)
                    .addParams("token", BookApplication.getInstance().getKey())
                    .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                    .addParams("address", Contants.getLibAddressId())
                    .addParams("books", jsonresult)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onResponse(String response, int id) {
                            JSONObject obj;
                            try {
                                obj = new JSONObject(response);
                                final int code = obj.getInt("code");
                                String value = obj.getString("value");
                                String data = obj.getString("data");
                                if(code == 200){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            validationRequestShopping.shoppingValidationSuccess(carts);
                                        }
                                    });
                                }else if(code == 527){
                                    JSONArray jsonArray = new JSONArray(data);
                                    int len = jsonArray.length();

                                    for(int i = 0; i < len; i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String book = jsonObject.getString("book");
                                        int number = jsonObject.getInt("number");
                                        for(int j = 0; j < carts.size(); j++){
                                            ShoppingCart shoppingCart = carts.get(j);
                                            if(shoppingCart.getBs_id().equals(book)){
                                                CartProvider cartProvider = new CartProvider(activity);
//                                                cartProvider.delete(shoppingCart);
                                                carts.remove(j);
                                                j = j - 1;
//                                                shoppingCart.setIsChecked(true);
//                                                cartProvider.put(shoppingCart);
                                            }
                                        }
                                    }
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            validationRequestShopping.shoppingValidationError(carts);
                                        }
                                    });
                                }

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
    }

    private String jointJson(List<ShoppingCart> carts) {
        StringBuilder jsonresult = new StringBuilder();
        try {
            JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
            for (ShoppingCart cart : carts) {
                JSONObject object = new JSONObject();

                object.put("book", cart.getBs_id());//向pet对象里面添加值
//                object.put("number", cart.getCount());//向pet对象里面添加值
                object.put("number", "1");//向pet对象里面添加值
                jsonarray.put(object);
            }
            jsonresult.append(jsonarray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonresult.toString();
    }


    public interface ValidationRequestShopping {
        void shoppingValidationSuccess(List<ShoppingCart> carts);
        void shoppingValidationError(List<ShoppingCart> carts);
    }

}
