package com.java.lzhmzx;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.*;
import org.json.JSONObject;

public class News implements Parcelable {
    private String title = "新闻标题";
    private String description = "新闻描述";
    private int pictureId = -1;
    private Boolean isRead = false;
    private String time = "XXXX年XX月XX日";
    private String origin = "清华大学";
    private String newsID;
    private Boolean isFavorite;
    private Boolean isBlocked;

    public News(String title, String description, int pictureId, Boolean isRead) {
        this.title=title;
        this.description=description;
        this.pictureId=pictureId;
        this.isRead=isRead;
    }
    public News(JSONObject JsonNews)throws Exception {
        this.title = JsonNews.getString("title");
        description = JsonNews.getString("content");
        this.pictureId = R.mipmap.news_one;
        this.isRead = false;
        this.isFavorite = false;
        this.isBlocked = false;
        this.time = JsonNews.getString("publishTime");
        this.newsID = JsonNews.getString("newsID");
    }
    public News(){}

    public void show(){
        System.out.println("title = "+ this.title);
        System.out.println("time = "+ this.time);
        System.out.println("newsID = "+ this.newsID);
        System.out.println("description = "+ this.description);
    }

    public boolean equals(final News another){
        return this.newsID == another.newsID;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getDescription() {
        return description;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsID(){return this.newsID;}

    public Boolean getIsRead() { return isRead; }

    public Boolean getIsFavorite(){ return this.isFavorite;}

    public Boolean getIsBlocked(){ return this.isBlocked;}

    public void changeFavorite(){ this.isFavorite = !this.isFavorite;}

    public void changeBlock(){ this.isBlocked = !this.isBlocked;}

    public String getTime(){ return time; }

    public String getOrigin(){ return origin; }

    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(pictureId);
    }

    public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
        public News createFromParcel(Parcel source) {
            News news = new News();
            news.title = source.readString();
            news.description = source.readString();
            news.pictureId = source.readInt();
            return news;
        }
        public News[] newArray(int size) {
            return new News[size];
        }
    };

}
