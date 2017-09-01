package com.example.bookadmin.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-05-16.
 */

public class Pack {

    private String packId;
    private String packName;
    private List<String>book_id;

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public List<String> getBook_id() {
        return book_id;
    }

    public void setBook_id(List<String> book_id) {
        this.book_id = book_id;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "packId='" + packId + '\'' +
                ", packName='" + packName + '\'' +
                ", book_id=" + book_id +
                '}';
    }
}
