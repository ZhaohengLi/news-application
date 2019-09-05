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

    public ArrayList<News> refresh(){
        displayNews = new ArrayList<>();
        System.out.println("refresh " + this.name);
        savedNews = DataHelper.reqNews("","","","",this.name);
        displayIndex = savedNews.size() - 1;

        for(int i = 0; i < originSize && displayIndex >= 0; i++){
            displayNews.add(savedNews.get(displayIndex));
            displayIndex--;
        }
        System.out.println("refresh() return");
        return displayNews;
    }

    public ArrayList<News> get(){
        System.out.println("get " + this.name);
        if(savedNews.size() == 0){
            return refresh();
        } else {
            return savedNews;
        }
    }

    public ArrayList<News> loadMore(){
        System.out.println("loadMore " + this.name);
        for(int i = 0; i < deltaSize && displayIndex >= 0; i++){
            displayNews.add(savedNews.get(displayIndex));
            displayIndex--;
        }
        return displayNews;
    }

}
