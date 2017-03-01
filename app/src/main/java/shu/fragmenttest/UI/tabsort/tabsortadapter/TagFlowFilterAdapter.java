package shu.fragmenttest.UI.tabsort.tabsortadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import shu.fragmenttest.R;
import shu.fragmenttest.UI.tabsort.FlowLayout;

public class TagFlowFilterAdapter  extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<String> dataTitle;
    private Map<String,List<String>> dataTag;
    private List<TextView> selectedTvArray;
    private int totalTvCount;

    public TagFlowFilterAdapter(Context context, List<String> list, Map<String,List<String>> tag) {
        this.context = context;
        this.dataTitle = list;
        this.dataTag = tag;
        initSelectedTagInt();
    }

    private void initSelectedTagInt()
    {
        selectedTvArray = new ArrayList<>();

    }
    @Override
    public int getCount() {
        return dataTitle == null ? 0 : dataTitle.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tab_sort_filter_item, null);

            holder = new ViewHolder();
            holder.flowLayout = (FlowLayout)convertView.findViewById(R.id.tab_sort_filter_item_flowlayout);
            holder.nameTV = (TextView) convertView.findViewById(R.id.tab_sort_filter_item_title_txt);

            String tagTitleTemp = dataTitle.get(position);
            holder.nameTV.setText(tagTitleTemp);
            List<String> tagListTemp = dataTag.get(tagTitleTemp);
            for(String tag0:tagListTemp) {
                int ranHeight = dip2px(context, 30);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(dip2px(context, ViewGroup.LayoutParams.WRAP_CONTENT), ranHeight);
                lp.setMargins(dip2px(context, 10), 0, dip2px(context, 10), 0);
                TextView tv = new TextView(context);
                tv.setPadding(dip2px(context, 15), 0, dip2px(context, 15), 0);
                tv.setTextColor(Color.parseColor("#757575"));
//            tv.setTextColor(context.getResources().getColorStateList(R.drawable.tab_filter_text_touch));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv.setText(tag0);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setLines(1);
                tv.setBackgroundResource(R.drawable.tab_shape_tab_filter_text_touch_off);
//            tv.setFocusableInTouchMode(true);
//            tv.setFocusable(true);
//            tv.setClickable(true);
                tv.setTag(R.id.tag_selected_int,0);
                tv.setTag(R.id.tag_position,totalTvCount);
                totalTvCount++;
                tv.setOnClickListener(this);
                holder.flowLayout.addView(tv, lp);
                selectedTvArray.add(tv);
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

         }
//        holder.flowLayout.relayoutToCompressAndAlign();
//            holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_18dp, 0);

        return convertView;
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
    public void onClick(View v) {
        TextView tv = (TextView)v;

        if((int)v.getTag(R.id.tag_selected_int)==0)
        {
            v.setTag(R.id.tag_selected_int,1);
            tv.setTextColor(context.getResources().getColor(R.color.flow_txt_on));
            tv.setBackgroundResource(R.drawable.tab_shape_tab_filter_text_touch_on);

        }
        else
        {
            v.setTag(R.id.tag_selected_int,0);
            tv.setTextColor(context.getResources().getColor(R.color.flow_txt_off));
            tv.setBackgroundResource(R.drawable.tab_shape_tab_filter_text_touch_off);
        }

//        Log.d("tag",v.getTag(R.id.tag_selected_int)+"");
    }

    private class ViewHolder {
        TextView nameTV;
        FlowLayout flowLayout;
        ImageView iconImageView;
    }
    public void resetSelectedTagInt()
    {
         for(TextView tv:selectedTvArray)
         {
             tv.setTag(R.id.tag_selected_int,0);
             tv.setTextColor(context.getResources().getColor(R.color.flow_txt_off));
             tv.setBackgroundResource(R.drawable.tab_shape_tab_filter_text_touch_off);
         }
    }
    public String handleResult(){
        String res = "";
        for(TextView tv:selectedTvArray)
        {
            if((int)tv.getTag(R.id.tag_selected_int)==1)
            res+=tv.getText()+" ";
        }
        //请注意返回如果是为""则应该不进行分类
        return res.trim();
    }

}