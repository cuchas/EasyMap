package br.com.cucha.easymap;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        OnSuccessListener<AutocompletePredictionBufferResponse>,
        OnFailureListener, SearchAdapter.PlaceClickListener {


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

        FragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getApplicationContext(), fm);

        viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.tabl_main);
        tabLayout.setupWithViewPager(viewPager);

        setupSearchRecycler();

        setupPlacesSearch();
    }

    private void setupSearchRecycler() {
        searchAdapter = new SearchAdapter();
        searchAdapter.setListener(this);

        searchRecyclerView = findViewById(R.id.recycler_search_main);
        searchRecyclerView.setVisibility(View.GONE);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(searchAdapter);
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
        if (DataBufferUtils.hasData(autocompletePredictions)) {

            ArrayList<AutocompletePrediction> list =
                    DataBufferUtils.freezeAndClose(autocompletePredictions);

            searchAdapter.setData(list);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: " + e.getMessage());
    }

    @Override
    public void onItemClick(AutocompletePrediction autocompletePrediction) {

        if (searchView.getQuery().length() == 0) {
            searchView.setIconified(true);
        } else {
            searchView.setIconified(true);
            searchView.setIconified(true);
        }

        geoDataClient
                .getPlaceById(autocompletePrediction.getPlaceId())
                .addOnSuccessListener(this, this::onPlaceFound);
    }

    public void onPlaceFound(PlaceBufferResponse response) {
        if (!DataBufferUtils.hasData(response)) return;

        ArrayList<Place> places = DataBufferUtils.freezeAndClose(response);

        if (places.isEmpty()) return;

        LocationInfo locationInfo = LocationInfo.of(places.get(0));
        LocationModel model = ViewModelProviders.of(this).get(LocationModel.class);
        model.setMapLocation(locationInfo);
    }

    private void hideSearch() {
        searchRecyclerView.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (tabLayout.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            searchView.setIconified(true);
        }
    }
}
