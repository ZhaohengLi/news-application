package com.java.lzhmzx;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class News implements Parcelable {
    private String title = "新闻标题";
    private String description = "新闻描述";
    private int pictureId = R.mipmap.footer;
    private boolean isRead = false;
    private boolean isFavorite = false;
    private boolean isBlocked = false;
    private String time = "XXXX年XX月XX日";
    private String origin = "清华大学";
    private String newsID;
    public String keywords;
    private String imageUrl;
    private String videoUrl;
    private Bitmap bitmap;


    public News(JSONObject JsonNews)throws Exception {
        this.title = JsonNews.getString("title");
        description = JsonNews.getString("content");
        this.pictureId = R.mipmap.footer;
        this.isRead = false;
        this.isFavorite = false;
        this.isBlocked = false;
        this.time = JsonNews.getString("publishTime");
        this.origin = JsonNews.getString("publisher");
        this.newsID = JsonNews.getString("newsID");
        this.keywords = JsonNews.getJSONArray("keywords").getJSONObject(0).getString("word");
        this.imageUrl = JsonNews.getString("image");
        this.imageUrl = this.imageUrl.substring(1, this.imageUrl.length()-1);
        this.videoUrl = JsonNews.getString("video");
        int temp = this.imageUrl.indexOf(',');
        if(temp >= 0){
            this.imageUrl = this.imageUrl.substring(0, temp);
        }
        this.bitmap = null;
    }
    public News(String str){
        int cur = 0;
        int next = str.indexOf('\n', cur+1);
        this.title = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.time = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.origin = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.newsID = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.keywords = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.imageUrl = str.substring(cur, next);
        cur = next+1;
        next = str.indexOf('\n', cur);
        this.videoUrl = str.substring(cur, next);
        cur = next+1;
        this.description = str.substring(cur);
        this.isRead = false;
        this.isFavorite = false;
        this.isBlocked = false;
        this.bitmap = null;
        this.pictureId = R.mipmap.footer;
    }
    public News(){}

    public void show(){
        System.out.println("title = "+ this.title);
        System.out.println("time = "+ this.time);
        System.out.println("newsID = "+ this.newsID);
        System.out.println("description = "+ this.description);
    }

    public String toString(){
        StringBuilder sb  = new StringBuilder();
        sb.append(title + "\n");
        sb.append(time + "\n");
        sb.append(origin + "\n");
        sb.append(newsID + "\n");
        sb.append(keywords + "\n");
        sb.append(imageUrl + "\n");
        sb.append(videoUrl + "\n");
        sb.append(description);
        return sb.toString();
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

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNewsID(){return this.newsID;}

    public boolean getIsRead() { return isRead; }

    public boolean getIsFavorite(){ return this.isFavorite;}

    public boolean getIsBlocked(){ return this.isBlocked;}

    public void changeRead() {this.isRead = !this.isRead;}

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
        parcel.writeByte((byte) (isRead?1:0));
        parcel.writeByte((byte) (isFavorite?1:0));
        parcel.writeByte((byte) (isBlocked?1:0));
        parcel.writeString(time);
        parcel.writeString(origin);
        parcel.writeString(newsID);
        parcel.writeString(imageUrl);
        parcel.writeString(videoUrl);
    }

    public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
        public News createFromParcel(Parcel source) {
            News news = new News();
            news.title = source.readString();
            news.description = source.readString();
            news.pictureId = source.readInt();
            news.isRead = source.readByte() != 0;
            news.isFavorite = source.readByte() != 0;
            news.isBlocked = source.readByte() != 0;
            news.time = source.readString();
            news.origin = source.readString();
            news.newsID = source.readString();
            news.imageUrl = source.readString();
            news.videoUrl = source.readString();
            return news;
        }
        public News[] newArray(int size) {
            return new News[size];
        }
    };

}
