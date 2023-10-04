package com.gharpeshiksha.tutorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.activities.Chats_all_StudentsActivity;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.utils.Utility;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Adapter_Chats extends RecyclerView.Adapter<Adapter_Chats.ViewHolder> {

    private List<Model_Chats> model_chatsList;
    private Context context;
    String TAG = "Adapter_Chats.java";

    public Adapter_Chats(List<Model_Chats> model_chatsList, Context context) {
        this.model_chatsList = model_chatsList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_Chats.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_students_all_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Chats.ViewHolder holder, int position) {
        try {
            Model_Chats model_chats = model_chatsList.get(position);
            Log.d(TAG, "onBindViewHolder:  === " + model_chats);
            holder.number.setText(model_chats.getMessage());
            if(model_chats.getIsApproved().toLowerCase().matches("declined")) {
                holder.rootCV.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
                holder.chatStatusLL.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            } else if(model_chats.getIsApproved().toLowerCase().matches("accepted")) {
                holder.rootCV.setCardBackgroundColor(context.getResources().getColor(R.color.lightBlue));
                holder.chatStatusLL.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.rootCV.setCardBackgroundColor(context.getResources().getColor(R.color.lightGreen));
                holder.chatStatusLL.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
            }
            holder.chatStatus.setText(model_chats.getChatStatus());
            //String[] currTime = model_chats.getTimestamp().split(" ");
           // String[] strArr = currTime[1].split(":");
           /* String chatTime;
            if(Long.parseLong(strArr[0]) > 12) {
                chatTime = strArr[0] + ":" + strArr[1] + " PM";
            } else {
                chatTime = strArr[0] + ":" + strArr[1] + " AM";
            }*/

            holder.time.setText(model_chats.getDay());
            holder.Name.setText(model_chats.getStudentname());
            String url = model_chats.getPicUrl();
            Picasso.get().load(url).placeholder(R.drawable.loogoo).into(holder.imageView);

            holder.ItemClieck.setOnClickListener(view -> {
                Intent i = new Intent(context, Chats_all_StudentsActivity.class);
                i.putExtra("tutorname", model_chats.getStudentname());
                i.putExtra("UrlPic", model_chats.getPicUrl());
                i.putExtra("studentsUUID1", model_chats.getStudentUUID());
                i.putExtra("tutorUUID1", model_chats.getTutorUUID());
                i.putExtra("enqId", model_chats.getEnqId());
                context.startActivity(i);
                Chats_API_Open();
            });
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e);
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return model_chatsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, number, time, icon, chatStatus;
        ImageView imageView;
        LinearLayout ItemClieck, chatStatusLL;
        CardView rootCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.namechateview);
            number = itemView.findViewById(R.id.number);
            time = itemView.findViewById(R.id.timechats);
            icon = itemView.findViewById(R.id.icon);
            imageView = itemView.findViewById(R.id.image);
            ItemClieck = itemView.findViewById(R.id.ItemClieck);
            chatStatus = itemView.findViewById(R.id.chatStatus);
            rootCV = itemView.findViewById(R.id.root);
            chatStatusLL = itemView.findViewById(R.id.chatStatLL);
        }
    }

    public void Chats_API_Open() {
       /* List<Model_Chats> model_chatsList = new ArrayList<>();
        ApiServies apiServies = RetrofitClient.getClient().create(ApiServies.class);
        apiServies.ChatOpen("b7051a6fd9","2d0f33a1bd").enqueue(new Callback<List<Students_Chat_Models>>() {
            @Override
            public void onResponse(Call<List<Students_Chat_Models>> call, Response<List<Students_Chat_Models>> response) {
                Log.d(TAG, "onResponse: Open Chate == "+response.body());
                try {
                    model_chatsList = response.body();
                    for (int i=0;i<students_chat_modelsList.size();i++)
                    {
                        Log.d(TAG, "onResponse == "+students_chat_modelsList.get(0).getMessage()+"\n second = "+students_chat_modelsList.get(0).getSendBy());
                    }


                    students_chatAdapter = new Students_chat(getApplicationContext(), students_chat_modelsList);
                    binding.recycleView.setAdapter(students_chatAdapter);
                    LinearLayoutManager llm = new LinearLayoutManager(Chats_all_StudentsActivity.this);
                    llm.setStackFromEnd(true);
                    binding.recycleView.setLayoutManager(llm);
                } catch(Exception e)
                {
                    Log.d(TAG, "onResponse: "+e);
                }

//                addToChat(question,Students_Chat_Models.SENT_BY_ME);
            }

            @Override
            public void onFailure(Call<List<Students_Chat_Models>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });*/

    }
}
