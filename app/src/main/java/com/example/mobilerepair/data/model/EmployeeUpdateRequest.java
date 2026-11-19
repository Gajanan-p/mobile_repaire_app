package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeUpdateRequest {
    @SerializedName("EmplID")
    private int emplID;

    @SerializedName("address")
    private String address;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("middle_name")
    private String middleName;

    public EmployeeUpdateRequest(int emplID, String address, String firstName, String lastName, String middleName) {
        this.emplID = emplID;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public int getEmplID() {
        return emplID;
    }

    public String getAddress() {
        return address;
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
}
