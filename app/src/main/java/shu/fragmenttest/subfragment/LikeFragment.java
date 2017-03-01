package shu.fragmenttest.subfragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shu.fragmenttest.Interface.SimpleItemTouchHelperCallback;
import shu.fragmenttest.Listener.RecyclerItemClickListener;
import shu.fragmenttest.R;
import shu.fragmenttest.UI.tabsort.model.FirstClassItem;
import shu.fragmenttest.UI.tabsort.model.SecondClassItem;
import shu.fragmenttest.UI.tabsort.tabsortadapter.FirstClassAdapter;
import shu.fragmenttest.UI.tabsort.tabsortadapter.SecondClassAdapter;
import shu.fragmenttest.UI.tabsort.tabsortadapter.TagFlowFilterAdapter;
import shu.fragmenttest.UI.tabsort.utils.ScreenUtils;
import shu.fragmenttest.entity.Cloth;


/**
 * Created by Kevin on 2016/11/28.
 * Blog:http://blog.csdn.net/student9128
 * Description:
 */

public class LikeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static LikeFragment newInstance(String s){
        LikeFragment homeFragment = new LikeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TAG",s);
        homeFragment.setArguments(bundle);
        return homeFragment;
}
    private View rootView = null;//缓存Fragment view
    public LikePageAdapter adapter;
    private SwipeRefreshLayout lay_fresh;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wardrobe_page_main, container, false);
        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.wardrobe_page_refresh);
        lay_fresh.setColorSchemeResources(R.color.color1, R.color.color1);
        lay_fresh.setOnRefreshListener(this);
        initBase();
        findView();
        initData();
        initPopup();
        initPopUpWindowFilter();
        initTabSortBarListener();
        return rootView;
    }

    private void initTabSortBarListener()
    {
        OnClickListenerImpl l = new OnClickListenerImpl();
        tabSortClassfication.setOnClickListener(l);
        tabSortFilter.setOnClickListener(l);
    }

    private void initBase() {
        recyclerView = (RecyclerView) this.rootView.findViewById(R.id.wardrobe_page_rv_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 6, GridLayoutManager.VERTICAL, false));
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

//        final List<Cloth> datashow = queryAllCloth();
//        for(int i=0;i<10;i++)
//        {
//            datashow.add(new Cloth());
//        }
        //           顶部插入动画 mRecyclerView.scrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new LikePageAdapter(getActivity()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
//                        view.findViewById(R.id.wardrobe_item_img).getResources();
//                        Intent intent=new Intent();
//                        intent.setClass(getActivity(),UploadClothItemActivity.class);
//                        intent.putExtra("position",position);
//                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {

                    }
                })
        );
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            /** 最后通过makeMovementFlags（dragFlag，swipe）创建方向的Flag*/
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlag = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
//                int swipe = ItemTouchHelper.START|ItemTouchHelper.END;
                int swipe = 0;
                return makeMovementFlags(dragFlag,swipe);
            }
            /**  * 返回true，开启长按拖拽  */
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            /** * 返回true，开启swipe事件 **/
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if(fromPosition == toPosition)
                {
                    return false;
                }
                adapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onItemDismiss(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    final float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }
        };
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void tabFilterClick()
    {
        if (popupWindowFilter.isShowing()) {
            popupWindowFilter.dismiss();
        } else {
            popupWindowFilter.showAsDropDown(rootView.findViewById(R.id.main_div_line));
            popupWindowFilter.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }
    private TextView tabSortClassfication,tabSortTime,tabSortFilter;
    private Button tabFilterReset,tabFilterSubmit;
    /**左侧一级分类的数据*/
    private List<FirstClassItem> firstTagSortList;

    /**右侧二级分类的数据*/
    private List<SecondClassItem> secondTagSortList;

    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow,popupWindowFilter;

    /**左侧和右侧两个ListView*/
    private ListView leftLV, rightLV;
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;
    private ListView mFilterRvList;
    private TagFlowFilterAdapter tagFlowFilterAdapter;

    private void initPopUpWindowFilter()
    {
        popupWindowFilter = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_sort_popup_window_filter, null);

        popupWindowFilter.setContentView(view);
        popupWindowFilter.setBackgroundDrawable(new PaintDrawable(Color.WHITE));
        popupWindowFilter.setFocusable(true);

        popupWindowFilter.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindowFilter.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindowFilter.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);
            }
        });

        List<String> dataTitle = new ArrayList<>();
        dataTitle.add("面料");
        dataTitle.add("场景");
        dataTitle.add("自定义");
        Map<String,List<String>> dataTag = new HashMap<>();
        List<String> tag1 = new ArrayList<>();
        tag1.add("九分裤");
        tag1.add("七分裤");
        tag1.add("法兰绒");
        tag1.add("毛呢");
        tag1.add("羽绒");

        tag1.add("羊毛");
        tag1.add("皮衣");
        dataTag.put(dataTitle.get(0),tag1);
        List<String> tag2 = new ArrayList<>();
        tag2.add("法兰绒");
        tag2.add("毛呢");
        tag2.add("羽绒");

        tag2.add("羊毛");
        tag2.add("皮衣");

        dataTag.put(dataTitle.get(1),tag2);
        List<String> tag3 = new ArrayList<>();
        tag3.add("反毛绒");
        tag3.add("毛线");
        tag3.add("印花");
        tag3.add("无袖");
        dataTag.put(dataTitle.get(2),tag3);
        mFilterRvList = (ListView) view.findViewById(R.id.tab_sort_filter_grid_list);
//        mFilterRvList.setItemAnimator(new DefaultItemAnimator());
//        mFilterRvList.setLayoutManager(new LinearLayoutManager(mFilterRvList.getContext(), LinearLayoutManager.VERTICAL, false));
//        mFilterRvList.setAdapter(new TabFilterAdapter(mFilterRvList.getContext(),dataTitle,dataTag));
        tagFlowFilterAdapter = new TagFlowFilterAdapter(getActivity(),dataTitle,dataTag);
        mFilterRvList.setAdapter(tagFlowFilterAdapter);

        tabFilterReset = (Button)view.findViewById(R.id.tab_sort_filter_reset);
        tabFilterSubmit = (Button)view.findViewById(R.id.tab_sort_filter_submit);
        tabFilterReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFlowFilterAdapter.resetSelectedTagInt();
                onRefresh();
            }
        });
        tabFilterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = tagFlowFilterAdapter.handleResult();
                //处理筛选结果
                Log.d("res:",res);
                String[]classfied = res.split(" ");
                adapter.classfiedByTagCollection(classfied);
                popupWindowFilter.dismiss();
            }
        });
    }

    private void findView() {
        tabSortClassfication = (TextView) rootView.findViewById(R.id.wardrobe_page_tab_sort_classfication);
        tabSortFilter = (TextView)rootView.findViewById(R.id.wardrobe_page_tab_sort_fillter);
        darkView = rootView.findViewById(R.id.main_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
    }

    private void initData() {
        firstTagSortList = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> secondList1 = new ArrayList<SecondClassItem>();
        secondList1.add(new SecondClassItem(101, "浅色"));
        secondList1.add(new SecondClassItem(102, "深色"));
        secondList1.add(new SecondClassItem(103, "长袖"));
        secondList1.add(new SecondClassItem(104, "短袖"));
        secondList1.add(new SecondClassItem(105, "大衣"));
        secondList1.add(new SecondClassItem(106, "牛仔"));
        secondList1.add(new SecondClassItem(107, "衬衫"));
        secondList1.add(new SecondClassItem(108, "毛衣"));
        secondList1.add(new SecondClassItem(109, "棉袄"));
        secondList1.add(new SecondClassItem(110, "圆领"));
        secondList1.add(new SecondClassItem(111, "方领"));
        secondList1.add(new SecondClassItem(112, "立领"));
        firstTagSortList.add(new FirstClassItem(1, "上装", secondList1));
        //2
        ArrayList<SecondClassItem> secondList2 = new ArrayList<SecondClassItem>();
        secondList2.add(new SecondClassItem(201, "长裤"));
        secondList2.add(new SecondClassItem(202, "短裤"));
        secondList2.add(new SecondClassItem(203, "牛仔裤"));
        secondList2.add(new SecondClassItem(204, "运动裤"));
        secondList2.add(new SecondClassItem(205, "西裤"));
        secondList2.add(new SecondClassItem(206, "棉裤"));
        firstTagSortList.add(new FirstClassItem(2, "下装", secondList2));
        //3
        ArrayList<SecondClassItem> secondList3 = new ArrayList<SecondClassItem>();
        secondList3.add(new SecondClassItem(301, "青春"));
        secondList3.add(new SecondClassItem(302, "流行"));
        secondList3.add(new SecondClassItem(303, "日韩"));
        secondList3.add(new SecondClassItem(304, "欧美"));
        secondList3.add(new SecondClassItem(305, "商务"));
        secondList3.add(new SecondClassItem(306, "职业"));
        firstTagSortList.add(new FirstClassItem(3, "场景", secondList3));

    }

    //点击事件
    class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wardrobe_page_tab_sort_classfication:
                    tab1OnClick();
                    break;
                case R.id.wardrobe_page_tab_sort_fillter:
                    tabFilterClick();
                default:
                    break;
            }
        }
    }

        //顶部第一个标签的点击事件
        private void tab1OnClick() {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                popupWindow.showAsDropDown(rootView.findViewById(R.id.main_div_line));
                popupWindow.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
        }

        //刷新右侧ListView
        private void updateSecondListView(List<SecondClassItem> list2,
                                          SecondClassAdapter secondAdapter) {
            secondTagSortList.clear();
            secondTagSortList.addAll(list2);
            secondAdapter.notifyDataSetChanged();
        }
    private void initPopup() {
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_sort_popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindow.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV.setSelection(0);
                rightLV.setSelection(0);
            }
        });


        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(getActivity(), firstTagSortList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondTagSortList = new ArrayList<SecondClassItem>();
        secondTagSortList.addAll(firstTagSortList.get(0).getSecondList());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(getActivity(), secondTagSortList);
        rightLV.setAdapter(secondAdapter);

        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstTagSortList.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow.dismiss();

                    int firstId = firstTagSortList.get(position).getId();
                    String selectedName = firstTagSortList.get(position).getName();
                    handleResult(firstId, -1, selectedName);
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                popupWindow.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                int firstId = firstTagSortList.get(firstPosition).getId();
                int secondId = firstTagSortList.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = firstTagSortList.get(firstPosition).getSecondList().get(position)
                        .getName();
                handleResult(firstId, secondId, selectedName);
            }
        });
    }


    //处理点击结果
    private void handleResult(int firstId, int secondId, String selectedName){
        String text = "first id:" + firstId + ",second id:" + secondId;
        tabSortClassfication.setText(selectedName);
        adapter.classfiedByTag(selectedName);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lay_fresh.setRefreshing(false);
                List<Cloth>a = adapter.queryAllCloth();
                if(a.size()!=0){
                    adapter.replaceAll(a);
                }
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}

