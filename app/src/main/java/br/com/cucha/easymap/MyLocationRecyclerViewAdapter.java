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

    private final LocationModel model;
    private final SimpleDateFormat dateFormat;

    public MyLocationRecyclerViewAdapter(LocationModel model) {
        this.model = model;
        dateFormat = new SimpleDateFormat();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LocationInfo location = model.getFavoriteList().getValue().get(position);

        holder.locationInfo = location;
        holder.textName.setText(location.getName());
        holder.textLat.setText(location.getLat());
        holder.textLng.setText(location.getLng());
        holder.textDate.setText(dateFormat.format(location.getCreationDate()));

        holder.view.setOnClickListener(v -> {
            model.setMapLocation(holder.locationInfo);
        });
    }

    @Override
    public int getItemCount() {
        LiveData<List<LocationInfo>> locationList = model.getFavoriteList();

        return locationList.getValue() != null ? locationList.getValue().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textName;
        public final TextView textLat;
        public final TextView textDate;
        public final TextView textLng;
        public LocationInfo locationInfo;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textName = view.findViewById(R.id.text_name);
            textLat = view.findViewById(R.id.text_lat);
            textLng = view.findViewById(R.id.text_lng);
            textDate = view.findViewById(R.id.text_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textLat.getText() + "'";
        }
    }
}
