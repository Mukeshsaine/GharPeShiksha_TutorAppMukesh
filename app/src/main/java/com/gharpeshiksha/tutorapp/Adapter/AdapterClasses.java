package com.gharpeshiksha.tutorapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.se.omapi.Session;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.EnquiryFilter;
import com.gharpeshiksha.tutorapp.data_model.Classes_Model;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.android.gms.maps.model.Dash;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterClasses extends RecyclerView.Adapter<AdapterClasses.Viewholder> {

    private Context context;
    private List<Classes_Model> classes_modelList;
    private Dashboard dashboardAct;
    private String TAG = "AdapterClasses.java";
    private VolleyHelper volleyHelper = new VolleyHelper();
    String filterdata = "https://api.gharpeshiksha.com/Tutor/getInitialFilterData";
    String lat = "NA";
    String lon = "NA";
    String radius = "NA";
    private SessionConfig sessionConfig;

    public AdapterClasses(Context context, List<Classes_Model> classes_modelList) {
        this.context = context;
        this.classes_modelList = classes_modelList;
        this.dashboardAct = (Dashboard) context;
        sessionConfig = new SessionConfig(context);
    }

    @NonNull
    @Override
    public AdapterClasses.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_contact_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Classes_Model classes_model = classes_modelList.get(position);

        holder.name.setText(classes_model.getStudentscla());

        String url = classes_model.getImage();
        Picasso.get()
                .load(url)
                .noFade()
                .into(holder.imageView);


        holder.imageView.setOnClickListener(view -> {
            if (classes_model.getFilterCls() != null) {
                Map<String, String> params = new HashMap<>();
                params.put("phone", sessionConfig.getPhone() + "");
                volleyHelper.VolleyPostRequest(context, filterdata, params, new VolleyResponse() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSucess(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            lat = jsonObject.getString("latitude");
                            Log.d(TAG, "onSucess: lati  =  " + lat);
                            lon = jsonObject.getString("longitude");
                            Log.d(TAG, "onSucess: long ==  " + lon);
                            radius = jsonObject.getString("radius");
                            Log.d(TAG, "onSucess:  == radius = " + radius);

                            dashboardAct.setIsAllIndiaChecked(false);
                            dashboardAct.setMIsSearchByEnqId(false);
                            dashboardAct.changeFrag(2, classes_model.getFilterCls(), true, radius + "," + lat + "," + lon);
//                            Dashboard d = (Dashboard) context;
//                            String[] clsArr = classes_model.getFilterCls().split(",");
//                            d.setFilterBadge(clsArr.length);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
            Log.d(TAG, "onBindViewHolder: " + classes_model.getFilterCls());
        });

    }

    @Override
    public int getItemCount() {
        return classes_modelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.Text_name_Classes);
            imageView = itemView.findViewById(R.id.image_classes);
        }
    }
}
