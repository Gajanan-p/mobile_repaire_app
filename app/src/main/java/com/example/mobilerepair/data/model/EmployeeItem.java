package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeItem {
    @SerializedName("EmplID")
    private int emplID;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("MiddleName")
    private String middleName;

    @SerializedName("Address")
    private String address;

    public int getEmplID() {
        return emplID;
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

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) sb.append(firstName);
        if (middleName != null && !middleName.trim().isEmpty()) sb.append(" ").append(middleName);
        if (lastName != null && !lastName.trim().isEmpty()) sb.append(" ").append(lastName);
        return sb.toString().trim();
    }
}
