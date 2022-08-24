package com.finalyear.microproject.model;

public class CategoryModel {

    private int image;
    private  String text;

    public CategoryModel(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
