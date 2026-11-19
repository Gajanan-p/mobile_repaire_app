package com.example.mobilerepair.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.RepairItem;
import com.example.mobilerepair.ui.adapter.DailyReportAdapter;
import com.example.mobilerepair.utils.EmailReportHelper;
import com.example.mobilerepair.viewmodel.RepairViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyCollectionActivity extends AppCompatActivity {

    private DailyReportAdapter reportAdapter;
    private TextView tvDaily24HrTotalAmount, tvDailyCount, tvStaticReceiverMail;
    private List<RepairItem> todayRepairItems = new ArrayList<>();
    private int todayTotalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_collection);

        setTitle("Today's Sales Report");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RepairViewModel repairViewModel = new ViewModelProvider(this).get(RepairViewModel.class);

        tvDaily24HrTotalAmount = findViewById(R.id.tvDaily24HrTotalAmount);
        tvDailyCount = findViewById(R.id.tvDailyCount);
        tvStaticReceiverMail = findViewById(R.id.tvStaticReceiverMail);
        ImageButton btnAutoMailReport = findViewById(R.id.btnAutoMailReport);
        ImageButton btnPrintReport = findViewById(R.id.btnPrintReport);
        ImageButton btnShareReport = findViewById(R.id.btnShareReport);
        ImageButton btnEditReceiverEmail = findViewById(R.id.btnEditReceiverEmail);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDailyRepairs);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new DailyReportAdapter();
        recyclerView.setAdapter(reportAdapter);

        updateReceiverMailDisplay();

        repairViewModel.getRepairList().observe(this, repairItems -> {
            if (repairItems != null) {
                filterTodaySales(repairItems);
            }
        });

        repairViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(DailyCollectionActivity.this, error, Toast.LENGTH_LONG).show();
        });

        repairViewModel.fetchRepairCollections();

        btnAutoMailReport.setOnClickListener(v -> triggerAutoEmailReport());
        btnPrintReport.setOnClickListener(v -> printReport());
        btnShareReport.setOnClickListener(v -> showShareOptionsDialog());
        btnEditReceiverEmail.setOnClickListener(v -> showEditEmailDialog());
    }

    private void updateReceiverMailDisplay() {
        String currentEmail = EmailReportHelper.getReceiverEmail(this);
        if (tvStaticReceiverMail != null) {
            tvStaticReceiverMail.setText("Auto Mail To: " + currentEmail);
        }
    }

    private void showEditEmailDialog() {
        String currentEmail = EmailReportHelper.getReceiverEmail(this);
        EditText etEmail = new EditText(this);
        etEmail.setPadding(40, 30, 40, 30);
        etEmail.setHint("Enter static receiver email");
        etEmail.setText(currentEmail);

        new AlertDialog.Builder(this)
            .setTitle("Static Receiver Email")
            .setMessage("Set the static receiver email for automatic day-wise collection reports:")
            .setView(etEmail)
            .setPositiveButton("Save", (dialog, which) -> {
                String newEmail = etEmail.getText().toString().trim();
                if (newEmail.contains("@")) {
                    EmailReportHelper.setReceiverEmail(this, newEmail);
                    updateReceiverMailDisplay();
                    Toast.makeText(this, "Receiver email updated: " + newEmail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void triggerAutoEmailReport() {
        if (todayRepairItems.isEmpty()) {
            Toast.makeText(this, "No sales records for today to send report", Toast.LENGTH_SHORT).show();
            return;
        }
        EmailReportHelper.sendAutoDailyEmailReport(this, todayRepairItems, todayTotalAmount);
    }

    private void showShareOptionsDialog() {
        String[] options = {"1. Excel (.csv)", "2. PDF", "3. Auto Email to Static Receiver"};
        new AlertDialog.Builder(this)
            .setTitle("Share Report")
            .setItems(options, (dialog, which) -> {
                if (which == 0) {
                    exportExcel();
                } else if (which == 1) {
                    printReport();
                } else if (which == 2) {
                    sendEmailReport();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterTodaySales(List<RepairItem> allItems) {
        todayRepairItems.clear();
        todayTotalAmount = 0;

        Calendar todayCal = Calendar.getInstance();
        int todayYear = todayCal.get(Calendar.YEAR);
        int todayDayOfYear = todayCal.get(Calendar.DAY_OF_YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        for (RepairItem item : allItems) {
            String dateStr = item.getCreatedDate();
            if (dateStr != null) {
                try {
                    Date date = sdf.parse(dateStr);
                    if (date != null) {
                        Calendar itemCal = Calendar.getInstance();
                        itemCal.setTime(date);
                        if (itemCal.get(Calendar.YEAR) == todayYear && itemCal.get(Calendar.DAY_OF_YEAR) == todayDayOfYear) {
                            todayRepairItems.add(item);
                            todayTotalAmount += item.getTotalAmount();
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        reportAdapter.setRepairList(todayRepairItems);
        tvDaily24HrTotalAmount.setText("₹" + todayTotalAmount);
        tvDailyCount.setText("Today's Entries Count: " + todayRepairItems.size());
    }

    private void printReport() {
        if (todayRepairItems.isEmpty()) {
            Toast.makeText(this, "No sales records for today to print", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; padding: 20px; color: #333; }");
        htmlBuilder.append("h2 { text-align: center; color: #222; margin-bottom: 5px; }");
        htmlBuilder.append(".header { text-align: center; margin-bottom: 20px; font-size: 14px; color: #555; }");
        htmlBuilder.append(".total-box { background: #f8f9fa; border: 1px solid #ddd; padding: 12px; margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: center; }");
        htmlBuilder.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
        htmlBuilder.append("th, td { border: 1px solid #ccc; padding: 8px; text-align: left; font-size: 12px; }");
        htmlBuilder.append("th { background-color: #5C5CFF; color: white; }");
        htmlBuilder.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        htmlBuilder.append("</style></head><body>");

        htmlBuilder.append("<h2>Rajput Mobile Repair Shop - Today's Sales Report</h2>");
        String currentDate = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date());
        htmlBuilder.append("<div class=\"header\">Generated on: ").append(currentDate).append("</div>");

        htmlBuilder.append("<div class=\"total-box\">Overall Total Amount Today: ₹").append(todayTotalAmount).append("</div>");

        htmlBuilder.append("<table><tr>");
        htmlBuilder.append("<th>Customer Name</th>");
        htmlBuilder.append("<th>Mobile No</th>");
        htmlBuilder.append("<th>Model Name</th>");
        htmlBuilder.append("<th>Issue</th>");
        htmlBuilder.append("<th>Address</th>");
        htmlBuilder.append("<th>Amount</th>");
        htmlBuilder.append("</tr>");

        for (RepairItem item : todayRepairItems) {
            String model = (item.getMobileCompanyName() != null ? item.getMobileCompanyName() : "") +
                    (item.getMobileModelNumber() != null ? " " + item.getMobileModelNumber() : "");
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(item.getCustomerName() != null ? item.getCustomerName() : "N/A").append("</td>");
            htmlBuilder.append("<td>").append(item.getMobileNumber() != null ? item.getMobileNumber() : "N/A").append("</td>");
            htmlBuilder.append("<td>").append(model.trim().isEmpty() ? "N/A" : model.trim()).append("</td>");
            htmlBuilder.append("<td>").append(item.getIssueDescription() != null ? item.getIssueDescription() : "N/A").append("</td>");
            htmlBuilder.append("<td>").append(item.getAddress() != null ? item.getAddress() : "N/A").append("</td>");
            htmlBuilder.append("<td>₹").append(item.getTotalAmount()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table></body></html>");

        WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, htmlBuilder.toString(), "text/html", "UTF-8", null);

        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        if (printManager != null) {
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("Today_Sales_Report");
            printManager.print("Today_Sales_Report", printAdapter, new PrintAttributes.Builder().build());
        } else {
            Toast.makeText(this, "Printing not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportExcel() {
        if (todayRepairItems.isEmpty()) {
            Toast.makeText(this, "No sales records for today to export", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Customer Name,Mobile No,Model Name,Issue,Address,Amount,Date\n");

        for (RepairItem item : todayRepairItems) {
            String model = (item.getMobileCompanyName() != null ? item.getMobileCompanyName() : "") +
                    (item.getMobileModelNumber() != null ? " " + item.getMobileModelNumber() : "");
            csvBuilder.append("\"").append(item.getCustomerName() != null ? item.getCustomerName() : "").append("\",");
            csvBuilder.append("\"").append(item.getMobileNumber() != null ? item.getMobileNumber() : "").append("\",");
            csvBuilder.append("\"").append(model.trim()).append("\",");
            csvBuilder.append("\"").append(item.getIssueDescription() != null ? item.getIssueDescription() : "").append("\",");
            csvBuilder.append("\"").append(item.getAddress() != null ? item.getAddress() : "").append("\",");
            csvBuilder.append("\"").append(item.getTotalAmount()).append("\",");
            csvBuilder.append("\"").append(item.getCreatedDate() != null ? item.getCreatedDate() : "").append("\"\n");
        }

        try {
            File exportDir = new File(getCacheDir(), "exports");
            if (!exportDir.exists()) exportDir.mkdirs();

            File csvFile = new File(exportDir, "Today_Sales_Report.csv");
            FileOutputStream out = new FileOutputStream(csvFile);
            out.write(csvBuilder.toString().getBytes());
            out.close();

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", csvFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share Today's Sales Excel (.csv)"));

        } catch (Exception e) {
            Toast.makeText(this, "Failed to export CSV: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailReport() {
        triggerAutoEmailReport();
    }
}
