package com.nitro.corona_tracker;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MaterialCardView confirmed_card, active_card, deceased_card, recovered_card;
    TextView confirmed, active, deceased, recovered;
    ListView data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmed_card = findViewById(R.id.confirmed_card);
        active_card = findViewById(R.id.active_card);
        deceased_card = findViewById(R.id.deceased_card);
        recovered_card = findViewById(R.id.deceased_card);
        data_list = findViewById(R.id.data);

        confirmed = findViewById(R.id.confirmed_cases);
        active = findViewById(R.id.active_cases);
        deceased = findViewById(R.id.deceased_cases);
        recovered = findViewById(R.id.recovered_cases);


        GetCurrentData getData = new GetCurrentData() {
            @Override
            void gotData(final ArrayList<Corona> cases) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Total Data
                        Corona total_details = cases.get(cases.size() - 1);
                        cases.remove(cases.size() - 1);
                        ValueAnimator animator = ValueAnimator.ofInt(0, Integer.parseInt(total_details.getPositive()));
                        animator.setDuration(1000);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                confirmed.setText(animation.getAnimatedValue().toString());
                            }
                        });
                        animator.start();
                        active.setText(formatNumber(total_details.getActive()));
                        recovered.setText(formatNumber(total_details.getCured()));
                        deceased.setText(formatNumber(total_details.getDeceased()));

                        //State And Data ListViews
                        CoronaAdapter coronaAdapter = new CoronaAdapter(MainActivity.this, cases);
                        data_list.setAdapter(coronaAdapter);
                    }
                });
            }
        };




        String date = new SimpleDateFormat("dd MM, HH:mm").format(Calendar.getInstance().getTime());
        ((TextView)findViewById(R.id.date_time)).setText(date);
        getData.getData();
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }

}
