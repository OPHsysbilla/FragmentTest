package shu.fragmenttest.subfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import shu.fragmenttest.LoginActivity;
import shu.fragmenttest.R;

import static shu.fragmenttest.MainActivity.RequestCodeLogin;

/**
 * Created by eva on 2017/2/26.
 */

public class SelfPreferenceFragment extends Fragment {

    private View rootview;
    public static SelfPreferenceFragment newInstance(){
        SelfPreferenceFragment selfPreferenceFragment = new SelfPreferenceFragment();
        Bundle bundle = new Bundle();
        selfPreferenceFragment.setArguments(bundle);
        return selfPreferenceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mepage_viewpaper_first, container, false);
        Bundle bundle = getArguments();
        rootview = view;
        InitBase();
        return view;
    }
    private ImageView sefi;
    private ImageView sefiBg;
    private void InitBase()
    {

        sefiBg = (ImageView)rootview.findViewById(R.id.mepage_sefiBG);
        Glide.with(this)
                .load("http://a3.topitme.com/7/a8/6f/11220277163fa6fa87o.jpg")
                .bitmapTransform(new BlurTransformation(getActivity(), 25))
                .crossFade(1000)
                .into(sefiBg);
        sefi = (ImageView)rootview.findViewById(R.id.mepage_sefi);
        Glide.with(this)
                .load("http://a4.topitme.com/o/201011/28/12909463348177.jpg")
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(sefi);
        sefi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivityForResult(intent,RequestCodeLogin);
            }
        });
    }
}
