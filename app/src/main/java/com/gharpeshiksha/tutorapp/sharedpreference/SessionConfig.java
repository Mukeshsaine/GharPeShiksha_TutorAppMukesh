package com.gharpeshiksha.tutorapp.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.gharpeshiksha.tutorapp.Dashboard;
import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.retrofit.ApiServies;
import com.gharpeshiksha.tutorapp.retrofit.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionConfig {

    final String TAG = SessionConfig.this.toString();
    Context context;
    SharedPreferences sharedPreferences;


    public SessionConfig(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preferences), Context.MODE_PRIVATE);

    }

    //save google API key in the SharedPreferences file
    public void setAPIKey(String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apiKey", apiKey);
        editor.apply();//it run set key/value pair in SharedPreferences in background so UI won't stop
        //instead of commit() method which make changes in SP file immediately in foreground Activity
    }

    //save user email id
    public void setEmailId(String email) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("emailId", email);
        edit.apply();
    }

    //return google API key from SharedPreferences file
    public String getAPIKey() {
        final String[] api = {sharedPreferences.getString("apiKey", "")};
        try {
            if(api[0].isEmpty()) {
                RetrofitClient.getClient().create(ApiServies.class)
                        .getMapApiKey().enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    if(response.isSuccessful() && response.body() != null) {
                                        JSONObject js = new JSONObject(response.body().string());
                                        api[0] = js.getString("map_key");
                                        setAPIKey(api[0]);
                                    } else {
                                        api[0] = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";
                                    }
                                } catch(Exception e) {
                                    api[0] = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";
                                    Log.d(TAG, "onResponse: " + e);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                api[0] = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";
                                Log.d(TAG, "onFailure: " + t);
                            }
                        });
            }
        } catch (Exception e) {
            api[0] = "AIzaSyC6_pWGCV9LapjdF0tTPFHPTRdTETOJqGY";
            Log.d(TAG, "getAPIKey: " + e);
        }
        return api[0];
    }

    //get user email id
    public String getEmailId() {
        return sharedPreferences.getString("emailId", "");
    }

    //remove emailId
    public void removeEmailId() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("emailId");
        edit.apply();
    }

    //put accessToken in shared pref
    public void setAccessToken(String tok) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("accessToken", tok);
        edit.apply();
    }

    //get accessToken from shared pref
    public String getAccessToken() {
        return sharedPreferences.getString("accessToken", "NA");
    }

    //delete accessToken from shared pref
    public void deleteAccessToken() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("accessToken");
        edit.apply();
    }

    public void setTempAddress(String tempAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tempAddress", tempAddress);
        editor.apply();
    }

    public String getTempAddress() {
        return sharedPreferences.getString("tempAddress", "");
    }

    public void LoginStatusWrite(boolean status) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preferences), status);
        editor.apply();// it will commit in background. so it does not block the other process

    }


    public void setUserLocation(String location) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserLocation", location);
    }

    public String getUserLocation() {
        return sharedPreferences.getString("UserLocation", "Delhi");
    }

    public boolean LoginStatusRead() {

        return sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preferences), false);

    }

    public void setPhone(long phone) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getResources().getString(R.string.login_status_preferences_phone), phone);
        editor.apply();
    }


    public long getPhone() {
        return sharedPreferences.getLong(context.getResources().getString(R.string.login_status_preferences_phone), 333);
    }

    public void setToken(String token) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.firebase_token), token);
        editor.apply();
        Log.e(TAG, "setToken: stored " + token);
    }

    public String getToken() {

        String token = sharedPreferences.getString(context.getResources().getString(R.string.firebase_token), "");
        Log.e(TAG, "getToken: success " + token);
        return token;
    }

    public void setEmail(String email) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.Email), email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(context.getResources().getString(R.string.Email), "");

    }

    public void setSource(String source) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.Source), source);
        editor.apply();
    }

    public String getSource() {
        return sharedPreferences.getString(context.getResources().getString(R.string.Source), "PlayStore");

    }

    public void setupdateAvailable(boolean version) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.Version), version);
        editor.apply();
    }

    public boolean getupdateAvailable() {
        return sharedPreferences.getBoolean(context.getResources().getString(R.string.Version), false);

    }

    public void setfirstTimeUser(boolean version) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.FirstTime), version);
        editor.apply();
    }

    public boolean getfirstTimeUser() {
        return sharedPreferences.getBoolean(context.getResources().getString(R.string.FirstTime), true);
    }

    public void setShowAboutDialog(boolean version) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.showAboutDialog), version);
        editor.apply();
    }

    public boolean getShowAboutDialog() {
        return sharedPreferences.getBoolean(context.getResources().getString(R.string.showAboutDialog), false);
    }

    public void clearSessionConfig() {
        sharedPreferences.edit().clear().apply();
    }
    public void setBadgeFree(int value) {
        if (value == 0) {
            Dashboard.badgecount_Free.setVisibility(View.GONE);
        } else {
            Dashboard.badgecount_Free.setVisibility(View.VISIBLE);
            Dashboard.badgecount_Free.setText(String.valueOf(value));
        }
    }
}
