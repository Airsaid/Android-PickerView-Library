package com.airsaid.android_pickerview_library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.View;

import com.airsaid.android_pickerview_library.R;
import com.airsaid.android_pickerview_library.adapter.WheelAdapter;
import com.airsaid.android_pickerview_library.listener.OnLoopGestureListener;
import com.airsaid.android_pickerview_library.model.IPickerViewModel;

/**
 * Created by zhouyou on 2016/12/2.
 * Class desc: 自定义 3D 滚轮 View
 */
public class WheelView extends View{

    // 中心文字偏移值
    private static final float CENTER_CONTENT_OFFSET = 6;

    private Context mContext;

    // 分割线颜色
    private int     mDividerColor   = Color.parseColor("#D5D5D5");
    // 外部滚轮字体颜色
    private int     mColorOut       = Color.parseColor("#A8A8A8");
    // 中心滚轮字体颜色
    private int     mColorCore      = Color.parseColor("#2A2A2A");
    // 滚轮字体大小
    private float   mTextSize       = dp2px(18);
    // 是否循环
    private boolean mIsLoop         = true;
    // 滚动总高度 y 值
    private int     mTotalScrollY   = 0;
    // 默认选中位置
    private int     mInitPosition   = -1;
    // 条目间距
    private float   mItemLineSpacing= 1.4f;
    // 条目高度
    private float   mItemHeight;
    // 显示的条目数
    private double  mItemsVisible   = 11;
    // 半圆周长
    private int mHalfCircumference;
    // 控件高度
    private int mMeasuredHeight;
    // 控件宽度
    private int mMeasureWidth;
    // 半径
    private int mRadius;
    // 第一条分割线 Y 轴
    private float mFirstLineY;
    // 第二条分割线 Y 轴
    private float mSecondLineY;
    // 中心文字 Y 轴
    private float mCenterY;
    // 上次条目索引
    private int mPreCurrentIndex;

    private GestureDetector mGestureDetector;
    private Paint mPaintCoreText;
    private Paint mPaintOutText;
    private Paint mPaintDivider;
    private int mMaxTextWidth;
    private int mMaxTextHeight;

    private WheelAdapter mAdapter   = null;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initPaint();

        // 创建手势识别器对象
        mGestureDetector = new GestureDetector(mContext, new OnLoopGestureListener(this));
        mGestureDetector.setIsLongpressEnabled(false);
    }

    /**
     * 初始化自定义属性
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.WheelView);
        mDividerColor = a.getColor(R.styleable.WheelView_wv_dividerColor, mDividerColor);
        mColorOut = a.getColor(R.styleable.WheelView_wv_textColorOut, mColorOut);
        mColorCore = a.getColor(R.styleable.WheelView_wv_textColorCore, mColorCore);
        mTextSize = a.getDimension(R.styleable.WheelView_wv_textSize, mTextSize);
        a.recycle();
    }

    /**
     * 初始化画笔对象
     */
    private void initPaint(){
        // 初始化外部滚轮文字画笔对象
        mPaintOutText = new Paint();
        mPaintOutText.setAntiAlias(true);
        mPaintOutText.setColor(mColorOut);
        mPaintOutText.setTextSize(mTextSize);
        mPaintOutText.setTypeface(Typeface.MONOSPACE);
        // 初始化滚轮中心文字画笔对象
        mPaintCoreText = new Paint();
        mPaintCoreText.setAntiAlias(true);
        mPaintCoreText.setTextScaleX(1.1f);// 默认中心文字放大比
        mPaintCoreText.setColor(mColorCore);
        mPaintCoreText.setTextSize(mTextSize);
        mPaintCoreText.setTypeface(Typeface.MONOSPACE);
        // 初始化分割线画笔对象
        mPaintDivider = new Paint();
        mPaintOutText.setAntiAlias(true);
        mPaintDivider.setColor(mDividerColor);
        // 开启硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        remeasure();
        setMeasuredDimension(mMeasureWidth, mMeasuredHeight);
    }

    /**
     * 重新测量
     */
    private void remeasure(){
        if(mAdapter == null) return;
        // 测量文字宽高
        measureTextWidthHeight();
        // 最大文字高度乘间距倍数得到,可见文字实际的总高度,半圆的周长
        mHalfCircumference = (int) (mMaxTextHeight * (mItemsVisible - 1));
        // 整个圆的周长除以 PI 得到直径,这个直径用作控件的总高度
        mMeasuredHeight = (int) ((mHalfCircumference * 2) / Math.PI);
        // 求出半径
        mRadius = (int) (mHalfCircumference / Math.PI);
        // 计算分割线 Y 轴
        mFirstLineY = (mMeasuredHeight - mItemHeight) / 2.0F;
        mSecondLineY = (mMeasuredHeight + mItemHeight) / 2.0F;
        // 计算中心文字 Y 轴
        mCenterY = (mMeasuredHeight + mMaxTextHeight) / 2.0F - CENTER_CONTENT_OFFSET;
        // 初始化显示条目的 position,根据是否 loop
        if (mInitPosition == -1) {
            if (mIsLoop) {
                mInitPosition = (mAdapter.getItemsCount() + 1) / 2;
            } else {
                mInitPosition = 0;
            }
        }
        mPreCurrentIndex = mInitPosition;
    }

    /**
     * 计算文字宽高
     */
    private void measureTextWidthHeight(){
        Rect rect = new Rect();
        for (int i = 0; i < mAdapter.getItemsCount(); i++) {
            String text = getContentText(mAdapter.getItem(i));
            mPaintCoreText.getTextBounds(text, 0, text.length(), rect);
            int width = rect.width();
            // 由于滚轮文字的长度不一,故需取最大宽度
            if(mMaxTextWidth < width)
                mMaxTextWidth = width;
            // 重新获取中文字体高度,避免数据是数字时高度过窄
            mPaintOutText.getTextBounds("\u661f", 0, 1, rect);
            mMaxTextHeight = rect.height();
        }
        // 计算条目高度
        mItemHeight = mItemLineSpacing * mMaxTextHeight;
    }

    /**
     * 获取滚轮所需要展示的数据
     * @param object Model 类，必须实现 IPickerViewModel 接口，重新 getPickerViewText() 方法。
     * @return
     */
    private String getContentText(Object object){
        if(object != null && object instanceof IPickerViewModel){
            return ((IPickerViewModel) object).getPickerViewText();
        }
        return "";
    }

    public final void scrollBy(float velocityY) {

    }

    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }



}
