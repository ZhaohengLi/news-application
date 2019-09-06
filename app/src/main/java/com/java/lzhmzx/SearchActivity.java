package com.java.lzhmzx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    //搜索相关
    private FloatingSearchView mSearchView;
    private ArrayList<HistorySuggestion> historySuggestionArrayList;
    private String lastQuery = "";

    //新闻列表相关
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mSearchView = findViewById(R.id.floating_search_view);

        setUpRecyclerView();
        historySuggestionArrayList = DataHelper.getHistorySuggestionArrayList();
        setUpFloatingSearchView();
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(SearchActivity.this, newsArrayList);

        recyclerView.setAdapter(newsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setUpFloatingSearchView(){
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                lastQuery = searchSuggestion.getBody();
                mSearchView.clearFocus();
                Search(searchSuggestion.getBody());
            }
            @Override
            public void onSearchAction(String query) {
                lastQuery = query;
                mSearchView.clearFocus();
                Search("智慧之光如何闪耀海洋");
            }
        });
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.clearSuggestions();//如果不加入这个clearSuggestion和下面那个clearSuggestion 显示顺序会有问题 不要删
                mSearchView.swapSuggestions(DataHelper.getHistorySuggestionArrayList());
            }
            @Override
            public void onFocusCleared() {
                mSearchView.clearSuggestions();//如果不加入这个clearSuggestion和上面那个clearSuggestion 显示顺序会有问题 不要删
                mSearchView.setSearchBarTitle(lastQuery);
            }
        });
        mSearchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {
                    @Override
                    public void onHomeClicked() { finish(); }
                });
    }

    public void Search(final String keyword){
        Snackbar.make(getWindow().getDecorView().findViewById(R.id.recycler_view), "正在为你搜索有关 "+keyword+" 的内容 请稍后", Snackbar.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<News> tempNewsArrayList = DataHelper.getSearchResultArrayList(keyword);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsRecyclerViewAdapter.swapData(tempNewsArrayList);
                        Snackbar.make(getWindow().getDecorView().findViewById(R.id.recycler_view), "已向你展示 "+tempNewsArrayList.size()+"条 搜索到的内容", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
        DataHelper.addToHistorySuggestion(new HistorySuggestion(keyword));
    }
}


class HistorySuggestion implements SearchSuggestion{
    private String string;
    public HistorySuggestion(){}
    public HistorySuggestion(String string){
        this.string = string;
    }

    @Override
    public String getBody() {
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(string);
    }

    public static final Parcelable.Creator<HistorySuggestion> CREATOR = new Creator<HistorySuggestion>() {
        public HistorySuggestion createFromParcel(Parcel source) {
            HistorySuggestion historySuggestion = new HistorySuggestion();
            historySuggestion.string = source.readString();
            return historySuggestion;
        }
        public HistorySuggestion[] newArray(int size) {
            return new HistorySuggestion[size];
        }
    };
}
