package com.java.lzhmzx;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {

    private ImageView newsPicture;
    private TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        newsPicture = findViewById(R.id.news_picture);
        newsTitle = findViewById(R.id.news_title);
        newsDescription = findViewById(R.id.news_description);

        News news = getIntent().getParcelableExtra("News");

        newsPicture.setImageResource(news.getPictureId());
        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());

    }



}
