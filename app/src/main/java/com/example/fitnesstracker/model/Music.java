package com.example.fitnesstracker.model;

public class Music {
    private String name;
    private int imageId,musicId;

    public Music(String name, int imageId,int musicId) {
        this.name = name;
        this.imageId = imageId;
        this.musicId = musicId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public int getMusicId() {
        return musicId;
    }
}
