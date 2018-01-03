package c.sziit.coolweather.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import c.sziit.coolweather.R;
import c.sziit.coolweather.db.City;
import c.sziit.coolweather.db.County;
import c.sziit.coolweather.db.Province;
import c.sziit.coolweather.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by inanitysloth on 2018/1/3.
 */

public class ChooseAreaFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    private Button mBtnBack;
    private TextView mTextView;
    private ListView mListview;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mDataList;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private RelativeLayout mRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        initView(view);
        initData();
        queryProvinces();
        return view;
    }

    private void initView(View view) {
        mTextView=(TextView)view.findViewById(R.id.textView);
        mBtnBack=(Button)view.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(this);
        mListview = (ListView) view.findViewById(R.id.listview);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mListview.setAdapter(mArrayAdapter);
        mListview.setOnItemClickListener(this);
    }


    /* *
      * 根据传入的地址和类型从服务器上查询省市县数据。
 */
    private void queryFromServer(String address, final String type) {
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("onFailure","result");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = HttpUtil.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = HttpUtil.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = HttpUtil.handleCountyResponse(responseText, selectedCity.getId());
                }
                Log.d("onResponse","result");
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    /* *
      * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
 */
    private void queryProvinces() {
        mTextView.setText("中国");
        mBtnBack.setVisibility(View.VISIBLE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            mDataList.clear();
            for (Province province : provinceList) {
                mDataList.add(province.getProvinceName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListview.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        mTextView.setText(selectedProvince.getProvinceName());
        mBtnBack.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            mDataList.clear();
            for (City city : cityList) {
                mDataList.add(city.getCityName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListview.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        mTextView.setText(selectedCity.getCityName());
        mBtnBack.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            mDataList.clear();
            for (County county : countyList) {
                mDataList.add(county.getCountyName());
            }
            mArrayAdapter.notifyDataSetChanged();
            mListview.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (currentLevel == LEVEL_PROVINCE) {
            selectedProvince = provinceList.get(i);
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            selectedCity = cityList.get(i);
            queryCounties();
        }
    }

    @Override
    public void onClick(View view) {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }
    }
}
