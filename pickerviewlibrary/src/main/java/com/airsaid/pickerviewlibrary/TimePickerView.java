package com.airsaid.pickerviewlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.airsaid.pickerviewlibrary.widget.BasePickerView;
import com.airsaid.pickerviewlibrary.widget.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {

    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    private Context mContext;

    private View mHeadView;
    private TextView mTxtTitle;
    private WheelTime mWheelTime;
    private Button mBtnSubmit, mBtnCancel;
    private OnTimeSelectListener mTimeSelectListener;

    public TimePickerView(Context context, Type type) {
        super(context);
        mContext = context;
        initView();
        initSelectTime(type);
    }

    /**
     * 初始化 View
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.pickerview_time, contentContainer);
        mHeadView = findViewById(R.id.rlt_head_view);
        mTxtTitle = (TextView) findViewById(R.id.tvTitle);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    /**
     * 初始化默认选中当前时间
     */
    private void initSelectTime(Type type) {
        View timePickerView = findViewById(R.id.timepicker);
        mWheelTime = new WheelTime(timePickerView, type);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Calendar rangeCalendar = Calendar.getInstance();
        setRange(rangeCalendar.get(Calendar.YEAR) - 100, rangeCalendar.get(Calendar.YEAR) + 100);
        mWheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     * @param startYear 开始年份
     * @param endYear 结束年份
     */
    public void setRange(int startYear, int endYear) {
        mWheelTime.setStartYear(startYear);
        mWheelTime.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     * @param date 时间
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mWheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置是否循环滚动
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        mWheelTime.setCyclic(cyclic);
    }

    /**
     * 设置头部背景颜色
     */
    public void setHeadBackgroundColor(int color){
        mHeadView.setBackgroundColor(color);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title){
        mTxtTitle.setText(title);
    }

    /**
     * 设置标题颜色
     */
    public void setTitleColor(int resId){
        mTxtTitle.setTextColor(resId);
    }

    /**
     * 设置标题大小
     */
    public void setTitleSize(float size){
        mTxtTitle.setTextSize(size);
    }

    /**
     * 设置取消文字
     */
    public void setCancelText(String text){
        mBtnCancel.setText(text);
    }

    /**
     * 设置取消文字颜色
     */
    public void setCancelTextColor(int resId){
        mBtnCancel.setTextColor(resId);
    }

    /**
     * 设置取消文字大小
     */
    public void setCancelTextSize(float size){
        mBtnCancel.setTextSize(size);
    }

    /**
     * 设置确认文字
     */
    public void setSubmitText(String text){
        mBtnSubmit.setText(text);
    }

    /**
     * 设置确认文字颜色
     */
    public void setSubmitTextColor(int resId){
        mBtnSubmit.setTextColor(resId);
    }

    /**
     * 设置确认文字大小
     */
    public void setSubmitTextSize(float size){
        mBtnSubmit.setTextSize(size);
    }

    /**
     * 设置滚动文字大小
     */
    public void setTextSize(float size){
        mWheelTime.setTextSize(size);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnSubmit){
            if(mTimeSelectListener != null){
                try {
                    Date date = WheelTime.dateFormat.parse(mWheelTime.getTime());
                    mTimeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        }else if(id == R.id.btnCancel){
            dismiss();
        }
    }

    public class TextSize{
        public static final float BIG       = 6f;
        public static final float DEFAULT   = 5f;
        public static final float SMALL     = 4f;
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mTimeSelectListener = timeSelectListener;
    }
}
