package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-05-16.
 */

public class FirstOrapin {

    private String address;
    private Pack pack;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    @Override
    public String toString() {
        return "FirstOrapin{" +
                "address='" + address + '\'' +
                ", pack=" + pack +
                '}';
    }
}
