package br.com.cucha.easymap;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static String TAG = MapFragment.class.getName();
    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private LocationModel mMapModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mMapModel = ViewModelProviders.of(getActivity()).get(LocationModel.class);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mMapModel.getLocationList().observe(this, list -> {

            if(list.isEmpty())
                return;

            LocationInfo location = list.get(list.size() -1);

            double lat = Double.parseDouble(location.getLat());
            double lng = Double.parseDouble(location.getLng());

            LatLng latLng = new LatLng(lat, lng);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            mGoogleMap.addMarker(markerOptions);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f);
            mGoogleMap.moveCamera(cameraUpdate);
        });


    }

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
