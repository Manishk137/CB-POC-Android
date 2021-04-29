
package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveData {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("PermittedCount")
    @Expose
    private Integer permittedCount;
    @SerializedName("AvailedCount")
    @Expose
    private Integer availedCount;
    @SerializedName("Id")
    @Expose
    private Long id;
    @SerializedName("Unit")
    @Expose
    private String unit;
    @SerializedName("BalanceCount")
    @Expose
    private Integer balanceCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPermittedCount() {
        return permittedCount;
    }

    public void setPermittedCount(Integer permittedCount) {
        this.permittedCount = permittedCount;
    }

    public Integer getAvailedCount() {
        return availedCount;
    }

    public void setAvailedCount(Integer availedCount) {
        this.availedCount = availedCount;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getBalanceCount() {
        return balanceCount;
    }

    public void setBalanceCount(Integer balanceCount) {
        this.balanceCount = balanceCount;
    }

}
