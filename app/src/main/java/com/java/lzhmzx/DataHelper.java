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
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.*;

public class DataHelper {

    private static User curUser = new User();

    public static ArrayList<String> getChannelArrayListAdded() {return curUser.channelNameList; }

    public static ArrayList<String> getChannelArrayListNotAdded() {return curUser.channelHidenNameList; }

    public static void setChannelArrayListAdded(ArrayList<String> channelArrayListAdded) {
        curUser.channelNameList = new ArrayList<>();
        for(int i = 0; i < channelArrayListAdded.size(); i++){
            curUser.channelNameList.add(channelArrayListAdded.get(i));
        }
        userPrint();
    }

    public static void setChannelArrayListNotAdded(ArrayList<String> channelArrayListNotAdded){
        curUser.channelHidenNameList = new ArrayList<>();
        for(int i = 0; i < channelArrayListNotAdded.size(); i++){
            curUser.channelHidenNameList.add(channelArrayListNotAdded.get(i));
        }
        userPrint();
    }

    public static void changeUser(final String userName){
        System.out.println("changeUser "+userName);
        userPrint();
        if(userName == ""){
            return;
        }
        String str = null;
        curUser.name = userName;
        try {
            for(News value: curUser.newsMap.values()){
                if(value.getIsRead()){
                    value.changeRead();
                }
                if(value.getIsFavorite()){
                    value.changeFavorite();
                }
                if(value.getIsBlocked()){
                    value.changeBlock();
                }
            }
            str = FileUtilities.read(userName + ".txt");
            //System.out.println(str);
            String[] Lists = str.split("\n");
            curUser.historyList = new ArrayList<>();
            String[] curList = null;
            if(Lists.length > 0) {
                curList =Lists[0].split(",");
                for (int i = 1; i < curList.length; i++) {
                    String newsID = curList[i];
                    News temp = curUser.newsMap.get(newsID);
                    if (temp == null) {
                        temp = new News(FileUtilities.read(newsID + ".news"));
                        curUser.newsMap.put(newsID, temp);
                    }
                    if (!temp.getIsRead()) {
                        temp.changeRead();
                    }
                    curUser.historyList.add(temp);
                }
            }
            curUser.favoriteList = new ArrayList<>();
            if(Lists.length > 1){
                curList = Lists[1].split(",");
                for(int i = 1; i < curList.length; i++){
                    String newsID = curList[i];
                    News temp = curUser.newsMap.get(newsID);
                    if(temp == null){
                        temp = new News(FileUtilities.read(newsID + ".news"));
                        curUser.newsMap.put(newsID, temp);
                    }
                    if(!temp.getIsFavorite()){
                        temp.changeFavorite();
                    }
                    curUser.favoriteList.add(temp);
                }
            }
            curUser.blockList = new ArrayList<>();
            if(Lists.length>2) {
                curList = Lists[2].split(",");
                for (int i = 1; i < curList.length; i++) {
                    String newsID = curList[i];
                    News temp = curUser.newsMap.get(newsID);
                    if (temp == null) {
                        temp = new News(FileUtilities.read(newsID + ".news"));
                        curUser.newsMap.put(newsID, temp);
                    }
                    if (!temp.getIsBlocked()) {
                        temp.changeBlock();
                    }
                    curUser.blockList.add(temp);
                }
            }
            curUser.searchHistory = new ArrayList<>();
            curUser.channelNameList = new ArrayList<>();
            curUser.channelHidenNameList = new ArrayList<>();
            if(Lists.length>3) {
                curList = Lists[3].split(",");
                for (int i = 1; i < curList.length; i++) {
                    curUser.searchHistory.add(new HistorySuggestion(curList[i]));
                }
                curList = Lists[4].split(",");
                for (int i = 1; i < curList.length; i++) {
                    curUser.channelNameList.add(curList[i]);
                }
                curList = Lists[5].split(",");
                for (int i = 1; i < curList.length; i++) {
                    curUser.channelHidenNameList.add(curList[i]);
                }
            } else {
                curUser.channelNameList = new ArrayList<>(Arrays.asList("娱乐","军事","教育","文化","健康","财经","体育","汽车","科技","社会"));
            }
        } catch(Exception e){
            curUser.historyList = new ArrayList<>();
            curUser.favoriteList = new ArrayList<>();
            curUser.blockList = new ArrayList<>();
            curUser.searchHistory = new ArrayList<>();
            curUser.channelNameList = new ArrayList<>(Arrays.asList("娱乐","军事","教育","文化","健康","财经","体育","汽车","科技","社会"));
            curUser.channelHidenNameList = new ArrayList<>();
        }
        userPrint();
    }

    public static void userPrint(){
        StringBuilder sb = new StringBuilder();
        ArrayList<News> curList = curUser.historyList;
        sb.append("xxx");
        for(int i = 0; i < curList.size(); i++){
            sb.append(",");
            sb.append(curList.get(i).getNewsID());
        }
        sb.append("\n");
        sb.append("xxx");
        curList = curUser.favoriteList;
        for(int i = 0; i < curList.size(); i++){
            sb.append(",");
            sb.append(curList.get(i).getNewsID());
        }
        sb.append("\n");
        sb.append("xxx");
        curList = curUser.blockList;
        for(int i = 0; i < curList.size(); i++){
            sb.append(",");
            sb.append(curList.get(i).getNewsID());
        }
        sb.append("\n");
        sb.append("xxx");
        ArrayList<HistorySuggestion> searchList = curUser.searchHistory;
        for(int i = 0; i < searchList.size(); i++){
            sb.append(",");
            sb.append(searchList.get(i).getBody());
        }
        sb.append("\n");
        sb.append("xxx");
        ArrayList<String> strList = curUser.channelNameList;
        for(int i = 0; i < strList.size(); i++){
            sb.append(",");
            sb.append(strList.get(i));
        }
        sb.append("\n");
        sb.append("xxx");
        strList = curUser.channelHidenNameList;
        for(int i = 0; i < strList.size(); i++){
            sb.append(",");
            sb.append(strList.get(i));
        }
        try {
            FileUtilities.save(curUser.name + ".txt", sb.toString());
        } catch(Exception e){

        }
    }

    public static ArrayList<News> reqNews(final String size, final String startDate, final String endDate, final String words, final String categories){
        ArrayList<News> ret = new ArrayList<>();
        try {
            String request_url = "https://api2.newsminer.net/svc/news/queryNewsList?";
            request_url += "size=" + size;
            request_url += "&startDate=" + startDate;
            request_url += "&endDate=" + endDate;
            request_url += "&words=" + words;
            request_url += "&categories=" + categories;
            URL url = new URL(request_url);
            InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
            BufferedReader input = new BufferedReader(isr);
            StringBuilder result = new StringBuilder();
            String inputLine;
            while((inputLine = input.readLine()) != null){
                result.append(inputLine);
            }
            input.close();

            String JsonStr = result.toString();
            //System.out.println("return " + JsonStr.length() + " words");
            JSONObject newsJson = new JSONObject(new JSONTokener(JsonStr));
            JSONArray JsonNewsArray = newsJson.getJSONArray("data");

            for (int i = 0; i < JsonNewsArray.length(); i++) {
                JSONObject JsonNews = JsonNewsArray.getJSONObject(i);
                News tNews = new News(JsonNews);

                News aNews = curUser.newsMap.get(tNews.getNewsID());
                if(aNews != null){
                    ret.add(aNews);
                } else {
                    ret.add(tNews);
                    try{
                        FileUtilities.save(tNews.getNewsID()+".news", tNews.toString());
                    } catch(Exception e){

                    }
                }
            }
            //System.out.println(ret.size());
        }catch(Exception e){
            //System.out.println(e);
        }
        return ret;
    }

    public static ArrayList<News> getNewsArrayList(String channel){ return curUser.get(channel); }

    public static ArrayList<News> getRefreshedNewsArrayList(String channel){ return curUser.refresh(channel); }

    public static ArrayList<News> getMoreNewsArrayList(String channel){ return curUser.loadMore(channel); }

    public static ArrayList<News> getFavoriteArrayList(){
        ArrayList<News> ret = new ArrayList<>();
        for(int i = 0; i < curUser.favoriteList.size(); i++){
            ret.add(curUser.favoriteList.get(i));
        }
        return ret;
    }

    public static ArrayList<News> getHistoryArrayList(){
        ArrayList<News> ret = new ArrayList<>();
        for(int i = curUser.historyList.size()-1; i >= 0; i--){
            ret.add(curUser.historyList.get(i));
        }
        return ret;
    }

    public static void addToFavorite(News news){
        //System.out.println("Add " + news.getNewsID());
        curUser.favoriteList.add(news);
    }

    public static void removeFromFavorite(News news){
        //System.out.println("Remove " + news.getNewsID());
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
        userPrint();
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
        userPrint();
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
        userPrint();
    }

    public static ArrayList<News> getRecommendArrayList(){
        return curUser.recommend();
    }

    public static ArrayList<HistorySuggestion> getHistorySuggestionArrayList(){ return curUser.searchHistory; }

    public static void addToHistorySuggestion(HistorySuggestion historySuggestion){
        String body = historySuggestion.getBody();
        for(int i = 0; i < curUser.searchHistory.size(); i++) {
            if (curUser.searchHistory.get(i).getBody().equals(body)) {
                curUser.searchHistory.remove(i);
                break;
            }
        }
        curUser.searchHistory.add(historySuggestion);
        userPrint();
    }

    public static ArrayList<News> getSearchResultArrayList(String keyword){ return curUser.search(keyword); }


    public static int logIn(String userName, String userPassword){
        try{
            String string = FileUtilities.read("user.list");
            String[] userNames = string.split("\n");
            for(String item : userNames){
                if(userName.equals(item)) {
                    if (userPassword.equals(item)){
                    changeUser(userName);return 0;
                    }else{
                        return 1;
                    }
                }
            }
        }catch (Exception e){
            //System.out.println("From LogIn "+e);
        }
        return 2;
    }

    public static Boolean register(String userName){
        try{
            String string = FileUtilities.read("user.list");
            string += userName+"\n";
            FileUtilities.save("user.list", string);
            return true;
        }catch (Exception e){
            //System.out.println("From Register "+e);
        }
        return false;
    }

    public static String getUserName(){ return curUser.name; }

    public static void writeUserList(){
        try{
            String string = "lzh\n";
            string += "mzx\n";
            string += "ssk\n";
            FileUtilities.save("user.list", string);
        }catch (Exception e) {System.out.println("From writeUserList "+e);}
    }

}
