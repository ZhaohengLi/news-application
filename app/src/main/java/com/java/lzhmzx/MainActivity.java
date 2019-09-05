package com.java.lzhmzx;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_FOR_CHANGE_TAB = 1;
    public static final int REQUEST_FOR_LOGIN = 2;

    private ArrayList<News> newsArrayList = new ArrayList<>();
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpStatusBar();
        init();
        setUserInfo();
        setUpRecyclerView();//newsArrayList一开始是初始化好的空的list
        setUpTabLayout();
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

    private void setUserInfo(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.text_view_user_name);
        userName.setText(DataHelper.getUserName());
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
    }

    public void setUpTabLayout(){
        ArrayList<String> channelArrayList = DataHelper.getChannelArrayListAdded();
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.removeAllTabs();
        for(String channel : channelArrayList){tabLayout.addTab(tabLayout.newTab().setText(channel));}

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<News> tempNewsArrayList = DataHelper.getNewsArrayList(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsRecyclerViewAdapter.swapData(tempNewsArrayList);
                    }
                });
            }
        }).start();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<News> tempNewsArrayList = DataHelper.getNewsArrayList(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsRecyclerViewAdapter.swapData(tempNewsArrayList);
                            }
                        });
                    }
                }).start();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(MainActivity.this, newsArrayList);

        recyclerView.setAdapter(newsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        final TwinklingRefreshLayout twinklingRefreshLayout = findViewById(R.id.refresh_layout);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<News> refreshedNewsArrayList = DataHelper.getRefreshedNewsArrayList(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (refreshedNewsArrayList.size() != 0){
                                    newsRecyclerViewAdapter.swapData(refreshedNewsArrayList);
                                    twinklingRefreshLayout.finishRefreshing();
                                    Toast.makeText(MainActivity.this, "已为你更新新闻列表", Toast.LENGTH_SHORT).show();
                                }else{
                                    twinklingRefreshLayout.finishRefreshing();
                                    Toast.makeText(MainActivity.this, "暂时没有更新的新闻", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<News> moreNewsArrayList = DataHelper.getMoreNewsArrayList(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (moreNewsArrayList.size() != 0){
                                    newsRecyclerViewAdapter.swapData(moreNewsArrayList);
                                    twinklingRefreshLayout.finishLoadmore();
                                    Toast.makeText(MainActivity.this, "已为你找到更多新闻", Toast.LENGTH_SHORT).show();
                                }else{
                                    twinklingRefreshLayout.finishLoadmore();
                                    Toast.makeText(MainActivity.this, "暂时没有更多新闻", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case(REQUEST_FOR_CHANGE_TAB):
                if (resultCode == RESULT_OK) setUpTabLayout();
                break;
            case(REQUEST_FOR_LOGIN):
                if (resultCode == RESULT_OK) setUpTabLayout();
                break;
        }
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
            startActivityForResult(intent,REQUEST_FOR_CHANGE_TAB);
            return true;
        }else if(id == R.id.action_night_mode){
            int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if(mode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivityForResult(intent, REQUEST_FOR_LOGIN);
        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recommend) {
            Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
