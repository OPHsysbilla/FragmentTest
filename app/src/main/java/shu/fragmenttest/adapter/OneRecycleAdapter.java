package shu.fragmenttest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shu.fragmenttest.ClothItemDetailsActivity;
import shu.fragmenttest.R;
import shu.fragmenttest.UI.SceneRecommand.SecneRecommendActivity;
import shu.fragmenttest.UI.SlideShowView;
import shu.fragmenttest.entity.Cloth;
import shu.fragmenttest.network.Constant;

public class OneRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Cloth> datashow;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                notifyDataSetChanged();
            }
        }
    };
    //type
    public static final int TYPE_SLIDER = 0xff01;
    public static final int TYPE_TYPE2_HEAD = 0xff02;
    public static final int TYPE_TYPE2 = 0xff03;
    public static final int TYPE_TYPE3_HEAD = 0xff04;
    public static final int TYPE_TYPE3 = 0xff05;
    public static final int TYPE_TYPE4 = 0xff06;

    public OneRecycleAdapter(Context context,List<Cloth> d) {
        this.context = context;
        datashow = d;
        InitPhotoList();
    }
    public void addALL(int position,List<Cloth> a )
    {
        datashow.addAll(position,a);
        notifyDataSetChanged();
    }


    public void InitPhotoList()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPhotoList();
            }
        }).start();
    }
    public void getPhotoList() {
        try {

            String username = "oph";
            String data = "method=photoes&username=" + URLEncoder.encode(username, "UTF-8");
                Log.d("data",data);
            String baseUrl = "http://" + Constant.baseRequestUrl  + ":8080/images";
            String httpUrl = "http://" + Constant.baseRequestUrl  + ":8080/hello2/UserServlet";
            Map<String, String> params = new HashMap<String, String>();
//            params.put("username",username);
//            params.put("method","photoes");
//            byte [] data = params.toString().getBytes();
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            int response = connection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(is);
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                is.close();
                String jsonRes = stringBuffer.toString();
                Log.d("jsonres",jsonRes);
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(jsonRes);
                JSONArray path = jsonObject.getJSONArray("imagePath");
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> paths = gson.fromJson(path.toString(),type);
                List<Cloth> newDataShow = new ArrayList<>();
                for(String p:paths)
                {
//                    返回的p全是文件名 XXX.jpg
                    Cloth cloth = new Cloth();
                    String imUrl = "http://" + Constant.baseRequestUrl  + ":8080/images/"+ p;
                    Log.d("path:---",imUrl);
                    cloth.setImgUrl(imUrl);
                    newDataShow.add(cloth);
                }
//                for (Cloth item:newDataShow){
//                    datashow.remove(1);
//                }
                datashow.addAll(1,newDataShow);
//                notifyDataSetChanged();
//                Message message = new Message();
//                message.what=1;
//                handler.sendMessage(message);

            }


            os.close();
            connection.disconnect();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case TYPE_SLIDER:
                return new HolderSlider(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_slider, parent, false));
            case TYPE_TYPE2_HEAD:
            case TYPE_TYPE3_HEAD:
                return new HolderType2Head(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_slider_divider, parent, false));
            case TYPE_TYPE3:
                return new HolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_item_recommendation, parent, false));
            case TYPE_TYPE2:
            case TYPE_TYPE4:
                return new HolderType3(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearpage_recommand_hot, parent, false));
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
        }else if (holder instanceof HolderType3){
            bindType3((HolderType3) holder, position);
        }else if(holder instanceof HolderType2){
            bindType2((HolderType2) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return datashow.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_SLIDER;
        }else if (position == 1){
            return TYPE_TYPE2_HEAD;
        }else if (2<=position && position <= 4){
            return TYPE_TYPE3;
        }else if (position == 5){
            return TYPE_TYPE3_HEAD;
        }else if (6<=position && position <= 11){
            return TYPE_TYPE2;
        }else if (position == 12){
            return TYPE_TYPE3_HEAD;
        }
        else if (12<=position && position <= 18){
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
//        String[] imgs= new String[]{
//                "http://img.hb.aicdn.com/da5fe17185836fde703df81a3e187a9adadd50075e631-nO1p1R_fw658",
//                "http://image.uisdc.com/wp-content/uploads/2017/02/002-2.jpg",
//                "http://a3.topitme.com/c/83/9e/118171493793d9e83cl.jpg"
//        };
//        holder.slideShowView.setImageUrls(imgs);
        holder.slideShowView.startPlay();
    }

    private void bindType2Head(HolderType2Head holder, int position){
        if(position==1) {
            holder.tvDividerTitle.setText("场景推荐");
        }else if(position == 5){
            holder.tvDividerTitle.setText("最新上传");
        }

    }
    String[] titles = new String[]{
             "蓝色t恤","休闲","走秀","会议","蓝色t恤","时尚男子潮流","布质工艺","皮夹克黑潮面","英爵伦男士长袖T恤欧美风修身男装个性V领上衣秋","男运动裤季新品运动长裤吸湿排汗收口男子"
            ,""
    };
    private void bindType2(final HolderType2 holder, final int position) {
        String titleTemp = titles[position];
        holder.item_txt_type2.setText(titleTemp);
//        String imageUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//        imageUrl = "http://" + Constant.baseRequestUrl  + ":8080/images/XX.jpg";
        String imageUrl = datashow.get(position%datashow.size()).getImgUrl();
        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.item_img_type2);
        holder.item_img_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String imageUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
                String imageUrl = datashow.get(position%datashow.size()).getImgUrl();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl",imageUrl);
                bundle.putString("textTitle",titles[position]);
                intent.putExtras(bundle);
                intent.setClass(context, SecneRecommendActivity.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context,
//                        Pair.create((View)holder.item_img_type2, "cloth_details_image"));
//                ,Pair.create((View)holder.item_txt_type2, "cloth_details_title")
//                context.startActivity(intent, options.toBundle());
                context.startActivity(intent);
            }
        });
    }

    private void bindType3(final HolderType3 holder, final int position){
//        holder.item_like.setBackgroundResource(R.drawable.shape_favorite);

        holder.item_like.setOnClickListener(new View.OnClickListener() {
            private boolean liked = false;
            @Override
            public void onClick(View v) {
                liked = !liked;
                if(liked == true)
                {
                    //加入收藏
                    Log.e("here","加入收藏");
                    v.setBackgroundResource(R.drawable.ic_favorite_red_400_24dp);
                }
                else{
                    //取消收藏
                    v.setBackgroundResource(R.drawable.ic_favorite_border_grey_400_24dp);
                }
            }
        });

        String title = datashow.get(position).getCloth_title();
        holder.item_txt_type2.setText(titles[position%5]);
//        String imageUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
//        imageUrl = "http://" + Constant.baseRequestUrl  + ":8080/images/XX.jpg";
        String imageUrl = datashow.get(position%datashow.size()).getImgUrl();
        if(position == 6)imageUrl = "http://img.hb.aicdn.com/7d6388fc58f0abd4b818c91edde755c5bf772dbf1b394-wtmCSR_fw658";
        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.item_img_type2);
        holder.item_img_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String imageUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
                String imageUrl = datashow.get(position%datashow.size()).getImgUrl();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl",imageUrl);
                bundle.putString("textTitle",titles[position%5]);
                bundle.putString("sdkPath",datashow.get(position).getSdkPath());
                bundle.putString("description",datashow.get(position).getDescription());
                bundle.putString("username",datashow.get(position).getUser_name());
                bundle.putString("tags",datashow.get(position).getTags());
                intent.putExtras(bundle);
                intent.setClass(context, ClothItemDetailsActivity.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context,
//                        Pair.create((View)holder.item_img_type2, "cloth_details_image"));
//                ,Pair.create((View)holder.item_txt_type2, "cloth_details_title")
//                context.startActivity(intent, options.toBundle());
                context.startActivity(intent);
            }
        });
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
        private TextView tvDividerTitle;
        public HolderType2Head(View itemView) {
            super(itemView);
            tvDividerTitle = (TextView)itemView.findViewById(R.id.nearpage_divider_title);
        }
    }
    public class HolderType3 extends RecyclerView.ViewHolder {
        public ImageView item_img_type2;
        public ImageView item_like;
        public TextView item_txt_type2;
        public HolderType3(View itemView) {
            super(itemView);
            item_img_type2 = (ImageView) itemView.findViewById(R.id.item_img_type2);
            item_txt_type2 = (TextView) itemView.findViewById(R.id.item_txt_type2);
            item_like = (ImageView) itemView.findViewById(R.id.item_like);
        }
    }
    public class HolderType2 extends RecyclerView.ViewHolder {
        public ImageView item_img_type2;
        public TextView item_txt_type2;
        public HolderType2(View itemView) {
            super(itemView);
            item_img_type2 = (ImageView) itemView.findViewById(R.id.nearpage_item_recommendation_img);
            item_txt_type2 = (TextView) itemView.findViewById(R.id.nearpage_item_recommendation_title);
        }
    }
}