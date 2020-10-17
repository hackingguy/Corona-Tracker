package com.nitro.corona_tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MaterialCardView confirmed_card, active_card, deceased_card, recovered_card;
    TextView confirmed, active, deceased, recovered, new_confirmed, new_recovered, new_deceased;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Long currentTime, oldTime;
    String state_url, district_url, change_data;
    ListView data_list;
    Corona total_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmed_card = findViewById(R.id.confirmed_card);
        active_card = findViewById(R.id.active_card);
        deceased_card = findViewById(R.id.deceased_card);
        recovered_card = findViewById(R.id.deceased_card);
        data_list = findViewById(R.id.data);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        sharedPreferences = getSharedPreferences("Corona_data", Context.MODE_PRIVATE);
        state_url = "https://api.covid19india.org/data.json";
        change_data = "https://api.covid19india.org/states_daily.json";
        district_url = "https://api.covid19india.org/state_district_wise.json";


        confirmed = findViewById(R.id.confirmed_cases);
        active = findViewById(R.id.active_cases);
        deceased = findViewById(R.id.deceased_cases);
        recovered = findViewById(R.id.recovered_cases);
        new_confirmed = findViewById(R.id.total_new_confirmed);
        new_recovered = findViewById(R.id.total_new_recovered);
        new_deceased = findViewById(R.id.total_new_deceased);

        oldTime = Long.parseLong(sharedPreferences.getString("updated_time", "0"));
        currentTime = System.currentTimeMillis();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //If Internet And Updated More Than 3 hrs Ago
        if (internetAvailable() && (currentTime - oldTime == 0 || currentTime - oldTime > 10800000)) {
            updateData();
        }
        //Get Data From Shared Preferences
        else {
            getAndSetDataFromSharedPreferences();
        }
    }

    private void updateData() {
        GetCurrentData getData = new GetCurrentData(state_url, district_url, change_data) {
            @Override
            void gotData(final HashMap<Integer, String> map) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Save Data In Shared Preferences
                        Log.d(TAG, "Updating From Internet");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String date = new SimpleDateFormat(getString(R.string.date_time_format)).format(Calendar.getInstance().getTime());
                        editor.putString("state_data", map.get(0));
                        editor.putString("district_data", map.get(1));
                        editor.putString("change_data", map.get(2));
                        editor.putString("updated_time", Long.toString(System.currentTimeMillis()));
                        editor.putString("last_updated_show_time", date);
                        editor.apply();
                        getAndSetDataFromSharedPreferences();
                    }
                });
            }
        };
    }

    private void getAndSetDataFromSharedPreferences() {

        final String state_data = sharedPreferences.getString("state_data", "");
        final String change_data = sharedPreferences.getString("change_data", "");

        //First Launch without Internet
        if (state_data.equals("")) {
            Toast.makeText(this, "Internet Not Found", Toast.LENGTH_SHORT).show();
        } else {
            //Do Transactions In Background, Don't Stress Main Activity
            ((TextView) findViewById(R.id.date_time)).setText("Last Updated On " + sharedPreferences.getString("last_updated_show_time", ""));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        final ArrayList<Corona> cases = new ArrayList<>();
                        JSONObject state = new JSONObject(state_data);
                        JSONObject change = new JSONObject(change_data);
                        JSONArray arr_1 = change.getJSONArray("states_daily");
                        JSONArray arr = state.getJSONArray("statewise");
                        JSONObject change_confirmed = arr_1.getJSONObject(arr_1.length() - 3);
                        JSONObject change_recovered = arr_1.getJSONObject(arr_1.length() - 2);
                        JSONObject change_deceased = arr_1.getJSONObject(arr_1.length() - 1);
                        for (int i = 0; i < arr.length() - 2; i++) {
                            JSONObject current = arr.getJSONObject(i);
                            try {
                                String state_code = current.getString("statecode");
                                cases.add(new Corona(current.getString("state"), current.getString("confirmed"), current.getString("active"),
                                        current.getString("recovered"), current.getString("deaths"),
                                        change_confirmed.getString(state_code.toLowerCase()), change_recovered.getString(state_code.toLowerCase()),
                                         change_deceased.getString(state_code.toLowerCase())));
                            } catch (JSONException e) {
                                cases.add(new Corona(current.getString("state"), current.getString("confirmed"), current.getString("active"),
                                        current.getString("recovered"), current.getString("deaths"),
                                        "0", "0",
                                        "0"));
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                total_details = cases.get(0);
                                cases.remove(0);
                                confirmed.setText(formatNumber(total_details.getPositive()));
                                active.setText(formatNumber(total_details.getActive()));
                                recovered.setText(formatNumber(total_details.getCured()));
                                deceased.setText(formatNumber(total_details.getDeceased()));
                                new_confirmed.setText("+" + formatNumber(total_details.getNew_active()));
                                new_recovered.setText("+" + formatNumber(total_details.getNew_cured()));
                                new_deceased.setText("+" + formatNumber(total_details.getNew_death()));

                                //State And Data ListViews
                                CoronaStateAdapter coronaAdapter = new CoronaStateAdapter(MainActivity.this, cases);
                                data_list.setAdapter(coronaAdapter);
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Data Error!", Toast.LENGTH_SHORT).show();
                    }
                }

                ;
            }).start();
        }
    }


    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }


    public boolean internetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
