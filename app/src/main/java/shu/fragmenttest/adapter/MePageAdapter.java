package shu.fragmenttest.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.UI.SlideShowView;
import shu.fragmenttest.entity.Cloth;

public class MePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Cloth> results;

    //type
    public static final int TYPE_SLIDER = 0xff01;
    public static final int TYPE_TYPE2_HEAD = 0xff02;
    public static final int TYPE_TYPE2 = 0xff03;
    public static final int TYPE_TYPE3_HEAD = 0xff04;
    public static final int TYPE_TYPE3 = 0xff05;
    public static final int TYPE_TYPE4 = 0xff06;

    public MePageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_SLIDER:
            case TYPE_TYPE2_HEAD:
            case TYPE_TYPE3_HEAD:
                return new HolderType2Head(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_slider_divider, parent, false));
            case TYPE_TYPE2:
            case TYPE_TYPE3:
            case TYPE_TYPE4:
                return new HolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_recommand_hot, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderSlider){
            bindTypeSlider((HolderSlider) holder, position);
        }else if (holder instanceof HolderType2Head){
            bindType2Head((HolderType2Head) holder, position);
        }else if (holder instanceof HolderType2){
            bindType2((HolderType2) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return 40;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_SLIDER;
        }else if (position == 1){
            return TYPE_TYPE2_HEAD;
        }else if (2<=position && position <= 7){
            return TYPE_TYPE2;
        }else if (position == 8){
            return TYPE_TYPE3_HEAD;
        }else if (9<=position && position <= 14){
            return TYPE_TYPE3;
        }else if (15<=position && position <= 18){
            return TYPE_TYPE4;
        }else {
            return TYPE_TYPE2;
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
                        case TYPE_SLIDER:
                        case TYPE_TYPE2_HEAD:
                        case TYPE_TYPE3_HEAD:
                        case TYPE_TYPE4:
                            return gridManager.getSpanCount();
                        case TYPE_TYPE2:
                            return 3;
                        case TYPE_TYPE3:
                            return 2;
                        default:
                            return 3;
                    }
                }
            });
        }
    }

    /////////////////////////////

    private void bindTypeSlider(HolderSlider holder, int position){
        String img = "http://pic16.nipic.com/20110921/7247268_215811562102_2.jpg";
        String[] imgs= new String[]{img,img,img,img,img,img,img};
        holder.slideShowView.setImageUrls(imgs);
        holder.slideShowView.startPlay();
    }

    private void bindType2Head(HolderType2Head holder, int position){
    }

    private void bindType2(HolderType2 holder, int position){
        String img = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//        x.image().bind(holder.item_img_type2, img,new ImageOptions.Builder().build(),new CustomBitmapLoadCallBack(holder.item_img_type2));
    }


    /////////////////////////////

    public class HolderSlider extends RecyclerView.ViewHolder {
        public SlideShowView slideShowView;

        public HolderSlider(View itemView) {
            super(itemView);
            slideShowView = (SlideShowView) itemView.findViewById(R.id.slideShowView);
        }
    }

    public class HolderType2Head extends RecyclerView.ViewHolder {
        public HolderType2Head(View itemView) {
            super(itemView);
        }
    }
    public class HolderType2 extends RecyclerView.ViewHolder {
        public ImageView item_img_type2;

        public HolderType2(View itemView) {
            super(itemView);
            item_img_type2 = (ImageView) itemView.findViewById(R.id.item_img_type2);
        }
    }

}