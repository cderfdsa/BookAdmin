package com.example.bookadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.adapter.TimeAdapter;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.requrest.OrderGs;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.TimeUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.calendar.NewCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-06-08.
 */

public class ClockActivity extends IMBaseActivity implements DatePicker.OnDateChangedListener, View.OnClickListener {

    private Toolbar toolbar;
    private TextView btnRight;
    //    private DatePicker datePicker;
    private ListView lvTime;
//    private TextView timePeriod;

    private int gs_id;
    private boolean isAlso;

    private DateBean mDateBean;
    private LtTime mLtTime;

    private List<LtTime> mLtTimes = new ArrayList<>();
    private TimeAdapter timeAdapter;

    private NewCalendar mCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        gs_id = getIntent().getIntExtra("gs_id", -1);
        isAlso = getIntent().getBooleanExtra("isAlso", false);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
//        datePicker = (DatePicker) findViewById(R.id.datepicker);
        lvTime = (ListView) findViewById(R.id.lv_time);
//        timePeriod = (TextView) findViewById(R.id.timePeriod);
//        timePeriod.setVisibility(View.GONE);

        setToolBarReplaceActionBar();
        mCalendar= (NewCalendar) findViewById(R.id.calendar);
        mCalendar.setNewCalendarListener(new NewCalendar.NewCalendarListener() {
            @Override
            public void onItemLongPress(Date day) {
                LogUtils.i(day.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long dateMillis = day.getTime();
                String dateTime = sdf.format(day);
                if(mDateBean != null) {
                    mDateBean.setDate(day);
                    mDateBean.setDateMillis(dateMillis);
                    mDateBean.setStrDate(dateTime);
                }
                chooseTime();
            }
        });
//        setDatePicker();

        mDateBean = new DateBean();

        timeAdapter = new TimeAdapter(ClockActivity.this, mLtTimes);
        lvTime.setAdapter(timeAdapter);
        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLtTime = mLtTimes.get(position);
                timeAdapter.setSelectItem(position);
                timeAdapter.notifyDataSetChanged();
//                timePeriod.setVisibility(View.GONE);
//                timePeriod.setText(mLtTime.getLt_starttime() + "  - - -  " + mLtTime.getLt_endtime());
            }
        });
    }

    private void setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private void setDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        datePicker.init(calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH), this);
//        long mindate = System.currentTimeMillis() - 1000L;  //注意：如果不提前一秒的话会报错"java.lang.IllegalArgumentException: fromDate: XXX does not precede toDate: XXX"
//        long maxdate = mindate + 7 * 24 * 3600 * 1000L; //设置DatePicker范围，从今天起之后一周
//        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
//            datePicker.setMinDate(mindate);
//            datePicker.setMaxDate(maxdate);
//        }
//    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date chooseDate = calendar.getTime();
//        long dateMillis = chooseDate.getTime();
//        String dateTime = sdf.format(chooseDate);
//        if(mDateBean != null) {
//            mDateBean.setDate(chooseDate);
//            mDateBean.setDateMillis(dateMillis);
//            mDateBean.setStrDate(dateTime);
//        }
//        chooseTime();
    }


    public void chooseTime() {
        OrderGs.grogshopTime(ClockActivity.this, gs_id, mDateBean, isAlso, new OrderGs.OnLtTimeListener() {
            @Override
            public void OnLtTime(List<LtTime> ltTimes) {

                mLtTimes = ltTimes;
                timeAdapter.setSelectItem(-1);
                timeAdapter.setData(mLtTimes);
//                timePeriod.setVisibility(View.GONE);
                mLtTime = null;
            }

            @Override
            public void OnGropTimeNoTime() {
                mLtTimes.clear();
                timeAdapter.setSelectItem(-1);
                timeAdapter.setData(mLtTimes);
//                timePeriod.setVisibility(View.GONE);
                mLtTime = null;
                boolean isToday = TimeUtils.IsToday(mDateBean);
                if (isToday) {
                    ToastUtils.showToastInCenter(ClockActivity.this, 1, "今天没有合适的时间段，请您另选择日期", Toast.LENGTH_SHORT);
                } else {
                    ToastUtils.showToastInCenter(ClockActivity.this, 1, "此书柜预约已满，请您另选择书柜", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void OnGropError() {
                IniterUtils.noIntent(ClockActivity.this, null, null);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRight:
                if(mDateBean == null || mLtTime == null){
                    ToastUtils.showToastInCenter(ClockActivity.this, 1, "请选择日期时间", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Contants.CLOCK_DATE, mDateBean);
                intent.putExtra(Contants.CLOCK_TIME, mLtTime);
                setResult(Contants.CLOCK_RESULTCODE, intent);
                finish();
                break;
        }
    }
}
