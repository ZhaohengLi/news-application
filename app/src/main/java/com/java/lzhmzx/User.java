package com.java.lzhmzx;

import java.util.*;

public class User {

    public String name;
    private String password;

    public ArrayList<News> historyList;
    public ArrayList<News> favoriteList;
    public ArrayList<News> recommendList;
    public ArrayList<News> blockList;
    public ArrayList<HistorySuggestion> searchHistory;
    public ArrayList<News> searchResult;

    public ArrayList<String> channelNameList;
    public ArrayList<String> channelHidenNameList;
    public ArrayList<Channel> channelList;
    public Map<String, News> newsMap;

    User(){
        name = "temp_user";
        password = "";
        historyList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        recommendList = new ArrayList<>();
        blockList = new ArrayList<>();
        searchHistory = new ArrayList<>();
        channelList = new ArrayList<>();
        newsMap = new HashMap<>();
        channelNameList = new ArrayList<>(Arrays.asList("娱乐","军事","教育","文化","健康","财经","体育","汽车","科技","社会"));
        channelHidenNameList = new ArrayList<>();
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



    private ArrayList<News> block(ArrayList<News> displayList){
        ArrayList<News> ret = new ArrayList<>();
        for(int i = 0; i < displayList.size(); i++){
            News temp = displayList.get(i);
            boolean flag = true;
            for(int j = 0; j < blockList.size(); j++){
                if(temp.keywords.equals(blockList.get(j).keywords)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                ret.add(temp);
            }
        }
        return ret;
    }

    public ArrayList<News> get(final String channelName){
        for(int i = 0; i < this.channelList.size(); i++){
            Channel curChannel = this.channelList.get(i);
            if(curChannel.getName().equals(channelName)){
                return block(curChannel.get(newsMap));
            }
        }
        return new ArrayList<News>();
    }
    public ArrayList<News> refresh(final String channelName){

        for(int i = 0; i < this.channelList.size(); i++) {
            Channel curChannel = this.channelList.get(i);
            if (curChannel.getName().equals(channelName)) {
                return block(curChannel.refresh(newsMap));
            }
        }

        return new ArrayList<News>();
    }
    public ArrayList<News> loadMore(final String channelName){
        for(int i = 0; i < this.channelList.size(); i++){
            Channel curChannel = this.channelList.get(i);
            if(curChannel.getName().equals(channelName)){
                return block(curChannel.loadMore());
            }
        }
        return new ArrayList<News>();
    }

    public ArrayList<News> search(final String keywords){
        searchResult = DataHelper.reqNews("","","2019-9-6",keywords,"");
        for(int i = 0; i < searchResult.size(); i++){
            News temp = searchResult.get(i);
            newsMap.put(temp.getNewsID(), temp);
        }
        return searchResult;
    }

    public ArrayList<News> recommend(){
        String keywords = null;
        if(favoriteList.size()>0){
            Random r = new Random();
            keywords = favoriteList.get(r.nextInt(favoriteList.size())).keywords;
        }
        recommendList = search(keywords);
        return recommendList;
    }
}