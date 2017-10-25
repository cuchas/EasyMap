package br.com.cucha.easymap;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LocationHelper.LocationCallback,
        SearchView.OnQueryTextListener,
        OnSuccessListener<AutocompletePredictionBufferResponse>,
        OnFailureListener, SearchAdapter.PlaceClickListener {

    private static final int REQUEST_LOCATION_CODE = 1001;
    private static final String TAG = MainActivity.class.getName();
    private AutocompleteFilter autocompleteFilter;
    private GeoDataClient geoDataClient;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView searchRecyclerView;
    private SearchAdapter searchAdapter;
    private SearchView searchView;

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

        viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.tabl_main);
        tabLayout.setupWithViewPager(viewPager);

        setupSearchRecycler();

        LocationModel locationModel = ViewModelProviders.of(this).get(LocationModel.class);

        new LocationHelper(this, getLifecycle(), this, locationModel);

        setupPlacesSearch();
    }

    private void setupSearchRecycler() {
        searchAdapter = new SearchAdapter();

        searchRecyclerView = findViewById(R.id.recycler_search_main);
        searchRecyclerView.setVisibility(View.GONE);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(searchAdapter);
        searchAdapter.setListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        searchView = new SearchView(this);
        searchView.setOnSearchClickListener(this::onSearchClick);
        searchView.setOnCloseListener(this::onSearchClose);
        searchView.setOnQueryTextListener(this);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setActionView(searchView);

        return true;
    }

    private boolean onSearchClose() {
        hideSearch();

        return false;
    }

    private void onSearchClick(View view) {
        showSearch();
    }

    private void showSearch() {
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Task<AutocompletePredictionBufferResponse> autocompletePredictions =
                geoDataClient.getAutocompletePredictions(query, null, autocompleteFilter);

        autocompletePredictions.addOnSuccessListener(this);
        autocompletePredictions.addOnFailureListener(this, this);

        return true;
    }

    private void setupPlacesSearch() {
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
        if(DataBufferUtils.hasData(autocompletePredictions)) {

            ArrayList<AutocompletePrediction> list =
                    DataBufferUtils.freezeAndClose(autocompletePredictions);

            searchAdapter.setData(list);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void onItemClick(AutocompletePrediction autocompletePrediction) {
        searchView.setIconified(true);
        hideSearch();
    }

    private void hideSearch() {
        searchRecyclerView.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(tabLayout.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            searchView.setIconified(true);
        }
    }
}
