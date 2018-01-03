package c.sziit.coolweather.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import c.sziit.coolweather.db.City;
import c.sziit.coolweather.db.County;
import c.sziit.coolweather.db.Province;
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
                    county.setWeatherId(cityObject.getInt("weather_id"));
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
}
