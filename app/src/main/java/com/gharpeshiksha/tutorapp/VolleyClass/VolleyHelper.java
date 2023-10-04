package com.gharpeshiksha.tutorapp.VolleyClass;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gharpeshiksha.tutorapp.Interface.VolleyResponse;

import java.util.HashMap;
import java.util.Map;

public class VolleyHelper {

    public VolleyHelper() {
    }

    public void VolleyPostRequest(Context context, String url, final Map<String, String> params, final VolleyResponse volleyResponse) {


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> volleyResponse.onSucess(response), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onError(error.getMessage());
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
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

}
