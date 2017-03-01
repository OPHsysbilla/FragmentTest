package shu.fragmenttest.subfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.adapter.MePageFragmentPaperAdapter;


/**
 * Created by Kevin on 2016/11/28.
 * Blog:http://blog.csdn.net/student9128
 * Description:
 */

public class PersonFragment extends Fragment {
    private View rootview;
    private SwipeRefreshLayout layout_fresh;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private MePageFragmentPaperAdapter paperAdapter;
    private ImageView sefi;
    private TabLayout tabLayout;
    public static PersonFragment newInstance(String s){
        PersonFragment homeFragment = new PersonFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mepage_main, container, false);
        Bundle bundle = getArguments();
        rootview = view;
        InitBase();

        return view;
    }


    private void InitBase(){
        tabLayout = (TabLayout)rootview.findViewById(R.id.mepage_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("个人设置"));
        tabLayout.addTab(tabLayout.newTab().setText("收藏"));
        tabLayout.addTab(tabLayout.newTab().setText("我的搭配"));
        viewPager = (ViewPager)rootview.findViewById(R.id.mepage_viewpaper);
        sefi = (ImageView)rootview.findViewById(R.id.header_sefi);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        paperAdapter = new MePageFragmentPaperAdapter(getChildFragmentManager(),getActivity());
        viewPager.setAdapter(paperAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

}
