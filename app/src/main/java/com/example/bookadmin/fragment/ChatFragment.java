package com.example.bookadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bookadmin.R;
import com.example.bookadmin.activity.ChatActivity;
import com.example.bookadmin.activity.GroupListActivity;
import com.example.bookadmin.im.group.GroupInfo;
import com.example.bookadmin.im.group.GroupManagerPresenter;
import com.example.bookadmin.tools.utils.LogUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;

/**
 * Created by Administrator on 2017-06-16.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mPublicGroupBtn;
    private LinearLayout mBGroupBtn;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mPublicGroupBtn = (LinearLayout) view.findViewById(R.id.btnPublicGroup);
        mPublicGroupBtn.setOnClickListener(this);
        mBGroupBtn = (LinearLayout) view.findViewById(R.id.btnBGroup);
        mBGroupBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void init() {
//        GroupInfo.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPublicGroup) {
            showGroups(GroupInfo.publicGroup);
        }else if(view.getId() == R.id.btnBGroup){
            final String roomId = "@TGS#bRY76P2EP";
            GroupManagerPresenter.applyJoinGroup(roomId, "reason", new TIMCallBack() {
                @Override
                public void onError(int code, String msg) {
                    LogUtils.i( String.format("join group error code = %d,msg = %s", code, msg));
                }

                @Override
                public void onSuccess() {
                    LogUtils.i( "join group success");
                    ChatActivity.navToChat(getActivity(), roomId, TIMConversationType.Group);
                }
            });
        }
    }

    private void showGroups(String type){
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        intent.putExtra("type", type);
        getActivity().startActivity(intent);
    }

}
