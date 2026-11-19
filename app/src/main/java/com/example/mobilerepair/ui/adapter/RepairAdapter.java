package com.example.mobilerepair.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.RepairItem;
import java.util.ArrayList;
import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairViewHolder> {

    private List<RepairItem> repairList = new ArrayList<>();
    private final OnUpdateClickListener updateClickListener;

    public interface OnUpdateClickListener {
        void onUpdateClick(RepairItem item);
    }

    public RepairAdapter(OnUpdateClickListener updateClickListener) {
        this.updateClickListener = updateClickListener;
    }

    public void setRepairList(List<RepairItem> repairList) {
        this.repairList = repairList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair, parent, false);
        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairViewHolder holder, int position) {
        RepairItem item = repairList.get(position);
        holder.tvCustomerName.setText(item.getCustomerName() != null ? item.getCustomerName() : "Unknown");
        holder.tvMobileNo.setText("Mobile: " + (item.getMobileNumber() != null ? item.getMobileNumber() : "N/A"));

        String companyDetails = (item.getMobileCompanyName() != null ? item.getMobileCompanyName() : "") +
                (item.getMobileModelNumber() != null ? " " + item.getMobileModelNumber() : "");
        holder.tvMobileCompany.setText("Company: " + (companyDetails.trim().isEmpty() ? "N/A" : companyDetails.trim()));

        holder.tvIssue.setText("Issue: " + (item.getIssueDescription() != null ? item.getIssueDescription() : "N/A"));
        holder.tvAddress.setText("Address: " + (item.getAddress() != null ? item.getAddress() : "N/A"));
        
        String status = item.getRepairStatus() != null ? item.getRepairStatus() : "";
        holder.tvStatus.setText("Status: " + (status.isEmpty() ? "N/A" : status));

        String lowerStatus = status.toLowerCase();
        if (lowerStatus.contains("received")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_received));
        } else if (lowerStatus.contains("completed")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_completed));
        } else if (lowerStatus.contains("delivered")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_delivered));
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_color));
        }

        holder.tvDate.setText(item.getCreatedDate() != null ? item.getCreatedDate() : "");

        holder.btnUpdate.setOnClickListener(v -> {
            if (updateClickListener != null) {
                updateClickListener.onUpdateClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    static class RepairViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvMobileNo, tvMobileCompany, tvIssue, tvAddress, tvStatus, tvDate;
        MaterialButton btnUpdate;

        public RepairViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvMobileNo = itemView.findViewById(R.id.tvMobileNo);
            tvMobileCompany = itemView.findViewById(R.id.tvMobileCompany);
            tvIssue = itemView.findViewById(R.id.tvIssue);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
