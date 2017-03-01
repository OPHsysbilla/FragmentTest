package shu.fragmenttest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import shu.fragmenttest.UI.Flow.FlowLayout;
import shu.fragmenttest.UI.Flow.TagAdapter;
import shu.fragmenttest.UI.Flow.TagFlowLayout;
import shu.fragmenttest.adapter.PhotoPickResultAdapter;

/**
 * Created by eva on 2017/2/25.
 */

public class PostFeelingActivity  extends Activity {
    private PhotoPickResultAdapter adapter;
    private PermissionChecker permissionChecker;
    private EditText edtTitle,edtDescription;
    private Button btnSubmit;
    private RecyclerView photoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfeeling_activity_main);

        photoList = (RecyclerView) findViewById(R.id.postfeeling_photo_rv_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(this).dp2px(PickConfig.ITEM_SPACE), 4));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String temp = bundle.getString("selectpath");
        List<String> selectpath = new ArrayList<>();
        selectpath.add(temp);
//        for(int i:temp)
//        {
//            selectpath.add(i);
//        }

        adapter = new PhotoPickResultAdapter(this,selectpath);
        photoList.setAdapter(adapter);
        initBase();
        initTagLayout();

    }


    String temp = "+、棉服、毛衣、大衣、马甲、皮衣、衬衫、T恤、夹克、法兰绒、卫衣、西服、风衣";
    final String[] texts = temp.split("、");
    private TagFlowLayout mFlowLayout;
    private TagAdapter<String> mTagAdapter;
    private void initTagLayout()
    {
        mFlowLayout = (TagFlowLayout)findViewById(R.id.postfeeling_flowlayout) ;
        mTagAdapter = new TagAdapter<String>(texts)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.flowlayout_tv,mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setAdapter(mTagAdapter);
//        预先设置选中
//        mTagAdapter.setSelectedList(1,3,5,7,8,9);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                TextView v = (TextView)view;
                if(v.getText().equals("+"))
                {
//                    末尾加标签
//                    mTagAdapter.addTag(mTagAdapter.getCount(),"新加标签");
                    mTagAdapter.addTag(1,"新加标签");
                    return false;
                }
//                Toast.makeText(UploadClothItemActivity.this, texts[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });



//        for(String clothname : texts) {
//            int ranHeight = dip2px(UploadClothItemActivity.this, 30);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(dip2px(UploadClothItemActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT), dip2px(UploadClothItemActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT));
//            lp.setMargins(dip2px(UploadClothItemActivity.this, 10), 0, dip2px(UploadClothItemActivity.this, 10), 0);
//            TextView tv = new TextView(UploadClothItemActivity.this);
//            tv.setPadding(dip2px(UploadClothItemActivity.this, 15), 0, dip2px(UploadClothItemActivity.this, 15), 0);
//            tv.setTextColor(Color.parseColor("#FF3030"));
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
////        int index = (int)(Math.random() * texts.length);
//            tv.setText(clothname);
//            tv.setGravity(Gravity.CENTER_VERTICAL);
//            tv.setLines(1);
//            tv.setBackgroundResource(R.drawable.tag_bg);
//            flowLayout.addView(tv, lp);
//        }
    }
    private Set<Integer> getSelectedTag()
    {
        //获得所有选中的pos集合
        return mFlowLayout.getSelectedList();
    }

    private void initBase()
    {
        edtDescription = (EditText)findViewById(R.id.postfeeling_description);
        edtTitle = (EditText)findViewById(R.id.postfeeling_title);
        btnSubmit = (Button)findViewById(R.id.postfeeling_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startPickPhoto();

            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
//    private void startPickPhoto(){
//        new PickPhotoView.Builder(UploadClothItemActivity.this)
//                .setPickPhotoSize(1)
//                .setShowCamera(true)
//                .setSpanCount(3)
//                .start();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(data == null){
            return;
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            List<String> selectPaths = (List<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
            for(String s : selectPaths){
                Log.d("selectpath",s);
            }
            adapter.updateData(selectPaths);
        }
    }


}
