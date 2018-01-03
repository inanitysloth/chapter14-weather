package c.sziit.coolweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class ChooseAreaFragment extends Fragment implements View.OnClickListener {

    private Button mBtnBack;
    private TextView mTextView;
    private ListView mListview;

    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mDataList;

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        initView(view);
        initData();
        return view;
    }



    private void initData() {
        mDataList=new ArrayList<>();
        mArrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mDataList);
        mListview.setAdapter(mArrayAdapter);
        queryProvinces();
    }

    private void initView(View view) {
        mBtnBack = (Button) view.findViewById(R.id.btnBack);
        mTextView = (TextView) view.findViewById(R.id.textView);
        mListview = (ListView) view.findViewById(R.id.listview);

        mBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:

                break;
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(String address, final String type){
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = HttpUtil.handleProvinceResponse(responseText);
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("province".equals(type)) {
                                queryProvinces();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        mTextView.setText("中国");
        mBtnBack.setVisibility(View.GONE);
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
}
