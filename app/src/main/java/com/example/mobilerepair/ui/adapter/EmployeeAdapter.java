package com.example.mobilerepair.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.EmployeeItem;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<EmployeeItem> employeeList = new ArrayList<>();
    private final OnEditClickListener editClickListener;

    public interface OnEditClickListener {
        void onEditClick(EmployeeItem item);
    }

    public EmployeeAdapter(OnEditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setEmployeeList(List<EmployeeItem> employeeList) {
        this.employeeList = employeeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        EmployeeItem item = employeeList.get(position);
        holder.tvEmployeeName.setText(item.getFullName());
        holder.tvEmployeeAddress.setText("Address: " + (item.getAddress() != null && !item.getAddress().trim().isEmpty() ? item.getAddress() : "N/A"));

        holder.btnEditEmployee.setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeName, tvEmployeeAddress;
        ImageButton btnEditEmployee;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvEmployeeAddress = itemView.findViewById(R.id.tvEmployeeAddress);
            btnEditEmployee = itemView.findViewById(R.id.btnEditEmployee);
        }
    }
}
