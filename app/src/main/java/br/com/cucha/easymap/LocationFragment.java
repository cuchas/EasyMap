package br.com.cucha.easymap;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LocationFragment extends Fragment implements Observer<List<LocationInfo>> {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static String TAG = LocationFragment.class.getName();
    private int mColumnCount = 1;
    private MyLocationRecyclerViewAdapter adapter;

    public LocationFragment() {
    }

    @SuppressWarnings("unused")
    public static LocationFragment newInstance(int columnCount) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            LocationModel model = ViewModelProviders.of(getActivity()).get(LocationModel.class);
            model.getFavoriteList().observe(this, this);

            adapter = new MyLocationRecyclerViewAdapter(model);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onChanged(@Nullable List<LocationInfo> locations) {
        adapter.notifyDataSetChanged();
    }
}
