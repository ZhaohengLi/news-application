package com.java.lzhmzx;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class RecommendActivity extends AppCompatActivity {

    private ArrayList<News> newsArrayList;

    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        setUpStatusBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpFloatingActionButton();

        newsArrayList = DataHelper.getRecommendArrayList();

        setUpRecyclerView();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setUpFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "已为您更新推荐的内容！", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                newsRecyclerViewAdapter.swapData(DataHelper.getRecommendArrayList());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(RecommendActivity.this);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(RecommendActivity.this, newsArrayList);

        recyclerView.setAdapter(newsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
