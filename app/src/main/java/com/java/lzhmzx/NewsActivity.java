package com.java.lzhmzx;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {

    private Boolean markedAsFavorite = false;
    private Boolean markedAsBlock = false;
    private News news = new News();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setUpFloatingActionButton();
        setUpNewsDetail();
        setUpButton();
    }

    public void setUpFloatingActionButton(){
        final FloatingActionButton fabFavorite = findViewById(R.id.fab_favorite);

        if(markedAsFavorite) fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_white_24dp));
        else fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white_24dp));

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markedAsFavorite){
                    Snackbar.make(view, "已取消收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsFavorite = false;
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white_24dp));
                }else{
                    Snackbar.make(view, "已收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsFavorite = true;
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_white_24dp));
                }
            }
        });

    }

    public void setUpNewsDetail(){
        ImageView newsPicture = findViewById(R.id.news_picture);
        TextView newsTitle = findViewById(R.id.news_title);
        TextView newsDescription = findViewById(R.id.news_description);

        news = getIntent().getParcelableExtra("News");

        newsPicture.setImageResource(news.getPictureId());
        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());
    }

    public void setUpButton(){
        final Button buttonShare = findViewById(R.id.btn_share);
        final Button buttonBlock = findViewById(R.id.btn_block);

        if(markedAsBlock) buttonBlock.setText("ALREADY BLOCKED");
        else buttonBlock.setText("BLOCK SIMILAR NEWS");

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        buttonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markedAsBlock){
                    Snackbar.make(view, "已取消屏蔽相似新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsBlock = false;
                    operateBlock();
                    buttonBlock.setText("BLOCK SIMILAR NEWS");
                }else{
                    Snackbar.make(view, "已屏蔽相似新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsBlock = true;
                    operateBlock();
                    buttonBlock.setText("ALREADY BLOCKED");
                }
            }
        });
    }

    public void operateFavorite(){
        //TODO
        if(markedAsFavorite){

        }else{

        }
    }

    public void operateBlock(){
        //TODO
        if(markedAsBlock){

        }else{

        }
    }


}
