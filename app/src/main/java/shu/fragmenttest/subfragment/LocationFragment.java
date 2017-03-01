package shu.fragmenttest.subfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.adapter.OneRecycleAdapter;
import shu.fragmenttest.entity.Cloth;
import shu.fragmenttest.network.Constant;

import static shu.fragmenttest.network.requestPhotoList.getPhotoList;


public class LocationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View rootView = null;//缓存Fragment view
    private OneRecycleAdapter adapter;
    private SwipeRefreshLayout lay_fresh;
    private static List<Cloth>dataHot = new ArrayList<>();
    private static List<Cloth>newDatashow = new ArrayList<>();
    public static LocationFragment newInstance() {
        LocationFragment f = new LocationFragment();
        return f;
    }


    private List<Cloth> InitNetworkData()
    {
        List<Cloth> newData = new ArrayList<>();
        String jsonRes = null;
        try {
            jsonRes = getPhotoList();
            if(jsonRes != null)
            {
                Log.d("jsonres", jsonRes);
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(jsonRes);
                JSONArray path = jsonObject.getJSONArray("imagePath");
                Type type = new TypeToken<List<String>>() {
                }.getType();
                List<String> paths = gson.fromJson(path.toString(), type);

                int i = 0;
                for (String p : paths) {
//                    返回的p全是文件名 XXX.jpg
                    Cloth cloth = new Cloth();
                    String imUrl = "http://" + Constant.baseRequestUrl  + ":8080/images/" + p;
                    Log.d("path:---", imUrl);
                    cloth.setImgUrl(imUrl);
                    newData.add(cloth);
                }
                //                notifyDataSetChanged();
//                Message message = new Message();
//                message.what=1;
//                handler.sendMessage(message);
                if(newDatashow.isEmpty())
                    newDatashow = newData;
            }

        }catch (SocketTimeoutException e)
        {
            Log.d("Timeout","网络连接超时");
            e.printStackTrace();
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
        return newData;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nearpage_fragment, container, false);
        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.nearpage_refresh);
        lay_fresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        lay_fresh.setOnRefreshListener(this);
        initBase();
        return rootView;
    }

    private void initBase() {
        RecyclerView recyclerView = (RecyclerView) this.rootView.findViewById(R.id.nearpage_rv_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 6, GridLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        List<Cloth> results = new ArrayList<>();

        for(int i=0;i<4;i++)
        {
            results.add(new Cloth());
        }

        if(newDatashow!=null&&!newDatashow.isEmpty()) {
//            for (Cloth item : newDatashow) {
//                results.remove(1);
//            }
            results.addAll(1, newDatashow);
        }
        dataHot = results;
        recyclerView.setAdapter(adapter = new OneRecycleAdapter(getActivity(),dataHot));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lay_fresh.setRefreshing(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Cloth>newdata = InitNetworkData();
                        if(!newdata.equals(newDatashow)&&!newDatashow.isEmpty())
                        {
                            newDatashow = newdata;
                        }

                    }
                }).start();
                if(!newDatashow.isEmpty()) {
                    adapter.addALL(1, newDatashow);
                }
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}
