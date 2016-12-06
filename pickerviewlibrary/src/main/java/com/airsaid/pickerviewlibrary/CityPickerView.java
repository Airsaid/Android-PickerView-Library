package com.airsaid.pickerviewlibrary;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by zhouyou on 2016/12/5.
 * Class desc:
 */
public class CityPickerView extends OptionsPickerView {

    private final Context mContext;

    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCity = new ArrayList<>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<>();
    private JSONObject mJsonObj;

    public CityPickerView(Context context) {
        super(context);
        mContext = context;
        // 初始化Json对象
        initJsonData();
        // 初始化Json数据
        initJsonDatas();
        initCitySelect();
    }

    private void initCitySelect() {
        setTitle("选择城市");
        setPicker(mListProvince, mListCity, mListArea, true);
        setCyclic(false, false, false);
        setSelectOptions(0, 0, 0);
        setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                if(mOnCitySelectListener != null){
                    if(mListCity.size() > option1 && mListCity.get(option1).size() > option2){
                        if(mListArea.size() > option1 && mListArea.get(option1).size() > option2
                                && mListArea.get(option1).get(option2).size() > option3){
                            String str = mListProvince.get(option1).concat(mListCity.get(option1)
                                    .get(option2)).concat(mListArea.get(option1).get(option2).get(option3));
                            mOnCitySelectListener.onCitySelect(str);
                        }
                    }
                }
            }
        });
    }

    /** 从assert文件夹中读取省市区的json文件，然后转化为json对象 */
    private void initJsonData() {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = mContext.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 初始化Json数据，并释放Json对象 */
    private void initJsonDatas(){
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 获取每个省的Json对象
                String province = jsonP.getString("name");

                ArrayList<String> options2Items_01 = new ArrayList<>();
                ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<>();
                JSONArray jsonCs = jsonP.getJSONArray("city");
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
                    String city = jsonC.getString("name");
                    options2Items_01.add(city);// 添加市数据

                    ArrayList<String> options3Items_01_01 = new ArrayList<>();
                    JSONArray jsonAs = jsonC.getJSONArray("area");
                    for (int k = 0; k < jsonAs.length(); k++) {
                        options3Items_01_01.add(jsonAs.getString(k));// 添加区数据
                    }
                    options3Items_01.add(options3Items_01_01);
                }
                mListProvince.add(province);// 添加省数据
                mListCity.add(options2Items_01);
                mListArea.add(options3Items_01);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    public OnCitySelectListener mOnCitySelectListener;

    public interface OnCitySelectListener {
        void onCitySelect(String str);
    }

    public void setOnCitySelectListener(OnCitySelectListener listener) {
        this.mOnCitySelectListener = listener;
    }
}
