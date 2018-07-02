package com.hp.householdpolicies.model;

public class LogisticsInformation {
    private String content;//物流内容
    private String time;//时间

    public LogisticsInformation(String content, String time) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
