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

    private ArrayList<News> newsArrayList = new ArrayList<>();

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
        setUpRecyclerView();

        Snackbar.make(getWindow().getDecorView().findViewById(R.id.recycler_view), "正在为你挑选内容 请稍后", Snackbar.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<News> tempNewsArrayList = DataHelper.getRecommendArrayList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsRecyclerViewAdapter.swapData(tempNewsArrayList);
                        Snackbar.make(getWindow().getDecorView().findViewById(R.id.recycler_view), "已为你更新推荐的内容", Snackbar.LENGTH_SHORT).show();

                    }
                });
            }
        }).start();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setUpFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "正在为你挑选内容 请稍后", Snackbar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<News> tempNewsArrayList = DataHelper.getRecommendArrayList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsRecyclerViewAdapter.swapData(tempNewsArrayList);
                                Snackbar.make(view, "已为你更新推荐的内容", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
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
