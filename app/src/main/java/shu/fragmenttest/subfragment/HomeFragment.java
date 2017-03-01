package shu.fragmenttest.subfragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import shu.fragmenttest.R;
import shu.fragmenttest.adapter.HomePageRecycleAdapter;
import shu.fragmenttest.adapter.RollViewPaperAdapter;
import shu.fragmenttest.entity.Cloth;


/**
 * Created by Kevin on 2016/11/28.
 * Blog:http://blog.csdn.net/student9128
 * Description: HomeFragment
 */

public class HomeFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    public static HomeFragment newInstance(String s){
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();

        homeFragment.setArguments(bundle);
        return homeFragment;
    }
    private final int PAGE_SIZE =10;
//    protected final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private OkHttpClient client = new OkHttpClient();
    private RollPagerView mRollViewPager;
    private RollViewPaperAdapter mLoopAdapter;
    private HomePageRecycleAdapter homePageRecycleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment, container, false);
        Bundle bundle = getArguments();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.home_swipelayout);
        homePageRecycleAdapter = new HomePageRecycleAdapter(PAGE_SIZE);
        homePageRecycleAdapter.openLoadAnimation();


        View rollview = inflater.inflate(R.layout.main_headerview, (ViewGroup) mRecyclerView.getParent(), false);
        mRollViewPager = (RollPagerView)rollview.findViewById(R.id.main_rollviewpaper);
        homePageRecycleAdapter.addHeaderView(rollview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //initSwipeRefreshLayout

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        //initAdapter
        homePageRecycleAdapter = new HomePageRecycleAdapter(PAGE_SIZE);
        homePageRecycleAdapter.openLoadAnimation();

        mRollViewPager.setHintView(new ColorPointHintView(activity, Color.YELLOW, Color.WHITE));

        mRollViewPager.setAdapter(mLoopAdapter = new RollViewPaperAdapter(mRollViewPager,activity));
        mRollViewPager.setOnItemClickListener(new com.jude.rollviewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(activity,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(homePageRecycleAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(activity,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });
        // getData(mPage);
        getData(1,activity);
    }
    public void getData(final int page,final Activity activity){
        Request request = new Request.Builder()
                .url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/"+5+"/"+page)
                .get()
                .build();
        okhttp3.Call call =  client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i("NetImageActivity","error:"+e.getMessage());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"网络请求失败，error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String content = response.body().string();
                    Log.i("NetImageActivity","raw data:"+content);

                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray strArr = jsonObject.getJSONArray("results");
                    final String[] imgs  = new String[strArr.length()];
                    for (int i = 0; i < strArr.length(); i++) {
                        JSONObject obj = strArr.getJSONObject(i);
                        imgs[i] = obj.getString("url");
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoopAdapter.setImgs(imgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int TOTAL_COUNTER = 18;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
    @Override
    public void onRefresh() {
        homePageRecycleAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Cloth> list = new ArrayList<>();
                for (int i = 0; i < PAGE_SIZE; i++) {
                    Cloth cloth = new Cloth();
                    list.add(cloth);
                }
                homePageRecycleAdapter.setNewData(list);
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
                homePageRecycleAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);

    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (homePageRecycleAdapter.getData().size() < PAGE_SIZE) {
                    homePageRecycleAdapter.loadMoreEnd(true);
                } else {
                    if (mCurrentCounter >= TOTAL_COUNTER) {
                        homePageRecycleAdapter.loadMoreEnd();//default visible
//                        homePageRecycleAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
//                    } else {
//                        if (isErr) {
//                           // homePageRecycleAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                            mCurrentCounter = homePageRecycleAdapter.getData().size();
//                            homePageRecycleAdapter.loadMoreComplete();
//                        } else {
//                            isErr = true;
//                            Toast.makeText(BaseActivity.this, "network_err", Toast.LENGTH_LONG).show();
//                            homePageRecycleAdapter.loadMoreFail();
//
//                        }
                    }
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        }, delayMillis);
    }
}
