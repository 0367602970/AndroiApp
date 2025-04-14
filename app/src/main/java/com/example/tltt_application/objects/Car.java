package com.example.tltt_application.objects;

public class Car {
    private String imageUrl;
    private String name;
    private int price;
    private String tidy;
    private String seats;
    private String trunk;
    private String kind;

    public Car() {
    }

    public Car( String imageUrl, String name, int price, String tidy, String seats, String trunk, String kind) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.tidy = tidy;
        this.seats = seats;
        this.trunk = trunk;
        this.kind = kind;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTidy() {
        return tidy;
    }

    public void setTidy(String tidy) {
        this.tidy = tidy;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTrunk() {
        return trunk;
    }

    public void setTrunk(String trunk) {
        this.trunk = trunk;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
