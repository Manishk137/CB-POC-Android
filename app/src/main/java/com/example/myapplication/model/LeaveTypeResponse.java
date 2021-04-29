
package com.example.myapplication.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveTypeResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<LeaveData> leaveData = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LeaveData> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(List<LeaveData> leaveData) {
        this.leaveData = leaveData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
