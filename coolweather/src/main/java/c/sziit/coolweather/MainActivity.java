package c.sziit.coolweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListview;
    private List<String> mDataList;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void initData() {
        mDataList = new ArrayList<>();
        mDataList.add("beijing");
        mDataList.add("beijing");
        mDataList.add("beijing");
        mDataList.add("beijing");
        mDataList.add("beijing");
        mDataList.add("beijing");
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDataList);
        mListview.setAdapter(mArrayAdapter);
    }

    private void initView() {
        mListview = (ListView) findViewById(R.id.listview);

    }
}
