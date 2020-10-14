package com.nitro.corona_tracker;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class GetCurrentData {

    static final String TAG = "GET_CURRENT_DATA";

    GetCurrentData() {
    }


    void getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.mohfw.gov.in/data/datanew.json")
                .build();


        client.newCall(request).enqueue(new Callback() {

            ArrayList<Corona> cases = new ArrayList<>();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: Internet Not Found");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        String res = response.body().string();
                        JSONArray array = new JSONArray(res);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            cases.add(new Corona(current.getString("state_name"), current.getString("positive"), current.getString("active"), current.getString("cured"), current.getString("death")));
                        }
                        gotData(cases);

                    } catch (JSONException e) {
                        Log.d(TAG, "Unable To Parse JSON" + e.toString());
                    }
                }
            }
        });
    }

    void gotData(ArrayList<Corona> cases) {
    }
}