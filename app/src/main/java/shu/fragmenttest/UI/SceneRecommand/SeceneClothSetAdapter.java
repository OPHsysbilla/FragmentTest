package shu.fragmenttest.UI.SceneRecommand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

import shu.fragmenttest.ClothItemDetailsActivity;
import shu.fragmenttest.R;
import shu.fragmenttest.entity.Cloth;

public class SeceneClothSetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private List<Cloth> datashow;
        //type
        public static final int TYPE_SORT_SPINNER = 0xff04;
        public static final int TYPE_ADD_ITEM = 0xff01;
        public static final int TYPE_ITEM = 0xff02;
        public static final int TYPE_BOTTOM_DOWN_REFRESH = 0xff03;

        public void addItems(int position,List<Cloth> list)
        {
            datashow.addAll(position,list);
//            notifyItemRangeInserted(position, list.size());
//            notifyItemRangeChanged(position + list.size(), getIteount()- list.size());
            notifyDataSetChanged();
        }

        public SeceneClothSetAdapter(Context context, List<Cloth> list) {
            this.context = context;
            datashow = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType)
            {
                case TYPE_ITEM:
                    return new HolderTypeItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.secene_recommend_rv_item, parent, false));
                default:
                    Log.d("error","viewholder is null");
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
             if (holder instanceof HolderTypeItem){
                bindTypeItem((HolderTypeItem)holder, position);
            }
        }

        @Override
        public int getItemCount() {
            return datashow.size();
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if(manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                final int layoutSize = gridManager.getSpanCount();
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                    return gridManager.getSpanCount();

//                            case TYPE_SORT_SPINNER:
//                            case TYPE_BOTTOM_DOWN_REFRESH:
//                                return gridManager.getSpanCount();
//                            case TYPE_ADD_ITEM:
//                            case TYPE_ITEM:
//                                return 3;
//                            default:
//                                return 3;

                    }
                });
            }
        }

        /////////////////////////////


        private void bindTypeItem(HolderTypeItem holder, int position){
//            String img = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//            holder.item_img.setImageResource(datashow.get(position).getImgUrl());
            final String img ="http://lorempixel.com/400/200";
            StringSignature signature = new StringSignature(String.valueOf(position+ Math.random()*100));
            Glide.with(context)
//                    .load(datashow.get(position).getImgUrl())
                    .load(img)
                    .centerCrop()
                    .signature(signature)
                    .into(holder.item_img);
            holder.item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("imageUrl", img);
                    intent.putExtras(bundle);
                    intent.setClass(context, ClothItemDetailsActivity.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context,
//                        Pair.create((View)holder.item_img_type2, "cloth_details_image"));
//                ,Pair.create((View)holder.item_txt_type2, "cloth_details_title")
//                context.startActivity(intent, options.toBundle());
                    context.startActivity(intent);
                }
            });

        }


        /////////////////////////////


        public class HolderTypeItem extends RecyclerView.ViewHolder {
            public ImageView item_img ;
            public HolderTypeItem(View itemView) {
                super(itemView);
                item_img  = (ImageView) itemView.findViewById(R.id.secne_recommend_rv_item_img);

            }
        }

    }