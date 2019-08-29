package com.java.lzhmzx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    //搜索相关
    private FloatingSearchView mSearchView;
    private ArrayList<HistorySuggestion> historySuggestionArrayList = new ArrayList<>();
    private String lastQuery = "";

    //新闻列表相关
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mSearchView = findViewById(R.id.floating_search_view);

        setUpRecyclerView();
        setUpHistorySuggestion();
        setUpFloatingSearchView();
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerViewAdapter = new RecyclerViewAdapter(SearchActivity.this, newsArrayList);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setUpHistorySuggestion(){
        this.historySuggestionArrayList.add(new HistorySuggestion("first"));
        this.historySuggestionArrayList.add(new HistorySuggestion("second"));
        this.historySuggestionArrayList.add(new HistorySuggestion("third"));
    }

    private void setUpFloatingSearchView(){
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mSearchView.clearFocus();
                if(!lastQuery.equals(searchSuggestion.getBody())){
                    historySuggestionArrayList.remove(searchSuggestion);
                    lastQuery = searchSuggestion.getBody();
                    historySuggestionArrayList.add((HistorySuggestion) searchSuggestion);
                }
                mSearchView.setSearchBarTitle(searchSuggestion.getBody());
                Search(searchSuggestion.getBody());
            }
            @Override
            public void onSearchAction(String query) {
                if(!lastQuery.equals(query)){
                    //TODO 删除之前的重复项
                    lastQuery = query;
                    historySuggestionArrayList.add(new HistorySuggestion(query));
                }
                mSearchView.setSearchBarTitle(query);
                Search(query);
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.clearSuggestions();//如果不加入这个clearSuggestion和下面那个clearSuggestion 显示顺序会有问题 不要删
                mSearchView.swapSuggestions(historySuggestionArrayList);
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
                    public void onHomeClicked() {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        SearchActivity.this.startActivity(intent);
                    }
                });
    }

    public void Search(String keyword){
        recyclerViewAdapter.swapData(NewsDataHelper.getDataExamples());
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
