package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.BillingHistory;

import java.util.List;

public class BillingHistoryAdapter extends RecyclerView.Adapter<BillingHistoryAdapter.BillingViewHolder> {
    private Context context;

    private List<BillingHistory> billingHistoryList;

    public BillingHistoryAdapter(Context context, List<BillingHistory> billingHistoryList) {
        this.context = context;
        this.billingHistoryList = billingHistoryList;
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_billing_history, parent, false);
        return new BillingHistoryAdapter.BillingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        BillingHistory billingHistory = billingHistoryList.get(position);

        holder.textPlan.setText(billingHistory.getPlan());
        holder.textBillDate.setText(billingHistory.getPaymentDate());
        holder.textAmount.setText(billingHistory.getPaymentAmount());
        holder.textPlanAct.setText(billingHistory.getStatus());

    }

    @Override
    public int getItemCount() {
        return billingHistoryList.size();
    }

    class BillingViewHolder extends RecyclerView.ViewHolder {
        private TextView textPlan, textBillDate, textAmount, textPlanAct;

        BillingViewHolder(View itemView) {
            super(itemView);

            textPlan = itemView.findViewById(R.id.textAppName);
            textBillDate = itemView.findViewById(R.id.textBillDate);
            textAmount = itemView.findViewById(R.id.textAmount);
            textPlanAct = itemView.findViewById(R.id.textPlanAct);
        }
    }
}
