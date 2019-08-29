package com.java.lzhmzx;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //设置TabLayout
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ArrayList<News> newsArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNewsData();
        init();
        setTabLayout();
        setRecyclerView();
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setTabLayout(){
        ArrayList<String> category = new ArrayList<>(Arrays.asList("娱乐","军事","教育","文化","健康","财经","体育","汽车","科技","社会"));
        tabLayout = findViewById(R.id.tab_layout);
        for(String cat : category){
            tabLayout.addTab(tabLayout.newTab().setText(cat));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, tab.getText(), Toast.LENGTH_LONG).show();
                ArrayList<News> newsArrayListTemp = new ArrayList<>();
                newsArrayListTemp.add(new News(getString(R.string.news_three_title),getString(R.string.news_three_desc),R.mipmap.news_three));
                newsArrayListTemp.add(new News(getString(R.string.news_four_title),getString(R.string.news_four_desc),R.mipmap.news_four));
                recyclerViewAdapter.swapData(newsArrayListTemp);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    public void setRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, newsArrayList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void setNewsData(){
        newsArrayList = new ArrayList<>();
        newsArrayList.add(new News(getString(R.string.news_one_title),getString(R.string.news_one_desc),R.mipmap.news_one));
        newsArrayList.add(new News(getString(R.string.news_two_title),getString(R.string.news_two_desc),R.mipmap.news_two));
        newsArrayList.add(new News(getString(R.string.news_three_title),getString(R.string.news_three_desc),R.mipmap.news_three));
        newsArrayList.add(new News(getString(R.string.news_four_title),getString(R.string.news_four_desc),R.mipmap.news_four));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

            // Handle the camera action
        } else if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_recommend) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
