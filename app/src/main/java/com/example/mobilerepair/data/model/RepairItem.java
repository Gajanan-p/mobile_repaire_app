package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class RepairItem {
    @SerializedName("Address")
    private String address;

    @SerializedName("ByEmpName")
    private String byEmpName;

    @SerializedName("CreatedDate")
    private String createdDate;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("IssueDescription")
    private String issueDescription;

    @SerializedName("MobileCompanyName")
    private String mobileCompanyName;

    @SerializedName("MobileModelNumber")
    private String mobileModelNumber;

    @SerializedName("MobileNumber")
    private String mobileNumber;

    @SerializedName("PaidAmount")
    private int paidAmount;

    @SerializedName("PendingAmount")
    private int pendingAmount;

    @SerializedName("RepairCollID")
    private int repairCollID;

    @SerializedName("RepairStatus")
    private String repairStatus;

    @SerializedName("TotalAmount")
    private int totalAmount;

    @SerializedName("UpdatedDate")
    private String updatedDate;

    public String getAddress() {
        return address;
    }

    public String getByEmpName() {
        return byEmpName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getMobileCompanyName() {
        return mobileCompanyName;
    }

    public String getMobileModelNumber() {
        return mobileModelNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public int getRepairCollID() {
        return repairCollID;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
