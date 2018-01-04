package c.sziit.coolweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import c.sziit.coolweather.jsonbean.HeWeather;
import c.sziit.coolweather.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private String mWeatherId;//显示的县天气id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initData();
    }


    private void initView() {

    }

    private void initData()
    {
        // 无缓存时去服务器查询天气
        mWeatherId=getIntent().getStringExtra("weather_id");
        requestWeather(mWeatherId);

    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + "CN101190401" + "&key=d0a6abae19a046e396bd47dfb50490a6";
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
                String responseText = response.body().string();
                HeWeather weather = HttpUtil.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* if (weather != null && "ok".equals(weather.getStatus())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.getBasic().getId();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }*/
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
        HeWeather weather1=weather;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_button:

                break;
        }
    }
}
