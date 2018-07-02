package com.hp.householdpolicies.model;

public class AdvisorySearch {
    private String name;//名称
    private boolean IsRecommend=false;//是否推荐

    public AdvisorySearch(String name, boolean isRecommend) {
        this.name = name;
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
}
