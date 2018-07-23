package com.hp.householdpolicies.model;

public class StaffFaculty {
    private String photo;//图片路径
    private String realName;//姓名
    private String roleName;//职务
    private String pno;//警号

    public StaffFaculty(String photo, String realName, String roleName, String pno) {
        this.photo = photo;
        this.realName = realName;
        this.roleName = roleName;
        this.pno = pno;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }
}
