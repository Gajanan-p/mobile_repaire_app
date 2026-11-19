package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeResponse {
    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
