package com.example.bookadmin.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-05-22.
 */

public class BookScreen {

    List<ScreenPublish>publishes;
    List<String>authors;
    List<String>title;

    public List<ScreenPublish> getPublishes() {
        return publishes;
    }

    public void setPublishes(List<ScreenPublish> publishes) {
        this.publishes = publishes;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }
}
