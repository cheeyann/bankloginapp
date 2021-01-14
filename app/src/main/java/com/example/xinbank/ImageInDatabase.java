package com.example.xinbank;

public class ImageInDatabase {
    private String filename;
    private String imageUrl;

    public ImageInDatabase(){

    }

    public ImageInDatabase (String filename, String imageUrl){
        this.filename = filename;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


}
