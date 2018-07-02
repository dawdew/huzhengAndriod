package com.hp.householdpolicies.model;

public class Salesman {
    private String name;//姓名
    private String  Siren;//警号

    public Salesman(String name, String siren) {
        this.name = name;
        Siren = siren;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiren() {
        return Siren;
    }

    public void setSiren(String siren) {
        Siren = siren;
    }
}
