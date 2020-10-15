package com.nitro.corona_tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    MaterialCardView confirmed_card, active_card, deceased_card, recovered_card;
    TextView confirmed, active, deceased, recovered;
    SharedPreferences sharedPreferences;
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
        sharedPreferences = MainActivity.this.getSharedPreferences("Corona_data", Context.MODE_PRIVATE);


        confirmed = findViewById(R.id.confirmed_cases);
        active = findViewById(R.id.active_cases);
        deceased = findViewById(R.id.deceased_cases);
        recovered = findViewById(R.id.recovered_cases);

        //If Internet Found Get Data From Internet And Save In Shared Preferences
        if (internetAvailable()) {
            GetCurrentData getData = new GetCurrentData() {
                @Override
                void gotData(final String res) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Save Data In Shared Preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("data", res);
                            editor.apply();
                            getAndSetDataFromSharedPreferences();
                        }
                    });
                }
            };
            getData.getData();
        }
        //Get Data From Shared Preferences
        else{
            getAndSetDataFromSharedPreferences();
        }

        //Date And Time Showing
        String date = new SimpleDateFormat(getString(R.string.date_time_format)).format(Calendar.getInstance().getTime());
        ((TextView) findViewById(R.id.date_time)).setText(date);

    }

    private void getAndSetDataFromSharedPreferences() {
        try {
            String res = sharedPreferences.getString("data", "");
            if (res.equals("")) {
                Toast.makeText(this, "Internet Not Found", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<Corona> cases = new ArrayList<>();
                JSONArray array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject current = array.getJSONObject(i);
                    cases.add(new Corona(current.getString("state_name"), current.getString("positive"), current.getString("active"), current.getString("cured"), current.getString("death"), "↑" + current.get("new_positive"), "↑" + current.get("new_cured"), "↑" + current.get("new_death")));
                }
                total_details = cases.get(cases.size() - 1);
                cases.remove(cases.size() - 1);
                confirmed.setText(formatNumber(total_details.getPositive()));
                active.setText(formatNumber(total_details.getActive()));
                recovered.setText(formatNumber(total_details.getCured()));
                deceased.setText(formatNumber(total_details.getDeceased()));

                //State And Data ListViews
                CoronaAdapter coronaAdapter = new CoronaAdapter(MainActivity.this, cases);
                data_list.setAdapter(coronaAdapter);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Unable To Get Data Properly!", Toast.LENGTH_SHORT).show();
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
