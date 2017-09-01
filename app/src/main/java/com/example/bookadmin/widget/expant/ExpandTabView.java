package com.example.bookadmin.widget.expant;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.tools.deserializer.UIHelper;
import com.example.bookadmin.widget.CustomToggleButton;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-09.
 */

public class ExpandTabView extends LinearLayout implements PopupWindow.OnDismissListener {

//    private CustomToggleButton tbCla;
//    private CustomToggleButton tbSort;
//    private CustomToggleButton tbScreen;

    private CustomToggleButton selectedButton;
    private int selectPosition;

    private Context mContext;

    private LayoutInflater inflater;

    private ArrayList<CustomToggleButton> mToggleButton = new ArrayList<CustomToggleButton>();

    private ArrayList<RelativeLayout> mRelativeArray = new ArrayList<RelativeLayout>();
    private ArrayList<String> mTextArray = new ArrayList<String>();

//    private final int SMALL = 0;

    private PopupWindow popupWindow;

    private PopupWindow filterPopupWindow;

//    private OnButtonClickListener mOnButtonClickListener;

//    private CustomToggleButton customToggleButton;

    public ExpandTabView(Context context) {
        super(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 设置tabitem的个数和初始值
     *
     * @param textArray
     * @param viewArray
     */
    public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
        if (mContext == null) {
            return;
        }
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTextArray = textArray;

        int maxHeight = (int) (Contants.displayHeight * 0.7);
        RelativeLayout.LayoutParams sevenParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
        sevenParams.leftMargin = 10;
        sevenParams.rightMargin = 10;

        RelativeLayout.LayoutParams wholeParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        createCla(0, viewArray, sevenParams);
        createSort(1, viewArray, sevenParams);
//        createPrice(2, viewArray);
        createScreen(2, viewArray, wholeParams);
    }

    private void createCla(int type, ArrayList<View> viewArray, RelativeLayout.LayoutParams sevenParams){
        RelativeLayout r = new RelativeLayout(mContext);
        View view = viewArray.get(type);
        r.addView(view, sevenParams);
        r.setBackgroundColor(mContext.getResources().getColor(R.color.app_custom_popup_bottom_bg));

        final CustomToggleButton tButton = (CustomToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
        tButton.setSTATE(CustomToggleButton.STATE_NOMAL);
        tButton.setTag(type);
        tButton.setText(mTextArray.get(type));
        addView(tButton);

        mRelativeArray.add(r);
        mToggleButton.add(tButton);

        r.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onPressBack();

            }
        });
        tButton.setOnToggleListener(new CustomToggleButton.OnToggleListener() {
            @Override
            public void onClick(View v) {
                CustomToggleButton tButton = (CustomToggleButton) v;
                if (selectedButton != null && selectedButton != tButton) {
                    selectedButton.setChecked(false);
                    mToggleButton.set(selectPosition, selectedButton);
                }
                selectedButton = tButton;
                selectPosition = (Integer) selectedButton.getTag();
                startAnimation();
            }
        });
    }

    private void createSort(int type, ArrayList<View> viewArray, RelativeLayout.LayoutParams sevenParams){
        RelativeLayout r = new RelativeLayout(mContext);
        View view = viewArray.get(type);
        r.addView(view, sevenParams);
        r.setBackgroundColor(mContext.getResources().getColor(R.color.app_custom_popup_bottom_bg));

        final CustomToggleButton tButton = (CustomToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
        tButton.setSTATE(CustomToggleButton.STATE_NOMAL);
        tButton.setTag(type);
        tButton.setText(mTextArray.get(type));
        addView(tButton);

        mRelativeArray.add(r);
        mToggleButton.add(tButton);

        r.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onPressBack();

            }
        });
        tButton.setOnToggleListener(new CustomToggleButton.OnToggleListener() {
            @Override
            public void onClick(View v) {
                CustomToggleButton tButton = (CustomToggleButton) v;
                if (selectedButton != null && selectedButton != tButton) {
                    selectedButton.setChecked(false);
                    mToggleButton.set(selectPosition, selectedButton);
                }
                selectedButton = tButton;
                selectPosition = (Integer) selectedButton.getTag();
                startAnimation();
            }
        });
    }

    private void createPrice(int type, ArrayList<View> viewArray){
        final CustomToggleButton tButton = (CustomToggleButton) inflater.inflate(R.layout.toggle_button, this, false);

        tButton.setSTATE(CustomToggleButton.STATE_CLICK);
        tButton.setTag(type);
        tButton.setText(mTextArray.get(type));
        addView(tButton);

        mToggleButton.add(tButton);
        mRelativeArray.add(null);
        tButton.setOnToggleListener(new CustomToggleButton.OnToggleListener() {
            @Override
            public void onClick(View v) {
                CustomToggleButton tButton = (CustomToggleButton) v;
                if (selectedButton != null && selectedButton != tButton) {
                    selectedButton.setChecked(false);
                    mToggleButton.set(selectPosition, selectedButton);
                }
                selectedButton = tButton;
                selectPosition = (Integer) selectedButton.getTag();
                if(onPriceSelectListener != null){
                    if (selectedButton.isChecked()) {
                        if(onPriceSelectListener != null) {
                            onPriceSelectListener.getValue("5", "价格正序");
                        }
                    }else{
                        if(onPriceSelectListener != null) {
                            onPriceSelectListener.getValue("10", "价格逆序");
                        }
                    }
                }
            }
        });
    }

    private void createScreen(int type, ArrayList<View> viewArray, RelativeLayout.LayoutParams wholeParams ){
        RelativeLayout r = new RelativeLayout(mContext);

        View view = viewArray.get(type);
        r.addView(view, wholeParams);
        ((ScreenRelativeLayout)view).setOnCancelListener(new ScreenRelativeLayout.OnCancelListener() {
            @Override
            public void OnCancel(View v) {
                if(filterPopupWindow != null && filterPopupWindow.isShowing()) {
                    filterPopupWindow.dismiss();
                }
            }
        });

        final CustomToggleButton tButton = (CustomToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
        tButton.setSTATE(CustomToggleButton.STATE_NOMAL);
        addView(tButton);
        mRelativeArray.add(r);
        mToggleButton.add(tButton);
        tButton.setTag(type);
        tButton.setText(mTextArray.get(type));
        r.setBackgroundColor(mContext.getResources().getColor(R.color.app_custom_popup_bottom_bg));
        r.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onPressBack();

            }
        });
        tButton.setOnToggleListener(new CustomToggleButton.OnToggleListener() {

            @Override
            public void onClick(View v) {
                CustomToggleButton tButton = (CustomToggleButton) v;
                if (selectedButton != null && selectedButton != tButton) {
                    selectedButton.setChecked(false);
                    mToggleButton.set(selectPosition, selectedButton);
                }
                selectedButton = tButton;
                selectPosition = (Integer) selectedButton.getTag();
                startAnimation(3);
//                customToggleButton =tButton;
//                customToggleButton.setScreenHas();

            }
        });

    }


    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String valueText, int position) {
        if (position < mToggleButton.size()) {
            mToggleButton.get(position).setText(valueText);
        }
    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
            return mToggleButton.get(position).getText().toString();
        }
        return "";
    }

    private void startAnimation() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(mRelativeArray.get(selectPosition),
                    LinearLayout.LayoutParams.MATCH_PARENT, Contants.displayHeight / 2, true);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        UIHelper.setPopupWindowTouchModal(popupWindow, false);
        if (selectedButton.isChecked()) {
            if (!popupWindow.isShowing()) {

                showPopup(selectPosition);
            } else {
                popupWindow.setOnDismissListener(this);
                popupWindow.dismiss();
                hideView();
            }
        } else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                hideView();
            }
        }
        filterPopDissmiss();
    }

    private void startAnimation(int type) {
        if (filterPopupWindow == null) {
            filterPopupWindow = new PopupWindow();
            filterPopupWindow.setAnimationStyle(R.style.FilterPopupWindowAnimation);

            filterPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
            filterPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
            ColorDrawable dw = new ColorDrawable(00000000);
            filterPopupWindow.setBackgroundDrawable(dw);
            filterPopupWindow.setFocusable(true);
            filterPopupWindow.setOutsideTouchable(false);
            filterPopupWindow.update();
        }
        UIHelper.setPopupWindowTouchModal(filterPopupWindow, false);
        if (selectedButton.isChecked()) {
            if (!filterPopupWindow.isShowing()) {

                View tView = mRelativeArray.get(selectPosition).getChildAt(0);
                if (tView instanceof ViewBaseAction) {
                    ViewBaseAction f = (ViewBaseAction) tView;
                    f.show();
                }
                if (filterPopupWindow.getContentView() != mRelativeArray.get(selectPosition)) {
                    filterPopupWindow.setContentView(mRelativeArray.get(selectPosition));
                }
                filterPopupWindow.showAsDropDown(this, 0, 0);
            } else {
                filterPopupWindow.setOnDismissListener(this);
                filterPopupWindow.dismiss();
                hideView();
            }
        } else {
            if (filterPopupWindow.isShowing()) {
                filterPopupWindow.dismiss();
                hideView();
            }
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }


    private void showPopup(int position) {
        View tView = mRelativeArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mRelativeArray.get(position)) {
            popupWindow.setContentView(mRelativeArray.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedButton != null) {
                selectedButton.setChecked(false);
            }
            return true;
        } else {
            return false;
        }

    }

    private void hideView() {
        View tView = mRelativeArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

//    public void setTButton(){
//
//        customToggleButton.setScreenNo();
//    }

    public void filterPopDissmiss(){
        if(filterPopupWindow != null) {
            if (filterPopupWindow.isShowing()) {
                filterPopupWindow.dismiss();
//            filterPopupWindow = null;
            }
        }
    }

    @Override
    public void onDismiss() {
        if(popupWindow != null) {
            showPopup(selectPosition);
            popupWindow.setOnDismissListener(null);
        }
    }

//    public void setValue(ArrayList<View> viewArray) {
//        if (mContext == null) {
//            return;
//        }
//        if (viewArray.size() != 3) {
//            return;
//        }
//        for (int i = 0; i < viewArray.size(); i++) {
//            RelativeLayout relativeLayout = new RelativeLayout(mContext);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            relativeLayout.addView(viewArray.get(i), params);
//            mRelativeArray.add(relativeLayout);
//            relativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onPressBack();
//                }
//            });
//            relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
//        }
//    }

    /**
     * 设置tabitem的点击监听事件
     */
//    public void setOnButtonClickListener(OnButtonClickListener l) {
//        mOnButtonClickListener = l;
//    }

    /**
     * 自定义tabitem点击回调接口
     */
    public interface OnButtonClickListener {
        public void onClick(int selectPosition);
    }

    private OnPriceSelectListener onPriceSelectListener;

    public void setOnPriceSelectListener(OnPriceSelectListener onPriceSelectListener) {
        this.onPriceSelectListener = onPriceSelectListener;
    }

    public interface OnPriceSelectListener {
        public void getValue(String distance, String showText);
    }
}
