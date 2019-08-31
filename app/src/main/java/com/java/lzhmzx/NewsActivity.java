package com.java.lzhmzx;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class NewsActivity extends AppCompatActivity {

    private Boolean markedAsFavorite = false;
    private Boolean markedAsBlock = false;
    private News news = new News();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setUpStatusBar();

        setUpFloatingActionButton();
        setUpNewsDetail();
        setUpButton();
        operateHistory();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    public void setUpFloatingActionButton(){
        final FloatingActionButton fabFavorite = findViewById(R.id.fab_favorite);

        if(markedAsFavorite) fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite));
        else fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markedAsFavorite){
                    Snackbar.make(view, "已取消收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsFavorite = false;
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
                }else{
                    Snackbar.make(view, "已收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsFavorite = true;
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                }
            }
        });

    }

    public void setUpNewsDetail(){
        ImageView newsPicture = findViewById(R.id.news_picture);
        TextView newsTitle = findViewById(R.id.news_title);
        TextView newsDescription = findViewById(R.id.news_description);
        TextView newsTime = findViewById(R.id.news_time);
        TextView newsOrigin = findViewById(R.id.news_origin);
        VideoView videoView = NewsActivity.this.findViewById(R.id.news_video);

        news = getIntent().getParcelableExtra("News");

        newsPicture.setImageResource(news.getPictureId());
        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());
        newsTime.setText("于 "+news.getTime());
        newsOrigin.setText("来自 "+news.getOrigin()+" 的报道");

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/hehe2.mp4");
        videoView.start();
        videoView.requestFocus();
    }

    public void setUpButton(){
        final Button buttonShare = findViewById(R.id.btn_share);
        final Button buttonBlock = findViewById(R.id.btn_block);

        if(markedAsBlock) buttonBlock.setText("已屏蔽类似的新闻");
        else buttonBlock.setText("屏蔽类似的新闻");

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
                    Snackbar.make(view, "已取消屏蔽类似的新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsBlock = false;
                    operateBlock();
                    buttonBlock.setText("屏蔽类似的新闻");
                }else{
                    Snackbar.make(view, "已屏蔽类似的新闻！", Snackbar.LENGTH_LONG).show();
                    markedAsBlock = true;
                    operateBlock();
                    buttonBlock.setText("已屏蔽类似的新闻");
                }
            }
        });
    }

    public void operateFavorite(){
        if(markedAsFavorite) DataHelper.addToFavorite(news);
        else DataHelper.removeFromFavorite(news);
    }

    public void operateBlock(){
        if(markedAsBlock) DataHelper.addToBlock(news);
        else DataHelper.removeFromBlock(news);
    }
    public void operateHistory(){
        DataHelper.addToHistory(news);
    }
}
