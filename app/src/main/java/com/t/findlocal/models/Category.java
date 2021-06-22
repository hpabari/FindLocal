package com.t.findlocal.models;

import android.graphics.Bitmap;

public class Category {
    private String name;
//    private int imageId;
    private Bitmap imageBitmap;

//    public Category(String name, int imageId){
//        this.name = name;
//        this.imageId = imageId;
//    }
    public Category(String name, Bitmap imageBitmap){
        this.name = name;
        this.imageBitmap = imageBitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
