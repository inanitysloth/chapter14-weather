package c.sziit.coolweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import c.sziit.coolweather.R;

/**
 * Created by inanitysloth on 2018/1/3.
 */

public class TestFragment extends Fragment {
    private ListView mListview;
    private List<String> mDataList;
    private ArrayAdapter<String> mArrayAdapter;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mListview = (ListView) view.findViewById(R.id.listview);
    }
    private void initData() {
        mDataList = new ArrayList<>();
        mDataList.add("beijing");
        mDataList.add("beijing");
        mDataList.add("beijing");
       
        mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mListview.setAdapter(mArrayAdapter);
		}
}
