package com.example.mobilerepair.data.repository;

import com.example.mobilerepair.data.model.EmployeeItem;
import com.example.mobilerepair.data.model.EmployeeRequest;
import com.example.mobilerepair.data.model.EmployeeResponse;
import com.example.mobilerepair.data.model.EmployeeUpdateRequest;
import com.example.mobilerepair.data.model.RepairItem;
import com.example.mobilerepair.data.model.RepairRequest;
import com.example.mobilerepair.data.model.RepairResponse;
import com.example.mobilerepair.data.network.ApiService;
import com.example.mobilerepair.data.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;

public class RepairRepository {
    private final ApiService apiService;

    public RepairRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public Call<RepairResponse> addRepairCollection(RepairRequest request) {
        return apiService.addRepairCollection(request);
    }

    public Call<List<RepairItem>> getAllRepairCollections() {
        return apiService.getAllRepairCollections();
    }

    public Call<List<EmployeeItem>> getAllEmployees() {
        return apiService.getAllEmployees();
    }

    public Call<EmployeeResponse> addEmployee(EmployeeRequest request) {
        return apiService.addEmployee(request);
    }

    public Call<EmployeeResponse> updateEmployee(EmployeeUpdateRequest request) {
        return apiService.updateEmployee(request);
    }
}
