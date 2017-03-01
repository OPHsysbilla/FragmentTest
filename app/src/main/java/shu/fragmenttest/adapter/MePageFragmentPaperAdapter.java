package shu.fragmenttest.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import shu.fragmenttest.subfragment.HomeFragment;
import shu.fragmenttest.subfragment.SelfPreferenceFragment;

/**
 * Created by eva on 2017/2/26.
 */

public class MePageFragmentPaperAdapter extends FragmentPagerAdapter {
    public final int COUNT = 3;
    private String[] titles = new String[]{"Tab1", "收藏", "我的搭配"};
    private Context context;

    public MePageFragmentPaperAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return SelfPreferenceFragment.newInstance();
        }
        else {
            return HomeFragment.newInstance("");
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
