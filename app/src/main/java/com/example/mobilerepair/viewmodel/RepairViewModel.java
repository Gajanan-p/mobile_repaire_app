package com.example.mobilerepair.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mobilerepair.data.model.EmployeeItem;
import com.example.mobilerepair.data.model.EmployeeRequest;
import com.example.mobilerepair.data.model.EmployeeResponse;
import com.example.mobilerepair.data.model.EmployeeUpdateRequest;
import com.example.mobilerepair.data.model.RepairItem;
import com.example.mobilerepair.data.model.RepairRequest;
import com.example.mobilerepair.data.model.RepairResponse;
import com.example.mobilerepair.data.repository.RepairRepository;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairViewModel extends ViewModel {
    private final RepairRepository repository = new RepairRepository();

    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<RepairItem>> repairList = new MutableLiveData<>();
    private final MutableLiveData<List<EmployeeItem>> employeeList = new MutableLiveData<>();
    private final MutableLiveData<String> employeeActionMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<RepairItem>> getRepairList() {
        return repairList;
    }

    public LiveData<List<EmployeeItem>> getEmployeeList() {
        return employeeList;
    }

    public LiveData<String> getEmployeeActionMessage() {
        return employeeActionMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void addRepairCollection(RepairRequest request) {
        isLoading.setValue(true);
        repository.addRepairCollection(request).enqueue(new Callback<RepairResponse>() {
            @Override
            public void onResponse(Call<RepairResponse> call, Response<RepairResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    successMessage.setValue(response.body().getMsg());
                } else {
                    String err;
                    try {
                        if (response.errorBody() != null) {
                            err = response.errorBody().string();
                        } else {
                            err = response.message();
                        }
                    } catch (Exception e) {
                        err = e.getMessage();
                    }
                    errorMessage.setValue(err);
                }
            }

            @Override
            public void onFailure(Call<RepairResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "Unknown error");
            }
        });
    }

    public void fetchRepairCollections() {
        isLoading.setValue(true);
        repository.getAllRepairCollections().enqueue(new Callback<List<RepairItem>>() {
            @Override
            public void onResponse(Call<List<RepairItem>> call, Response<List<RepairItem>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    repairList.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to fetch repair collections");
                }
            }

            @Override
            public void onFailure(Call<List<RepairItem>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "Unknown error");
            }
        });
    }

    public void fetchEmployees() {
        isLoading.setValue(true);
        repository.getAllEmployees().enqueue(new Callback<List<EmployeeItem>>() {
            @Override
            public void onResponse(Call<List<EmployeeItem>> call, Response<List<EmployeeItem>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    employeeList.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to fetch employees");
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeItem>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "Unknown error");
            }
        });
    }

    public void addEmployee(EmployeeRequest request) {
        isLoading.setValue(true);
        repository.addEmployee(request).enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    employeeActionMessage.setValue(response.body().getMsg());
                    fetchEmployees();
                } else {
                    errorMessage.setValue("Failed to add employee");
                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "Unknown error");
            }
        });
    }

    public void updateEmployee(EmployeeUpdateRequest request) {
        isLoading.setValue(true);
        repository.updateEmployee(request).enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    employeeActionMessage.setValue(response.body().getMsg());
                    fetchEmployees();
                } else {
                    errorMessage.setValue("Failed to update employee");
                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "Unknown error");
            }
        });
    }
}
