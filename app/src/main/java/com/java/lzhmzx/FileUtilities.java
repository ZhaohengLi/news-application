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
    public static Context mContext;

    public static void save(String filename, String filecontent){
        try{
            FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            output.write(filecontent.getBytes());
            output.close();
        }catch (Exception e){
            System.out.println("From save txt file "+e);
        }

    }
    public static void savePicture(String fileName, Bitmap bitmap){
        try{
            FileOutputStream fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
        }catch (Exception e){
            System.out.println("From save picture file "+e);
        }

    }

    public static String read(String filename) throws IOException {
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

    public static Bitmap readPicture(String fileName){
        Bitmap bitmap = null;
        try {
            FileInputStream fileInputStream = mContext.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch(Exception e){
            System.out.println("From read picture file "+e);
        }
        return bitmap;
    }

    public static Bitmap getPictureFromURL(String imageURL){
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            System.out.println("From read picture from URL "+e);
        }
        return bitmap;
    }
}
