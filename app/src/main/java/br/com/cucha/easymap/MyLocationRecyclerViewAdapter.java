package br.com.cucha.easymap;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
        
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        LiveData<List<LocationInfo>> locationList = model.getFavoriteList();

        return locationList.getValue() != null ? locationList.getValue().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView textName;
        final TextView textLat;
        final TextView textDate;
        final TextView textLng;
        LocationInfo locationInfo;

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

        public void bind(LocationInfo location) {
            locationInfo = location;
            textName.setText(location.getName());
            textLat.setText(location.getLat());
            textLng.setText(location.getLng());
            textDate.setText(dateFormat.format(location.getCreationDate()));

            view.setOnClickListener(v -> {
                model.setMapLocation(locationInfo);
            });

            view.setOnLongClickListener(v -> {
                Context context = v.getContext();

                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.remove_favorite))
                        .setMessage(context.getString(R.string.are_you_sure_you_want_remove_))
                        .setPositiveButton(context.getString(android.R.string.yes), (dialogInterface, i) -> {
                            model.deleteFavorite(location);
                        })
                        .setNegativeButton(context.getString(android.R.string.cancel), (dialog, i) -> {
                            dialog.dismiss();
                        }).create()
                        .show();

                return true;
            });
        }
    }
}
