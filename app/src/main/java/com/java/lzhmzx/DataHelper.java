package com.java.lzhmzx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.*;

public class DataHelper {

    public static FileUtilities fileUtilities;

    public static ArrayList<String> getSampleChannelList(){
        ArrayList<String> channelList = new ArrayList<>();
        channelList.add("娱乐");
        channelList.add("军事");
        channelList.add("教育");
        channelList.add("文化");
        channelList.add("健康");
        channelList.add("财经");
        channelList.add("体育");
        channelList.add("汽车");
        channelList.add("科技");
        channelList.add("社会");
        return channelList;
    }

    public static ArrayList<String> getChannelArrayListAdded(){
        return curUser.channelNameList;
    }

    public static ArrayList<String> getChannelArrayListNotAdded(){
        return curUser.channelHidenNameList;
    }

    public static void setChannelArrayListAdded(ArrayList<String> channelArrayListAdded){
        curUser.channelNameList = channelArrayListAdded;
    }

    public static void setChannelArrayListNotAdded(ArrayList<String> channelArrayListNotAdded){
        curUser.channelHidenNameList = channelArrayListNotAdded;
    }

    private static User curUser = new User();

    //访问服务器并获得资源
    public static ArrayList<News> reqNews(final String size, final String startDate, final String endDate, final String words, final String categories){
        ArrayList<News> ret = new ArrayList<>();
        try {
            String request_url = "https://api2.newsminer.net/svc/news/queryNewsList?";
            request_url += "size=" + size;
            request_url += "&startDate=" + startDate;
            request_url += "&endDate=" + endDate;
            request_url += "&words=" + words;
            request_url += "&categories=" + categories;
            System.out.println(request_url);
            URL url = new URL(request_url);
            InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
            System.out.println("reached.");
            BufferedReader input = new BufferedReader(isr);
            StringBuilder result = new StringBuilder();
            String inputLine;
            while((inputLine = input.readLine()) != null){
                result.append(inputLine);
            }
            input.close();

            String JsonStr = result.toString();
            System.out.println("return " + JsonStr.length() + " words");
            JSONObject newsJson = new JSONObject(new JSONTokener(JsonStr));
            JSONArray JsonNewsArray = newsJson.getJSONArray("data");
            for (int i = 0; i < JsonNewsArray.length(); i++) {
                JSONObject JsonNews = JsonNewsArray.getJSONObject(i);
                News tNews = new News(JsonNews);
                //tNews.show();
                News aNews = curUser.newsMap.get(tNews.getNewsID());
                if(aNews != null){
                    ret.add(aNews);
                } else {
                    if(tNews.imageUrl.length() > 1) {
                        try {
                           /* fileUtilities.save(tNews.getNewsID()+".news", tNews.getTitle());
                            String text = fileUtilities.read(tNews.getNewsID()+".news");
                            System.out.println(text);*/
                            /*fileUtilities.savePicture(tNews.getNewsID()+".news", fileUtilities.getPictureFromURL(tNews.imageUrl));
                            tNews.image = fileUtilities.readPicture(tNews.getNewsID()+".news");*/
                        } catch(Exception e){ }
                    }
                    ret.add(tNews);
                }
            }
            System.out.println(ret.size());
        }catch(Exception e){
            System.out.println(e);
        }

        return ret;
    }


    public static ArrayList<News> getNewsArrayList(String channel){
        return curUser.get(channel);
    }   

    public static ArrayList<News> getRefreshedNewsArrayList(String channel){
        return curUser.refresh(channel);
    }

    public static ArrayList<News> getMoreNewsArrayList(String channel){
        return curUser.loadMore(channel);
    }

    public static ArrayList<News> getFavoriteArrayList(){
        return curUser.favoriteList;
    }

    public static ArrayList<News> getHistoryArrayList(){
        //传全局变量的引用
        ArrayList<News> ret = new ArrayList<>();
        for(int i = curUser.historyList.size()-1; i >= 0; i--){
            ret.add(curUser.historyList.get(i));
        }
        return ret;
    }

    public static void addToFavorite(News news){
        System.out.println("Add " + news.getNewsID());
        curUser.favoriteList.add(news);
    }
    public static void removeFromFavorite(News news){
        System.out.println("Remove " + news.getNewsID());
        String nID = news.getNewsID();
        for(int i = 0; i < curUser.favoriteList.size(); i++) {
            if (curUser.favoriteList.get(i).getNewsID().equals(nID)) {
                curUser.favoriteList.remove(i);
                break;
            }
        }
    }

    public static void changeFavorite(String newsID){
        News temp = curUser.newsMap.get(newsID);
        if(temp.getIsFavorite()){
            removeFromFavorite(temp);
        } else {
            addToFavorite(temp);
        }
        temp.changeFavorite();
    }

    public static void addToBlock(News news){
        curUser.blockList.add(news);
    }
    public static void removeFromBlock(News news){
        String nID = news.getNewsID();
        for(int i = 0; i < curUser.blockList.size(); i++) {
            if (curUser.blockList.get(i).getNewsID().equals(nID)) {
                curUser.blockList.remove(i);
                break;
            }
        }
    }

    public static void changeBlock(String newsID){
        News temp = curUser.newsMap.get(newsID);
        if(temp.getIsBlocked()){
            removeFromBlock(temp);
        } else {
            addToBlock(temp);
        }
        temp.changeBlock();
    }

    public static void addToHistory(String newsID){
        News temp = curUser.newsMap.get(newsID);
        for(int i = 0; i < curUser.historyList.size(); i++) {
            if (curUser.historyList.get(i).getNewsID().equals(newsID)) {
                curUser.historyList.remove(i);
                break;
            }
        }
        curUser.historyList.add(temp);
        if(!temp.getIsRead()) temp.changeRead();
    }

    public static ArrayList<News> getRecommendArrayList(){
        return curUser.recommend();
    }

    public static ArrayList<HistorySuggestion> getHistorySuggestionArrayList(){
        /*ArrayList<HistorySuggestion> historySuggestionArrayList = new ArrayList<>();
        historySuggestionArrayList.add(new HistorySuggestion("first"));
        historySuggestionArrayList.add(new HistorySuggestion("second"));
        historySuggestionArrayList.add(new HistorySuggestion("third"));
        return historySuggestionArrayList;*/
        return curUser.searchHistory;
    }

    public static void addToHistorySuggestion(HistorySuggestion historySuggestion){
        //检查之前是否存在 存在要删除
        //新加入的历史记录放在末尾就好
        String body = historySuggestion.getBody();
        for(int i = 0; i < curUser.searchHistory.size(); i++) {
            if (curUser.searchHistory.get(i).getBody().equals(body)) {
                curUser.searchHistory.remove(i);
                break;
            }
        }
        curUser.searchHistory.add(historySuggestion);
    }

    public static ArrayList<News> getSearchResultArrayList(String keyword){
        return curUser.search(keyword);
    }

    public static Boolean isLoggedIn(){
        return true;
    }
    public static Boolean logOut(){
        return false;
    }
    public static Boolean logIn(String userName, String userPassword){
        return true;
    }
    public static Boolean Register(String userName, String userPassword){
        return false;
    }
    public static String getUserName(){
        return "清华大学";
    }

}
