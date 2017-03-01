package shu.fragmenttest.subfragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shu.fragmenttest.ClothItemDetailsActivity;
import shu.fragmenttest.Interface.ItemTouchHelperAdapter;
import shu.fragmenttest.Interface.ItemTouchHelperViewHolder;
import shu.fragmenttest.MainActivity;
import shu.fragmenttest.R;
import shu.fragmenttest.entity.Cloth;


/**
 * Created by eva on 2017/2/13.
 */
public class LikePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
        private Context context;
        private List<Cloth> datashow;
        //type
        public static final int TYPE_SORT_SPINNER = 0xff04;
        public static final int TYPE_ADD_ITEM = 0xff01;
        public static final int TYPE_ITEM = 0xff02;
        public static final int TYPE_BOTTOM_DOWN_REFRESH = 0xff03;

        public void replaceAll(List<Cloth> list){
            datashow.clear();
            if(list!=null) {
                datashow.addAll(0,list);
            }
            notifyDataSetChanged();

        }
        public void classfiedByTag(String t) {
            if (t == null) return;
            if (t.trim().equals("")) return;
            List<Cloth> clothList = new ArrayList<>();
            SQLiteDatabase db = MainActivity.localDBHelper.getReadableDatabase();
            String sql = "select * from tagcloth where tagcloth.tag_title = ? ";
            Cursor cursor = db.rawQuery(sql,new String[]{t});
            Log.e("ByTagCollection:",sql+" tag: "+ t);
            if (cursor.moveToFirst()) {
                do {
                    Cloth clothtemp = new Cloth();
                    String uuid = cursor.getString(cursor.getColumnIndex("id"));
                    String cloth_title = cursor.getString(cursor.getColumnIndex("cloth_title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
                    String Tags = cursor.getString(cursor.getColumnIndex("Tags"));
                    String sdkPath = cursor.getString(cursor.getColumnIndex("sdkPath"));
                    String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                    Log.e("uuid in cursor :::", uuid);
                    clothtemp.setUuid(uuid);
                    clothtemp.setCloth_title(cloth_title);
                    clothtemp.setDescription(description);
                    clothtemp.setSdkPath(sdkPath);
                    clothtemp.setImgUrl(imgUrl);
                    clothtemp.setTags(Tags);
                    clothtemp.setUser_name(user_name);
                    clothList.add(clothtemp);
                } while (cursor.moveToNext());
            }
            replaceAll(clothList);

        }
        public void classfiedByTagCollection(String[] ts){
            if(ts.length == 0 )return;
            List<Cloth> clothList = new ArrayList<>();
            SQLiteDatabase db = MainActivity.localDBHelper.getReadableDatabase();
            String tags = "(";
            for(int i =0;i<ts.length;i++){
                if(i!=0)tags += ",";
                tags += "'"+ ts[i] + "'";
//                Log.e("ts:",ts[i]);
//                tags += "?";
            }
            tags += ")";
            String sql = "select * from cloth\n" +
                    "where not exists\n" +
                    "(select * from tag\n" +
                    "where title in " +
                    tags +
                    "and not exists (select * from tagcloth where tagcloth.cloth_imgUrl = cloth.imgUrl and tagcloth.tag_title = tag.title))";
            Log.e("ByTagCollection:",sql);
            Log.e("tags:",tags);

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.moveToFirst()) {
                do {
                    Cloth clothtemp = new Cloth();
                    String uuid = cursor.getString(cursor.getColumnIndex("id"));
                    String cloth_title = cursor.getString(cursor.getColumnIndex("cloth_title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
                    String Tags = cursor.getString(cursor.getColumnIndex("Tags"));
                    String sdkPath = cursor.getString(cursor.getColumnIndex("sdkPath"));
                    String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                    Log.e("uuid in cursor :::", uuid);
                    clothtemp.setUuid(uuid);
                    clothtemp.setCloth_title(cloth_title);
                    clothtemp.setDescription(description);
                    clothtemp.setSdkPath(sdkPath);
                    clothtemp.setImgUrl(imgUrl);
                    clothtemp.setTags(Tags);
                    clothtemp.setUser_name(user_name);
                    clothList.add(clothtemp);
                } while (cursor.moveToNext());
            }
            replaceAll(clothList);
        }
        public void addAll(int position,List<Cloth> list)
        {
            datashow.addAll(position,list);
            notifyDataSetChanged();
        }

        public void addOne(int position,Cloth cloth)
        {
            datashow.add(position,cloth);
    //            notifyItemRangeInserted(position, list.size());
    //            notifyItemRangeChanged(position + list.size(), getIteount()- list.size());
            notifyDataSetChanged();
        }
    public List<Cloth> queryAllCloth(){
        List<Cloth> clothList = new ArrayList<>();
        if(MainActivity.localDBHelper == null)
        {
            return clothList;
        }
        SQLiteDatabase db = MainActivity.localDBHelper.getWritableDatabase();
//
        //  取出所有衣服
        Cursor cursor = db.rawQuery("select * from cloth",null);
        if(cursor.moveToFirst()){
            do{
                Cloth clothtemp = new Cloth();
                String uuid = cursor.getString(cursor.getColumnIndex("id"));
                String cloth_title = cursor.getString(cursor.getColumnIndex("cloth_title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
                String Tags = cursor.getString(cursor.getColumnIndex("Tags"));
                String sdkPath = cursor.getString(cursor.getColumnIndex("sdkPath"));
                String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                Log.e("uuid in cursor :::",uuid);
                clothtemp.setUuid(uuid);
                clothtemp.setCloth_title(cloth_title);
                clothtemp.setDescription(description);
                clothtemp.setSdkPath(sdkPath);
                clothtemp.setImgUrl(imgUrl);
                clothtemp.setTags(Tags);
                clothtemp.setUser_name(user_name);
                clothList.add(clothtemp);
            }while(cursor.moveToNext());
        }
        return clothList;
    }

    public LikePageAdapter(Context context) {
        this.context = context;
        datashow = queryAllCloth();
    }


    public LikePageAdapter(Context context,List<Cloth> list) {
            this.context = context;
            datashow = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType)
            {
                case TYPE_ITEM:
                    return new HolderTypeItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.wardrobe_page_item, parent, false));
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
//            if(position==0)
//            {
//                return TYPE_SORT_SPINNER;
//            }
//            else if(position==1)
//            {
//                return TYPE_ADD_ITEM;
//            }
//            else if(position<datashow.size()) {
//                return TYPE_ITEM;
//            }
//            else
//            {
//                return TYPE_BOTTOM_DOWN_REFRESH;
//            }
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


        private void bindTypeItem(HolderTypeItem holder, final int position){
//            String img = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//            holder.item_img.setImageResource(datashow.get(position));
            Glide.with(context)
                    .load(datashow.get(position).getSdkPath())
                    .into(holder.item_img);
            holder.item_title.setText(datashow.get(position).getCloth_title());
            holder.item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = MainActivity.localDBHelper.getWritableDatabase();
                    db.execSQL("delete from cloth where id = ? and sdkPath = ?",
                            new String[]{datashow.get(position).getUuid(),datashow.get(position).getSdkPath()});
                    datashow.remove(position);
                    notifyItemRemoved(position);
                }
            });

            holder.item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl = datashow.get(position).getImgUrl();
                    String sdkPath = datashow.get(position).getSdkPath();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("imageUrl",imageUrl);
                    bundle.putString("sdkPath",sdkPath);
                    bundle.putString("tags",datashow.get(position).getTags());
                    bundle.putString("description",datashow.get(position).getDescription());
                    bundle.putString("username",datashow.get(position).getUser_name());
                    bundle.putString("textTitle",datashow.get(position).getCloth_title());
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

    @Override
    public void onItemDismiss(int position) {
        datashow.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(datashow, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /////////////////////////////


        public class HolderTypeItem extends RecyclerView.ViewHolder implements
                ItemTouchHelperViewHolder {
            public ImageView item_img ;
            public TextView item_title;
            public ImageView item_delete;
            public HolderTypeItem(View itemView) {
                super(itemView);
                item_delete = (ImageView)itemView.findViewById(R.id.wardrobe_item_delete);
                item_img  = (ImageView) itemView.findViewById(R.id.wardrobe_item_img);
                item_title = (TextView) itemView.findViewById(R.id.wardrobe_item_txt);
            }

            @Override
            public void onItemSelected() {
//                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear() {
//                itemView.setBackgroundColor(0);
            }
        }

    }
