package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.TransactionModel;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    ArrayList<TransactionModel> arrayList;
    Context context;


    public TransactionAdapter(ArrayList<TransactionModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.transaction_layout, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {
        final TransactionModel model = arrayList.get(i);


        transactionViewHolder.date.setText(model.getDate());

        transactionViewHolder.point.setText("+" + model.getPoints());

        transactionViewHolder.referedto.setText(model.getReferedto());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView point, date, referedto, referedby;

        public TransactionViewHolder(@NonNull View itemView) {

            super(itemView);

            point = itemView.findViewById(R.id.pointearn);
            date = itemView.findViewById(R.id.date);
            referedto = itemView.findViewById(R.id.referto);


        }
    }
}
