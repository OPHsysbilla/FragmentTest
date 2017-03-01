package shu.fragmenttest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.werb.pickphotoview.util.PickConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import shu.fragmenttest.UI.Flow.FlowLayout;
import shu.fragmenttest.UI.Flow.TagAdapter;
import shu.fragmenttest.UI.Flow.TagFlowLayout;
import shu.fragmenttest.adapter.PhotoPickResultAdapter;
import shu.fragmenttest.entity.Cloth;
import shu.fragmenttest.network.Constant;
import shu.fragmenttest.network.useOkhttp;

/**
 * Created by eva on 2017/2/14.
 */

public class UploadClothItemActivity extends Activity {

    private PhotoPickResultAdapter adapter;
    private PermissionChecker permissionChecker;
    private EditText edtTitle,edtDescription;
    private TextView tvHint;
    private Button btnSubmit,btnSuggestion;
    private ImageView imageView;
    private RecyclerView photoList;

    public Cloth cloth = null;
    static public String uuid;
    static public String imgUrl;
    static final public int PHOTO_POST_ASYNC_COMPLETE = 42;
    static final public int PHOTO_POST_ASYNC_ERROR = 43;
    static public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case PHOTO_POST_ASYNC_COMPLETE:
                    Bundle bundle = msg.getData();
                    uuid = bundle.getString("uuid");
                    imgUrl = bundle.getString("imgUrl");
                    Log.e("bundle",uuid+" "+imgUrl);
                    break;
                case PHOTO_POST_ASYNC_ERROR:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadclothitem_activity_main);
        initBase();
        initTagLayout();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tempPic = bundle.getString("selectpath");
        Glide.with(UploadClothItemActivity.this)
                .load(tempPic)
                .into(imageView);
        //每次新建一个Cloth对象并不保存
        cloth = new Cloth();
        cloth.setSdkPath(tempPic);
    }

    String temp = "+、棉服、毛衣、大衣、马甲、皮衣、衬衫、T恤、夹克、法兰绒、卫衣、西服、风衣";
    final String[] texts = temp.split("、");
    private TagFlowLayout mFlowLayout;
    private TagAdapter<String> mTagAdapter;
    private void initTagLayout()
    {
        mFlowLayout = (TagFlowLayout)findViewById(R.id.uploadclothitem_flowlayout) ;
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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UploadClothItemActivity.this);
                    builder.setTitle("添加一个标签");

//                    builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
                    builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    View inputLayout = LayoutInflater.from(UploadClothItemActivity.this).inflate(R.layout.input_edittext, null);
                    builder.setView(inputLayout);
                    final EditText inputView = (EditText)inputLayout.findViewById(R.id.input_edt);
                    builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTagAdapter.addTag(1,inputView.getText().toString());                         }
                    });
                    builder.show();

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
        imageView = (ImageView)findViewById(R.id.uploadclothitem_image);
        edtDescription = (EditText)findViewById(R.id.uploadclothitem_description);
        edtTitle = (EditText)findViewById(R.id.uploadclothitem_title);
        tvHint = (TextView)findViewById(R.id.uploadclothitem_hint);
        btnSuggestion = (Button)findViewById(R.id.uploadclothitem_suggestion);
        btnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.setClass(UploadClothItemActivity.this,ClothSuggestionActivity.class);
                startActivity(intent);
            }
        });
        btnSubmit = (Button)findViewById(R.id.uploadclothitem_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cloth_title = edtTitle.getText().toString();
                if(cloth_title.trim().equals("")|cloth_title == null){
                    tvHint.setText("标题不能为空");
                    return;
                }
                cloth.setCloth_title(cloth_title);

                Set<Integer> TagInt = getSelectedTag();
                if(TagInt.isEmpty()){
                    tvHint.setText("请选择至少一个标签");
                    return;
                }
                else {
                    //无序tag集   Log.d("----------------", getSelectedTag().toString());
                    String resTagText = "";
                    for (Integer i : TagInt) {

                        //不能改逻辑！！！！！！！！！

                        resTagText += texts[i.intValue()] + ",";
                    }
                    if(resTagText!=null) {
                        String cloth_tags = resTagText.substring(0, resTagText.length() - 1);
                        Log.e("Tagggggggggg::", cloth_tags);
                        cloth.setTags(cloth_tags);
                    }
                }

                if(uuid == null || imgUrl == null){
                    tvHint.setText("正在上传图片，稍等几秒再次提交\n退出后内容将被清空，此时衣服将只会被存到本地");
                }
                else{
                    cloth.setUuid(uuid);
                    cloth.setImgUrl(imgUrl);
                }

                String cloth_description = edtDescription.getText().toString();
                if(cloth_description == null){
                    cloth_description = "";
                }
                cloth.setDescription(cloth_description);

                String userName = MainActivity.userInfo.getAccountName();
                if(userName != null){
                    cloth.setUser_name(userName);
                }

                Gson gson = new Gson();
                String clothJson = gson.toJson(cloth,Cloth.class);
                Log.e("clothJsoh",clothJson);

                //上传json   报错networkOnMainThread
                useOkhttp.postJson("http://" + Constant.baseRequestUrl  + ":8080/hello2/LoginServlet?method=insertCloth"
                        ,clothJson);
                if(cloth.getUuid() == null ){
                    tvHint.setText("服务器未能连接");
                    cloth.setUuid(UUID.randomUUID().toString());
                }

                List<String>clothtemp = new ArrayList<String>();
                clothtemp.add(cloth.getUuid());
                clothtemp.add(cloth.getCloth_title());
                clothtemp.add(cloth.getDescription());
                clothtemp.add(cloth.getImgUrl());
                clothtemp.add(cloth.getTags());
                clothtemp.add(cloth.getSdkPath());
                clothtemp.add(cloth.getUser_name());
                String[] insertValues = clothtemp.toArray(new String[]{});

                for(int i =0;i<insertValues.length;i++){
                    Log.e("--:------------",insertValues[i]);
                }


                //存储到本地
                SQLiteDatabase db = MainActivity.localDBHelper.getWritableDatabase();
                db.execSQL("insert into cloth(id,cloth_title,description,imgUrl,Tags,sdkPath,user_name) " +
                        "values(?,?,?,?,?,?,?)", insertValues);
                //                insert into cloth(id,cloth_title,description,imgUrl,Tags,sdkPath,user_name) values(UUID(),"风尚大衣","今年最火的就是...","6.jpg","好嘛","sdkpath","oph")
//                测试是否插入成功
                Cursor cursor = db.rawQuery("select * from cloth where id = ?",new String[]{cloth.getUuid()});
                if(cursor.moveToFirst()){
                    do{
                        String uuid = cursor.getString(cursor.getColumnIndex("id"));
                        Log.e("插入成功cloth :::",uuid);
                    }while(cursor.moveToNext());
                }



                String Tags = cloth.getTags();
                if(Tags!=null){
                    String[]TagArray = Tags.split(",");

                    for(int i =0 ;i<TagArray.length;i++){
                        cursor = db.rawQuery("select * from tag where title = ?",new String[]{TagArray[i]});
                        if(!cursor.moveToFirst()){
                            //如果为空则添加
                            db.execSQL("insert into tag values(?)",new String[]{TagArray[i]});

                        }else{Log.e("Tags:",TagArray[i]+"已存在");}
                        db.execSQL("insert into tagcloth(tag_title,cloth_id,cloth_imgUrl,cloth_sdkPath) values(?,?,?,?)",
                                new String[]{TagArray[i],cloth.getUuid(),cloth.getImgUrl(),cloth.getSdkPath()});
//                      insert into tagcloth(tag_title,cloth_id,cloth_imgUrl,cloth_sdkPath) values("大衣","uuid","http","sdk")

                    }
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgUrl = null;
        uuid = null;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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
            for(String s : selectPaths){
                Log.d("selectpath",s);
            }
            adapter.updateData(selectPaths);
        }
    }


}
