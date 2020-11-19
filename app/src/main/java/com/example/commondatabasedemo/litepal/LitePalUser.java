package com.example.commondatabasedemo.litepal;

import org.litepal.crud.LitePalSupport;

public class LitePalUser extends LitePalSupport {
    public String name;
    public String number;
    public String area;

    public LitePalUser() {
    }

    public LitePalUser(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
