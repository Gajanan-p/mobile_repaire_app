package com.example.mobilerepair.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.EmployeeItem;
import com.example.mobilerepair.data.model.EmployeeRequest;
import com.example.mobilerepair.data.model.EmployeeUpdateRequest;
import com.example.mobilerepair.ui.adapter.EmployeeAdapter;
import com.example.mobilerepair.viewmodel.RepairViewModel;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;
    private RepairViewModel repairViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Admin Dashboard - Employees");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        repairViewModel = new ViewModelProvider(this).get(RepairViewModel.class);

        Button btnAddEmployeeBottom = findViewById(R.id.btnAddEmployeeBottom);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);

        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        employeeAdapter = new EmployeeAdapter(employeeItem -> {
            showUpdateEmployeeDialog(employeeItem);
        });
        recyclerViewEmployees.setAdapter(employeeAdapter);

        repairViewModel.getEmployeeList().observe(this, employeeItems -> {
            if (employeeItems != null) {
                employeeAdapter.setEmployeeList(employeeItems);
            }
        });

        repairViewModel.getEmployeeActionMessage().observe(this, msg -> {
            Toast.makeText(AdminActivity.this, msg, Toast.LENGTH_LONG).show();
        });

        repairViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(AdminActivity.this, error, Toast.LENGTH_LONG).show();
        });

        repairViewModel.fetchEmployees();

        btnAddEmployeeBottom.setOnClickListener(v -> {
            showAddEmployeeDialog();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddEmployeeDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_employee, null);
        EditText etFirstName = dialogView.findViewById(R.id.etFirstName);
        EditText etLastName = dialogView.findViewById(R.id.etLastName);
        EditText etMiddleName = dialogView.findViewById(R.id.etMiddleName);
        EditText etAddress = dialogView.findViewById(R.id.etEmployeeAddress);

        new AlertDialog.Builder(AdminActivity.this)
            .setTitle("Add New Employee")
            .setView(dialogView)
            .setPositiveButton("Submit", (dialog, which) -> {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String middleName = etMiddleName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (!firstName.isEmpty()) {
                    EmployeeRequest request = new EmployeeRequest(firstName, lastName, middleName, address);
                    Toast.makeText(AdminActivity.this, "Adding employee...", Toast.LENGTH_SHORT).show();
                    repairViewModel.addEmployee(request);
                } else {
                    Toast.makeText(AdminActivity.this, "First Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showUpdateEmployeeDialog(EmployeeItem item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_employee, null);
        EditText etFirstName = dialogView.findViewById(R.id.etFirstName);
        EditText etLastName = dialogView.findViewById(R.id.etLastName);
        EditText etMiddleName = dialogView.findViewById(R.id.etMiddleName);
        EditText etAddress = dialogView.findViewById(R.id.etEmployeeAddress);

        etFirstName.setText(item.getFirstName());
        etLastName.setText(item.getLastName());
        etMiddleName.setText(item.getMiddleName());
        etAddress.setText(item.getAddress());

        new AlertDialog.Builder(AdminActivity.this)
            .setTitle("Update Employee (ID: " + item.getEmplID() + ")")
            .setView(dialogView)
            .setPositiveButton("Update", (dialog, which) -> {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String middleName = etMiddleName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (!firstName.isEmpty()) {
                    EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                        item.getEmplID(),
                        address,
                        firstName,
                        lastName,
                        middleName
                    );
                    Toast.makeText(AdminActivity.this, "Updating employee...", Toast.LENGTH_SHORT).show();
                    repairViewModel.updateEmployee(request);
                } else {
                    Toast.makeText(AdminActivity.this, "First Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
