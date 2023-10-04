package com.gharpeshiksha.tutorapp.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.BasicFeatures;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.UpgradeActivity;
import com.gharpeshiksha.tutorapp.VolleyClass.VolleyHelper;
import com.gharpeshiksha.tutorapp.activities.ClassesForMeActivity;
import com.gharpeshiksha.tutorapp.data_model.ParentsContactedModel;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentsContactedAdapter extends RecyclerView.Adapter<ParentsContactedAdapter.ViewHolder> {

    //Data source of RecyclerView list adapter in ArrayList
    ArrayList<ParentsContactedModel> dataSrc;
    Context context;
    SessionConfig sessionConfig;
    ParentsContactedModel itemDataObj;
    private BasicFeatures basicFeatures = new BasicFeatures();
    String tempId;
    private Dialog dialog;
    private Button retry;
    private VolleyHelper volleyHelper;

    public ParentsContactedAdapter(ArrayList<ParentsContactedModel> list) {
        this.dataSrc = list;//assigning the input params list to local same object type list of adapter
    }

    @NonNull
    @Override
    public ParentsContactedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parents_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentsContactedAdapter.ViewHolder holder, int position) {
        //Item data object from data source at position
        itemDataObj = dataSrc.get(position);//data object of item
        holder.getDateAndTime().setText(itemDataObj.getmDate());//date and time
        holder.getEnqId().setText("Enquiry Id:" + itemDataObj.getmEnqId());//enquiry id
        holder.getUserClasses().setText(itemDataObj.getmUserClass());//class
        holder.getUserName().setText(itemDataObj.getmName());//name of parent
        holder.getFees().setText(itemDataObj.getmFees());//fees of session
        holder.getAddress().setText(itemDataObj.getmAddress());//address of parent enquiry
        holder.getContact().setText(itemDataObj.getmContact());
//        if (!itemDataObj.getmAltContact().equals(""))//display alternate contact if there is alt contact
//            holder.getAltContact().setText(" ," + itemDataObj.getmAltContact());
        if (itemDataObj.getmTeachingMode().equals("")) {//add not specified if teaching mode is null
            holder.getTeachingMode().setText("Not Specified");
        } else {//else set text of teaching mode field
            holder.getTeachingMode().setText(itemDataObj.getmTeachingMode());
        }

        //Set OnClickListener on call button of RecyclerView list item
        holder.getCallBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass an implicit Intent request to android to open an dialer Activity inside any app of device that can handle our request
                //To open an dialer with phone number to dial use Implicit Intent with Action: ACTION_DIAL and data(tel:9773602742 in string)
                //is in URI(uniform Resource-Identifier) no mime type(data type). If you want no user interaction means directly call on num.
                //use Action to ACTION_CALL and add permission in manifest file for CALL_PHONE.
                //If you want to create your TextView as link which directly call the number without Intent then add below attribute to TextView
                //autoLink="phone", linksClickable="true" both are android xmlns attributes.
                if (isNetworkAvailable()) {
                    tempId = itemDataObj.getmEnqId() + "";
                    checkStatus("textcall", itemDataObj);
                } else {
                    noNetworkDialog();
                }
//                Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
//                dialerIntent.setData(Uri.parse("tel:" + itemDataObj.getmContact()));
//                context.startActivity(dialerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSrc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateAndTime, enqId, userClasses, name, fees, address, teachingMode, contact, altContact, callBtn;
        CardView rootOfItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootOfItem = itemView.findViewById(R.id.root);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeParentsContacted);
            enqId = itemView.findViewById(R.id.enqIdParentsContacted);
            userClasses = itemView.findViewById(R.id.userClassParentsContacted);
            name = itemView.findViewById(R.id.nameParentsContacted);
            fees = itemView.findViewById(R.id.feesParentsContacted);
            address = itemView.findViewById(R.id.addressParentsContacted);
            teachingMode = itemView.findViewById(R.id.teachingModeParentsContacted);
            contact = itemView.findViewById(R.id.contactParentsContacted);
//            altContact = itemView.findViewById(R.id.altContactParentsContacted);
            //call button of list item to open dialer with contact number not alt_contact
            callBtn = itemView.findViewById(R.id.parentsCallBtn);

            //setOnClickListener on root means CardView of item, before set listener set true android:clickable to true so it accept click events on it.
            rootOfItem.setOnClickListener(this);
        }

        public TextView getCallBtn() {
            return callBtn;
        }

        public CardView getRootOfItem() {
            return rootOfItem;
        }

        //This onClick method of implemented interface View.OnClickListener that is get called every time the ViewHolder Views have click event occurs
        @Override
        public void onClick(View view) {
            String id = view.getId() + "";
            Log.v("cardview---", id + " " + R.id.parentsCallBtn + "");
            //Call getAdapterPosition() method on context which returns what is the item position is going on in the Adapter of item that was clicked
            int itemPosition = this.getAdapterPosition();
            ParentsContactedModel itemDataObject = dataSrc.get(itemPosition);
            //Open new Activity for detailed layout of item in list, for master-detailed Navigation Ui pattern.
            Log.v("itemClicked---", itemDataObject.getmContact() + "");
            Intent i = new Intent(context, ClassesForMeActivity.class);
            i.putExtra("minDis", itemDataObject.getmUserClass());
            i.putExtra("enqId", Long.parseLong(itemDataObject.getmEnqId() + ""));
            i.putExtra("viewContact", "no View");
            context.startActivity(i);
        }

        public TextView getDateAndTime() {
            return dateAndTime;
        }

        public TextView getEnqId() {
            return enqId;
        }

        public TextView getUserClasses() {
            return userClasses;
        }

        public TextView getUserName() {
            return name;
        }

        public TextView getFees() {
            return fees;
        }

        public TextView getAddress() {
            return address;
        }

        public TextView getTeachingMode() {
            return teachingMode;
        }

        public TextView getContact() {
            return contact;
        }

        public TextView getAltContact() {
            return altContact;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void noNetworkDialog() {

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.no_network_dialog);
        retry = dialog.findViewById(R.id.retry);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isNetworkAvailable()) {
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    noNetworkDialog();
                }
            }
        });
    }

    private void checkStatus(final String type, final ParentsContactedModel classesForMe) {

        sessionConfig = new SessionConfig(context);

        basicFeatures.CheckPaymentStatus(context, sessionConfig.getPhone(), new BasicFeatures.CheckStatus() {
            @Override
            public void Result(String status) {

                if (type.matches("textcall")) {
                    if (status.matches("active") || status.matches("expired"))
                        intent(ClassesForMeActivity.class, classesForMe, "enq_viewed");
                    else if (status.matches("nonpaid"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);
                    else if (status.matches("freeactivation"))
                        intent(ClassesForMeActivity.class, classesForMe, "calldirect");
                    /*requestfornumber(classesForMe);*/

                } else if (type.matches("textcon")) {
                    if (status.matches("active"))
                        showMsgBeforecall(classesForMe);
                    else if (status.matches("nonpaid") || status.matches("expired"))
                        context.startActivity(new Intent(context, UpgradeActivity.class));
                    else if (status.matches("freeactivation"))
                        requestfornumber(classesForMe);
                    else if (status.matches("free"))
                        basicFeatures.showFreePaidDialog(context);

                }

            }

            @Override
            public void onError() {

            }
        });
    }
    private void intent(Class intentClass, ParentsContactedModel classesForMe, String view) {
        Intent intent = new Intent(context, intentClass);
        intent.putExtra("minDis", classesForMe.getmUserClass());
        intent.putExtra("enqId", classesForMe.getmEnqId());
        intent.putExtra("viewContact", view);
        context.startActivity(intent);
    }
    private void showMsgBeforecall(ParentsContactedModel classesForMe) {
        final ProgressDialog progressDialog1 = new ProgressDialog(context);
        progressDialog1.setCancelable(false);
        progressDialog1.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                progressDialog1.dismiss();
                return true;
            }

            return false;
        });
        progressDialog1.setMessage("Fetching data...");
        progressDialog1.show();
        ActiveDialog(classesForMe, progressDialog1);
    }
    private void requestfornumber(ParentsContactedModel classesForMe) {
        showMsgBeforecall(classesForMe);
    }

    private void ActiveDialog(final ParentsContactedModel classesForMe, final ProgressDialog prg) {


        String url = "https://api.gharpeshiksha.com" + "/Tutor/viewcontactmsg";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                prg.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                if (TextUtils.equals(jsonObject.getString("status"), "success")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    alertDialog.setTitle("View Contact");

                    alertDialog.setMessage(jsonObject.getString("message"));
                    alertDialog.setIcon(R.drawable.icon_phone);

                    alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        viewContactFun(context, tempId);
                    });
                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());


                    alertDialog.show();


                } else if (TextUtils.equals(jsonObject.getString("status"), "error")) {

                    if (jsonObject.has("time_left")) {
                        basicFeatures.AlertDialogFreeViews(context, jsonObject.getLong("time_left"));
                    }
                }

            } catch (JSONException e) {

                prg.dismiss();
            }


        }, error -> {

            prg.dismiss();
            noNetworkDialog();

        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("phone", "" + sessionConfig.getPhone());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);
    }

    private void viewContactFun(Context context, String id) {
        String RealnumberURL = "https://api.gharpeshiksha.com" + "/GetClasses/getContact";
        Map<String, String> params = new HashMap<>();
        params.put("phone", sessionConfig.getPhone() + "");
        params.put("enq_id", id);
        if (volleyHelper == null) {
            volleyHelper = new VolleyHelper();
        }
        volleyHelper.VolleyPostRequest(context, RealnumberURL, params, new VolleyResponse() {
            @Override
            public void onSucess(String response) {
                try {
                    final JSONObject part = new JSONObject(response);

                    String result = part.getString("result");
                    if (result.equals("success")) {


                        String num = part.getString("phone");
                        Toast.makeText(context, "1 contact viewed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL); // opens up all available dialers
                        intent.setData(Uri.parse("tel:" + num)); // tel:  is required or else app will crash
                        context.startActivity(intent); // startActivity


                    } else {
                        if (result.matches("error")) {
                            if (part.has("time_left")) {
                                basicFeatures.AlertDialogFreeViews(context, part.getLong("time_left"));
                            } else {
                                try {
                                    new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                            .setMessage(part.getString("message"))
                                            .setPositiveButton("Okay", null)
                                            .show();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to view this class", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
