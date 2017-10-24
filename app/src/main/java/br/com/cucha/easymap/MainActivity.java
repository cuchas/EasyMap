package br.com.cucha.easymap;

import android.Manifest;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements
        LocationHelper.LocationCallback,
        SearchView.OnQueryTextListener,
        OnSuccessListener<AutocompletePredictionBufferResponse>,
        OnFailureListener {

    private static final int REQUEST_LOCATION_CODE = 1001;
    private static final int REQUEST_SEARCH_PLACE_CODE = 1002;
    private AutocompleteFilter autocompleteFilter;
    private GeoDataClient geoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {

                Fragment fragment = null;

                switch (position) {
                    case 0:
                        fragment = getMapFragment();
                        break;
                    case 1:
                        fragment = getListFragment();
                        break;
                }

                return fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                String title = null;

                switch (position) {
                    case 0:
                        title = getString(R.string.map);
                        break;
                    case 1:
                        title = getString(R.string.list);
                        break;
                }
                return title;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        ViewPager vp = findViewById(R.id.view_pager_map);
        vp.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabl_map);
        tabLayout.setupWithViewPager(vp);

        LocationModel locationModel = ViewModelProviders.of(this).get(LocationModel.class);

        new LocationHelper(this, getLifecycle(), this, locationModel);

        initGEOInfo();
    }

    private Fragment getListFragment() {
        Fragment fragment =
                getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);

        if (fragment == null)
            fragment = LocationFragment.newInstance(0);

        return fragment;
    }

    private Fragment getMapFragment() {
        Fragment fragment =
                getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);

        if (fragment == null)
            fragment = MapFragment.newInstance();

        return fragment;
    }

    @Override
    public void noLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = CurrentLocationService.newStopIntent(this);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ContextCompat.startForegroundService(this, CurrentLocationService.newIntent(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = new SearchView(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setActionView(searchView);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        Task<AutocompletePredictionBufferResponse> autocompletePredictions =
//                geoDataClient.getAutocompletePredictions(query, null, autocompleteFilter);
//
//        autocompletePredictions.addOnSuccessListener(this);
//        autocompletePredictions.addOnFailureListener(this, this);

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, REQUEST_SEARCH_PLACE_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

        return true;
    }

    private void initGEOInfo() {
        autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        geoDataClient = Places.getGeoDataClient(this, null);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {

    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }
}
