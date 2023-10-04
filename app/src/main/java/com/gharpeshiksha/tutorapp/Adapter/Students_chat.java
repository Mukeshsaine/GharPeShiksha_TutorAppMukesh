package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.Students_Chat_Models;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Students_chat extends RecyclerView.Adapter<Students_chat.Viewholder> {

    private Context context;
    private List<Students_Chat_Models> students_chat_modelsList;
    private String TAG = "Students_chat.java";

    public Students_chat(Context context, List<Students_Chat_Models> students_chat_modelsList) {
        this.context = context;
        this.students_chat_modelsList = students_chat_modelsList;
    }

    @NonNull
    @Override
    public Students_chat.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.send_contact_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Students_chat.Viewholder holder, int position) {
        Students_Chat_Models models = students_chat_modelsList.get(position);
        Log.d(TAG, "onBindViewHolder:  send by == " + models.toString());

        //  String ts = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        Log.d(TAG, "onBindViewHolder: " + models.getTimestamp());
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
        String[] strArr = models.getTimestamp().split(" ");
        String setime = simpleDateFormat.format(d);
        String date = strArr[0];
        String timeRaw = strArr[1];
        String[] strArrFormat = timeRaw.split(":");
        String time = strArrFormat[0] + ":" + strArrFormat[1];
        if (position == 0) {
            holder.timeDate.setText(date);
            holder.timeDate.setVisibility(View.VISIBLE);
            holder.timeleft.setText(time);
            holder.timeleft.setVisibility(View.VISIBLE);
        } else {
            Students_Chat_Models modelsPrev = students_chat_modelsList.get(position - 1);
            String[] strArr1 = modelsPrev.getTimestamp().split(" ");
            String date1 = strArr1[0];
            String time1 = strArr1[1];
            if (date1.matches(date)) {
                holder.timeDate.setVisibility(View.GONE);
                holder.timeleft.setVisibility(View.VISIBLE);
//                holder.dateCardView.setVisibility(View.GONE);
            } else {
//                holder.dateCardView.setVisibility(View.VISIBLE);
                holder.timeDate.setText(date);
                holder.timeDate.setVisibility(View.VISIBLE);
                holder.timeleft.setText(time1);
                holder.timeleft.setVisibility(View.VISIBLE);
            }
        }
        Log.d(TAG, "onBindViewHolder: " + models.getSendBy());
        if (models.getSendBy() != null) {
            if (models.getSendBy().equals(Students_Chat_Models.SENT_BY_ME)) {
                if (models.isSended()) {
                    //message is successfully send-ed to server
                    holder.Recivedlayout.setVisibility(View.GONE);
                    holder.Sendlayout.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onBindViewHolder: " + models.getMessage());
                    holder.Send.setText(models.getMessage());
                    holder.timeRight.setText(time);
                    holder.pandingTimeRight.setVisibility(View.GONE);
                    holder.timeRight.setVisibility(View.VISIBLE);
                } else {
                    //message is not successfully send-ed to server
                    holder.Recivedlayout.setVisibility(View.GONE);
                    holder.Sendlayout.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onBindViewHolder: " + models.getMessage());
                    holder.Send.setText(models.getMessage());
                    holder.timeRight.setText(time);
                    holder.pandingTimeRight.setVisibility(View.VISIBLE);
                    holder.timeRight.setVisibility(View.GONE);
                }

            } else {
                holder.Sendlayout.setVisibility(View.GONE);
                holder.Recivedlayout.setVisibility(View.VISIBLE);
                holder.Recived.setText(models.getMessage());
                holder.timeleft.setText(time);
            }
        }

    }

    @Override
    public int getItemCount() {
        return students_chat_modelsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Send, Recived, timeDate, timeleft, timeRight, pandingTimeRight;
        CardView Recivedlayout, Sendlayout, dateCardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Send = itemView.findViewById(R.id.send);
            Recived = itemView.findViewById(R.id.Recived);
            Recivedlayout = itemView.findViewById(R.id.Recivedlayout);
//            dateCardView = itemView.findViewById(R.id.Recivedlayout1);
            Sendlayout = itemView.findViewById(R.id.send_layout);
            timeDate = itemView.findViewById(R.id.timedate);
            timeleft = itemView.findViewById(R.id.Timeleft);
            timeRight = itemView.findViewById(R.id.TimeRight);
            pandingTimeRight = itemView.findViewById(R.id.pendingTimeRight);

        }
    }

}
