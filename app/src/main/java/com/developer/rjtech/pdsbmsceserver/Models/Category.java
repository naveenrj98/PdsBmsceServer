package com.developer.rjtech.pdsbmsceserver.Models;

public class Category {



    private String Name;
    private String Image;
    private String details;


    public Category() {

    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public Category(String name, String image, String details) {
        Name = name;
        Image = image;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public String toString() {
        return "Category{" +
                "Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }
}
