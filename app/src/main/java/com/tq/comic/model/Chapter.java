package com.tq.comic.model;

import java.io.Serializable;

public class Chapter  implements Serializable {
    private String number;
    private String title;
    private String pageCount;
    private boolean isRead;

    private boolean allowRead;

    public Chapter(String number, String title, String pageCount, boolean isRead) {
        this.number = number;
        this.title = title;
        this.pageCount = pageCount;
        this.isRead = isRead;
    }

    public Chapter(String number, String title, String pageCount, boolean isRead, boolean allowRead) {
        this.number = number;
        this.title = title;
        this.pageCount = pageCount;
        this.isRead = isRead;
        this.allowRead = allowRead;
    }

    public String getNumber() { return number; }
    public String getTitle() { return title; }
    public String getPageCount() { return pageCount; }
    public boolean isRead() { return isRead; }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isAllowRead() {
        return allowRead;
    }
}
