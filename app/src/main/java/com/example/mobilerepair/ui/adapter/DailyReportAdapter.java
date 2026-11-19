package com.example.mobilerepair.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobilerepair.R;
import com.example.mobilerepair.data.model.RepairItem;
import java.util.ArrayList;
import java.util.List;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.ReportViewHolder> {

    private List<RepairItem> repairList = new ArrayList<>();

    public void setRepairList(List<RepairItem> repairList) {
        this.repairList = repairList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        RepairItem item = repairList.get(position);
        holder.tvReportCustomerName.setText(item.getCustomerName() != null ? item.getCustomerName() : "Unknown");
        
        String createdDate = item.getCreatedDate();
        String timeStr = "N/A";
        if (createdDate != null) {
            // e.g. "Tue, 06 Oct 2026 08:45:19 GMT" -> extract time or display date/time
            timeStr = createdDate;
        }
        holder.tvReportTime.setText(timeStr);
        holder.tvReportPayment.setText("₹" + item.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportCustomerName, tvReportTime, tvReportPayment;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportCustomerName = itemView.findViewById(R.id.tvReportCustomerName);
            tvReportTime = itemView.findViewById(R.id.tvReportTime);
            tvReportPayment = itemView.findViewById(R.id.tvReportPayment);
        }
    }
}
