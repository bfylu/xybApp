package cn.payadd.majia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by df on 2017/6/21.
 */

public class ProfitFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public ProfitFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("f", "getItem: ");
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
