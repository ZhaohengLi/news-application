package com.java.lzhmzx;
import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private String title;
    private String description;
    private int pictureId;

    public News(String name, String age, int photoId) {
        this.title=name;
        this.description=age;
        this.pictureId=photoId;
    }
    public News(){}

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
