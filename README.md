# Android-PickerView-Library
这是一个高仿 IOS PickerView 控件的库。代码来自：https://github.com/saiwu-bigkoo/Android-PickerView
，在原有代码基础上进行封装，并提供了一些修改属性方法。后期如有时间，将会对原有代码进行优化。

# 预览
![](https://github.com/Airsaid/Android-PickerView-Library/blob/master/gif/pickerview.gif)

# 使用
* 首先需要在 build.gradle 文件中添加依赖：
```
dependencies {
   compile 'com.airsaid.library:pickerviewlibrary:1.0.0'
}
```

添加好依赖后，重新同步工程。可根据需求使用如下选择器：

* 城市选择：
```
        CityPickerView mCityPickerView = new CityPickerView(this);
        // 设置点击外部是否消失
//        mCityPickerView.setCancelable(true);
        // 设置滚轮字体大小
//        mCityPickerView.setTextSize(18f);
        // 设置标题
//        mCityPickerView.setTitle("我是标题");
        // 设置取消文字
//        mCityPickerView.setCancelText("我是取消文字");
        // 设置取消文字颜色
//        mCityPickerView.setCancelTextColor(Color.GRAY);
        // 设置取消文字大小
//        mCityPickerView.setCancelTextSize(14f);
        // 设置确定文字
//        mCityPickerView.setSubmitText("我是确定文字");
        // 设置确定文字颜色
//        mCityPickerView.setSubmitTextColor(Color.BLACK);
        // 设置确定文字大小
//        mCityPickerView.setSubmitTextSize(14f);
        // 设置头部背景
//        mCityPickerView.setHeadBackgroundColor(Color.RED);
        mCityPickerView.setOnCitySelectListener(new CityPickerView.OnCitySelectListener() {
            @Override
            public void onCitySelect(String str) {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
        mCityPickerView.show();
```
* 时间选择：
```
 //     TimePickerView 同样有上面设置样式的方法
        TimePickerView mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        // 设置是否循环
//        mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
//        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
//        Calendar calendar = Calendar.getInstance();
//        mTimePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));
        // 设置选中时间
//        mTimePickerView.setTime(new Date());
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Toast.makeText(MainActivity.this, format.format(date), Toast.LENGTH_SHORT).show();
            }
        });
        mTimePickerView.show();
```
* 选项选择：
```
        OptionsPickerView<String> mOptionsPickerView = new OptionsPickerView<>(this);
        final ArrayList<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        // 设置数据
        mOptionsPickerView.setPicker(list);
        // 设置选项单位
//        mOptionsPickerView.setLabels("性");
        mOptionsPickerView.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                String sex = list.get(option1);
                Toast.makeText(MainActivity.this, sex, Toast.LENGTH_SHORT).show();
            }
        });
        mOptionsPickerView.show();
```


# Contact Me
* 博 客 ：http://blog.csdn.net/airsaid
* QQ 群 ：5707887


# Thanks
* https://github.com/saiwu-bigkoo/Android-PickerView
