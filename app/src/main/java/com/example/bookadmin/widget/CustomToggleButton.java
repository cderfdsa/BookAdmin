package com.example.bookadmin.widget;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookadmin.R;

/**
 * Created by Administrator on 2017-05-22.
 */

public class CustomToggleButton extends RelativeLayout {

    private TextView tvTitle;
    private ImageView ivPressBottomLine;
    private View rlCusToggle, verticalDivider, vBottomDivider;
    private boolean checked;
    private OnToggleListener onToggleListener;
    private Context context;

    private int STATE = STATE_NOMAL;
    public static int STATE_NOMAL = 1;
    public static int STATE_CLICK = 2;
    public static int STATE_NO = 3;

    public CustomToggleButton(Context context) {
        super(context);
        this.context = context;
    }

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_toggle_button, this);
        tvTitle = (TextView) findViewById(R.id.title);
        ivPressBottomLine = (ImageView) findViewById(R.id.ivPressBottomLine);
        verticalDivider = findViewById(R.id.verticalDivider);
        rlCusToggle = findViewById(R.id.rlCusToggle);
        vBottomDivider = findViewById(R.id.v_tab_bottom_divider);
        rlCusToggle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(STATE == STATE_NOMAL){

                    if (checked) {
                        checked = false;
                        setChecked(checked);
                    } else {
                        checked = true;
                        setChecked(checked);
                    }
                    if(onToggleListener != null) {
                        onToggleListener.onClick(CustomToggleButton.this);
                    }
                }else if(STATE == STATE_CLICK){
                    if (checked) {
                        checked = false;
                        setStateChecked(checked);
                    } else {
                        checked = true;
                        setStateChecked(checked);
                    }
                    if(onToggleListener != null) {
                        onToggleListener.onClick(CustomToggleButton.this);
                    }
                }else if(STATE == STATE_NO){
//                    if (checked) {
//                        checked = false;
//                        setStateNoChecked(checked);
//                    } else {
//                        checked = true;
//                        setStateNoChecked(checked);
//                    }
                    if(onToggleListener != null) {
                        onToggleListener.onClick(CustomToggleButton.this);
                    }
                }
            }
        });
    }

    public void setSTATE(int STATE) {
        this.STATE = STATE;
    }

    public void setText(String lable) {
        if (tvTitle != null) {
            this.tvTitle.setText(lable);
        }
    }

    public String getText() {
        if (tvTitle != null) {
            return tvTitle.getText().toString();
        }
        return null;
    }

    public void setChecked(boolean isChecked) {
        this.checked = isChecked;
        if (tvTitle == null || ivPressBottomLine == null) {
            return;
        }
        if (checked) {
            tvTitle.setTextColor(Color.rgb(255, 114, 1));
            Drawable drawable = context.getResources().getDrawable(
                    R.drawable.icon_up);
            tvTitle.setCompoundDrawablePadding(3);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
            ivPressBottomLine.setVisibility(View.VISIBLE);
            vBottomDivider.setVisibility(View.GONE);
        } else {
            tvTitle.setTextColor(Color.rgb(72, 77, 81));
            Drawable drawable = context.getResources().getDrawable(
                    R.drawable.icon_down);
            tvTitle.setCompoundDrawablePadding(3);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
            ivPressBottomLine.setVisibility(View.GONE);
            vBottomDivider.setVisibility(View.VISIBLE);
        }
    }

    public void setStateChecked(boolean isChecked) {
        this.checked = isChecked;
        if (tvTitle == null || ivPressBottomLine == null) {
            return;
        }
        tvTitle.setTextColor(Color.rgb(255, 114, 1));
        tvTitle.setCompoundDrawablePadding(3);
        ivPressBottomLine.setVisibility(View.VISIBLE);
        vBottomDivider.setVisibility(View.GONE);
        if (checked) {
            Drawable drawable = context.getResources().getDrawable(
                    R.drawable.icon_up);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(
                    R.drawable.icon_down);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
        }
    }

//    public void setStateNoChecked(boolean isChecked) {
//        this.checked = isChecked;
//        if (tvTitle == null || ivPressBottomLine == null) {
//            return;
//        }
//        tvTitle.setTextColor(Color.rgb(255, 114, 1));
//        tvTitle.setCompoundDrawablePadding(3);
//        ivPressBottomLine.setVisibility(View.VISIBLE);
//        vBottomDivider.setVisibility(View.GONE);
//        Drawable drawable = context.getResources().getDrawable(
//                R.drawable.icon_up);
//        if (checked) {
//            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
//                    drawable, null);
//        } else {
//            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
//                    drawable, null);
//        }
//    }
//    public void setScreenHas(){
//        tvTitle.setTextColor(Color.rgb(255, 114, 1));
//        Drawable drawable = context.getResources().getDrawable(
//                R.drawable.icon_up);
//        tvTitle.setCompoundDrawablePadding(3);
//        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
//                drawable, null);
//        ivPressBottomLine.setVisibility(View.VISIBLE);
//        vBottomDivider.setVisibility(View.GONE);
//    }
//
//    public void setScreenNo(){
//        tvTitle.setTextColor(Color.rgb(72, 77, 81));
//        Drawable drawable = context.getResources().getDrawable(
//                R.drawable.icon_down);
//        tvTitle.setCompoundDrawablePadding(3);
//        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
//                drawable, null);
//        ivPressBottomLine.setVisibility(View.GONE);
//        vBottomDivider.setVisibility(View.VISIBLE);
//    }

    public boolean isChecked() {
        return checked;
    }

    public void hideVerticalDivider() {
        if (verticalDivider != null) {
            verticalDivider.setVisibility(View.GONE);
        }
    }

    public void showVerticalDivider() {
        if (verticalDivider != null) {
            verticalDivider.setVisibility(View.VISIBLE);
        }
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        this.onToggleListener = onToggleListener;
    }

    public interface OnToggleListener {
        void onClick(View v);
    }
}
