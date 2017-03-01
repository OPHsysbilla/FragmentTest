package shu.fragmenttest.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;

import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.adapter.PhotoPickResultAdapter;

/**
 * Created by eva on 2017/2/17.
 */

public class PostMessageActivity extends Activity {

    private PhotoPickResultAdapter adapter;
    private PermissionChecker permissionChecker;
    private EditText edtTitle,edtDescription;
    private Button btnSubmit,btnCancel;
    private RecyclerView photoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_message_activity);

        photoList = (RecyclerView) findViewById(R.id.post_message_photo_rv_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(PostMessageActivity.this).dp2px(PickConfig.ITEM_SPACE), 4));

        adapter = new PhotoPickResultAdapter(this,null);
        photoList.setAdapter(adapter);

        initBase();
        initTagLayout();

    }


//    https://github.com/hongyangAndroid/FlowLayout/blob/master/flowlayout/src/main/java/com/zhy/flowlayout/LimitSelectedFragment.java
//    https://github.com/fyales/tagcloud/blob/master/library/src/main/java/com/fyales/tagcloud/library/TagCloudLayout.java
//      https://github.com/lankton/android-flowlayout/tree/master/flowlayout/src/main/java/cn/lankton/flowlayout
//    FlowLayout flowLayout;

    private void initTagLayout()
    {


        String temp = "棉服、毛衣、大衣、马甲、皮衣、衬衫、T恤、夹克、法兰绒、卫衣、西服、风衣";
        String[] texts = temp.split("、");

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

    private void initBase()
    {
        edtDescription = (EditText)findViewById(R.id.post_message_description);
        edtTitle = (EditText)findViewById(R.id.post_message_title);
        btnSubmit = (Button)findViewById(R.id.post_message_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPickPhoto();
            }
        });
        btnCancel = (Button)findViewById(R.id.post_message_cancel);
//        flowLayout = (FlowLayout)findViewById(R.id.flowlayout);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTagLayout();
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
    private void startPickPhoto(){
        new PickPhotoView.Builder(PostMessageActivity.this)
                .setPickPhotoSize(9)
                .setShowCamera(true)
                .setSpanCount(3)
                .start();
    }

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
            adapter.updateData(selectPaths);
        }
    }


}
