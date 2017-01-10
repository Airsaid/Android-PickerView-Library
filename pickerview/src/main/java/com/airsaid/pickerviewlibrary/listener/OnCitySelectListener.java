package com.airsaid.pickerviewlibrary.listener;

/**
 * Created by zhouyou on 2017/1/10.
 * Class desc: 城市选择回调
 */
public interface OnCitySelectListener {
    void onCitySelect(String str);
    void onCitySelect(String prov, String city, String area);
}
