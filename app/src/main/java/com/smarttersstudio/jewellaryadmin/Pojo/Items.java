package com.smarttersstudio.jewellaryadmin.Pojo;

public class Items {
    private String image;
    private String name;
    private String sold;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public Items(String image, String name, String sold) {

        this.image = image;
        this.name = name;
        this.sold = sold;
    }

    public Items() {

    }
}
