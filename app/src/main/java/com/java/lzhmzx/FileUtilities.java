package com.java.lzhmzx;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;

import javax.net.ssl.HttpsURLConnection;

public class FileUtilities {
    private Context mContext;
    public FileUtilities() {}

    public FileUtilities(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void save(String filename, String filecontent) throws Exception {
        FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
        output.write(filecontent.getBytes());
        output.close();
    }
    public  void savePicture(String fileName, Bitmap bitmap) throws Exception{
        FileOutputStream fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
        fileOutputStream.close();
    }


    public String read(String filename) throws IOException {
        FileInputStream input = mContext.openFileInput(filename);
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }
        input.close();
        return sb.toString();
    }

    public Bitmap readPicture(String fileName) throws IOException{
        FileInputStream fileInputStream = new FileInputStream(fileName);
        return BitmapFactory.decodeStream(fileInputStream);
    }

    public Bitmap getPictureFromURL(String imageURL){
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageURL).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }




}
