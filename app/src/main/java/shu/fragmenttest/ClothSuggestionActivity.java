package shu.fragmenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by eva on 2017/2/28.
 */

public class ClothSuggestionActivity extends AppCompatActivity{
//    http://img.hb.aicdn.com/721883c3bce7d6e2a56ae857cf914ad18ea0efa818745-q7B5jo_fw658
    private ImageView clothSuggestionBG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloth_suggestion);
        initBase();
    }
    private void initBase(){
        clothSuggestionBG = (ImageView)findViewById(R.id.cloth_suggestion_bg);
        Glide.with(this)
                .load("http://img.hb.aicdn.com/721883c3bce7d6e2a56ae857cf914ad18ea0efa818745-q7B5jo_fw658")
                .into(clothSuggestionBG);

    }
}
