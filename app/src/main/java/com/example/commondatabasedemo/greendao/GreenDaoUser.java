package com.example.commondatabasedemo.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GreenDaoUser {
    @Id(autoincrement = true)//设置主键自增长，而且必须为Long类型
    private Long id;

    private String name;

    private String number;

    private String area;

    public GreenDaoUser(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Generated(hash = 1771973115)
    public GreenDaoUser(Long id, String name, String number, String area) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.area = area;
    }

    @Generated(hash = 83249558)
    public GreenDaoUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
