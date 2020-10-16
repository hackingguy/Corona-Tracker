package com.nitro.corona_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class State_data extends AppCompatActivity {

    TextView state,active,confirmed,deceased,recovered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_data);

        state = findViewById(R.id.state_card);
        active = findViewById(R.id.state_active);
        deceased = findViewById(R.id.state_deceased);
        confirmed = findViewById(R.id.state_confirmed);
        recovered = findViewById(R.id.state_recovered);

        Intent i = getIntent();
        state.setText(i.getStringExtra("state"));
        active.setText(i.getStringExtra("active"));
        confirmed.setText(i.getStringExtra("confirmed"));
        recovered.setText(i.getStringExtra("cured"));
        deceased.setText(i.getStringExtra("deceased"));
    }
}
