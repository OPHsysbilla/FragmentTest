package shu.fragmenttest.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import shu.fragmenttest.R;
import shu.fragmenttest.entity.Cloth;




public class HomePageRecycleAdapter extends BaseQuickAdapter<Cloth, BaseViewHolder> {

    public HomePageRecycleAdapter(int dataSize) {
        super(R.layout.homepage_rv_item,getSampleData(dataSize));
    }
    public static List<Cloth> getSampleData(int lenth) {
        List<Cloth> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Cloth cloth = new Cloth();
            list.add(cloth);
        }
        return list;
    }
    @Override
    protected void convert(BaseViewHolder helper, Cloth item) {
        switch (helper.getLayoutPosition()%
                3){
            case 0:
                helper.setImageResource(R.id.home_item_img,R.drawable.header);
                break;
            case 1:
                helper.setImageResource(R.id.home_item_img,R.drawable.nav_header_bg);
                break;

        }
    }


}
