package com.example.bookadmin.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookadmin.R;
import com.example.bookadmin.tools.utils.LogUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017-06-12.
 */

public class NewCalendar extends LinearLayout {

    private static final String TAG = "liuchen";

    private Context mContext;

    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
    private RelativeLayout calendar_last_month, calendar_next_month;
    private TextView txtDate;
    private GridView grid;
    private Calendar curDate = Calendar.getInstance();
    private String displayFormat;
    private NewCalendarListener listener;

    public static final int COLOR_TX_THIS_MONTH_DAY = Color.parseColor("#aa564b4b"); // 当前月日历数字颜色
    public static final int COLOR_TX_OTHER_MONTH_DAY = Color.parseColor("#ffcccccc"); // 其他月日历数字颜色
    public static final int COLOR_TX_THIS_DAY = Color.parseColor("#ff008000"); // 当天日历数字颜色
    public static final int COLOR_BG_THIS_DAY = Color.parseColor("#ffcccccc"); // 当天日历背景颜色


    private float x1 = 0;
    private float x2 = 0;

    //    private Date minDate = new Date(System.currentTimeMillis() - 1);
//    private Date maxDate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
    private long minTimeMillis;
    private long maxTimeMillis;

    private CalendarAdapter calendarAdapter;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        Log.e(TAG, " initControl called! TWO");
    }

    public NewCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        Log.e(TAG, " initControl called! THREE");
    }

    private void initControl(Context context, AttributeSet attrs) {
        minTimeMillis = System.currentTimeMillis() - 1;
        BigDecimal bigDecimal = new BigDecimal(35).multiply(BigDecimal.valueOf(24 * 60 * 60 * 1000));

        maxTimeMillis = System.currentTimeMillis() + bigDecimal.longValue() + 1;

        bindControl(context);
        bindControlEvent();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NewCalendar);
        try {
            String format = ta.getString(R.styleable.NewCalendar_dateFormat);
            displayFormat = format;
            if (format == null) {
                displayFormat = "MMMM yyyy";
            }
        } finally {
            ta.recycle();
        }
        renderCalendar(null);
    }

    private void bindControlEvent() {
        calendar_last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, -1);
                renderCalendar(null);
            }
        });

        calendar_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, 1);
                renderCalendar(null);
            }
        });
    }

    private void renderCalendar(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        txtDate.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Log.e(TAG, sdf1.format(calendar.getTime()));
        //6.1星期四calendar.get(Calendar.DAY_OF_WEEK)=5
        //
        int preDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.e(TAG, "---" + calendar.get(Calendar.DAY_OF_WEEK));

        //所以向后减去四天
        //然后按着排位
        calendar.add(Calendar.DAY_OF_MONTH, -preDays);
        Log.e(TAG, sdf1.format(calendar.getTime()));
        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendarAdapter = new CalendarAdapter(getContext(), cells);
        if (date != null) {
            calendarAdapter.setCurrPos(date);
        }
        grid.setAdapter(calendarAdapter);
    }

    private void bindControl(Context context) {
        mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.calendar_view, this, true);
        calendar_last_month = (RelativeLayout) view.findViewById(R.id.calendar_last_month);
        calendar_next_month = (RelativeLayout) view.findViewById(R.id.calendar_next_month);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        grid = (GridView) view.findViewById(R.id.calendar_grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                Date date = (Date) parent.getItemAtPosition(position);

                Date currDate = curDate.getTime();

                if (date.getTime() > minTimeMillis && date.getTime() < maxTimeMillis) {
                    if (date.getYear() == currDate.getYear()) {
                        if (date.getMonth() == currDate.getMonth()) {
                            calendarAdapter.setCurrPos(date);
                            calendarAdapter.notifyDataSetChanged();
                        } else if (date.getMonth() < currDate.getMonth()) {//shang
                            curDate.add(Calendar.MONTH, -1);
                            renderCalendar(date);
                        } else if (date.getMonth() > currDate.getMonth()) {
                            curDate.add(Calendar.MONTH, 1);
                            renderCalendar(date);
                        }
                    } else if (date.getYear() < currDate.getYear()) {// shang
                        curDate.add(Calendar.MONTH, -1);
                        renderCalendar(date);
                    } else if (date.getYear() > currDate.getYear()) {
                        curDate.add(Calendar.MONTH, 1);
                        renderCalendar(date);
                    }


                    if (listener != null) {
                        listener.onItemLongPress(date);
                    }
                } else {
                    LogUtils.i("日期选择超过指定范围");
                }
            }
        });
        grid.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    if (x1 - x2 > 50) {//左
                        curDate.add(Calendar.MONTH, 1);
                        renderCalendar(null);
                    } else if (x2 - x1 > 50) {//右
                        curDate.add(Calendar.MONTH, -1);
                        renderCalendar(null);

                    }
                }
                return false;
            }
        });
        Log.e(TAG, " bindControl called! ");
    }

    private class CalendarAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        Date date;

        public CalendarAdapter(Context context, ArrayList<Date> days) {
            super(context, R.layout.calendar_text_day, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Date date = (Date) getItem(position);
            convertView = inflater.inflate(R.layout.calendar_text_day, parent, false);
            CalendarTextView tvDate = (CalendarTextView) convertView.findViewById(R.id.txt_calendar_day);

            int day = date.getDate();
            tvDate.setText(String.valueOf(day));
            long dateTime = date.getTime();
            if (dateTime > minTimeMillis && dateTime < maxTimeMillis) {
                Date currDate = curDate.getTime();

                Calendar calendar = (Calendar) curDate.clone();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                boolean isTheSameMonth = false;
                if (date.getMonth() == currDate.getMonth()) {
                    isTheSameMonth = true;
                }
                if (isTheSameMonth) {
                    tvDate.setTextColor(COLOR_TX_THIS_MONTH_DAY);
                } else {
                    tvDate.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                }

                Date now = new Date();
                if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()) {
                    tvDate.setToday(true);
                }
                if (this.date != null) {
                    if (date.getTime() == this.date.getTime()) {
                        tvDate.setTextColor(Color.WHITE);
                        tvDate.setBackgroundResource(R.drawable.calendar_date_focused);
                    }
                }

            } else {

                tvDate.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
            }
            return convertView;
        }

        public void setCurrPos(Date date) {
            this.date = date;
        }
    }

    public interface NewCalendarListener {
        void onItemLongPress(Date day);
    }

    public void setNewCalendarListener(NewCalendarListener listener) {
        this.listener = listener;
    }
}
