package shu.fragmenttest.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by eva on 2017/1/28.
 */

public class AdapterViewpager extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
