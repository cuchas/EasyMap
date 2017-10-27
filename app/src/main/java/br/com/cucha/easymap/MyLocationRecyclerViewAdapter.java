package br.com.cucha.easymap;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyLocationRecyclerViewAdapter extends
        RecyclerView.Adapter<MyLocationRecyclerViewAdapter.ViewHolder> {

    private final LocationModel mModel;
    private final SimpleDateFormat mDateFormat;

    public MyLocationRecyclerViewAdapter(LocationModel model) {
        mModel = model;
        mDateFormat = new SimpleDateFormat();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LocationInfo location = mModel.getFavoriteList().getValue().get(position);

        holder.mItem = location;

        holder.mIdView.setText(location.getLat());

        holder.mContentView.setText(location.getLng());

        holder.dateView.setText(mDateFormat.format(location.getCreationDate()));

        holder.mView.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        LiveData<List<LocationInfo>> locationList = mModel.getFavoriteList();

        return locationList.getValue() != null ? locationList.getValue().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView dateView;
        public LocationInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
            dateView = view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
