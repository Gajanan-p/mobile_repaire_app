package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeRequest {
    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("MiddleName")
    private String middleName;

    @SerializedName("address")
    private String address;

    public EmployeeRequest(String firstName, String lastName, String middleName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getAddress() {
        return address;
    }
}
