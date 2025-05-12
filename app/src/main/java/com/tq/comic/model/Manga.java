package com.tq.comic.model;

import java.io.Serializable;

public class Manga  implements Serializable {

    public String title;
    public String author;
    public String chapter;
    public int imageResId;

    public Manga(String title, String author, int imageResId) {
        this.title = title;
        this.author = author;
        this.chapter = "";
        this.imageResId = imageResId;
    }

    public Manga(String title, String author, String chapter, int imageResId) {
        this.title = title;
        this.author = author;
        this.chapter = chapter;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
