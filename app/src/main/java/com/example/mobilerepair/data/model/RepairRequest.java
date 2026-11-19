package com.example.mobilerepair.data.model;

import com.google.gson.annotations.SerializedName;

public class RepairRequest {
    @SerializedName("Address")
    private String address;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("EmpName")
    private String empName;

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

    @SerializedName("RepairStatus")
    private String repairStatus;

    @SerializedName("TotalAmount")
    private int totalAmount;

    public RepairRequest(String address, String customerName, String empName, String issueDescription,
                         String mobileCompanyName, String mobileModelNumber, String mobileNumber,
                         int paidAmount, int pendingAmount, String repairStatus, int totalAmount) {
        this.address = address;
        this.customerName = customerName;
        this.empName = empName;
        this.issueDescription = issueDescription;
        this.mobileCompanyName = mobileCompanyName;
        this.mobileModelNumber = mobileModelNumber;
        this.mobileNumber = mobileNumber;
        this.paidAmount = paidAmount;
        this.pendingAmount = pendingAmount;
        this.repairStatus = repairStatus;
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getMobileCompanyName() {
        return mobileCompanyName;
    }

    public void setMobileCompanyName(String mobileCompanyName) {
        this.mobileCompanyName = mobileCompanyName;
    }

    public String getMobileModelNumber() {
        return mobileModelNumber;
    }

    public void setMobileModelNumber(String mobileModelNumber) {
        this.mobileModelNumber = mobileModelNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
