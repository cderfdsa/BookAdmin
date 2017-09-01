package com.example.bookadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.im.group.GroupEvent;
import com.example.bookadmin.im.group.GroupInfo;
import com.example.bookadmin.im.group.GroupManagerPresenter;
import com.example.bookadmin.im.group.GroupProfile;
import com.example.bookadmin.im.group.ProfileSummary;
import com.example.bookadmin.im.group.ProfileSummaryAdapter;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.qcloud.ui.TemplateTitle;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017-06-22.
 */

public class GroupListActivity extends IMBaseActivity  implements Observer, View.OnClickListener {


    private ProfileSummaryAdapter adapter;
    private ListView listView;
    private String type;
    private List<ProfileSummary> list;
    private final int CREATE_GROUP_CODE = 100;

    private TextView btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
        type = getIntent().getStringExtra("type");
        listView =(ListView) findViewById(R.id.group_list);
        list = GroupInfo.getInstance().getGroupListByType(type);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(GroupListActivity.this);
            }
        });
        GroupEvent.getInstance().addObserver(this);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        GroupEvent.getInstance().deleteObserver(this);
    }


    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GroupEvent){
            if (data instanceof GroupEvent.NotifyCmd){
                GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
                switch (cmd.type){
                    case DEL:
                        delGroup((String) cmd.data);
                        break;
                    case ADD:
                        addGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                    case UPDATE:
                        updateGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                }
            }
        }
    }

    private void delGroup(String groupId){
        Iterator<ProfileSummary> it = list.iterator();
        while (it.hasNext()){
            ProfileSummary item = it.next();
            if (item.getIdentify().equals(groupId)){
                it.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }


    private void addGroup(TIMGroupCacheInfo info){
        if (info!=null && info.getGroupInfo().getGroupType().equals(type)){
            GroupProfile profile = new GroupProfile(info);
            list.add(profile);
            adapter.notifyDataSetChanged();
        }

    }

    private void updateGroup(TIMGroupCacheInfo info){
        delGroup(info.getGroupInfo().getGroupId());
        addGroup(info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRight:
                createGroup();
                break;
        }
    }

    private void createGroup(){

        GroupManagerPresenter.createGroup(UserInfo.getInstance().getNickname(),
                type,
                null,
                new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int i, String s) {
                        if (i == 80001){
                            Toast.makeText(GroupListActivity.this, getString(R.string.create_group_fail_because_wording), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(GroupListActivity.this, getString(R.string.create_group_fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(GroupListActivity.this, getString(R.string.create_group_succeed), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
