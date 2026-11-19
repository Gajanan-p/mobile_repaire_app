package com.example.mobilerepair.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.EmployeeItem;
import com.example.mobilerepair.data.model.RepairItem;
import com.example.mobilerepair.data.model.RepairRequest;
import com.example.mobilerepair.ui.adapter.RepairAdapter;
import com.example.mobilerepair.viewmodel.RepairViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RepairViewModel repairViewModel;
    private RepairAdapter repairAdapter;
    private List<RepairItem> fullRepairList = new ArrayList<>();
    private List<EmployeeItem> employeeItemList = new ArrayList<>();
    private String currentSearchQuery = "";
    private String currentStatusFilter = "All";
    private RepairRequest lastSubmittedRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repairViewModel = new ViewModelProvider(this).get(RepairViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        MenuItem placeholderItem = bottomNavigationView.getMenu().findItem(R.id.nav_placeholder);
        if (placeholderItem != null) {
            placeholderItem.setEnabled(false);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRepairs);
        EditText etSearch = findViewById(R.id.etSearch);
        ImageButton btnFilter = findViewById(R.id.btnFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repairAdapter = new RepairAdapter(item -> {
            Toast.makeText(MainActivity.this, "Update clicked for: " + item.getCustomerName() + " (ID: " + item.getRepairCollID() + ")", Toast.LENGTH_LONG).show();
        });
        recyclerView.setAdapter(repairAdapter);

        repairViewModel.getSuccessMessage().observe(this, msg -> {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            if (lastSubmittedRequest != null) {
                String message = buildWhatsAppMessage(lastSubmittedRequest);
//                sendWhatsAppMessage(lastSubmittedRequest.getMobileNumber(), message);
                sendInformationToWhatsApp(lastSubmittedRequest.getMobileNumber(), message);
                lastSubmittedRequest = null;
            }
            repairViewModel.fetchRepairCollections();
        });

        repairViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        });

        repairViewModel.getRepairList().observe(this, repairItems -> {
            if (repairItems != null) {
                fullRepairList = repairItems;
                applyFilters();
            }
        });

        repairViewModel.getEmployeeList().observe(this, employees -> {
            if (employees != null) {
                employeeItemList = employees;
            }
        });

        repairViewModel.fetchRepairCollections();
        repairViewModel.fetchEmployees();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim().toLowerCase();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnFilter.setOnClickListener(v -> {
            String[] statusOptions = {
                "All Statuses",
                "Received for repairing",
                "Repairing completed",
                "Mobile delivered to customer"
            };

            int checkedItem = 0;
            if (currentStatusFilter.equals("Received for repairing")) checkedItem = 1;
            else if (currentStatusFilter.equals("Repairing completed")) checkedItem = 2;
            else if (currentStatusFilter.equals("Mobile delivered to customer")) checkedItem = 3;

            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Filter by Status")
                .setSingleChoiceItems(statusOptions, checkedItem, (dialog, which) -> {
                    if (which == 0) currentStatusFilter = "All";
                    else if (which == 1) currentStatusFilter = "Received for repairing";
                    else if (which == 2) currentStatusFilter = "Repairing completed";
                    else if (which == 3) currentStatusFilter = "Mobile delivered to customer";

                    applyFilters();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_daily) {
                showReportLoginDialog();
                return true;
            } else if (id == R.id.nav_admin) {
                showAdminLoginDialog();
                return true;
            }
            return false;
        });

        fab.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_repair, null);
            EditText etCustomerName = dialogView.findViewById(R.id.etCustomerName);
            EditText etMobileNumber = dialogView.findViewById(R.id.etMobileNumber);
            EditText etMobileCompany = dialogView.findViewById(R.id.etMobileCompany);
            EditText etAddress = dialogView.findViewById(R.id.etAddress);
            EditText etIssueDescription = dialogView.findViewById(R.id.etIssueDescription);
            EditText etTotalAmount = dialogView.findViewById(R.id.etTotalAmount);
            Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);
            Spinner spinnerEmployee = dialogView.findViewById(R.id.spinnerEmployee);

            String[] statuses = {
                "Received for repairing",
                "Repairing completed",
                "Mobile delivered to customer"
            };
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, statuses);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerStatus.setAdapter(statusAdapter);

            List<String> employeeNames = new ArrayList<>();
            for (EmployeeItem emp : employeeItemList) {
                employeeNames.add(emp.getFullName());
            }
            if (employeeNames.isEmpty()) {
                employeeNames.add("Default Employee");
            }
            ArrayAdapter<String> empAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, employeeNames);
            empAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEmployee.setAdapter(empAdapter);

            new AlertDialog.Builder(MainActivity.this)
                .setTitle("New Repair Entry")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    try {
                        String customerName = etCustomerName.getText().toString().trim();
                        String mobileNumber = etMobileNumber.getText().toString().trim();
                        String mobileCompany = etMobileCompany.getText().toString().trim();
                        String address = etAddress.getText().toString().trim();
                        String issueDescription = etIssueDescription.getText().toString().trim();

                        int totalAmount = etTotalAmount.getText().toString().isEmpty() ? 0 : Integer.parseInt(etTotalAmount.getText().toString().trim());
                        String repairStatus = spinnerStatus.getSelectedItem() != null ? spinnerStatus.getSelectedItem().toString() : "Received for repairing";
                        String empName = spinnerEmployee.getSelectedItem() != null ? spinnerEmployee.getSelectedItem().toString() : "Unknown";

                        RepairRequest request = new RepairRequest(
                            address,
                            customerName,
                            empName,
                            issueDescription,
                            mobileCompany,
                            "",
                            mobileNumber,
                            0,
                            totalAmount,
                            repairStatus,
                            totalAmount
                        );

                        lastSubmittedRequest = request;
                        Toast.makeText(MainActivity.this, "Submitting new repair...", Toast.LENGTH_SHORT).show();
                        repairViewModel.addRepairCollection(request);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }

    private String buildWhatsAppMessage(RepairRequest request) {
        String customerName = request.getCustomerName();
        StringBuilder sb = new StringBuilder();
        sb.append("नमस्कार! ").append(customerName).append("\n");
        sb.append("तुमची नोंदणी यशस्वीरित्या पूर्ण झाली आहे.\n\n");
        sb.append("मोबाइल :- ").append(request.getMobileCompanyName()).append("\n");
        sb.append("समस्या:- ").append(request.getIssueDescription()).append("\n");
        sb.append("रक्कम:- ").append(request.getTotalAmount()).append("\n");
        sb.append("गाव:- ").append(request.getAddress()).append("\n");
        sb.append("Status:- ").append(request.getRepairStatus()).append("\n");
        sb.append("By:- ").append(request.getEmpName()).append("\n\n");
        sb.append("राजपूत मोबाईल ला भेट दिल्याबद्दल धन्यवाद");
        return sb.toString();
    }

    private void sendWhatsAppMessage(String mobileNo, String message) {
        if (mobileNo == null || mobileNo.trim().isEmpty()) return;
        String cleanNumber = mobileNo.replaceAll("[^0-9]", "");
        if (cleanNumber.length() == 10) {
            cleanNumber = "91" + cleanNumber;
        }

        try {
            String url = "https://api.whatsapp.com/send?phone=" + cleanNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp is not installed or unable to open", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyFilters() {
        List<RepairItem> filteredList = new ArrayList<>();
        for (RepairItem item : fullRepairList) {
            boolean matchesSearch = currentSearchQuery.isEmpty() ||
                    (item.getCustomerName() != null && item.getCustomerName().toLowerCase().contains(currentSearchQuery)) ||
                    (item.getMobileNumber() != null && item.getMobileNumber().toLowerCase().contains(currentSearchQuery));

            boolean matchesStatus = currentStatusFilter.equals("All") ||
                    (item.getRepairStatus() != null && item.getRepairStatus().toLowerCase().contains(currentStatusFilter.toLowerCase()));

            if (matchesSearch && matchesStatus) {
                filteredList.add(item);
            }
        }
        repairAdapter.setRepairList(filteredList);
    }

    private void showAdminLoginDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_admin_login, null);
        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);

        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Admin Authentication")
            .setView(dialogView)
            .setPositiveButton("Login", (dialog, which) -> {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if ("Viju".equals(username) && "Viju@#123!".equals(password)) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showReportLoginDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_admin_login, null);
        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);

        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Report Authentication")
            .setView(dialogView)
            .setPositiveButton("Login", (dialog, which) -> {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if ("Viju".equals(username) && "Viju@#123!".equals(password)) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DailyCollectionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    public void sendInformationToWhatsApp(String mobileNo, String message) {
        String mobile = mobileNo.replaceAll("[^0-9]", "");
        if (mobile.length() == 10) {
            mobile = "91" + mobile;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(
                    "https://wa.me/" + mobile +
                            "?text=" + Uri.encode(message)
            ));

            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "WhatsApp is not available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
