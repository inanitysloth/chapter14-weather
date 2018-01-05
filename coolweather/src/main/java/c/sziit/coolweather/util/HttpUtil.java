package c.sziit.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import c.sziit.coolweather.db.City;
import c.sziit.coolweather.db.County;
import c.sziit.coolweather.db.Province;
import c.sziit.coolweather.jsonbean.HeWeather;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by inanitysloth on 2018/1/3.
 */

public class HttpUtil {
    /*
    * 函数功能：网络请求的接口 *
    * String adress:网络请求的地址
    * okhttp3.Callback callback:网络请求的回调接口，网络数据返回后，谁来处理返回的数据
    * */
    public static void sendOkHttpRequest(String adress,okhttp3.Callback callback)
    {
        //建立一个服务的客户访问
        OkHttpClient client=new OkHttpClient();
        //根据网络地址发一个请求给要访问的网络地址
        Request request=new Request.Builder().url(adress).build();
        client.newCall(request).enqueue(callback);

    }
    /*
    * 函数功能：解析处理服务器返回的省份的数据 *
    * String response:网络请求的返回数据
    * */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvinces=new JSONArray(response);

                for (int i = 0; i <allProvinces.length() ; i++) {
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    //使用litepal存储到数据库
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
   * 函数功能：解析处理服务器返回的城市的数据 *
   * String response:网络请求的返回数据
   * int provinceId：省份的id
   * */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCitys=new JSONArray(response);

                for (int i = 0; i <allCitys.length() ; i++) {
                    JSONObject cityObject=allCitys.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
   * 函数功能：解析处理服务器返回的城市的数据 *
   * String response:网络请求的返回数据
   * int cityId：市的id
   * */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCountys=new JSONArray(response);
                for (int i = 0; i <allCountys.length() ; i++) {
                    JSONObject cityObject=allCountys.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(cityObject.getString("name"));
                    county.setWeatherId(cityObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成HeWeather实体类
     * {"HeWeather": [{"aqi":{"city":{"aqi":"22","qlty":"优","pm25":"15","pm10":"18","no2":"41","so2":"7","co":"1","o3":"36"}},"basic":{"city":"苏州","cnty":"中国","id":"CN101190401","lat":"31.29937935","lon":"120.61958313","update":{"loc":"2018-01-04 09:51","utc":"2018-01-04 01:51"}},"daily_forecast":[{"astro":{"mr":"19:43","ms":"08:38","sr":"06:57","ss":"17:09"},"cond":{"code_d":"306","code_n":"404","txt_d":"中雨","txt_n":"雨夹雪"},"date":"2018-01-04","hum":"82","pcpn":"27.4","pop":"98","pres":"1026","tmp":{"max":"3","min":"1"},"uv":"1","vis":"13","wind":{"deg":"60","dir":"东北风","sc":"4-5","spd":"20"}},{"astro":{"mr":"20:49","ms":"09:27","sr":"06:58","ss":"17:09"},"cond":{"code_d":"104","code_n":"101","txt_d":"阴","txt_n":"多云"},"date":"2018-01-05","hum":"77","pcpn":"4.8","pop":"90","pres":"1025","tmp":{"max":"5","min":"-2"},"uv":"2","vis":"17","wind":{"deg":"354","dir":"北风","sc":"3-4","spd":"11"}},{"astro":{"mr":"21:53","ms":"10:10","sr":"06:58","ss":"17:10"},"cond":{"code_d":"104","code_n":"306","txt_d":"阴","txt_n":"中雨"},"date":"2018-01-06","hum":"75","pcpn":"3.2","pop":"86","pres":"1023","tmp":{"max":"7","min":"2"},"uv":"2","vis":"17","wind":{"deg":"82","dir":"东风","sc":"3-4","spd":"13"}}],"hourly_forecast":[{"cond":{"code":"306","txt":"中雨"},"date":"2018-01-04 10:00","hum":"81","pop":"72","pres":"1028","tmp":"1","wind":{"deg":"80","dir":"东风","sc":"3-4","spd":"20"}},{"cond":{"code":"305","txt":"小雨"},"date":"2018-01-04 13:00","hum":"84","pop":"71","pres":"1025","tmp":"1","wind":{"deg":"84","dir":"东风","sc":"3-4","spd":"18"}},{"cond":{"code":"306","txt":"中雨"},"date":"2018-01-04 16:00","hum":"87","pop":"70","pres":"1023","tmp":"1","wind":{"deg":"64","dir":"东北风","sc":"3-4","spd":"18"}},{"cond":{"code":"306","txt":"中雨"},"date":"2018-01-04 19:00","hum":"87","pop":"69","pres":"1024","tmp":"2","wind":{"deg":"54","dir":"东北风","sc":"3-4","spd":"21"}},{"cond":{"code":"307","txt":"大雨"},"date":"2018-01-04 22:00","hum":"87","pop":"68","pres":"1024","tmp":"1","wind":{"deg":"55","dir":"东北风","sc":"3-4","spd":"21"}},{"cond":{"code":"306","txt":"中雨"},"date":"2018-01-05 01:00","hum":"90","pop":"71","pres":"1022","tmp":"1","wind":{"deg":"36","dir":"东北风","sc":"3-4","spd":"18"}},{"cond":{"code":"307","txt":"大雨"},"date":"2018-01-05 04:00","hum":"88","pop":"70","pres":"1023","tmp":"1","wind":{"deg":"28","dir":"东北风","sc":"3-4","spd":"17"}},{"cond":{"code":"305","txt":"小雨"},"date":"2018-01-05 07:00","hum":"85","pop":"71","pres":"1025","tmp":"1","wind":{"deg":"30","dir":"东北风","sc":"微风","spd":"15"}}],"now":{"cond":{"code":"404","txt":"雨夹雪"},"fl":"-4","hum":"95","pcpn":"0.8","pres":"1028","tmp":"2","vis":"10","wind":{"deg":"48","dir":"东北风","sc":"微风","spd":"8"}},"status":"ok","suggestion":{"air":{"brf":"较差","txt":"气象条件较不利于空气污染物稀释、扩散和清除，请适当减少室外活动时间。"},"comf":{"brf":"较不舒适","txt":"白天天气较凉，且风力较强，同时在降雨天气的伴随下，会使您感觉偏冷，不很舒适，请注意添加衣物。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"寒冷","txt":"天气寒冷，建议着厚羽绒服、毛皮大衣加厚毛衣等隆冬服装。年老体弱者尤其要注意保暖防冻。"},"flu":{"brf":"极易发","txt":"天气寒冷，昼夜温差极大且风力较强，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。"},"sport":{"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"},"trav":{"brf":"一般","txt":"风稍大，天气稍冷，同时有较强降雨，旅游指数一般，若坚持旅行建议带上雨具。"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}}]}
     */
    public static HeWeather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            HeWeather heWeather=new Gson().fromJson(weatherContent, HeWeather.class);
            return heWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
