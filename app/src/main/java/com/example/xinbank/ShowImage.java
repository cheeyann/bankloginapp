package com.example.xinbank;

public class ShowImage {
    public String filename;
    public String imageUrl;

    public ShowImage(){

    }

    public ShowImage(String filename, String imageUrl){
        this.filename = filename;
        this.imageUrl = imageUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
