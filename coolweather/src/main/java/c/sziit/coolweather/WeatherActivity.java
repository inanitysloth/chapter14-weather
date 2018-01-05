package c.sziit.coolweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import c.sziit.coolweather.jsonbean.Daily_forecast;
import c.sziit.coolweather.jsonbean.HeWeather;
import c.sziit.coolweather.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private String mWeatherId;//显示的县天气id
    private Button mNavButton;
    private TextView mTitleCity;
    private TextView mTitleUpdateTime;
    private TextView mDegreeText;
    private TextView mWeatherInfoText;
    private LinearLayout mForecastLayout;
    private TextView mAqiText;
    private TextView mPm25Text;
    private TextView mComfortText;
    private TextView mCarWashText;
    private TextView mSportText;
    private ScrollView mWeatherLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        initData();
    }


    private void initView() {

        mNavButton = (Button) findViewById(R.id.nav_button);
        mNavButton.setOnClickListener(this);
        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleCity.setOnClickListener(this);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        mTitleUpdateTime.setOnClickListener(this);
        mDegreeText = (TextView) findViewById(R.id.degree_text);
        mDegreeText.setOnClickListener(this);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mWeatherInfoText.setOnClickListener(this);
        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mForecastLayout.setOnClickListener(this);
        mAqiText = (TextView) findViewById(R.id.aqi_text);
        mAqiText.setOnClickListener(this);
        mPm25Text = (TextView) findViewById(R.id.pm25_text);
        mPm25Text.setOnClickListener(this);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mComfortText.setOnClickListener(this);
        mCarWashText = (TextView) findViewById(R.id.car_wash_text);
        mCarWashText.setOnClickListener(this);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mSportText.setOnClickListener(this);
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        mWeatherLayout.setOnClickListener(this);
    }

    private void initData() {
        // 无缓存时去服务器查询天气
        mWeatherId = getIntent().getStringExtra("weather_id");
        requestWeather(mWeatherId);

    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=d0a6abae19a046e396bd47dfb50490a6";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        /*swipeRefresh.setRefreshing(false);*/
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final HeWeather weather = HttpUtil.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.getStatus())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.getBasic().getId();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        /*swipeRefresh.setRefreshing(false);*/
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(HeWeather weather) {
        //title.xml
        mTitleCity.setText(weather.getBasic().getCity());
        mTitleUpdateTime.setText(weather.getBasic().getUpdate().getLoc().split("")[1]);
       //now.xml
        mDegreeText.setText(weather.getNow().getTmp()+"℃");
        mWeatherInfoText.setText(weather.getNow().getCond().getTxt());
        //forecast.xml
        mForecastLayout.removeAllViews();
        //未来几天
        for (Daily_forecast forecast:weather.getDaily_forecast())
        {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.getDate().toString());
            infoText.setText(forecast.getCond().getTxt());
            maxText.setText(forecast.getTmp().getMax());
            minText.setText(forecast.getTmp().getMin());
            mForecastLayout.addView(view);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.nav_button:

                break;
            case R.id.nav_button:
                break;*/
        }
    }
}
