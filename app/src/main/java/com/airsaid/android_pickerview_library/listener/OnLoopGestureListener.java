package com.airsaid.android_pickerview_library.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.airsaid.android_pickerview_library.widget.WheelView;

/**
 * Created by zhouyou on 2016/12/2.
 * Class desc:
 */
public class OnLoopGestureListener extends GestureDetector.SimpleOnGestureListener {

    WheelView wheelView;

    public OnLoopGestureListener(WheelView view){
        this.wheelView = view;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
