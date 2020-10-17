package com.nitro.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class State_data extends AppCompatActivity {

    private static final String TAG = "State_data";
    TextView state,active,confirmed,deceased,recovered,new_confirmed,new_recovered,new_deceased;
    ListView district_list;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_data);

        state = findViewById(R.id.state_card);
        active = findViewById(R.id.state_active);
        deceased = findViewById(R.id.state_deceased);
        confirmed = findViewById(R.id.state_confirmed);
        recovered = findViewById(R.id.state_recovered);
        district_list = findViewById(R.id.district_list);
        new_confirmed = findViewById(R.id.total_new_confirmed_state);
        new_recovered = findViewById(R.id.total_new_recovered_state);
        new_deceased = findViewById(R.id.total_new_deceased_state);

        i = getIntent();
        state.setText(i.getStringExtra("state"));
        active.setText(i.getStringExtra("active"));
        confirmed.setText(i.getStringExtra("confirmed"));
        recovered.setText(i.getStringExtra("cured"));
        deceased.setText(i.getStringExtra("deceased"));
        new_confirmed.setText("+"+i.getStringExtra("new_confirmed"));
        new_recovered.setText("+"+i.getStringExtra("new_recovered"));
        new_deceased.setText("+"+i.getStringExtra("new_deaths"));
        parseJSON();
    }

    private void parseJSON() {
        String state_data = getSharedPreferences("Corona_data", Context.MODE_PRIVATE).getString("district_data","");
        if(state_data.equals("")){
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                ArrayList<Corona> cases=new ArrayList<>();
                JSONObject total_data = new JSONObject(state_data);
                JSONObject state= total_data.getJSONObject(i.getStringExtra("state")).getJSONObject("districtData");
                Iterator<String> keys = state.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    JSONObject current = state.getJSONObject(key);
                    cases.add(new Corona(key,current.getString("confirmed"),current.getString("active"),current.getString("recovered"),current.getString("deceased")));
                }
                CoronaDistrictAdapter adapter = new CoronaDistrictAdapter(State_data.this,cases);
                district_list.setAdapter(adapter);
            } catch (JSONException e) {
                Log.d(TAG, "parseJSON: "+e);
                Toast.makeText(this, "Failed To Parse Data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
