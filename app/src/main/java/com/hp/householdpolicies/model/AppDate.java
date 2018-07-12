package com.hp.householdpolicies.model;

/**
 * 预约时间
 */

public class AppDate {
    private String dateStr;
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
}
