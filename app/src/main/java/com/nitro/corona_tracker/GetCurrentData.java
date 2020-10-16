package com.nitro.corona_tracker;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class GetCurrentData {

    private static final String TAG = "GET_CURRENT_DATA";
    private String url;
    private int n;
    private HashMap<Integer, String> map = new HashMap<>();

    GetCurrentData(String... url) {
        n = url.length;
        for (int i = 0; i < url.length; i++) {
            this.url = url[i];
            getData(i);
        }
    }


    void getData(final int index) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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
                    gotSingleData(index, res);
                }
            }
        });
    }

    private void gotSingleData(int i, String res) {
        map.put(i, res);
        if (map.size() == n) {
            gotData(map);
        }
    }

    void gotData(HashMap<Integer, String> map) {

    }

}