package shu.fragmenttest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.UI.SlideShowView;

/**
 * Created by eva on 2017/2/5.
 */

public class WardrobePageAdapter<T> extends MCyclerAdapter<T>{
    final int TOP_TYPE = 0xff01;
    final int ITEM_TYPE = 0xff02;
    public WardrobePageAdapter(List<T> list, Context context) {
        super(list, context);
    }

    @Override
    public int getDisplayType(int postion) {
//        return list.get(postion).Type;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case TOP_TYPE:
            case ITEM_TYPE:
                return new HolderTypeItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.wardrobe_page_item, parent, false));

        }
         return null;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        String img = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//        switch (getDisplayType(position))
//        if(holder instanceof HolderTypeItem)
        {
            HolderTypeItem holderTypeItem = (HolderTypeItem)holder;

            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.ic_more_horiz_grey_400_48dp)
                    .into(((HolderTypeItem) holder).item_img_wardrobe);

        }
    }

    public class HolderSlider extends RecyclerView.ViewHolder {
        public SlideShowView slideShowView;

        public HolderSlider(View itemView) {
            super(itemView);
            slideShowView = (SlideShowView) itemView.findViewById(R.id.slideShowView);
        }
    }

    public class HolderType2 extends RecyclerView.ViewHolder {
        public HolderType2(View itemView) {
            super(itemView);

        }
    }
    public class HolderTypeItem extends RecyclerView.ViewHolder {
        public ImageView item_img_wardrobe;
        public TextView item_txt_wardrobe;
        public HolderTypeItem(View itemView) {
            super(itemView);
            item_img_wardrobe = (ImageView) itemView.findViewById(R.id.wardrobe_item_img);
            item_txt_wardrobe = (TextView) itemView.findViewById(R.id.wardrobe_item_txt);
        }
    }

}
