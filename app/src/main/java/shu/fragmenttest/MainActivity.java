package shu.fragmenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.werb.pickphotoview.util.PickConfig;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import shu.fragmenttest.UI.SceneRecommand.SecneRecommendActivity;
import shu.fragmenttest.database.LocalDBHelper;
import shu.fragmenttest.entity.UserInfo;
import shu.fragmenttest.network.Constant;
import shu.fragmenttest.network.useOkhttp;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    static private TextTabFragment mTextTabFragment;
    private SearchFragment searchFragment;
    public ImageView sefi,headerBg;
    public TextView tvNickName;
    static public UserInfo userInfo = new UserInfo();
    static public final int RequestCodeLogin = 134;

    static public LocalDBHelper localDBHelper;
//    static public final int NotifyFragmentChange = 56;
//    static public final Handler handlerMain = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what)
//            {
//                case NotifyFragmentChange:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化数据库
        if(MainActivity.localDBHelper == null ){
            MainActivity.localDBHelper = new LocalDBHelper(this,"user_information.db",null,4);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        setSupportActionBar(mToolbar);
//        mToolbar.setOnMenuItemClickListener(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_layout_open, R.string.drawer_layout_close);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

//        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
     //   mNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.black));
     //   mNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.black));
        View headerView = mNavigationView.getHeaderView(0);
        tvNickName = (TextView)headerView.findViewById(R.id.header_nickname);
        headerBg = (ImageView)headerView.findViewById(R.id.header_view);
        Glide.with(this)
                .load("http://a3.topitme.com/7/a8/6f/11220277163fa6fa87o.jpg")
                .bitmapTransform(new BlurTransformation(this, 25))
                .crossFade(1000)
                .into(headerBg);
        sefi = (ImageView)headerView.findViewById(R.id.header_sefi);
        Glide.with(this)
                .load("http://a4.topitme.com/o/201011/28/12909463348177.jpg")
                .bitmapTransform(new CropCircleTransformation(this))
                .into(sefi);
        sefi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,RequestCodeLogin);
            }
        });

        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                Snackbar.make(mDrawerLayout, keyword+"，缺少搜索结果页面", Snackbar.LENGTH_SHORT).show();
            }
        });
        initSearchView();
//        setNavigationViewChecked(0);
        setCurrentFragment();

    }

//    private void setNavigationViewChecked(int position) {
//        mNavigationView.getMenu().getItem(position).setChecked(true);
//
//           for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
//            if (i != position) {
//                mNavigationView.getMenu().getItem(i).setChecked(false);
//            }
//        }
//    }

    private void setCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mTextTabFragment == null) {
            mTextTabFragment = TextTabFragment.newInstance();
        }
        transaction.replace(R.id.frame_content, mTextTabFragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                Snackbar.make(mDrawerLayout, "私信", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.menu_about:
                Snackbar.make(mDrawerLayout, "关于", Snackbar.LENGTH_SHORT).show();
                intent.setClass(MainActivity.this,SecneRecommendActivity.class);
                startActivity(intent);
//                setNavigationViewChecked(2);
                break;
            case R.id.menu_private_message:
                Snackbar.make(mDrawerLayout, "联系我们", Snackbar.LENGTH_SHORT).show();

//                intent.setClass(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//                setNavigationViewChecked(0);
                break;

        }
        mDrawerLayout.closeDrawers();
        transaction.commit();
        return true;
    }

    public void showSnackBar(String s)
    {
        Snackbar.make(mDrawerLayout,s, Snackbar.LENGTH_SHORT).show();
    }
    public void showToast(String s)
    {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    public void initSearchView()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1086:
                Bundle bundle = data.getExtras();
                String nickName = bundle.getString("nickName","");
                String account = bundle.getString("account","");
                String email = bundle.getString("email","");
                String avatar = bundle.getString("avatar","");
                String id = bundle.getString("id","");
                userInfo.setNickName(nickName);
                userInfo.setAccountName(account);
                userInfo.setEmail(email);
                userInfo.setAvatar(avatar);
                userInfo.setId(id);
                tvNickName.setText(userInfo.getNickName());
                avatar = "http://a4.topitme.com/o/201011/28/12909463348177.jpg";
                Glide.with(this)
                        .load(avatar)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(sefi);
//                userInfo.setAvatar(data.getStringExtra("avatar"));
//                userInfo.setId(data.getStringExtra("id"));
                tvNickName.setText(userInfo.getNickName());
                break;
            case RESULT_CANCELED:
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

            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("username","oph");
            useOkhttp.post_file("http://" + Constant.baseRequestUrl  + ":8080/hello2/UploadServlet?username="+userInfo.getAccountName(),
                    map,
                    selectPaths.get(0));

//            final String a = selectPaths.get(0);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//            useOkhttp.postAsynFile(a);
//                    Log.d("aaaaaaaaa",a);
//
//                    requestPhotoList.uploadFile("http://" + Constant.baseRequestUrl  + ":8080/hello2/UploadServlet"
//                            ,a);
//                }
//            }).start();

            Intent intent= new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("selectpath",selectPaths.get(0));
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this,UploadClothItemActivity.class);
            startActivity(intent);
        }
    }

}
