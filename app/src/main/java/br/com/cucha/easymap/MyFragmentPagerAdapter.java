package br.com.cucha.easymap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by eduardo on 10/27/17.
 */
class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final Context context;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }

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
                title = context.getString(R.string.map);
                break;
            case 1:
                title = context.getString(R.string.list);
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }

    private Fragment getListFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(LocationFragment.TAG);

        if (fragment == null)
            fragment = LocationFragment.newInstance(0);

        return fragment;
    }

    private Fragment getMapFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(MapFragment.TAG);

        if (fragment == null)
            fragment = MapFragment.newInstance();

        return fragment;
    }
}
