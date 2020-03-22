package com.developer.rjtech.pdsbmsceserver.Models;

public class Year {



    private String Name;
    private String Image;
    private String detail;


    public Year() {

    }

    public Year(String name, String image) {
        Name = name;
        Image = image;
    }

    public Year(String name, String image, String detail) {
        Name = name;
        Image = image;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        return "Year{" +
                "Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
