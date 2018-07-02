package com.hp.householdpolicies.model;

public class OptimalPushResult {
    private String name;//名称
    private String id;
    private boolean IsRecommend=false;

    public OptimalPushResult(String id,String name, boolean isRecommend) {
        this.name = name;
        this.id = id;
        IsRecommend = isRecommend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRecommend() {
        return IsRecommend;
    }

    public void setRecommend(boolean recommend) {
        IsRecommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
