package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.activities.ReferAndEarn_Activity;
import com.gharpeshiksha.tutorapp.data_model.HowtoEarnModel;

import java.util.ArrayList;
import java.util.List;

public class HowToEarnAdapter extends RecyclerView.Adapter<HowToEarnAdapter.ViewHolder> {

    Context context;
    List<HowtoEarnModel> list = new ArrayList<>();
    View view;

    public HowToEarnAdapter(Context context, List<HowtoEarnModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.free_zone_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final HowtoEarnModel model = list.get(i);

        viewHolder.title.setText(model.getTitle());
        viewHolder.subTitle.setText(model.getSubTitle());
        viewHolder.buttonName.setText(model.getButtonName());

        viewHolder.buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getButtonAction().equals("ReferAndEarn")) {
                    context.startActivity(new Intent(context, ReferAndEarn_Activity.class));
                } else if (model.getButtonAction().equals("Community")) {

                }
            }
        });

        viewHolder.learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                        .setMessage(model.getDescription())
                        .setPositiveButton("Earn Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (model.getButtonAction().equals("ReferAndEarn")) {
                                    context.startActivity(new Intent(context, ReferAndEarn_Activity.class));
                                } else if (model.getButtonAction().equals("Community")) {

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        if (model.getStatus().equals("used")) {
            int r = 162;
            int g = 162;
            int b = 162;

            viewHolder.card.setClickable(false);
            viewHolder.buttonName.setClickable(false);
            viewHolder.buttonName.setBackgroundColor(Color.rgb(245, 245, 245));
            viewHolder.card.setCardBackgroundColor(Color.rgb(245, 245, 245));
            viewHolder.title.setTextColor(Color.rgb(162, 62, 162));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView learn;
        TextView title, subTitle;
        Button buttonName;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            learn = itemView.findViewById(R.id.earn_learn);
            title = itemView.findViewById(R.id.earn_heading);
            subTitle = itemView.findViewById(R.id.earn_details);
            buttonName = itemView.findViewById(R.id.earn_button);
            card = itemView.findViewById(R.id.howToEarnCard);
        }
    }
}
