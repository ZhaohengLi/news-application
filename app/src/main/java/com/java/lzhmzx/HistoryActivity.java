package com.java.lzhmzx;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<News> newsArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpFloatingActionButton();

        setUpNewsData();
        setUpRecyclerView();
    }

    private void setUpFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUpRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        recyclerViewAdapter = new RecyclerViewAdapter(HistoryActivity.this, newsArrayList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setUpNewsData(){
        newsArrayList = new ArrayList<>();
        newsArrayList.add(new News(getString(R.string.news_one_title),getString(R.string.news_one_desc),R.mipmap.news_one));
        newsArrayList.add(new News(getString(R.string.news_two_title),getString(R.string.news_two_desc),R.mipmap.news_two));
        newsArrayList.add(new News(getString(R.string.news_three_title),getString(R.string.news_three_desc),R.mipmap.news_three));
        newsArrayList.add(new News(getString(R.string.news_four_title),getString(R.string.news_four_desc),R.mipmap.news_four));
    }
}
