package com.java.lzhmzx;
import java.util.*;
import java.io.*;

public class Channel {

    public static final int originSize = 5;
    public static final int deltaSize = 3;

    private String name;
    private int displayIndex;
    private ArrayList<News> savedNews;
    private ArrayList<News> displayNews;//show savedNews（displayIndex,length())开区间

    Channel(final String name){
        this.name = name;
        displayIndex = 0;
        savedNews = new ArrayList<>();
        displayNews = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<News> refresh(Map<String, News> newsMap){
        displayNews = new ArrayList<>();
        savedNews = DataHelper.reqNews("","","2019-9-6","",this.name);
        displayIndex = savedNews.size() - 1;
        for(int i = 0; i < savedNews.size(); i++){
            News temp = savedNews.get(i);
            newsMap.put(temp.getNewsID(), temp);
        }

        for(int i = 0; i < originSize && displayIndex >= 0; i++){
            displayNews.add(savedNews.get(displayIndex));
            displayIndex--;
        }
        return displayNews;
    }

    public ArrayList<News> get(Map<String, News> newsMap){
        if(savedNews.size() == 0){
            return refresh(newsMap);
        } else {
            return displayNews;
        }
    }

    public ArrayList<News> loadMore(){
        for(int i = 0; i < deltaSize && displayIndex >= 0; i++){
            displayNews.add(savedNews.get(displayIndex));
            displayIndex--;
        }
        return displayNews;
    }

}
