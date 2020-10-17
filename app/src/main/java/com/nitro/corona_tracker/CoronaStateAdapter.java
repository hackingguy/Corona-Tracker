package com.nitro.corona_tracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CoronaStateAdapter extends ArrayAdapter<Corona> {


    private ArrayList<Corona> corona_list;
    private Context mContext;

    CoronaStateAdapter(@NonNull Context context, @NonNull ArrayList<Corona> list) {
        super(context, 0, list);
        this.corona_list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) v = LayoutInflater.from(mContext).inflate(R.layout.data_list_item, null);

        final TextView state = v.findViewById(R.id.state);
        final TextView confirmed = v.findViewById(R.id.confirmed);
        final TextView active = v.findViewById(R.id.active);
        final TextView deceased = v.findViewById(R.id.deceased);
        final TextView cured = v.findViewById(R.id.cured);
        final TextView new_active = v.findViewById(R.id.new_confirmed);
        final TextView new_recovered = v.findViewById(R.id.new_cured);
        final TextView new_deaths = v.findViewById(R.id.new_deceased);

        state.setText(formatNumber(corona_list.get(position).getState()));
        confirmed.setText(formatNumber(corona_list.get(position).getPositive()));
        active.setText(formatNumber(corona_list.get(position).getActive()));
        deceased.setText(formatNumber(corona_list.get(position).getDeceased()));
        cured.setText(formatNumber(corona_list.get(position).getCured()));
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, State_data.class);
                intent.putExtra("state", state.getText());
                intent.putExtra("confirmed", confirmed.getText());
                intent.putExtra("active", active.getText());
                intent.putExtra("cured", cured.getText());
                intent.putExtra("deceased", deceased.getText());
                intent.putExtra("new_confirmed", new_active.getText().toString().substring(1));
                intent.putExtra("new_recovered", new_recovered.getText().toString().substring(1));
                intent.putExtra("new_deaths", new_deaths.getText().toString().substring(1));
                mContext.startActivity(intent);
            }
        });

        if (Integer.parseInt(corona_list.get(position).getNew_cured()) == 0) {
            new_recovered.setVisibility(View.GONE);
        } else {
            new_recovered.setText("↑" +formatNumber(corona_list.get(position).getNew_cured()));
        }
        if (Integer.parseInt(corona_list.get(position).getNew_active()) == 0) {
            new_active.setVisibility(View.GONE);
        } else {
            new_active.setText("↑" +formatNumber(corona_list.get(position).getNew_active()));
        }
        if (Integer.parseInt(corona_list.get(position).getNew_death()) == 0) {
            new_deaths.setVisibility(View.GONE);
        } else {
            new_deaths.setText("↑" +formatNumber(corona_list.get(position).getNew_death()));
        }

        return v;
    }

    @Override
    public int getCount() {
        return corona_list.size();
    }

    private static String formatNumber(String number) {
        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
        } catch (Exception e) {
            return number;
        }
    }

}
