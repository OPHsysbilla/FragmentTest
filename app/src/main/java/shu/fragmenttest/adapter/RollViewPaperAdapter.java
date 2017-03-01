package shu.fragmenttest.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import shu.fragmenttest.R;


public class RollViewPaperAdapter extends LoopPagerAdapter {
        String[] imgs = new String[0];
        private Activity mActivity;
        public void setImgs(String[] imgs){
            this.imgs = imgs;
            notifyDataSetChanged();
        }

        public RollViewPaperAdapter(RollPagerView viewPager, Activity activity) {
            super(viewPager);
            mActivity = activity;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager","getView:"+imgs[position]);
            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("RollViewPager","onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(mActivity)
                    .load(imgs[position])
                    .placeholder(R.drawable.ic_more_horiz_grey_400_48dp)
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }