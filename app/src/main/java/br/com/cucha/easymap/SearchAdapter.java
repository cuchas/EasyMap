package br.com.cucha.easymap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;

/**
 * Created by eduardo on 10/25/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    interface PlaceClickListener {
        void onItemClick(AutocompletePrediction autocompletePrediction);
    }

    private ArrayList<AutocompletePrediction> data = new ArrayList<>();
    private PlaceClickListener listener;

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.view_search_item, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(PlaceClickListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<AutocompletePrediction> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private AutocompletePrediction autocompletePrediction;

        SearchViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text_search_item);
            text.setOnClickListener(view -> {
                if(listener == null) return;

                listener.onItemClick(autocompletePrediction);
            });
        }

        void bind(AutocompletePrediction autocompletePrediction) {
            this.autocompletePrediction = autocompletePrediction;
            text.setText(autocompletePrediction.getFullText(null));
        }
    }
 }
