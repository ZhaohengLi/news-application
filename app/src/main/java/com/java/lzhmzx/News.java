package com.java.lzhmzx;
import java.io.Serializable;

public class News implements Serializable{
    private String title;
    private String description;
    private int photoId;

    public News(String name, String age, int photoId) {
        this.title=name;
        this.description=age;
        this.photoId=photoId;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getDescription() {
        return description;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getTitle() {
        return title;
    }
}
