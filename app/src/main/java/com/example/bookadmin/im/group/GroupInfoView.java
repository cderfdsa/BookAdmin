package com.example.bookadmin.im.group;

import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

import java.util.List;

/**
 * 群资料接口
 * Created by Administrator on 2017-06-22.
 */

public interface GroupInfoView {

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    void showGroupInfo(List<TIMGroupDetailInfo> groupInfos);


}
