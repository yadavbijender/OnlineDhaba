package com.example.bijen.onlinedhaba.app;

public class UserItemList {
    String name, description, image, prize, hotel, location;

    public UserItemList() {
    }

    public UserItemList(String name, String description, String image, String prize, String hotel, String location) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.prize = prize;
        this.hotel = hotel;
        this.location = location;
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

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
