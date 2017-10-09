package br.com.cucha.archlab;

import android.Manifest;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity implements LocationHelper.LocationCallback {

    private static final int REQUEST_LOCATION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
                        title =  getString(R.string.map);
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
    }

    private Fragment getListFragment() {
        Fragment fragment =
                getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);

        if(fragment == null)
            fragment = LocationFragment.newInstance(0);

        return fragment;
    }

    private Fragment getMapFragment() {
        Fragment fragment =
                getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);

        if(fragment == null)
            fragment = MapFragment.newInstance();

        return fragment;
    }

    @Override
    public void noLocationPermission() {
        String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };

        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
    }
}
