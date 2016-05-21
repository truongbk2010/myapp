package tepy.com.myapp;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Truong on 20/5/2016.
 */
public class DailyItem {
   private String date;
   private List<Bitmap> listImages;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Bitmap> getListImages() {
        return listImages;
    }

    public void setListImages(List<Bitmap> listImages) {
        this.listImages = listImages;
    }
}