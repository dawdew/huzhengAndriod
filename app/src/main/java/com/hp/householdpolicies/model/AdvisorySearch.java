package com.hp.householdpolicies.model;

public class AdvisorySearch {
    private String name;//名称
    private String cname;//分类名称
    private String content;//内容
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
