package com.hp.householdpolicies.model;

public class Police {
    private String id;
    private String name;//姓名
    private String img;//头像
    private String  siren;//警号
    private Integer  dayOfWeek;//接收预约日

    public Police(String id, String name, String img, String siren, Integer dayOfWeek) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.siren = siren;
        this.dayOfWeek = dayOfWeek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiren() {
        return siren;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
