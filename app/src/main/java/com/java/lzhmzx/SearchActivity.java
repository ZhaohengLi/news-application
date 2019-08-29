package com.java.lzhmzx;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FloatingSearchView mSearchView;
    private ArrayList<HistorySuggestion> historySuggestionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchView = findViewById(R.id.floating_search_view);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setHistorySuggestion();

        setUpFloatingSearchView();
    }

    public void setHistorySuggestion(){
//        this.historySuggestionArrayList = historySuggestionArrayList;
        this.historySuggestionArrayList.add(new HistorySuggestion("first"));
        this.historySuggestionArrayList.add(new HistorySuggestion("second"));
        this.historySuggestionArrayList.add(new HistorySuggestion("third"));
    }

    private void setUpFloatingSearchView(){
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                Toast.makeText(SearchActivity.this, "Click Suggestion: "+searchSuggestion.getBody(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchAction(String query) {
                Toast.makeText(SearchActivity.this, "Search: "+query, Toast.LENGTH_SHORT).show();
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(historySuggestionArrayList);
            }

            @Override
            public void onFocusCleared() {
                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle("消失");
                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());
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
