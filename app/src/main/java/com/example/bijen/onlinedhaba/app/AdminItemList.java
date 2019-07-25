package com.example.bijen.onlinedhaba.app;

public class AdminItemList {

    String name, description, image, prize;

    public AdminItemList(String name, String description, String image, String prize) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.prize = prize;
    }

    public AdminItemList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
