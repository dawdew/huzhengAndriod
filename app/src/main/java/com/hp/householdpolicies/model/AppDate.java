package com.hp.householdpolicies.model;

import java.util.Date;

/**
 * 预约时间
 */

public class AppDate {
    private String dateStr;
    private Date day;
    private Boolean available;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public AppDate() {
    }

    public AppDate(String dateStr, Date day, Boolean available) {
        this.dateStr = dateStr;
        this.day = day;
        this.available = available;
    }
}
