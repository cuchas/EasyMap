package br.com.cucha.easymap;


import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        LocationHelper.LocationCallback {

    public static String TAG = MapFragment.class.getName();
    private GoogleMap googleMap;
    private LocationModel model;
    private static final int REQUEST_LOCATION_CODE = 1001;
    private LocationHelper locationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        view.findViewById(R.id.fab_myplace_map).setOnClickListener(this::onMyPositionClick);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        model = ViewModelProviders.of(getActivity()).get(LocationModel.class);

        locationHelper = new LocationHelper(getContext(), getLifecycle(), this, model);

        return view;
    }

    private void onMyPositionClick(View view) {
        locationHelper.findCurrentLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        model.getMapLocation().observe(this, location -> {

            googleMap.clear();

            showOnMap(location);
        });
    }

    private void showOnMap(LocationInfo location) {
        double lat = Double.parseDouble(location.getLat());
        double lng = Double.parseDouble(location.getLng());

        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f);
        googleMap.moveCamera(cameraUpdate);
    }

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void noLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_LOCATION_CODE);
    }
}
