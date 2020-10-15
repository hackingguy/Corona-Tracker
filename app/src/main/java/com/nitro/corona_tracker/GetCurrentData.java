package com.nitro.corona_tracker;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class GetCurrentData {

    private static final String TAG = "GET_CURRENT_DATA";

    GetCurrentData() {
    }


    void getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.mohfw.gov.in/data/datanew.json")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: Internet Not Found");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = Objects.requireNonNull(response.body()).string();
                    gotData(res);
                }
            }
        });
    }

    void gotData(String res) {
    }
}