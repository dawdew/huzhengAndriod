package com.hp.householdpolicies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-07-31.
 */

public class PersonInfo implements Serializable {
    private String name;
    private Date birth;
    private Integer age;
    private String sex;
    private String idcard;
    private String education;
    private String household;//户籍地
    private Boolean householdTj;//户籍地是否天津

    private Boolean house;
    private Boolean housePrice_a;//房价
    private Boolean housePrice_b;//房价
    private Boolean houseLoan;//有无房贷

    private String marriage;
    private String marriageParent;//父母婚姻状态
    private Boolean marriageThree;//结婚时间是否满3年
    private Boolean marriageParentThree;//再婚时间是否满3年
    private Boolean marriageParentThenLessThan18;//再婚时子女不满18岁

    private String special;//特殊身份

    private Boolean children;
    private Boolean childrensHousehold;//所有子女户籍均在津
    private Boolean childrenInfo1;//有无子女小于18岁
    private Boolean childrenInfo2;//有无子女小于22岁未婚
    private Boolean adopt;//收养
    private Boolean adopted;//被收养
    private Boolean adoptCard;//有收养证
    private Boolean adoptAge30;//收养人年满30周岁
    private Boolean adopterHasChild;//收养人有无子女
    private Boolean custody;//有子女抚养权

    private Boolean liveFiveYear;//在津连续居住满五年

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household;
    }

    public Boolean getHouse() {
        return house;
    }

    public void setHouse(Boolean house) {
        this.house = house;
    }


    public Boolean getHouseLoan() {
        return houseLoan;
    }

    public void setHouseLoan(Boolean houseLoan) {
        this.houseLoan = houseLoan;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public Boolean getMarriageThree() {
        return marriageThree;
    }

    public void setMarriageThree(Boolean marriageThree) {
        this.marriageThree = marriageThree;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public Boolean getChildrensHousehold() {
        return childrensHousehold;
    }

    public void setChildrensHousehold(Boolean childrensHousehold) {
        this.childrensHousehold = childrensHousehold;
    }

    public Boolean getAdopted() {
        return adopted;
    }

    public void setAdopted(Boolean adopted) {
        this.adopted = adopted;
    }

    public Boolean getAdoptCard() {
        return adoptCard;
    }

    public void setAdoptCard(Boolean adoptCard) {
        this.adoptCard = adoptCard;
    }

    public Boolean getCustody() {
        return custody;
    }

    public void setCustody(Boolean custody) {
        this.custody = custody;
    }


    public Boolean getLiveFiveYear() {
        return liveFiveYear;
    }

    public void setLiveFiveYear(Boolean liveFiveYear) {
        this.liveFiveYear = liveFiveYear;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Boolean getChildrenInfo1() {
        return childrenInfo1;
    }

    public void setChildrenInfo1(Boolean childrenInfo1) {
        this.childrenInfo1 = childrenInfo1;
    }

    public Boolean getChildrenInfo2() {
        return childrenInfo2;
    }

    public void setChildrenInfo2(Boolean childrenInfo2) {
        this.childrenInfo2 = childrenInfo2;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getAdopt() {
        return adopt;
    }

    public void setAdopt(Boolean adopt) {
        this.adopt = adopt;
    }

    public Boolean getHouseholdTj() {
        return householdTj;
    }

    public void setHouseholdTj(Boolean householdTj) {
        this.householdTj = householdTj;
    }

    public Boolean getAdoptAge30() {
        return adoptAge30;
    }

    public void setAdoptAge30(Boolean adoptAge30) {
        this.adoptAge30 = adoptAge30;
    }

    public Boolean getAdopterHasChild() {
        return adopterHasChild;
    }

    public void setAdopterHasChild(Boolean adopterHasChild) {
        this.adopterHasChild = adopterHasChild;
    }

    public String getMarriageParent() {
        return marriageParent;
    }

    public void setMarriageParent(String marriageParent) {
        this.marriageParent = marriageParent;
    }

    public Boolean getMarriageParentThenLessThan18() {
        return marriageParentThenLessThan18;
    }

    public void setMarriageParentThenLessThan18(Boolean marriageParentThenLessThan18) {
        this.marriageParentThenLessThan18 = marriageParentThenLessThan18;
    }

    public Boolean getMarriageParentThree() {
        return marriageParentThree;
    }

    public void setMarriageParentThree(Boolean marriageParentThree) {
        this.marriageParentThree = marriageParentThree;
    }

    public Boolean getHousePrice_a() {
        return housePrice_a;
    }

    public void setHousePrice_a(Boolean housePrice_a) {
        this.housePrice_a = housePrice_a;
    }

    public Boolean getHousePrice_b() {
        return housePrice_b;
    }

    public void setHousePrice_b(Boolean housePrice_b) {
        this.housePrice_b = housePrice_b;
    }
}
