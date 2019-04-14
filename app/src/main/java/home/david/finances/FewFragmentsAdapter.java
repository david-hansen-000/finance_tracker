package home.david.finances;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by david on 2/23/18.
 */

public class FewFragmentsAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    public FewFragmentsAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void add(Fragment fragment) {
        fragments.add(fragment);
    }
}
