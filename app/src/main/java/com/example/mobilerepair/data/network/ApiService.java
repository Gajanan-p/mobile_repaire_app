package com.example.mobilerepair.data.network;

import com.example.mobilerepair.data.model.EmployeeItem;
import com.example.mobilerepair.data.model.EmployeeRequest;
import com.example.mobilerepair.data.model.EmployeeResponse;
import com.example.mobilerepair.data.model.EmployeeUpdateRequest;
import com.example.mobilerepair.data.model.RepairItem;
import com.example.mobilerepair.data.model.RepairRequest;
import com.example.mobilerepair.data.model.RepairResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("add_repaire_collections")
    Call<RepairResponse> addRepairCollection(@Body RepairRequest request);

    @GET("get_all_repair_collections")
    Call<List<RepairItem>> getAllRepairCollections();

    @GET("get_all_empl")
    Call<List<EmployeeItem>> getAllEmployees();

    @POST("add_save_emp")
    Call<EmployeeResponse> addEmployee(@Body EmployeeRequest request);

    @POST("update_empl")
    Call<EmployeeResponse> updateEmployee(@Body EmployeeUpdateRequest request);
}
