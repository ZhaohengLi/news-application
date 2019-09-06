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

public class FavoriteActivity extends AppCompatActivity {

    private ArrayList<News> newsArrayList;

    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setUpStatusBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        newsArrayList = DataHelper.getFavoriteArrayList();
        setUpRecyclerView();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(FavoriteActivity.this);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(FavoriteActivity.this, newsArrayList);

        recyclerView.setAdapter(newsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
