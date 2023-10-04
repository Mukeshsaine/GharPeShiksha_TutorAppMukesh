package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.data_model.AppliedLeads;

import java.util.List;

public class AllFeedbacRecyclerviewkAdptor extends RecyclerView.Adapter<AllFeedbacRecyclerviewkAdptor.CustomViewHolder> {

    Context context;
    List<AppliedLeads> List_feedback;

    public AllFeedbacRecyclerviewkAdptor(Context context, List<AppliedLeads> List_feedback) {
        this.context = context;
        this.List_feedback = List_feedback;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feedback_details, viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, final int i) {

        AppliedLeads feedback = List_feedback.get(i);

        viewHolder.StudentName.setText(feedback.getTextAppName());
        viewHolder.TutorRequirement.setText(feedback.getTextAppTutorReq());
        viewHolder.StudentPostedOn.setText(feedback.getTextAppPosted());
        viewHolder.ViewedOn.setText(feedback.getTextAppViewed());
        viewHolder.StudentBudget.setText(feedback.getTextAppBudget());
        viewHolder.StudentDistanceFromTutor.setText(feedback.getTextAppDis());
        viewHolder.StudentLocation.setText(feedback.getTextAppLoc());
        viewHolder.Teacherfeedback.setText(feedback.getTextAppMyFeed());
        viewHolder.GPSReply.setText(feedback.getTextAppGPSMes());
        viewHolder.end_id.setText(feedback.getTextEnq_id());

            /* viewHolder.applied_card.setOnClickListener(new View.OnClickListener() {

                 @Override
                 public void onClick(View v) {
                  Toast toast=  Toast.makeText(context,"view",Toast.LENGTH_SHORT);
                  toast.setGravity(Gravity.CENTER,0 ,0);
                  toast.show();
                 }
             });*/
    }

    @Override
    public int getItemCount() {
        return List_feedback.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView end_id, StudentName, TutorRequirement, GPSReply, Teacherfeedback, ViewedOn, StudentBudget, StudentLocation, StudentDistanceFromTutor, StudentPostedOn;
        private CardView applied_card;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            applied_card = itemView.findViewById(R.id.applied_card);
            StudentName = itemView.findViewById(R.id.textAppName);
            TutorRequirement = itemView.findViewById(R.id.textAppTutorReq);
            StudentPostedOn = itemView.findViewById(R.id.textAppPosted);
            ViewedOn = itemView.findViewById(R.id.textAppViewed);
            StudentBudget = itemView.findViewById(R.id.textAppBudget);
            StudentDistanceFromTutor = itemView.findViewById(R.id.textAppDis);
            StudentLocation = itemView.findViewById(R.id.textAppLoc);
            Teacherfeedback = itemView.findViewById(R.id.feedback);
            GPSReply = itemView.findViewById(R.id.our_msg);
            end_id = itemView.findViewById(R.id.end_id);
            // ViewStudentDetails = itemView.findViewById(R.id.textFeed);

        }
    }
}
