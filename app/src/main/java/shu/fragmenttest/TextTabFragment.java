package shu.fragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.werb.pickphotoview.PickPhotoView;

import shu.fragmenttest.subfragment.HomeFragment;
import shu.fragmenttest.subfragment.LikeFragment;
import shu.fragmenttest.subfragment.LocationFragment;
import shu.fragmenttest.subfragment.PersonFragment;

public class TextTabFragment extends Fragment implements View.OnClickListener {
    private TextView mTHome, mTLocation, mTLike, mTMe;
    private ImageView mCamera;
    public HomeFragment mSecondFragment;
    public LocationFragment mHomeFragment;
    public LikeFragment mLikeFragment;
    public PersonFragment mPersonFragment;

    public static TextTabFragment newInstance() {
        TextTabFragment viewPagerFragment = new TextTabFragment();
        return viewPagerFragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_tab, container, false);

        mTHome = (TextView) view.findViewById(R.id.tv_home);
        mTLocation = (TextView) view.findViewById(R.id.tv_location);
        mTLike = (TextView) view.findViewById(R.id.tv_like);
        mTMe = (TextView) view.findViewById(R.id.tv_person);
        mCamera = (ImageView)view.findViewById(R.id.tv_camera);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String s = bundle.getString("TAG");
            Log.i("Kevin", s + "");
            if (!TextUtils.isEmpty(s)) {

            }
        }
        mTHome.setOnClickListener(this);
        mTLocation.setOnClickListener(this);
        mTLike.setOnClickListener(this);
        mTMe.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        setDefaultFragment();
        return view;
    }

    /**
     * set the default Fragment
     */
    private void setDefaultFragment() {
        switchFrgment(0);
        //set the defalut tab state
        setTabState(mTHome, R.drawable.ic_home_amber_600_24dp, getColor(R.color.color1));
    }

    @Override
    public void onClick(View view) {
        resetTabState();//reset the tab state
        switch (view.getId()) {
            case R.id.tv_home:
                setTabState(mTHome, R.drawable.ic_home_amber_600_24dp, getColor(R.color.color1));
                switchFrgment(0);
                break;
            case R.id.tv_location:
                setTabState(mTLocation, R.drawable.ic_near_me_amber_600_24dp, getColor(R.color.color2));
                switchFrgment(1);
                break;
            case R.id.tv_like:
                setTabState(mTLike, R.drawable.ic_shopping_basket_amber_600_24dp, getColor(R.color.color1));
                switchFrgment(2);
                break;
            case R.id.tv_person:
                setTabState(mTMe, R.drawable.ic_account_circle_amber_600_24dp, getColor(R.color.color2));
                switchFrgment(3);
                break;
            case R.id.tv_camera:
                startPickPhoto();
                break;
        }
    }
    private void startPickPhoto(){
        new PickPhotoView.Builder(getActivity())
                .setPickPhotoSize(1)
                .setShowCamera(true)
                .setSpanCount(3)
                .start();
    }


    public void switchTo2(){
        setTabState(mTLike, R.drawable.ic_shopping_basket_amber_600_24dp, getColor(R.color.color1));
        switchFrgment(2);
    }
    /**
     * switch the fragment accordting to id
     * @param i id
     */
    public void switchFrgment(int i) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = LocationFragment.newInstance();
                }
                transaction.replace(R.id.sub_content, mHomeFragment);
                break;
            case 1:
                if (mSecondFragment == null) {
                    mSecondFragment = HomeFragment.newInstance("灵感");
                }
                transaction.replace(R.id.sub_content, mSecondFragment);
                break;
            case 2:
                if (mLikeFragment == null) {
                    mLikeFragment = LikeFragment.newInstance("衣橱");
                }
                transaction.replace(R.id.sub_content, mLikeFragment);
                break;
            case 3:
                if (mPersonFragment == null) {
                    mPersonFragment = PersonFragment.newInstance("个人");
                }
                transaction.replace(R.id.sub_content, mPersonFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * set the tab state of bottom navigation bar
     *
     * @param textView the text to be shown
     * @param image    the image
     * @param color    the text color
     */
    private void setTabState(TextView textView, int image, int color) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, image, 0, 0);//Call requires API level 17
        textView.setTextColor(color);
    }


    /**
     * revert the image color and text color to black
     */
    private void resetTabState() {
        setTabState(mTHome, R.drawable.ic_home_grey_500_24dp, getColor(R.color.md_blue_grey_500));
        setTabState(mTLocation, R.drawable.ic_near_me_grey_500_24dp, getColor(R.color.md_blue_grey_500));
        setTabState(mTLike, R.drawable.ic_shopping_basket_grey_500_24dp, getColor(R.color.md_blue_grey_500));
        setTabState(mTMe, R.drawable.ic_account_circle_grey_500_24dp, getColor(R.color.md_blue_grey_500));

    }

    /**
     * @param i the color id
     * @return color
     */
    private int getColor(int i) {
        return ContextCompat.getColor(getActivity(), i);
    }
}