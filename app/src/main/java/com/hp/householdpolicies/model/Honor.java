package com.hp.householdpolicies.model;

public class Honor {
    private Boolean IsShow;
    private String title;
    private String image;
    public Honor(Boolean isShow) {
        IsShow = isShow;
    }

    public Boolean getShow() {
        return IsShow;
    }

    public void setShow(Boolean show) {
        IsShow = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
