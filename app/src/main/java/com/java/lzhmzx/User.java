package com.java.lzhmzx;

import java.util.*;

public class User {

    private String name;
    private String password;

    public ArrayList<News> historyList;
    public ArrayList<News> favoriteList;
    public ArrayList<News> recommendList;
    public ArrayList<News> blockList;
    public ArrayList<String> blockedKeywords;
    public ArrayList<HistorySuggestion> searchHistory;
    public ArrayList<News> searchResult;

    public ArrayList<Channel> channelList;
    public Map<String, News> newsMap;

    User(){
        name = "temp_user";
        password = "";
        historyList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        recommendList = new ArrayList<>();
        blockedKeywords = new ArrayList<>();
        searchHistory = new ArrayList<>();
        channelList = new ArrayList<>();
        newsMap = new HashMap<>();
        channelList.add(new Channel("娱乐"));
        channelList.add(new Channel("军事"));
        channelList.add(new Channel("教育"));
        channelList.add(new Channel("文化"));
        channelList.add(new Channel("健康"));
        channelList.add(new Channel("财经"));
        channelList.add(new Channel("体育"));
        channelList.add(new Channel("汽车"));
        channelList.add(new Channel("科技"));
        channelList.add(new Channel("社会"));
    }

    public ArrayList<News> get(final String channelName){
        for(int i = 0; i < this.channelList.size(); i++){
            Channel curChannel = this.channelList.get(i);
            if(curChannel.getName().equals(channelName)){
                return curChannel.get(newsMap);
            }
        }
        return new ArrayList<News>();
    }
    public ArrayList<News> refresh(final String channelName){

        for(int i = 0; i < this.channelList.size(); i++) {
            Channel curChannel = this.channelList.get(i);
            if (curChannel.getName().equals(channelName)) {
                return curChannel.refresh(newsMap);
            }
        }

        return new ArrayList<News>();
    }
    public ArrayList<News> loadMore(final String channelName){
        for(int i = 0; i < this.channelList.size(); i++){
            Channel curChannel = this.channelList.get(i);
            if(curChannel.getName().equals(channelName)){
                return curChannel.loadMore();
            }
        }
        return new ArrayList<News>();
    }

};