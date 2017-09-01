package com.example.bookadmin.tools.deserializer;

import com.example.bookadmin.bean.DetailExpress;
import com.example.bookadmin.bean.OrderState;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017-05-25.
 */

public class DetailDeserializerExpress implements JsonDeserializer<DetailExpress> {

    @Override
    public DetailExpress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        OrderState e1 = null;
        JsonElement eElement1 = jsonObject.get("1");//订单id
        if (eElement1 != null && !eElement1.isJsonNull()) {
            e1 = GsonUtil.GsonToBean(eElement1.toString(), OrderState.class);
        }

        OrderState e2 = null;
        JsonElement eElement2 = jsonObject.get("2");//订单id
        if (eElement2 != null && !eElement2.isJsonNull()) {
            e2 = GsonUtil.GsonToBean(eElement2.toString(), OrderState.class);
        }

        OrderState e3 = null;
        JsonElement eElement3 = jsonObject.get("3");//订单id
        if (eElement3 != null && !eElement3.isJsonNull()) {
            e3 = GsonUtil.GsonToBean(eElement3.toString(), OrderState.class);
        }

        OrderState e4 = null;
        JsonElement eElement4 = jsonObject.get("4");//订单id
        if (eElement4 != null && !eElement4.isJsonNull()) {
            e4 = GsonUtil.GsonToBean(eElement4.toString(), OrderState.class);
        }

        OrderState e5 = null;
        JsonElement eElement5 = jsonObject.get("5");//订单id
        if (eElement5 != null && !eElement5.isJsonNull()) {
            e5 = GsonUtil.GsonToBean(eElement5.toString(), OrderState.class);
        }

        DetailExpress detailExpress = new DetailExpress();
        detailExpress.setOrderState1(e1);
        detailExpress.setOrderState2(e2);
        detailExpress.setOrderState3(e3);
        detailExpress.setOrderState4(e4);
        detailExpress.setOrderState5(e5);
        return detailExpress;
    }
}
